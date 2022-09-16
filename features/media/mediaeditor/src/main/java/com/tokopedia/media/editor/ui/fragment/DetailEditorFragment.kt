package com.tokopedia.media.editor.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.values
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.base.BaseEditorFragment
import com.tokopedia.media.editor.databinding.FragmentDetailEditorBinding
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorViewModel
import com.tokopedia.media.editor.ui.component.BrightnessToolUiComponent
import com.tokopedia.media.editor.ui.component.ContrastToolsUiComponent
import com.tokopedia.media.editor.ui.component.CropToolUiComponent
import com.tokopedia.media.editor.ui.component.RemoveBackgroundToolUiComponent
import com.tokopedia.media.editor.ui.component.RotateToolUiComponent
import com.tokopedia.media.editor.ui.component.WatermarkToolUiComponent
import com.tokopedia.media.editor.ui.uimodel.EditorCropRotateModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.ui.widget.EditorDetailPreviewWidget
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.R as principleR
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import com.yalantis.ucrop.util.FastBitmapDrawable
import java.io.File
import javax.inject.Inject

class DetailEditorFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val userSession: UserSessionInterface
) : BaseEditorFragment(), BrightnessToolUiComponent.Listener, ContrastToolsUiComponent.Listener,
    RemoveBackgroundToolUiComponent.Listener, WatermarkToolUiComponent.Listener,
    RotateToolUiComponent.Listener, CropToolUiComponent.Listener,
    EditorDetailPreviewWidget.Listener {

    private val viewBinding: FragmentDetailEditorBinding? by viewBinding()
    private val viewModel: DetailEditorViewModel by activityViewModels { viewModelFactory }

    private val brightnessComponent by uiComponent { BrightnessToolUiComponent(it, this) }
    private val removeBgComponent by uiComponent { RemoveBackgroundToolUiComponent(it, this) }
    private val contrastComponent by uiComponent { ContrastToolsUiComponent(it, this) }
    private val watermarkComponent by uiComponent { WatermarkToolUiComponent(it, this) }
    private val rotateComponent by uiComponent { RotateToolUiComponent(it, this) }
    private val cropComponent by uiComponent { CropToolUiComponent(it, this) }

    private var data = EditorDetailUiModel()
    private var detailState = EditorUiModel()

    // original bitmap that already implement previous state
    // start point for each editor detail
    private var implementedBaseBitmap: Bitmap? = null

    private var removeBackgroundRetryLimit = 3
    private var removeBackgroundType = 0

    private var isEdited = false
    private var initialImageMatrix: Matrix? = null

    fun isShowDialogConfirmation(): Boolean {
        return isEdited
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
            // if current tools editor not rotate then skip crop data set by sent empty object on data
            viewBinding?.imgUcropPreview?.cropRotate(
                finalRotationDegree = viewModel.rotateRotationFinalDegree,
                sliderValue = viewModel.rotateSliderValue,
                rotateNumber = viewModel.rotateNumber,
                data
            ) {
                data.resultUrl = viewModel.saveImageCache(
                    requireContext(),
                    it
                )?.path

                finishPage()
            }
        } else {
            getBitmap()?.let {
                data.resultUrl = viewModel.saveImageCache(
                    requireContext(),
                    it
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

                if (removeBgType == RemoveBackgroundToolUiComponent.REMOVE_BG_TYPE_ORI) {
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
                    viewModel.setRemoveBackground(it) { _ ->
                        viewBinding?.let {
                            Toaster.build(
                                it.editorFragmentDetailRoot,
                                getString(editorR.string.editor_tool_remove_background_failed_normal),
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_NORMAL,
                                getString(editorR.string.editor_tool_remove_background_failed_cta)
                            ) {
                                removeBackgroundRetryLimit--
                                if (removeBackgroundRetryLimit == 0) {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(editorR.string.editor_tool_remove_background_failed_normal),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    activity?.finish()
                                } else
                                    onRemoveBackgroundClicked(removeBackgroundType)
                            }.show()
                        }
                    }
                }
            }
        }
    }

    override fun onWatermarkChanged(value: Int) {
        implementedBaseBitmap?.let {
            val shopName = if (userSession.shopName.isEmpty())
                DEFAULT_VALUE_SHOP_TEXT else userSession.shopName
            viewModel.setWatermark(
                requireContext(),
                it,
                value,
                shopName,
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
        viewModel.setMirror(viewBinding?.imgUcropPreview)
        isEdited = true
    }

    override fun onImageRotate(rotateDegree: Float) {
        viewModel.setRotate(
            viewBinding?.imgUcropPreview,
            RotateToolUiComponent.ROTATE_BTN_DEGREE,
            true,
            getImagePairRatio()
        )
        isEdited = true
    }

    override fun onCropRatioClicked(ratio: Float) {
        viewBinding?.imgUcropPreview?.let {
            val overlayView = it.overlayView
            val cropView = it.cropImageView

            overlayView.setTargetAspectRatio(ratio)
            cropView.targetAspectRatio = ratio
        }
    }

    // EditorDetailPreviewWidget finish load image
    override fun onLoadComplete() {
        viewBinding?.imgUcropPreview?.cropImageView?.post {
            readPreviousState()
            initialImageMatrix = Matrix(viewBinding?.imgUcropPreview?.cropImageView?.imageMatrix)
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
                                RemoveBackgroundToolUiComponent.REMOVE_BG_TYPE_GRAY -> principleR.color.Unify_NN200
                                RemoveBackgroundToolUiComponent.REMOVE_BG_TYPE_WHITE -> principleR.color.Unify_Static_White
                                else -> null
                            }?.let {
                                val color = ContextCompat.getColor(requireContext(), it)

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
        viewModel.intentUiModel.observe(viewLifecycleOwner) {
            // make this ui model as global variable
            data = it

            renderUiComponent(it.editorToolType)
        }
    }

    private fun observeIntentUiState() {
        viewModel.intentStateList.observe(viewLifecycleOwner) {
            detailState = it
        }
    }

    private fun observeEditorParamModel() {
        viewModel.editorParam.observe(viewLifecycleOwner) {
            if (data.isToolCrop()) cropComponent.setupView(it, data.cropRotateValue)
        }
    }

    private fun observeLoader() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            viewBinding?.ldrPreview?.showWithCondition(it)
        }
    }

    private fun observeWatermark() {
        viewModel.watermarkFilter.observe(viewLifecycleOwner) { watermarkBitmap ->
            getImageView()?.setImageBitmap(watermarkBitmap)
        }
    }

    private fun renderUiComponent(@EditorToolType type: Int) {
        val url = data.removeBackgroundUrl ?: data.originalUrl
        val uri = Uri.fromFile(File(url))

        viewBinding?.imgUcropPreview?.apply {
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
                EditorToolType.ROTATE -> {
                    initializeRotate(uri, this@DetailEditorFragment, data)
                    rotateComponent.setupView(data)
                }
                // ==========
                EditorToolType.CROP -> {
                    initializeCrop(uri, this@DetailEditorFragment)
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

    private fun implementPreviousStateCrop(cropRotateData: EditorCropRotateModel) {
        if (cropRotateData.imageWidth == 0 && cropRotateData.imageHeight == 0) return
        viewBinding?.imgUcropPreview?.let {
            val cropView = it.cropImageView
            val overlayView = it.overlayView

            overlayView.setTargetAspectRatio(cropRotateData.imageWidth / cropRotateData.imageHeight.toFloat())
            overlayView.setupCropBounds()
            cropView.zoomInImage(cropRotateData.scale)

            // if crop state is not produce from auto crop on beginning of landing page
            if (!cropRotateData.isAutoCrop) {
                cropView.postDelayed({
                    val cropImageMatrix = cropView.imageMatrix.values()
                    val deltaX = (cropImageMatrix[2] * -1) + cropRotateData.translateX
                    val deltaY = (cropImageMatrix[5] * -1) + cropRotateData.translateY
                    cropView.postTranslate(deltaX, deltaY)

                    cropView.setImageToWrapCropBounds(false)
                }, DELAY_IMPLEMENT_CROP)
            }
        }
    }

    private fun implementPreviousStateRotate(cropRotateData: EditorCropRotateModel) {
        if (cropRotateData.imageWidth == 0 && cropRotateData.imageHeight == 0) return
        viewBinding?.imgUcropPreview?.let {
            val cropView = it.cropImageView

            val rotateDegree =
                (cropRotateData.rotateDegree + (cropRotateData.orientationChangeNumber * RotateToolUiComponent.ROTATE_BTN_DEGREE))

            // need to check if previous value is rotate / not, if rotated then ratio is changed
            val isRotate = cropRotateData.orientationChangeNumber % 2 == 1

            cropView.scaleX = cropRotateData.scaleX
            cropView.scaleY = cropRotateData.scaleY
            viewModel.setRotate(it, rotateDegree, isRotate)
            viewModel.rotateNumber = cropRotateData.orientationChangeNumber
            viewModel.rotateSliderValue = cropRotateData.rotateDegree
            viewModel.rotateInitialScale = cropRotateData.scale

            cropView.setImageToWrapCropBounds(false)
        }
    }

    private fun implementPreviousWatermark(detailUiModel: EditorDetailUiModel?) {
        detailUiModel?.let {
            getBitmap()?.let { bitmap ->
                val shopName = if (userSession.shopName.isEmpty())
                    DEFAULT_VALUE_SHOP_TEXT else userSession.shopName

                it.watermarkMode?.let { watermarkData ->
                    viewModel.setWatermark(
                        requireContext(),
                        bitmap,
                        watermarkData.watermarkType,
                        shopName,
                        detailUiModel = it,
                        useStorageColor = true
                    )
                }

                watermarkComponent.setWatermarkTypeSelected(it.watermarkMode?.watermarkType)
            }
        }
    }

    private fun readPreviousState() {
        if (viewBinding?.imgUcropPreview?.isVisible == false && data.cropRotateValue.imageWidth != 0) {
            manualCropBitmap(data.cropRotateValue)
        }

        detailState.getFilteredStateList().forEach { editorDetailUi ->
            if (editorDetailUi.editorToolType == data.editorToolType) return@forEach
            readPreviousDetailState(editorDetailUi)
        }

        if(data.isToolRotate() || data.isToolCrop()) {
            implementPreviousStateRotate(data.cropRotateValue)
            implementPreviousStateCrop(data.cropRotateValue)
        }

        implementedBaseBitmap = getBitmap()
        readPreviousDetailState(data)
    }

    private fun readPreviousDetailState(previousState: EditorDetailUiModel) {
        previousState.apply {
            when (editorToolType) {
                EditorToolType.BRIGHTNESS -> implementPreviousStateBrightness(brightnessValue)
                EditorToolType.CONTRAST -> implementPreviousStateContrast(contrastValue)
                EditorToolType.WATERMARK -> implementPreviousWatermark(this)
            }
        }
    }

    private fun manualCropBitmap(cropRotateData: EditorCropRotateModel) {
        val currentBitmap = getBitmap()
        currentBitmap?.let {
            // need normalize between crop data and loaded image data, ucrop bound the loaded image size according to the view size
            // but glide will load the image full size
            val scalingSize = it.width.toFloat() / cropRotateData.croppedSourceWidth

            val finalRotationDegree =
                (cropRotateData.orientationChangeNumber * RotateToolUiComponent.ROTATE_BTN_DEGREE) + (cropRotateData.rotateDegree)

            val offsetX = (cropRotateData.offsetX * scalingSize).toInt()
            val imageWidth = (cropRotateData.imageWidth * scalingSize).toInt()

            val offsetY = (cropRotateData.offsetY * scalingSize).toInt()
            val imageHeight = (cropRotateData.imageHeight * scalingSize).toInt()

            // get processed, since data param is set to be null then other data value is not necessary
            val bitmapResult = viewBinding?.imgUcropPreview?.getProcessedBitmap(
                it,
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
                cropRotateData.scaleX,
                cropRotateData.scaleY
            )


            viewBinding?.imgViewPreview?.setImageBitmap(bitmapResult)
        }
    }

    private fun setWatermarkDrawerItem(bitmap: Bitmap) {
        val shopName = if (userSession.shopName.isEmpty())
            DEFAULT_VALUE_SHOP_TEXT else userSession.shopName

        viewModel.setWatermarkFilterThumbnail(
            requireContext(),
            bitmap,
            shopName,
            watermarkComponent.getButtonRef()
        )
    }

    private fun initButtonListener() {
        viewBinding?.btnCancel?.setOnClickListener {
            activity?.finish()
        }

        viewBinding?.btnSave?.setOnClickListener { _ ->
            // check if user move crop area via image matrix translation, works for crop
            initialImageMatrix?.values()?.let { initialMatrixValue ->
                val currentMatrix =
                    viewBinding?.imgUcropPreview?.cropImageView?.imageMatrix?.values()
                currentMatrix?.let {
                    initialMatrixValue.forEachIndexed { index, value ->
                        if (value != currentMatrix[index]) isEdited = true
                    }
                }
            }

            // check if current ratio is same as source, works for crop
            (data.cropRotateValue.getRatio() ?: data.originalRatio).let {
                viewBinding?.imgUcropPreview?.overlayView?.let { overlayView ->
                    if (it != (overlayView.cropViewRect.width() / overlayView.cropViewRect.height())){
                        isEdited = true
                    }
                }
            }

            // if no editing perform, then skip save
            if (isEdited) {
                if (data.isToolRemoveBackground()) {
                    showRemoveBackgroundSaveConfirmation {
                        saveAndExit()
                    }
                } else {
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
                    hide()
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

    private fun setImageView(url: String, readPreviousValue: Boolean) {
        viewBinding?.imgUcropPreview?.hide()
        viewBinding?.imgViewPreview?.visible()

        loadImageWithEmptyTarget(requireContext(),
            url,
            properties = {},
            mediaTarget = MediaBitmapEmptyTarget(
                onReady = { bitmap ->
                    viewBinding?.imgViewPreview?.setImageBitmap(bitmap)

                    if (readPreviousValue) readPreviousState()
                    else implementedBaseBitmap = bitmap

                    if (data.isToolWatermark()) {
                        setWatermarkDrawerItem(bitmap)
                    }
                },
                onCleared = {}
            ))
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

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Detail Editor"

        private const val DEFAULT_VALUE_CONTRAST = 0f
        private const val DEFAULT_VALUE_BRIGHTNESS = 0f

        private const val DEFAULT_VALUE_SHOP_TEXT = "Shop Name"

        private const val DELAY_IMPLEMENT_CROP = 300L
    }
}