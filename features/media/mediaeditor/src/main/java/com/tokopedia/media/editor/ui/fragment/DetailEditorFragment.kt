package com.tokopedia.media.editor.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.media.editor.R
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
import com.tokopedia.media.editor.ui.uimodel.EditorCropRectModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorRotateModel
import com.tokopedia.media.editor.utils.getDestinationUri
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import com.yalantis.ucrop.callback.BitmapCropCallback
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
    lateinit var contrastFilterRepositoryImpl: ContrastFilterRepositoryImpl

    @Inject
    lateinit var watermarkFilterRepositoryImpl: WatermarkFilterRepositoryImpl

    @Inject
    lateinit var rotateFilterRepositoryImpl: RotateFilterRepositoryImpl

    @Inject
    lateinit var brightnessFilterRepositoryImpl: ColorFilterRepositoryImpl

    private val brightnessComponent by uiComponent { BrightnessToolUiComponent(it, this) }
    private val removeBgComponent by uiComponent { RemoveBackgroundToolUiComponent(it, this) }
    private val contrastComponent by uiComponent { ContrastToolsUiComponent(it, this) }
    private val watermarkComponent by uiComponent { WatermarkToolUiComponent(it, this) }
    private val rotateComponent by uiComponent { RotateToolUiComponent(it, this) }
    private val cropComponent by uiComponent { CropToolUiComponent(it, this) }

    private var data = EditorDetailUiModel()
    private var implementedBaseBitmap: Bitmap? = null

    private var removeBackgroundRetryLimit = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_detail_editor,
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
    }

    override fun onContrastValueChanged(value: Float) {
        viewModel.setContrast(value)
        data.contrastValue = value
    }

    override fun onRemoveBackgroundClicked() {
        viewBinding?.imgUcropPreview?.let { editorDetailPreviewImage ->
            saveImage(editorDetailPreviewImage.getBitmap(), isFinish = false)
            data.resultUrl?.let { it ->
                viewModel.setRemoveBackground(it) { _ ->
                    viewBinding?.let {
                        Toaster.build(
                            it.editorFragmentDetailRoot,
                            getString(R.string.editor_tool_remove_background_failed_normal),
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_NORMAL,
                            getString(R.string.editor_tool_remove_background_failed_cta)
                        ) {
                            removeBackgroundRetryLimit--
                            if (removeBackgroundRetryLimit == 0) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.editor_tool_remove_background_failed_normal),
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
    }

    override fun onRotateValueChanged(rotateValue: Float) {
        rotateFilterRepositoryImpl.rotate(viewBinding?.imgUcropPreview, rotateValue, false)
    }

    override fun onImageMirror() {
        rotateFilterRepositoryImpl.mirror(viewBinding?.imgUcropPreview)
    }

    override fun onImageRotate(rotateDegree: Float) {
        rotateFilterRepositoryImpl.rotate(
            viewBinding?.imgUcropPreview,
            RotateToolUiComponent.ROTATE_BTN_DEGREE,
            true
        )
    }

    override fun onCropRatioClicked(ratio: Float) {
        viewBinding?.imgUcropPreview?.let {
            val overlayView = it.overlayView
            val cropView = it.cropImageView

            overlayView.setTargetAspectRatio(ratio)
            cropView.targetAspectRatio = ratio
            Handler().postDelayed({
                cropView.setCropRect(overlayView.cropViewRect)
                cropView.setImageToWrapCropBounds()
            },1000)
//            if(ratio == 1f/1f){
//                it.cropImageView.zoomOutImage(it.cropImageView.minScale + 0.01f)
//                it.cropImageView.setImageToWrapCropBounds()
//            } else if(ratio == 3f/4f){
//                val x = 0
//            } else {
//                val bitmap = it.getBitmap()
//                val ratio = bitmap.width.toFloat() / bitmap.height
//                val overlayOld = it.overlayView?.cropViewRect
//
//                it.overlayView.setTargetAspectRatio(ratio)
//                it.cropImageView.targetAspectRatio = ratio
//
//                it.cropImageView.setImageToWrapCropBounds()
//                it.overlayView.setupCropBounds()
//
//                it.overlayView.post {
//                    it.cropImageView.setCropRect(RectF(
//                        overlayOld.left,
//                        overlayOld.top,
//                        overlayOld.left + implementedBaseBitmap!!.width,
//                        overlayOld.top + implementedBaseBitmap!!.height
//                    ))
//
//                    it.cropImageView.postDelayed({
//                        it.cropImageView.setImageToWrapCropBounds()
//                    }, 500)
//                }
//            }

        }
    }

    override fun initObserver() {
        observeIntentUiModel()
        observeLoader()
        observeBrightness()
        observeContrast()
        observeWatermark()
        observeRemoveBackground()
    }

    private fun observeBrightness() {
        viewModel.brightnessFilter.observe(viewLifecycleOwner) {
            viewBinding?.imgUcropPreview?.cropImageView?.colorFilter = it
        }
    }

    private fun observeContrast() {
        viewModel.contrastFilter.observe(viewLifecycleOwner) {
            implementedBaseBitmap?.let { itBitmap ->
                val contrastBitmap = contrastFilterRepositoryImpl.contrast(
                    it,
                    itBitmap.copy(itBitmap.config, true)
                )

                viewBinding?.imgUcropPreview?.cropImageView?.setImageBitmap(contrastBitmap)
            }
        }
    }

    private fun observeRemoveBackground() {
        viewModel.removeBackground.observe(viewLifecycleOwner) {
            data.removeBackgroundUrl = it?.path
            viewBinding?.imgUcropPreview?.cropImageView?.loadImage(it?.path)
        }
    }

    private fun observeIntentUiModel() {
        viewModel.intentUiModel.observe(viewLifecycleOwner) {
            // make this ui model as global variable
            data = it

            renderUiComponent(it.editorToolType)
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
                    disabledTouchEvent()
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
                cropComponent.setupView()
            }
        }
    }

    private fun implementPreviousStateBrightness(
        previousValue: Float?,
        isRemoveFilter: Boolean = true
    ) {
        val cropView = viewBinding?.imgUcropPreview?.cropImageView
        cropView?.colorFilter = brightnessFilterRepositoryImpl.brightness(previousValue ?: 0f)

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
                contrastFilterRepositoryImpl.contrast(
                    previousValue ?: 0f,
                    bitmap.copy(bitmap.config, true)
                )
            )
        }
    }

    private fun implementPreviousStateRotate(rotateData: EditorRotateModel) {
        viewBinding?.imgUcropPreview?.let {
            val originalAsset = it.getBitmap()

            val cropView = it.cropImageView
            val overlayView = it.overlayView

            val rotateDegree = (rotateData.rotateDegree + (rotateData.orientationChangeNumber * RotateToolUiComponent.ROTATE_BTN_DEGREE))

            // read previous value & implement if current editor is rotate
            if(data.isToolRotate() || data.isToolCrop()){
                cropView.post {
                    // need to check if previous value is rotate / not, if rotated then ratio is changed
                    val isRotate = rotateData.orientationChangeNumber % 2 == 1

                    val originalWidth = originalAsset.width.toFloat()
                    val originalHeight = originalAsset.height.toFloat()

                    val ratio = if(isRotate) originalHeight / originalWidth else originalWidth / originalHeight

                    cropView.scaleX = rotateData.scaleX
                    cropView.scaleY = rotateData.scaleY
                    rotateFilterRepositoryImpl.rotate(it, rotateDegree, isRotate)
                    rotateFilterRepositoryImpl.previousDegree = rotateData.rotateDegree
                    rotateFilterRepositoryImpl.rotateNumber = rotateData.orientationChangeNumber
                    rotateFilterRepositoryImpl.sliderValue = rotateData.rotateDegree

                    overlayView.setTargetAspectRatio(ratio)

                    cropView.setImageToWrapCropBounds()
                }
            }else {
                val normalizeDegree = rotateDegree * (rotateData.scaleX * rotateData.scaleY)
                val imageWidth = rotateData.rightRectPos
                val imageHeight = rotateData.bottomRectPos

                val bitmapResult = it.getProcessedBitmap(
                    originalAsset,
                    rotateData.leftRectPos,
                    rotateData.topRectPos,
                    imageWidth,
                    imageHeight,
                    normalizeDegree,
                    sliderValue = rotateData.rotateDegree,
                    rotateNumber = rotateData.orientationChangeNumber,
                    null
                )

                cropView.setImageBitmap(bitmapResult)
            }
        }

    }

    private fun implementPreviousStateCrop(editorUiModel: EditorDetailUiModel){
        val cropBound = editorUiModel.cropBound ?: return
        if(data.isToolCrop()){
            viewBinding?.imgUcropPreview?.let {
                val overlayView = it.overlayView
                val cropView = it.cropImageView

                Handler().postDelayed({
                    overlayView.setTargetAspectRatio(cropBound.imageWidth / cropBound.imageHeight.toFloat())
                    cropView.zoomInImage(cropBound.scale)
                    cropView.postTranslate(cropBound.imageWidth * cropBound.scale, cropBound.imageHeight * cropBound.scale)
                    cropView.setImageToWrapCropBounds()
                    Handler().postDelayed({
                        cropView.postTranslate(-cropBound.offsetX * cropBound.scale, -cropBound.offsetY * cropBound.scale)
                    },2000)
                },2000)

            }
        }
        else {
            val rotateData = editorUiModel.rotateData
            val temp = EditorRotateModel(
                rotateData?.rotateDegree ?: 0f,
                rotateData?.scaleX ?: 1f,
                rotateData?.scaleY ?: 1f,
                cropBound.offsetX,
                cropBound.offsetY,
                cropBound.imageWidth,
                cropBound.imageHeight,
                editorUiModel?.rotateData?.orientationChangeNumber ?: 0
            )
            implementPreviousStateRotate(temp)
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

        // === Rotate ===
        if (previousState.rotateData != null) {
            implementPreviousStateRotate(previousState.rotateData!!)
        }

        // === Crop ===
        if(previousState.cropBound != null) {
            implementPreviousStateCrop(previousState)
        }

        // image that already implemented previous filter
        implementedBaseBitmap = viewBinding?.imgUcropPreview?.getBitmap()

        if (previousState.brightnessValue != null && previousState.isToolBrightness()) {
            // if current editor is brightness keep the filter color so we can adjust it later
            implementPreviousStateBrightness(previousState.brightnessValue, false)
        } else if (previousState.contrastValue != null && previousState.isToolContrast()) {
            viewBinding?.imgUcropPreview?.cropImageView?.setImageBitmap(
                contrastFilterRepositoryImpl.contrast(
                    previousState.contrastValue!!,
                    implementedBaseBitmap!!.copy(implementedBaseBitmap!!.config, true)
                )
            )
        }

//        viewBinding?.imgUcropPreview?.cropImageView?.setImageToWrapCropBounds()
    }

    private fun setWatermarkDrawerItem() {
        implementedBaseBitmap?.let { bitmap ->
            val text = if (userSession.shopName.isEmpty())
                DEFAULT_VALUE_SHOP_TEXT else userSession.shopName
            val resultBitmap1 = watermarkFilterRepositoryImpl.watermark(
                requireContext(),
                bitmap,
                WatermarkToolUiComponent.WATERMARK_TOKOPEDIA,
                text,
                true
            )

            val resultBitmap2 = watermarkFilterRepositoryImpl.watermark(
                requireContext(),
                bitmap,
                WatermarkToolUiComponent.WATERMARK_SHOP,
                text,
                true
            )

            watermarkComponent.getButtonRef().apply {
                val roundedCorner =
                    context?.resources?.getDimension(R.dimen.editor_watermark_rounded) ?: 0f
                this.first.loadImageRounded(resultBitmap1, roundedCorner)
                this.second.loadImageRounded(resultBitmap2, roundedCorner)
            }
        }
    }

    private fun initButtonListener() {
        viewBinding?.btnCancel?.setOnClickListener {
            activity?.finish()
        }

        viewBinding?.btnSave?.setOnClickListener {
            if (data.isToolRotate() || data.isToolCrop()) {
                // if current tools editor not rotate then skip crop data set by sent empty object on data
                viewBinding?.imgUcropPreview?.cropRotate(
                    finalRotationDegree = rotateFilterRepositoryImpl.getFinalRotationDegree(),
                    sliderValue = rotateFilterRepositoryImpl.sliderValue,
                    rotateNumber = rotateFilterRepositoryImpl.rotateNumber,
                    data,
                    data.isToolRotate()
                ) {
                    saveImage(it)
                }
            } else if(data.isToolCrop()){
                viewBinding?.imgUcropPreview?.cropImageView?.cropAndSaveImage(
                    Bitmap.CompressFormat.PNG,
                    100,
                    object: BitmapCropCallback{
                        override fun onBitmapCropped(
                            resultUri: Uri,
                            offsetX: Int,
                            offsetY: Int,
                            imageWidth: Int,
                            imageHeight: Int
                        ) {
                            val newCropBound = EditorCropRectModel(
                                offsetX,
                                offsetY,
                                imageWidth,
                                imageHeight,
                                viewBinding?.imgUcropPreview?.cropImageView?.currentScale ?: 1f,
                                resultUri.path ?: ""
                            )

                            data.resultUrl = resultUri.path
                            data.cropBound = newCropBound

                            editingSave()
                        }

                        override fun onCropFailure(t: Throwable) {
                            Toast.makeText(context, "crop failed", Toast.LENGTH_LONG).show()
                        }
                    }
                )
            } else {
                viewBinding?.imgUcropPreview?.getBitmap()?.let {
                    saveImage(it)
                }
            }
        }
    }

    private fun editingSave() {
        val intent = Intent()

        if (data.isToolRemoveBackground()) data.clearValue()

        intent.putExtra(DetailEditorActivity.EDITOR_RESULT_PARAM, data)
        activity?.setResult(DetailEditorActivity.EDITOR_RESULT_CODE, intent)
        activity?.finish()
    }

    private fun saveImage(bitmapParam: Bitmap, filename: String? = null, isFinish: Boolean = true) {
        viewBinding?.let {
            try {
                val file = getDestinationUri(requireContext(), filename).toFile()
                file.createNewFile()

                val bos = ByteArrayOutputStream()
                bitmapParam.compress(Bitmap.CompressFormat.PNG, 0, bos)
                val bitmapData = bos.toByteArray()

                val fos = FileOutputStream(file)
                fos.write(bitmapData)
                fos.flush()
                fos.close()

                val uri = file.toUri()
                data.resultUrl = uri.path
                if(data.isToolCrop()) data.cropBound?.croppedUrl = uri.path ?: ""

                if (isFinish) editingSave()
            } catch (e: Exception) {
            }
        }
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