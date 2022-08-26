package com.tokopedia.media.editor.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.values
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
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
import com.tokopedia.media.editor.ui.widget.EditorDetailPreviewWidget
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
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
    private var implementedBaseBitmap: Bitmap? = null

    private var removeBackgroundRetryLimit = 3

    private var isEdited = false
    private var initialImageMatrix: Matrix? = null

    fun isShowDialogConfirmation(): Boolean {
        return isEdited
    }

    fun saveAndExit() {
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
            viewBinding?.imgUcropPreview?.getBitmap()?.let {
                data.resultUrl = viewModel.saveImageCache(
                    requireContext(),
                    it
                )?.path

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

    override fun onRemoveBackgroundClicked() {
        viewBinding?.imgUcropPreview?.let { _ ->
            data.resultUrl?.let { it ->
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
                                onRemoveBackgroundClicked()
                        }.show()
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
                shopName
            )
            data.watermarkMode = value
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
            true
        )
        isEdited = true
    }

    override fun onCropRatioClicked(ratio: Float) {
        viewBinding?.imgUcropPreview?.let {
            val overlayView = it.overlayView
            val cropView = it.cropImageView

            overlayView.setTargetAspectRatio(ratio)
            cropView.targetAspectRatio = ratio

            isEdited = true
        }
    }

    // EditorDetailPreviewWidget finish load image
    override fun onLoadComplete() {
        if (!data.isToolRemoveBackground()){
            readPreviousState(data)
            if(data.isWatermark()) setWatermarkDrawerItem()
        }
    }

    override fun initObserver() {
        observeIntentUiModel()
        observeLoader()
        observeBrightness()
        observeContrast()
        observeWatermark()
        observeRemoveBackground()
        observeEditorParamModel()
    }

    private fun observeBrightness() {
        viewModel.brightnessFilter.observe(viewLifecycleOwner) {
            viewBinding?.imgUcropPreview?.cropImageView?.colorFilter = it
            isEdited = true
        }
    }

    private fun observeContrast() {
        viewModel.contrastFilter.observe(viewLifecycleOwner) {
            viewBinding?.imgUcropPreview?.cropImageView?.setImageBitmap(it)
            isEdited = true
        }
    }

    private fun observeRemoveBackground() {
        viewModel.removeBackground.observe(viewLifecycleOwner) {
            data.removeBackgroundUrl = it?.path
            viewBinding?.imgUcropPreview?.cropImageView?.loadImage(it?.path)
            isEdited = true
        }
    }

    private fun observeIntentUiModel() {
        viewModel.intentUiModel.observe(viewLifecycleOwner) {
            // make this ui model as global variable
            data = it

            renderUiComponent(it.editorToolType)
        }
    }

    private fun observeEditorParamModel() {
        viewModel.editorParam.observe(viewLifecycleOwner) {
            cropComponent.setupView(it)
        }
    }

    private fun observeLoader() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            viewBinding?.ldrPreview?.showWithCondition(it)
        }
    }

    private fun observeWatermark() {
        viewModel.watermarkFilter.observe(viewLifecycleOwner) { watermarkBitmap ->
            viewBinding?.imgUcropPreview?.cropImageView?.setImageBitmap(watermarkBitmap)
            isEdited = true
        }
    }

    private fun renderUiComponent(@EditorToolType type: Int) {
        val uri = Uri.fromFile(File(data.removeBackgroundUrl ?: data.originalUrl))

        viewBinding?.imgUcropPreview?.apply {
            when (type) {
                EditorToolType.BRIGHTNESS -> {
                    val brightnessValue = data.brightnessValue ?: DEFAULT_VALUE_BRIGHTNESS
                    initializeBrightness(uri, this@DetailEditorFragment)
                    brightnessComponent.setupView(brightnessValue)
                }
                // ==========
                EditorToolType.REMOVE_BACKGROUND -> {
                    initializeRemoveBackground(
                        data.resultUrl?.let {
                            Uri.fromFile(File(it))
                        } ?: uri,
                        this@DetailEditorFragment
                    )
                    removeBgComponent.setupView()
                }
                // ==========
                EditorToolType.CONTRAST -> {
                    val contrastValue = data.contrastValue ?: DEFAULT_VALUE_CONTRAST
                    initializeContrast(uri, this@DetailEditorFragment)
                    contrastComponent.setupView(contrastValue)
                }
                // ==========
                EditorToolType.WATERMARK -> {
                    initializeWatermark(uri, this@DetailEditorFragment)
                    watermarkComponent.setupView()
                }
                // ==========
                EditorToolType.ROTATE -> {
                    initializeRotate(uri, this@DetailEditorFragment)
                    rotateComponent.setupView(data)
                }
                // ==========
                EditorToolType.CROP -> {
                    initializeCrop(uri, this@DetailEditorFragment)
                    initialImageMatrix = viewBinding?.imgUcropPreview?.cropImageView?.imageMatrix
                }
            }
        }
    }

    private fun implementPreviousStateBrightness(
        previousValue: Float?,
        isRemoveFilter: Boolean = true
    ) {
        val cropView = viewBinding?.imgUcropPreview?.cropImageView
        viewModel.setBrightness(previousValue ?: 0f)

        // need to remove the filter to prevent any filter trigger re-apply the brightness color filter
        if (isRemoveFilter) viewBinding?.imgUcropPreview?.getBitmap()?.let {
            cropView?.clearColorFilter()
            cropView?.setImageBitmap(it)
        }
    }

    private fun implementPreviousStateContrast(previousValue: Float?) {

        viewBinding?.imgUcropPreview?.let {
            val bitmap = it.getBitmap()
            viewModel.setContrast(previousValue ?: 0f, bitmap)
        }
    }

    private fun implementPreviousStateCrop(cropRotateData: EditorCropRotateModel) {
        viewBinding?.imgUcropPreview?.let {
            val cropView = it.cropImageView
            val overlayView = it.overlayView

            overlayView.setTargetAspectRatio(cropRotateData.imageWidth / cropRotateData.imageHeight.toFloat())
            overlayView.setupCropBounds()
            cropView.zoomInImage(cropRotateData.scale)

            if (!cropRotateData.isAutoCrop) {
                cropView.post {
                    val cropImageMatrix = cropView.imageMatrix.values()
                    val ax = (cropImageMatrix[2] * -1) + cropRotateData.translateX
                    val ay = (cropImageMatrix[5] * -1) + cropRotateData.translateY
                    cropView.postTranslate(ax, ay)
                }
            }
        }
    }

    private fun implementPreviousStateRotate(cropRotateData: EditorCropRotateModel) {
        viewBinding?.imgUcropPreview?.let {
            val originalAsset = it.getBitmap()

            val cropView = it.cropImageView
            val overlayView = it.overlayView

            val rotateDegree =
                (cropRotateData.rotateDegree + (cropRotateData.orientationChangeNumber * RotateToolUiComponent.ROTATE_BTN_DEGREE))

            // need to check if previous value is rotate / not, if rotated then ratio is changed
            val isRotate = cropRotateData.orientationChangeNumber % 2 == 1

            val originalWidth = originalAsset.width.toFloat()
            val originalHeight = originalAsset.height.toFloat()

            val ratio =
                if (isRotate) originalHeight / originalWidth else originalWidth / originalHeight

            cropView.scaleX = cropRotateData.scaleX
            cropView.scaleY = cropRotateData.scaleY
            viewModel.setRotate(it, rotateDegree, isRotate)
            viewModel.rotateNumber = cropRotateData.orientationChangeNumber
            viewModel.rotateSliderValue = cropRotateData.rotateDegree

            overlayView.setTargetAspectRatio(ratio)

            cropView.setImageToWrapCropBounds()
        }
    }

    private fun readPreviousState(previousState: EditorDetailUiModel) {
        // if current selected editor not brightness and contrast, implement filter with sequence
        if (!previousState.isToolBrightness() && !previousState.isToolContrast()) {
            if (previousState.isContrastExecuteFirst == true) {
                implementPreviousStateContrast(previousState.contrastValue)
                implementPreviousStateBrightness(previousState.brightnessValue)
            } else {
                implementPreviousStateBrightness(previousState.brightnessValue)
                implementPreviousStateContrast(previousState.contrastValue)
            }
        } else {
            // === Contrast ===
            if (previousState.contrastValue != null
                && !previousState.isToolContrast()
            ) {
                implementPreviousStateContrast(previousState.contrastValue ?: 0f)
            }
            // === Brightness ===
            if (previousState.brightnessValue != null
                && !previousState.isToolBrightness()
            ) {
                implementPreviousStateBrightness(previousState.brightnessValue ?: 0f)
            }
        }

        // if crop / rotate tool implement ucrop previous state, if not then create cropped image
        if (data.isToolRotate() || data.isToolCrop()) {
            viewBinding?.imgUcropPreview?.cropImageView?.post {
                data.cropRotateValue.let { cropRotateValue ->
                    if (data.cropRotateValue.isRotate) {
                        implementPreviousStateRotate(cropRotateValue)
                    }

                    if (data.cropRotateValue.isCrop) {
                        implementPreviousStateCrop(cropRotateValue)
                    }
                }
            }
        } else if (data.cropRotateValue.isCrop || data.cropRotateValue.isRotate) {
            val currentBitmap = viewBinding?.imgUcropPreview?.getBitmap()
            val cropRotateData = data.cropRotateValue
            currentBitmap?.let {
                val finalRotationDegree =
                    (cropRotateData.orientationChangeNumber * RotateToolUiComponent.ROTATE_BTN_DEGREE) + (cropRotateData.rotateDegree)

                val offsetX = cropRotateData.offsetX
                val imageWidth = cropRotateData.imageWidth

                val offsetY = cropRotateData.offsetY
                val imageHeight = cropRotateData.imageHeight

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

                viewBinding?.imgUcropPreview?.cropImageView?.setImageBitmap(bitmapResult)
            }
        }

        // image that already implemented previous filter
        implementedBaseBitmap = viewBinding?.imgUcropPreview?.getBitmap()

        if (previousState.brightnessValue != null && previousState.isToolBrightness()) {
            // if current editor is brightness keep the filter color so we can adjust it later
            implementPreviousStateBrightness(previousState.brightnessValue, false)
        } else if (previousState.contrastValue != null && previousState.isToolContrast()) {
            viewModel.setContrast(previousState.contrastValue!!, implementedBaseBitmap!!)
        }
    }

    private fun setWatermarkDrawerItem() {
        implementedBaseBitmap?.let {
            val shopName = if (userSession.shopName.isEmpty())
                DEFAULT_VALUE_SHOP_TEXT else userSession.shopName

            viewModel.setWatermarkFilterThumbnail(
                requireContext(),
                it,
                shopName,
                watermarkComponent.getButtonRef()
            )
        }
    }

    private fun initButtonListener() {
        viewBinding?.btnCancel?.setOnClickListener {
            activity?.finish()
        }

        viewBinding?.btnSave?.setOnClickListener { _ ->
            // check if user move crop area via image matrix translation
            initialImageMatrix?.values()?.let { initialMatrixValue ->
                val currentMatrix =
                    viewBinding?.imgUcropPreview?.cropImageView?.imageMatrix?.values()
                currentMatrix?.let {
                    initialMatrixValue.forEachIndexed { index, value ->
                        if (value != currentMatrix[index]) isEdited = true
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

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Detail Editor"

        private const val DEFAULT_VALUE_CONTRAST = 0f
        private const val DEFAULT_VALUE_BRIGHTNESS = 0f

        private const val DEFAULT_VALUE_SHOP_TEXT = "Shop Name"
    }
}