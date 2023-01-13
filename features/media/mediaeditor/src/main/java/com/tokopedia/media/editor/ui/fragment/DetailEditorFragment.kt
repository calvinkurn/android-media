package com.tokopedia.media.editor.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.values
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.editor.analytics.editordetail.EditorDetailAnalytics
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.base.BaseEditorFragment
import com.tokopedia.media.editor.ui.component.RotateToolUiComponent.Companion.ROTATE_BTN_DEGREE
import com.tokopedia.media.editor.data.repository.WatermarkType
import com.tokopedia.media.editor.databinding.FragmentDetailEditorBinding
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorViewModel
import com.tokopedia.media.editor.ui.component.*
import com.tokopedia.media.editor.ui.uimodel.EditorAddLogoUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorCropRotateUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel.Companion.REMOVE_BG_TYPE_WHITE
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel.Companion.REMOVE_BG_TYPE_DEFAULT
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel.Companion.REMOVE_BG_TYPE_GRAY
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.ui.widget.EditorDetailPreviewWidget
import com.tokopedia.media.editor.utils.addLogoToText
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.media.editor.utils.cropRatioToText
import com.tokopedia.media.editor.utils.getToolEditorText
import com.tokopedia.media.editor.utils.removeBackgroundToText
import com.tokopedia.media.editor.utils.watermarkToText
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.picker.common.EXTRA_RESULT_PICKER
import com.tokopedia.picker.common.ImageRatioType
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.R as principleR
import com.tokopedia.utils.view.binding.viewBinding
import com.yalantis.ucrop.util.FastBitmapDrawable
import java.io.File
import javax.inject.Inject
import kotlin.math.max

class DetailEditorFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val editorDetailAnalytics: EditorDetailAnalytics,
    private val pickerParam: PickerCacheManager
) : BaseEditorFragment(),
    BrightnessToolUiComponent.Listener,
    ContrastToolsUiComponent.Listener,
    RemoveBackgroundToolUiComponent.Listener,
    WatermarkToolUiComponent.Listener,
    RotateToolUiComponent.Listener,
    CropToolUiComponent.Listener,
    EditorDetailPreviewWidget.Listener,
    AddLogoToolUiComponent.Listener {

    private val viewBinding: FragmentDetailEditorBinding? by viewBinding()
    private val viewModel: DetailEditorViewModel by activityViewModels { viewModelFactory }

    private val brightnessComponent by uiComponent { BrightnessToolUiComponent(it, this) }
    private val removeBgComponent by uiComponent { RemoveBackgroundToolUiComponent(it, this) }
    private val contrastComponent by uiComponent { ContrastToolsUiComponent(it, this) }
    private val watermarkComponent by uiComponent { WatermarkToolUiComponent(it, this) }
    private val rotateComponent by uiComponent { RotateToolUiComponent(it, this) }
    private val cropComponent by uiComponent { CropToolUiComponent(it, this) }
    private val addLogoComponent by uiComponent { AddLogoToolUiComponent(it, this) }

    private var data = EditorDetailUiModel()
    private var detailState = EditorUiModel()

    // original bitmap that already implement previous state
    // start point for each editor detail
    private var implementedBaseBitmap: Bitmap? = null

    private var removeBackgroundRetryLimit = 3
    private var removeBackgroundType = 0

    private var isEdited = false
    private var initialImageMatrix: Matrix? = null

    private var initialRotateNumber = 0

    // storage variable for watermark case
    private var globalWidth = 0
    private var globalHeight = 0

    private var originalImageWidth = 0
    private var originalImageHeight = 0

    private var isAddLogoTipsShowed = false

    fun isShowDialogConfirmation(): Boolean {
        return isEdited
    }

    private fun saveOverlay() {
        viewBinding?.imgPreviewOverlay?.let {
            val logoBitmap = it.drawable.toBitmap()
            viewModel.saveImageCache(it.drawable.toBitmap(), sourcePath = PNG_KEY)
                ?.let { fileResult ->
                    data.addLogoValue = EditorAddLogoUiModel(
                        Pair(logoBitmap.width, logoBitmap.height),
                        fileResult.path,
                        addLogoComponent.getLogoUrl()
                    )
                }
        }
    }

    fun saveAndExit() {
        // clear un related edit state since crop & rotate value is gather from previous state
        data.cropRotateValue.apply {
            when {
                data.isToolRotate() -> isCrop = false
                data.isToolCrop() -> isRotate = false
                else -> {
                    isCrop = false
                    isRotate = false
                }
            }
        }

        if (data.isToolRotate() || data.isToolCrop()) {
            val rotateNumber = viewModel.rotateNumber - initialRotateNumber

            // if current tools editor not rotate then skip crop data set by sent empty object on data
            viewBinding?.imgUcropPreview?.cropRotate(
                finalRotationDegree = viewModel.rotateRotationFinalDegree,
                sliderValue = viewModel.rotateSliderValue,
                rotateNumber = rotateNumber,
                initialRotateNumber = initialRotateNumber,
                data
            ) {
                data.resultUrl = viewModel.saveImageCache(
                    it,
                    sourcePath = data.originalUrl
                )?.path

                if (data.addLogoValue != EditorAddLogoUiModel()) {
                    // crop current overlay
                    val isWidthSame = data.addLogoValue.imageRealSize.first == it.width
                    val isHeightSame = data.addLogoValue.imageRealSize.second == it.height

                    if (!isWidthSame || !isHeightSame) {
                        updateAddLogoOverlay(Pair(it.width, it.height)) {
                            finishPage()
                        }
                    } else {
                        finishPage()
                    }
                } else {
                    finishPage()
                }
            }
        } else {
            if (data.isToolAddLogo()) {
                saveOverlay()
            }

            getBitmap()?.let {
                data.resultUrl = viewModel.saveImageCache(
                    it,
                    sourcePath = data.originalUrl
                )?.path

                if (data.isToolRemoveBackground()) {
                    data.removeBackgroundUrl = data.resultUrl
                }

                finishPage()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            editorR.layout.fragment_detail_editor,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtonListener()
    }

    override fun onBrightnessValueChanged(value: Float) {
        viewModel.setBrightness(value)
        data.brightnessValue = value
        isEdited = true
    }

    override fun onContrastValueChanged(value: Float) {
        viewModel.setContrast(value, implementedBaseBitmap)
        data.contrastValue = value
        isEdited = true
    }

    override fun onRemoveBackgroundClicked(removeBgType: Int) {
        getImageView()?.let { imageView ->
            data.resultUrl?.let { it ->
                removeBackgroundType = removeBgType
                data.removeBackgroundColor = removeBgType

                if (removeBgType == REMOVE_BG_TYPE_DEFAULT) {
                    loadImageWithEmptyTarget(requireContext(),
                        it,
                        {},
                        mediaTarget = MediaBitmapEmptyTarget(
                            onReady = { resultBitmap ->
                                imageView.setImageBitmap(
                                    resultBitmap
                                )
                            }
                        )
                    )
                } else {
                    viewModel.setRemoveBackground(it) {
                        if (activity?.isFinishing != false) return@setRemoveBackground
                        removeBgConnectionToast {
                            removeBackgroundRetryLimit--
                            if (removeBackgroundRetryLimit == 0) {
                                removeBgClosePage()
                            } else {
                                Handler().postDelayed({
                                    onRemoveBackgroundClicked(removeBackgroundType)
                                }, DELAY_REMOVE_BG_TOASTER)
                            }
                        }?.show()
                    }
                }
            }
        }
    }

    override fun onWatermarkChanged(type: WatermarkType) {
        implementedBaseBitmap?.let {
            viewModel.setWatermark(
                it,
                type,
                detailUiModel = data,
                useStorageColor = false
            )
            isEdited = true
        }
    }

    override fun onRotateValueChanged(rotateValue: Float) {
        viewModel.setRotate(viewBinding?.imgUcropPreview, rotateValue, false)
        isEdited = true
    }

    override fun onImageMirror() {
        editorDetailAnalytics.clickRotationFlip()
        viewModel.setMirror(viewBinding?.imgUcropPreview)
        isEdited = true
    }

    override fun onImageRotate(rotateDegree: Float) {
        editorDetailAnalytics.clickRotationRotate()
        viewModel.setRotate(
            viewBinding?.imgUcropPreview,
            ROTATE_BTN_DEGREE,
            true,
            getImagePairRatio()
        )
        isEdited = true

        viewBinding?.imgUcropPreview?.let {
            val cropViewRect = it.overlayView.cropViewRect
            val realImageWidth = it.cropImageView.drawable.intrinsicWidth
            val realImageHeight = it.cropImageView.drawable.intrinsicHeight

            val rotateSize = if (viewModel.rotateNumber % 2 == 1) {
                Pair(realImageHeight, realImageWidth)
            } else {
                Pair(realImageWidth, realImageHeight)
            }

            updateAddLogoOverlay(rotateSize) { resultUrl ->
                setOverlaySize(
                    Pair(cropViewRect.width(), cropViewRect.height())
                )
                viewBinding?.imgPreviewOverlay?.loadImage(resultUrl)
            }
        }
    }

    override fun onCropRatioClicked(ratio: ImageRatioType) {
        viewBinding?.imgUcropPreview?.let {
            val overlayView = it.overlayView
            val cropView = it.cropImageView

            overlayView.setTargetAspectRatio(ratio.getRatio())
            cropView.targetAspectRatio = ratio.getRatio()

            val newRatio = Pair(ratio.getRatioX(), ratio.getRatioY())
            if (data.cropRotateValue.cropRatio != newRatio) {
                data.cropRotateValue.cropRatio = newRatio
                isEdited = true
            }
        }
    }

    // EditorDetailPreviewWidget finish load image
    override fun onLoadComplete() {
        viewBinding?.imgUcropPreview?.let {
            it.post {
                readPreviousState()
                initialImageMatrix = Matrix(it.cropImageView.imageMatrix)

                setOverlaySize(
                    Pair(
                        it.overlayView.cropViewRect.width(),
                        it.overlayView.cropViewRect.height()
                    )
                )
            }
        }
    }

    override fun onLogoChosen(bitmap: Bitmap) {
        viewBinding?.imgPreviewOverlay?.apply {
            show()
            setImageBitmap(bitmap)
            isEdited = true
        }
    }

    override fun onUpload() {
        editorDetailAnalytics.clickAddLogoUpload()
        if (!addLogoComponent.isUploadAvatarReady() && !isAddLogoTipsShowed) {
            showAddLogoUploadTips()
        } else {
            showAddLogoPicker()
        }
    }

    override fun onLoadRetry() {
        editorDetailAnalytics.clickAddLogoLoadRetry()
    }

    override fun onLoadFailed() {
        val text = requireContext().getString(editorR.string.editor_add_logo_toast_final)
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_LOGO_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val elements = data?.getParcelableExtra(EXTRA_RESULT_PICKER) ?: PickerResult()
            addLogoComponent.initUploadAvatar(elements.originalPaths.first())
            viewModel.setLocalLogo(elements.originalPaths.first())
        }
    }

    override fun initObserver() {
        observeIntentUiModel()
        observeIntentUiState()
        observeLoader()
        observeBrightness()
        observeContrast()
        observeWatermark()
        observeRemoveBackground()
        observeEditorParamModel()
    }

    private fun observeBrightness() {
        viewModel.brightnessFilter.observe(viewLifecycleOwner) { colorFilter ->
            getImageView()?.let {
                val bitmap = implementedBaseBitmap ?: it.drawable.toBitmap()
                val fastBitmapDrawable = FastBitmapDrawable(bitmap)
                fastBitmapDrawable.colorFilter = colorFilter

                val tempCanvas = Canvas(fastBitmapDrawable.bitmap)
                fastBitmapDrawable.draw(tempCanvas)

                it.setImageDrawable(fastBitmapDrawable)
            }
        }
    }

    private fun observeContrast() {
        viewModel.contrastFilter.observe(viewLifecycleOwner) {
            getImageView()?.setImageBitmap(it)
        }
    }

    private fun observeRemoveBackground() {
        viewModel.removeBackground.observe(viewLifecycleOwner) {
            it?.let {
                loadImageWithEmptyTarget(requireContext(),
                    it.path,
                    {},
                    mediaTarget = MediaBitmapEmptyTarget(
                        onReady = { resultBitmap ->
                            when (removeBackgroundType) {
                                REMOVE_BG_TYPE_GRAY -> principleR.color.Unify_NN200
                                REMOVE_BG_TYPE_WHITE -> principleR.color.Unify_Static_White
                                else -> null
                            }?.let { backgroundColor ->
                                val color =
                                    ContextCompat.getColor(requireContext(), backgroundColor)

                                val backgroundBitmap = Bitmap.createBitmap(
                                    resultBitmap.width,
                                    resultBitmap.height,
                                    resultBitmap.config
                                )
                                backgroundBitmap.eraseColor(color)

                                val canvas = Canvas(backgroundBitmap)
                                canvas.drawBitmap(resultBitmap, 0f, 0f, null)

                                getImageView()?.setImageBitmap(
                                    backgroundBitmap
                                )
                            }

                            isEdited = true
                        }
                    )
                )
            }
        }
    }

    private fun observeIntentUiModel() {
        viewModel.intentDetailUiModel.observe(viewLifecycleOwner) {
            // make this ui model as global variable
            data = it
            initialRotateNumber = it.cropRotateValue.orientationChangeNumber

            renderUiComponent(it.editorToolType)
        }
    }

    private fun observeIntentUiState() {
        viewModel.intentUiModel.observe(viewLifecycleOwner) {
            detailState = it
        }
    }

    private fun observeEditorParamModel() {
        viewModel.editorParam.observe(viewLifecycleOwner) {
            if (data.isToolCrop() || data.isToolRotate()) {
                viewBinding?.imgViewPreview?.hide()

                // uCrop height must be same between rotate & crop to get same result when implement state
                // crop item is dynamic according to the editor param
                rotateComponent.setupView(data)
                cropComponent.setupView(it, data)

                viewBinding?.ucToolContainer?.post {
                    val rotateHeight = rotateComponent.container().height
                    val cropHeight = cropComponent.container().height

                    val cropRotateHeight = max(rotateHeight, cropHeight)

                    // set both rotate & crop view height same for using cross state value
                    cropComponent.container().apply {
                        val lp = layoutParams
                        lp.height = cropRotateHeight
                        layoutParams = lp
                    }
                    rotateComponent.container().apply {
                        val lp = layoutParams
                        lp.height = cropRotateHeight
                        layoutParams = lp
                    }

                    val url = data.removeBackgroundUrl ?: data.originalUrl
                    val uri = Uri.fromFile(File(url))

                    if (data.isToolCrop()) {
                        rotateComponent.container().hide()
                        viewBinding?.imgUcropPreview?.initializeCrop(uri, this@DetailEditorFragment)
                    } else {
                        cropComponent.container().hide()
                        viewBinding?.imgUcropPreview?.setOverlayRotate()
                        viewBinding?.imgUcropPreview?.initializeRotate(
                            uri,
                            this@DetailEditorFragment,
                            data
                        )
                    }
                }
            } else {
                rotateComponent.container().hide()
                cropComponent.container().hide()
            }
        }
    }

    private fun observeLoader() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            viewBinding?.ldrPreview?.showWithCondition(it)
        }
    }

    private fun observeWatermark() {
        viewModel.watermarkFilter.observe(viewLifecycleOwner) { watermarkBitmap ->
            // if watermark tool just implement the result, on another tools need to neutralize rotate value
            val usedImage = if (!data.isToolCrop() && !data.isToolRotate()) {
                watermarkBitmap
            } else {
                neutralizeWatermarkResult(watermarkBitmap)
            }
            getImageView()?.setImageBitmap(usedImage)
        }
    }

    private fun renderUiComponent(@EditorToolType type: Int) {
        val url = data.removeBackgroundUrl ?: data.originalUrl

        // UI crop & rotate initialize on editor param observe
        when (type) {
            EditorToolType.BRIGHTNESS -> {
                val brightnessValue = data.brightnessValue ?: DEFAULT_VALUE_BRIGHTNESS
                setImageView(url, true)
                brightnessComponent.setupView(brightnessValue)
            }
            // ==========
            EditorToolType.CONTRAST -> {
                val contrastValue = data.contrastValue ?: DEFAULT_VALUE_CONTRAST
                setImageView(url, true)
                contrastComponent.setupView(contrastValue)
            }
            // ==========
            EditorToolType.REMOVE_BACKGROUND -> {
                val removeBgUrl = data.resultUrl ?: url
                setImageView(removeBgUrl, false)
                removeBgComponent.setupView()
            }
            // ==========
            EditorToolType.WATERMARK -> {
                setImageView(url, true)
                watermarkComponent.setupView()
            }
            // ==========
            EditorToolType.ADD_LOGO -> {
                val addLogoUrl = data.resultUrl ?: url
                setImageView(addLogoUrl, false) {
                    // init add logo when image is already done (waiting for image size)
                    addLogoComponent.setupView(
                        originalImageWidth,
                        originalImageHeight,
                        avatarUrl = viewModel.getAvatarShop(),
                        localAvatarUrl = viewModel.getLocalLogo(),
                        data.addLogoValue
                    )
                }
            }
        }
    }

    private fun implementPreviousStateBrightness(
        previousValue: Float?
    ) {
        viewModel.setBrightness(previousValue ?: 0f)
    }

    private fun implementPreviousStateContrast(previousValue: Float?) {
        viewModel.setContrast(previousValue ?: 0f, getBitmap())
    }

    private fun implementPreviousStateCrop(cropRotateData: EditorCropRotateUiModel) {
        if (cropRotateData.imageWidth == 0 && cropRotateData.imageHeight == 0) return
        viewBinding?.imgUcropPreview?.let {
            val cropView = it.cropImageView
            val overlayView = it.overlayView

            cropView.zoomInImage(cropRotateData.scale)
            overlayView.setTargetAspectRatio(cropRotateData.imageWidth.toFloat() / cropRotateData.imageHeight)

            // if crop state is not produce from auto crop on beginning of landing page
            if (!cropRotateData.isAutoCrop) {
                cropView.postDelayed({
                    val cropImageMatrix = cropView.imageMatrix.values()
                    val deltaX = (cropImageMatrix[2] * -1) + cropRotateData.translateX
                    val deltaY = (cropImageMatrix[5] * -1) + cropRotateData.translateY

                    cropView.postTranslate(deltaX, deltaY)

                    cropView.setImageToWrapCropBounds(false)

                    // set default crop active ratio if already cropped by autoCrop / rotate
                    if (data.isToolCrop()) {
                        cropView.post {
                            viewBinding?.imgUcropPreview?.cropImageView?.post {
                                cropComponent.setActiveCropRatio(data.cropRotateValue.cropRatio)
                            }
                        }
                    }
                }, DELAY_EXECUTION_PREVIOUS_CROP)
            }
        }
    }

    private fun implementPreviousStateRotate(cropRotateData: EditorCropRotateUiModel) {
        if (cropRotateData.imageWidth == 0 && cropRotateData.imageHeight == 0) return
        viewBinding?.imgUcropPreview?.let {
            val cropView = it.cropImageView

            val rotateDegree =
                (cropRotateData.rotateDegree + (cropRotateData.orientationChangeNumber * ROTATE_BTN_DEGREE))

            // need to check if previous value is rotate / not, if rotated then ratio is changed
            val isRotate = cropRotateData.orientationChangeNumber % 2 == 1

            cropView.scaleX = cropRotateData.scaleX
            cropView.scaleY = cropRotateData.scaleY
            viewModel.setRotate(it, rotateDegree, isRotate, isPreviousState = true)
            viewModel.rotateNumber = cropRotateData.orientationChangeNumber
            viewModel.rotateSliderValue = cropRotateData.rotateDegree
            viewModel.rotatePreviousDegree =
                cropRotateData.rotateDegree * cropRotateData.scaleX * cropRotateData.scaleY

            cropView.setImageToWrapCropBounds(false)

            cropView.postDelayed({
                implementPreviousStateCrop(cropRotateData)
            }, DELAY_EXECUTION_PREVIOUS_ROTATE)
        }
    }

    private fun implementPreviousWatermark(detailUiModel: EditorDetailUiModel) {
        detailUiModel.watermarkMode?.let {
            if (it.watermarkType != data.watermarkMode?.watermarkType) {
                return
            }

            getBitmap()?.let { bitmap ->
                val finalBitmap = if (!data.isToolCrop() && !data.isToolRotate()) {
                    bitmap
                } else {
                    watermarkRotateBitmap(detailUiModel.cropRotateValue, bitmap)
                }

                WatermarkType.map(it.watermarkType)?.let { type ->
                    viewModel.setWatermark(
                        finalBitmap,
                        type,
                        detailUiModel = detailUiModel,
                        useStorageColor = true
                    )

                    watermarkComponent.setWatermarkTypeSelected(type)
                }
            }
        }
    }

    private fun watermarkRotateBitmap(
        rotateValue: EditorCropRotateUiModel,
        source: Bitmap,
        isInverse: Boolean = false
    ): Bitmap {
        var finalRotateDegree = rotateValue.let {
            it.rotateDegree + (it.orientationChangeNumber * ROTATE_BTN_DEGREE)
        }

        val mirrorX = rotateValue.scaleX
        val mirrorY = rotateValue.scaleY
        val matrix = Matrix()

        // rotate is used on image that sent to watermark, inverse is used on watermark image result
        if (isInverse) {
            finalRotateDegree *= -1

            matrix.preRotate(finalRotateDegree)
            matrix.postScale(mirrorX, mirrorY)
        } else {
            matrix.preScale(mirrorX, mirrorY)
            matrix.postRotate(finalRotateDegree)
        }

        if (globalWidth == 0) {
            globalWidth = source.width
            globalHeight = source.height
        }
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    // neutralize rotate value on watermark result
    private fun neutralizeWatermarkResult(watermarkBitmap: Bitmap): Bitmap {
        val neutralizeBitmap =
            watermarkRotateBitmap(data.cropRotateValue, watermarkBitmap, isInverse = true)

        val cropX = (neutralizeBitmap.width - globalWidth) / 2
        val cropY = (neutralizeBitmap.height - globalHeight) / 2
        return Bitmap.createBitmap(neutralizeBitmap, cropX, cropY, globalWidth, globalHeight)
    }

    private fun readPreviousState() {
        var cropScale = 0f
        var latestBrightnessIndex = -1
        var latestContrastIndex = -1

        detailState.getFilteredStateList().forEachIndexed { index, editorDetailUi ->
            if (editorDetailUi.cropRotateValue.isCrop) cropScale =
                editorDetailUi.cropRotateValue.scale

            if (editorDetailUi.isToolBrightness()) latestBrightnessIndex = index

            if (editorDetailUi.isToolContrast()) latestContrastIndex = index
        }

        implementBrightnessAndContrast(latestBrightnessIndex, latestContrastIndex)

        if (viewBinding?.imgUcropPreview?.isVisible == false && data.cropRotateValue.imageWidth != 0) {
            manualCropBitmap(data.cropRotateValue)
        }

        // need to provide sequence for watermark that implemented before / after rotate
        if (!data.isToolWatermark()) {
            implementPreviousWatermark(data)
        }

        if ((data.isToolRotate() || data.isToolCrop()) && data.cropRotateValue.imageWidth != 0) {
            implementPreviousStateRotate(data.cropRotateValue)
            if (cropScale != 0f) viewModel.rotateInitialScale = cropScale
        }

        implementedBaseBitmap = getBitmap()
        readPreviousDetailState(data)
    }

    private fun readPreviousDetailState(previousState: EditorDetailUiModel) {
        previousState.apply {
            when (editorToolType) {
                EditorToolType.BRIGHTNESS -> implementPreviousStateBrightness(brightnessValue)
                EditorToolType.CONTRAST -> implementPreviousStateContrast(contrastValue)
                EditorToolType.WATERMARK -> implementPreviousWatermark(previousState)
            }
        }
    }

    private fun implementBrightnessAndContrast(brightnessIndex: Int, contrastIndex: Int) {
        if (brightnessIndex < contrastIndex) {
            if (!data.isToolBrightness()) implementPreviousStateBrightness(data.brightnessValue)
            if (!data.isToolContrast()) implementPreviousStateContrast(data.contrastValue)
        } else {
            if (!data.isToolContrast()) implementPreviousStateContrast(data.contrastValue)
            if (!data.isToolBrightness()) implementPreviousStateBrightness(data.brightnessValue)
        }
    }

    private fun manualCropBitmap(cropRotateData: EditorCropRotateUiModel) {
        val currentBitmap = getBitmap()
        currentBitmap?.let {
            // need normalize between crop data and loaded image data, uCrop bound the loaded image size according to the view size
            // but glide will load the image full size
            val scalingSize = if (cropRotateData.croppedSourceWidth != 0) {
                it.width.toFloat() / cropRotateData.croppedSourceWidth
            } else {
                1f
            }

            val finalRotationDegree =
                (cropRotateData.orientationChangeNumber * ROTATE_BTN_DEGREE) + (cropRotateData.rotateDegree)

            val offsetX = (cropRotateData.offsetX * scalingSize).toInt()
            val imageWidth = (cropRotateData.imageWidth * scalingSize).toInt()

            val offsetY = (cropRotateData.offsetY * scalingSize).toInt()
            val imageHeight = (cropRotateData.imageHeight * scalingSize).toInt()

            val mirrorMatrix = Matrix()
            mirrorMatrix.preScale(cropRotateData.scaleX, cropRotateData.scaleY)
            val mirroredBitmap =
                Bitmap.createBitmap(it, 0, 0, it.width, it.height, mirrorMatrix, true)

            // get processed, since data param is set to be null then other data value is not necessary
            val bitmapResult = viewBinding?.imgUcropPreview?.getProcessedBitmap(
                mirroredBitmap,
                offsetX,
                offsetY,
                imageWidth,
                imageHeight,
                finalRotationDegree,
                cropRotateData.rotateDegree,
                cropRotateData.orientationChangeNumber,
                null,
                0f,
                0f,
                0f,
                isRotate = false,
                isCrop = false,
                1f,
                1f
            )


            viewBinding?.imgViewPreview?.setImageBitmap(bitmapResult)
        }
    }

    private fun setWatermarkDrawerItem(bitmap: Bitmap) {
        val bitmapResult = viewModel.setWatermarkFilterThumbnail(
            bitmap
        )

        watermarkComponent.getButtonRef().apply {
            val roundedCorner =
                requireContext().resources.getDimension(editorR.dimen.editor_watermark_rounded)

            first.loadImageRounded(bitmapResult.first, roundedCorner) {
                centerCrop()
            }
            second.loadImageRounded(bitmapResult.second, roundedCorner) {
                centerCrop()
            }
        }
    }

    private fun initButtonListener() {
        viewBinding?.btnCancel?.setOnClickListener {
            activity?.finish()
        }

        viewBinding?.btnSave?.setOnClickListener { _ ->
            if (data.isToolCrop()) {
                // check if user move crop area via image matrix translation, works for crop
                initialImageMatrix?.values()?.let { initialMatrixValue ->
                    val currentMatrix =
                        viewBinding?.imgUcropPreview?.cropImageView?.imageMatrix?.values()
                    currentMatrix?.let {
                        initialMatrixValue.forEachIndexed { index, value ->
                            if (value != currentMatrix[index]) {
                                isEdited = true
                            }
                        }
                    }
                }
            }

            // if no editing perform, then skip save
            if (isEdited) {
                if (data.isToolRemoveBackground()) {
                    showRemoveBackgroundSaveConfirmation {
                        onEditSaveAnalytics()
                        saveAndExit()
                    }
                } else {
                    onEditSaveAnalytics()
                    saveAndExit()
                }
            } else {
                activity?.finish()
            }
        }
    }

    private fun showRemoveBackgroundSaveConfirmation(onPrimaryClick: () -> Unit) {
        DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(editorR.string.editor_remove_bg_dialog_title))
            setDescription(getString(editorR.string.editor_remove_bg_dialog_desc))

            dialogPrimaryCTA.apply {
                text = getString(editorR.string.editor_remove_bg_dialog_primary_button_text)
                setOnClickListener {
                    onPrimaryClick()
                }
            }

            dialogSecondaryLongCTA.apply {
                text = getString(editorR.string.editor_remove_bg_dialog_secondary_button_text)
                setOnClickListener {
                    dismiss()
                }
            }

            show()
        }
    }

    private fun finishPage() {
        val intent = Intent()

        if (data.isToolRemoveBackground()) data.clearValue()

        intent.putExtra(DetailEditorActivity.EDITOR_RESULT_PARAM, data)
        activity?.setResult(DetailEditorActivity.EDITOR_RESULT_CODE, intent)
        activity?.finish()
    }

    private fun getImageView(): ImageView? {
        viewBinding?.let {
            return if (it.imgUcropPreview.isVisible) it.imgUcropPreview.cropImageView else it.imgViewPreview
        }
        return null
    }

    private fun getBitmap(): Bitmap? {
        return getImageView()?.drawable?.toBitmap()
    }

    private fun setImageView(
        url: String,
        readPreviousValue: Boolean,
        onImageReady: () -> Unit = {}
    ) {
        viewBinding?.imgUcropPreview?.hide()
        viewBinding?.imgViewPreview?.visible()

        loadImageWithEmptyTarget(requireContext(),
            url,
            properties = {},
            mediaTarget = MediaBitmapEmptyTarget(
                onReady = { bitmap ->
                    originalImageWidth = bitmap.width
                    originalImageHeight = bitmap.height

                    viewBinding?.imgViewPreview?.setImageBitmap(bitmap)

                    if (readPreviousValue) {
                        readPreviousState()
                        viewBinding?.imgViewPreview?.let {
                            setOverlaySize(
                                getDisplayedImageSize(
                                    viewBinding?.imgViewPreview,
                                    it.drawable.toBitmap()
                                )
                            )
                        }
                    } else {
                        implementedBaseBitmap = bitmap
                        setOverlaySize(getDisplayedImageSize(viewBinding?.imgViewPreview, bitmap))
                    }

                    if (data.isToolWatermark()) {
                        setWatermarkDrawerItem(bitmap)
                        watermarkComponent.setWatermarkTypeSelected(
                            WatermarkType.map(data.watermarkMode?.watermarkType)
                        )
                    }

                    onImageReady()
                },
                onCleared = {}
            ))
    }

    private fun setOverlaySize(displaySize: Pair<Float, Float>?) {
        if (data.isToolCrop()) return
        displaySize?.let {
            viewBinding?.imgPreviewOverlay?.apply {
                val lp = layoutParams

                lp.width = it.first.toInt()
                lp.height = it.second.toInt()

                layoutParams = lp

                post {
                    this.loadImage(data.addLogoValue.overlayLogoUrl) {
                        setPlaceHolder(-1)
                    }
                }
            }
        }
    }

    private fun getDisplayedImageSize(view: View?, bitmap: Bitmap): Pair<Float, Float>? {
        if (view == null) return null
        val imageViewHeight = view.height
        val imageViewWidth = view.width
        val bitmapHeight = bitmap.height
        val bitmapWidth = bitmap.width

        val actualHeight: Float
        val actualWidth: Float
        if (imageViewHeight * bitmapWidth <= imageViewWidth * bitmapHeight) {
            actualWidth = bitmapWidth * imageViewHeight.toFloat() / bitmapHeight
            actualHeight = imageViewHeight.toFloat()
        } else {
            actualHeight = bitmapHeight * imageViewWidth.toFloat() / bitmapWidth
            actualWidth = imageViewWidth.toFloat()
        }

        return Pair(actualWidth, actualHeight)
    }

    private fun getImagePairRatio(): Pair<Float, Float>? {
        data.cropRotateValue.getRatio()?.let {
            return Pair(
                data.cropRotateValue.imageWidth / data.cropRotateValue.imageHeight.toFloat(),
                data.cropRotateValue.imageHeight / data.cropRotateValue.imageWidth.toFloat()
            )
        } ?: kotlin.run {
            getBitmap()?.let {
                return Pair(
                    it.width / it.height.toFloat(),
                    it.height / it.width.toFloat()
                )
            }
        }
        return null
    }

    private fun onEditSaveAnalytics() {
        getHistoryState().apply {
            val cropRatioText = cropRatioToText(cropRotateValue.cropRatio)
            val removeBackgroundText = removeBackgroundToText(removeBackgroundColor)
            val watermarkText = watermarkToText(watermarkMode?.watermarkType)
            val brightnessText = brightnessValue?.toInt() ?: 0
            val contrastText = contrastValue?.toInt() ?: 0
            val rotateText = if (!data.isToolRotate()) {
                cropRotateValue.rotateDegree.toInt()
            } else {
                viewModel.rotateSliderValue.toInt()
            }
            val addLogoValue = addLogoToText(addLogoComponent.getLogoState())

            val currentEditorText =
                requireContext().getText(getToolEditorText(data.editorToolType)).toString()
            editorDetailAnalytics.clickSave(
                currentEditorText,
                brightnessText,
                contrastText,
                cropRatioText,
                rotateText,
                watermarkText,
                removeBackgroundText,
                addLogoValue
            )
        }
    }

    private fun getHistoryState(): EditorDetailUiModel {
        val editHistory = EditorDetailUiModel()
        detailState.editList.toMutableList().apply {
            add(data)
            forEach {
                when (it.editorToolType) {
                    EditorToolType.WATERMARK -> editHistory.watermarkMode = it.watermarkMode
                    EditorToolType.CROP, EditorToolType.ROTATE -> editHistory.cropRotateValue =
                        it.cropRotateValue
                    EditorToolType.CONTRAST -> editHistory.contrastValue = it.contrastValue
                    EditorToolType.BRIGHTNESS -> editHistory.brightnessValue = it.brightnessValue
                    EditorToolType.REMOVE_BACKGROUND -> editHistory.removeBackgroundColor =
                        it.removeBackgroundColor
                }
            }
        }

        return editHistory
    }

    private fun updateAddLogoOverlay(
        newSize: Pair<Int, Int>,
        onFinish: (filePath: String) -> Unit
    ) {
        loadImageWithEmptyTarget(requireContext(),
            data.addLogoValue.logoUrl,
            {},
            MediaBitmapEmptyTarget(
                onReady = { logoBitmap ->
                    viewModel.saveImageCache(
                        addLogoComponent.generateOverlayImage(
                            logoBitmap,
                            newSize,
                            isCircular = data.addLogoValue.logoUrl.contains(HTTPS_KEY)
                        ), sourcePath = PNG_KEY
                    )?.let { fileResult ->
                        data.addLogoValue.overlayLogoUrl = fileResult.path
                        onFinish(fileResult.path)
                    }
                }
            ))
    }

    fun showAddLogoUploadTips(isUpload: Boolean = true) {
        addLogoComponent.bottomSheet(isUpload).show(childFragmentManager, bottomSheetTag)
        isAddLogoTipsShowed = true
    }

    private fun showAddLogoPicker() {
        val intent = MediaPicker.intent(requireContext()) {
            withoutEditor()
            pageType(PageType.GALLERY)
            modeType(ModeType.IMAGE_ONLY)
            minImageResolution(ADD_LOGO_IMAGE_RES_MIN)
            maxImageResolution(ADD_LOGO_IMAGE_RES_MAX)
            pageSource(pickerParam.get().pageSource())
            subPageSource(PageSource.AddLogo)
            singleSelectionMode()
        }

        startActivityForResult(intent, ADD_LOGO_PICKER_REQUEST_CODE)
    }

    private fun removeBgConnectionToast(ctaAction: () -> Unit): Snackbar? {
        viewBinding?.let {
            return Toaster.build(
                it.editorFragmentDetailRoot,
                getString(editorR.string.editor_tool_remove_background_failed_normal),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(editorR.string.editor_tool_remove_background_failed_cta)
            ) { ctaAction() }
        }
        return null
    }

    private fun removeBgClosePage() {
        Toast.makeText(
            requireContext(),
            getString(editorR.string.editor_tool_remove_background_failed_normal),
            Toast.LENGTH_LONG
        ).show()
        activity?.finish()
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Detail Editor"

        private const val DEFAULT_VALUE_CONTRAST = 0f
        private const val DEFAULT_VALUE_BRIGHTNESS = 0f

        private const val DELAY_EXECUTION_PREVIOUS_CROP = 400L
        private const val DELAY_EXECUTION_PREVIOUS_ROTATE = 400L
        private const val DELAY_REMOVE_BG_TOASTER = 300L

        private const val bottomSheetTag = "Add Logo BottomSheet"

        private const val ADD_LOGO_PICKER_REQUEST_CODE = 979

        private const val HTTPS_KEY = "https:"

        // key to generate PNG result for AddLogo overlay
        private const val PNG_KEY = "image.png"

        private const val ADD_LOGO_IMAGE_RES_MIN = 500
        private const val ADD_LOGO_IMAGE_RES_MAX = 1000
    }
}
