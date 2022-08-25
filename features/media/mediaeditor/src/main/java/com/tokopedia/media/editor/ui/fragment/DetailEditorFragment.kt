package com.tokopedia.media.editor.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.values
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.base.BaseEditorFragment
import com.tokopedia.media.editor.data.repository.ColorFilterRepositoryImpl
import com.tokopedia.media.editor.data.repository.ContrastFilterRepositoryImpl
import com.tokopedia.media.editor.data.repository.RotateFilterRepositoryImpl
import com.tokopedia.media.editor.data.repository.WatermarkFilterRepositoryImpl
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
import com.tokopedia.media.editor.utils.getDestinationUri
import com.tokopedia.media.editor.utils.writeBitmapToStorage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.Exception

class DetailEditorFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val userSession: UserSessionInterface
) : BaseEditorFragment(), BrightnessToolUiComponent.Listener, ContrastToolsUiComponent.Listener,
    RemoveBackgroundToolUiComponent.Listener, WatermarkToolUiComponent.Listener,
    RotateToolUiComponent.Listener, CropToolUiComponent.Listener {

    private val viewBinding: FragmentDetailEditorBinding? by viewBinding()
    private val viewModel: DetailEditorViewModel by activityViewModels { viewModelFactory }

    @Inject
    lateinit var watermarkFilterRepositoryImpl: WatermarkFilterRepositoryImpl

    @Inject
    lateinit var rotateFilterRepositoryImpl: RotateFilterRepositoryImpl

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

    fun isShowDialogConfirmation(): Boolean{
        return isEdited
    }

    fun saveAndExit(){
        if (data.isToolRotate() || data.isToolCrop()) {
            // if current tools editor not rotate then skip crop data set by sent empty object on data
            viewBinding?.imgUcropPreview?.cropRotate(
                finalRotationDegree = rotateFilterRepositoryImpl.getFinalRotationDegree(),
                sliderValue = rotateFilterRepositoryImpl.sliderValue,
                rotateNumber = rotateFilterRepositoryImpl.rotateNumber,
                data
            ) {
                writeToStorage(it)
            }
        } else {
            viewBinding?.imgUcropPreview?.getBitmap()?.let {
                writeToStorage(it)
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
        viewModel.setContrast(value)
        data.contrastValue = value
        isEdited = true
    }

    override fun onRemoveBackgroundClicked() {
        viewBinding?.imgUcropPreview?.let { editorDetailPreviewImage ->
            writeToStorage(editorDetailPreviewImage.getBitmap(), isFinish = false)
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
            // set result null, just use it as temporary for save image to cache
            data.resultUrl = null
        }
    }

    override fun onWatermarkChanged(value: Int) {
        viewModel.setWatermark(value)
        data.watermarkMode = value
        isEdited = true
    }

    override fun onRotateValueChanged(rotateValue: Float) {
        rotateFilterRepositoryImpl.rotate(viewBinding?.imgUcropPreview, rotateValue, false)
        isEdited = true
    }

    override fun onImageMirror() {
        rotateFilterRepositoryImpl.mirror(viewBinding?.imgUcropPreview)
        isEdited = true
    }

    override fun onImageRotate(rotateDegree: Float) {
        rotateFilterRepositoryImpl.rotate(
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
            implementedBaseBitmap?.let { itBitmap ->
                val contrastBitmap = viewModel.getContrastFilter(it, itBitmap)

                viewBinding?.imgUcropPreview?.cropImageView?.setImageBitmap(contrastBitmap)
                isEdited = true
            }
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
        viewModel.watermarkFilter.observe(viewLifecycleOwner) { watermarkType ->
            implementedBaseBitmap?.let { bitmap ->
                val text = if (userSession.shopName.isEmpty())
                    DEFAULT_VALUE_SHOP_TEXT else userSession.shopName
                val result = watermarkFilterRepositoryImpl.watermark(
                    requireContext(),
                    bitmap,
                    watermarkType,
                    text,
                    false
                )
                viewBinding?.imgUcropPreview?.cropImageView?.setImageBitmap(result)
                isEdited = true
            }
        }
    }

    private fun renderUiComponent(@EditorToolType type: Int) {
        val uri = Uri.fromFile(File(data.removeBackgroundUrl ?: data.originalUrl))

        when (type) {
            EditorToolType.BRIGHTNESS -> {
                val brightnessValue = data.brightnessValue ?: DEFAULT_VALUE_BRIGHTNESS
                viewBinding?.imgUcropPreview?.apply {
                    initializeBrightness(uri)
                    onLoadComplete = {
                        readPreviousState(data)
                    }
                }
                brightnessComponent.setupView(brightnessValue)
            }
            // ==========
            EditorToolType.REMOVE_BACKGROUND -> {
                viewBinding?.imgUcropPreview?.apply {
                    initializeRemoveBackground(uri)
                    onLoadComplete = {
                        readPreviousState(data)
                    }
                }
                removeBgComponent.setupView()
            }
            // ==========
            EditorToolType.CONTRAST -> {
                val contrastValue = data.contrastValue ?: DEFAULT_VALUE_CONTRAST
                viewBinding?.imgUcropPreview?.apply {
                    initializeContrast(uri)
                    onLoadComplete = {
                        readPreviousState(data)
                    }
                }
                contrastComponent.setupView(contrastValue)
            }
            // ==========
            EditorToolType.WATERMARK -> {
                viewBinding?.imgUcropPreview?.apply {
                    initializeWatermark(uri)
                    onLoadComplete = {
                        readPreviousState(data)
                        setWatermarkDrawerItem()
                    }
                }

                watermarkComponent.setupView()
            }
            // ==========
            EditorToolType.ROTATE -> {
                viewBinding?.imgUcropPreview?.apply {
                    initializeRotate(uri)
                    onLoadComplete = {
                        readPreviousState(data)
                    }
                }
                rotateComponent.setupView(data)
            }
            // ==========
            EditorToolType.CROP -> {
                viewBinding?.imgUcropPreview?.apply {
                    initializeCrop(uri)
                    onLoadComplete = {
                        readPreviousState(data)
                    }
                }

                initialImageMatrix = viewBinding?.imgUcropPreview?.cropImageView?.imageMatrix
            }
        }
    }

    private fun implementPreviousStateBrightness(
        previousValue: Float?,
        isRemoveFilter: Boolean = true
    ) {
        val cropView = viewBinding?.imgUcropPreview?.cropImageView
        cropView?.colorFilter = viewModel.getBrightnessFilter(previousValue ?: 0f)

        // need to remove the filter to prevent any filter trigger re-apply the brightness color filter
        if (isRemoveFilter) viewBinding?.imgUcropPreview?.getBitmap()?.let {
            cropView?.clearColorFilter()
            cropView?.setImageBitmap(it)
        }
    }

    private fun implementPreviousStateContrast(previousValue: Float?) {
        viewBinding?.imgUcropPreview?.let {
            val bitmap = it.getBitmap()
            it.cropImageView.setImageBitmap(
                viewModel.getContrastFilter(
                    previousValue ?: 0f,
                    bitmap
                )
            )
        }
    }

    private fun implementPreviousStateRotate(cropRotateData: EditorCropRotateModel) {
        viewBinding?.imgUcropPreview?.let {
            val originalAsset = it.getBitmap()

            val cropView = it.cropImageView
            val overlayView = it.overlayView

            val rotateDegree = (cropRotateData.rotateDegree + (cropRotateData.orientationChangeNumber * RotateToolUiComponent.ROTATE_BTN_DEGREE))

            // read previous value & implement if current editor is rotate
            cropView.post {
                // === Read previous ROTATE data & implement
                if(cropRotateData.isRotate){
                    // need to check if previous value is rotate / not, if rotated then ratio is changed
                    val isRotate = cropRotateData.orientationChangeNumber % 2 == 1

                    val originalWidth = originalAsset.width.toFloat()
                    val originalHeight = originalAsset.height.toFloat()

                    val ratio = if(isRotate) originalHeight / originalWidth else originalWidth / originalHeight

                    cropView.scaleX = cropRotateData.scaleX
                    cropView.scaleY = cropRotateData.scaleY
                    rotateFilterRepositoryImpl.rotate(it, rotateDegree, isRotate)
                    rotateFilterRepositoryImpl.rotateNumber = cropRotateData.orientationChangeNumber
                    rotateFilterRepositoryImpl.sliderValue = cropRotateData.rotateDegree

                    overlayView.setTargetAspectRatio(ratio)

                    cropView.setImageToWrapCropBounds()
                }

                // == Read previous CROP data & implement
                if(cropRotateData.isCrop) {
                    overlayView.setTargetAspectRatio(cropRotateData.imageWidth / cropRotateData.imageHeight.toFloat())
                    overlayView.setupCropBounds()
                    cropView.zoomInImage(cropRotateData.scale)

                    if(!cropRotateData.isAutoCrop){
                        cropView.post {
                            val cropImageMatrix = cropView.imageMatrix.values()
                            val ax = (cropImageMatrix[2] * -1) + cropRotateData.translateX
                            val ay = (cropImageMatrix[5] * -1) + cropRotateData.translateY
                            cropView.postTranslate(ax, ay)
                        }
                    }
                }
            }
        }
    }

    private fun readPreviousState(previousState: EditorDetailUiModel) {
        // if current selected editor not brightness and contrast, implement filter with sequence
        if (!previousState.isToolBrightness() && !previousState.isToolContrast()) {
            if (previousState.isContrastExecuteFirst == 1) {
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
            implementPreviousStateRotate(previousState.cropRotateValue)
        } else if(data.cropRotateValue.isCrop || data.cropRotateValue.isRotate) {
            val currentBitmap = viewBinding?.imgUcropPreview?.getBitmap()
            val cropRotateData = data.cropRotateValue
            currentBitmap?.let {
                val finalRotationDegree = (cropRotateData.orientationChangeNumber * RotateToolUiComponent.ROTATE_BTN_DEGREE) + (cropRotateData.rotateDegree)

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
            viewBinding?.imgUcropPreview?.cropImageView?.setImageBitmap(
                viewModel.getContrastFilter(
                    previousState.contrastValue!!,
                    implementedBaseBitmap!!
                )
            )
        }
    }

    private fun setWatermarkDrawerItem() {
        implementedBaseBitmap?.let {
            val shopName = if (userSession.shopName.isEmpty())
                DEFAULT_VALUE_SHOP_TEXT else userSession.shopName

            watermarkFilterRepositoryImpl.watermarkDrawerItem(
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
                val currentMatrix = viewBinding?.imgUcropPreview?.cropImageView?.imageMatrix?.values()
                currentMatrix?.let {
                    initialMatrixValue.forEachIndexed { index, value ->
                        if(value != currentMatrix[index]) isEdited = true
                    }
                }
            }

            // if no editing perform, then skip save
            if(isEdited) {
                if(data.isToolRemoveBackground()){
                    showRemoveBackgroundSaveConfirmation{
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

    private fun showRemoveBackgroundSaveConfirmation(onPrimaryClick: () -> Unit){
        val dialog = DialogUnify(requireContext(),DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(editorR.string.editor_remove_bg_dialog_title))
        dialog.setDescription(getString(editorR.string.editor_remove_bg_dialog_desc))

        dialog.dialogPrimaryCTA.apply {
            text = getString(editorR.string.editor_remove_bg_dialog_primary_button_text)
            setOnClickListener {
                onPrimaryClick()
            }
        }

        dialog.dialogSecondaryLongCTA.apply {
            text = getString(editorR.string.editor_remove_bg_dialog_secondary_button_text)
            setOnClickListener {
                dialog.hide()
            }
        }

        dialog.show()
    }

    private fun finishPage() {
        val intent = Intent()

        if (data.isToolRemoveBackground()) data.clearValue()

        intent.putExtra(DetailEditorActivity.EDITOR_RESULT_PARAM, data)
        activity?.setResult(DetailEditorActivity.EDITOR_RESULT_CODE, intent)
        activity?.finish()
    }

    private fun writeToStorage(bitmapParam: Bitmap, filename: String? = null, isFinish: Boolean = true) {
        val fileResult = writeBitmapToStorage(
            requireContext(),
            bitmapParam,
            filename
        )

        data.resultUrl = fileResult?.path
        if (isFinish) finishPage()
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Detail Editor"

        private const val DEFAULT_VALUE_CONTRAST = 0f
        private const val DEFAULT_VALUE_BRIGHTNESS = 0f
        private const val DEFAULT_VALUE_WATERMARK = 0f

        private const val DEFAULT_VALUE_SHOP_TEXT = "Shop Name"
    }
}