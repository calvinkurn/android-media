package com.tokopedia.media.editor.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
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
    RotateToolUiComponent.Listener {

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
    private val cropComponent by uiComponent { CropToolUiComponent(it, 0) }

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
        viewBinding?.imgUcropPreview?.cropImageView?.let { cropView ->
            saveImage(cropView.drawable.toBitmap(), isFinish = false)
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
//                viewBinding?.imgUcropPreview?.initialize(uri)
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
        if (isRemoveFilter) viewBinding?.imgUcropPreview?.cropImageView?.drawable?.toBitmap()?.let {
            cropView?.clearColorFilter()
            cropView?.setImageBitmap(it)
        }
    }

    private fun implementPreviousStateContrast(previousValue: Float?) {
        viewBinding?.imgUcropPreview?.cropImageView?.let {
            val bitmap = it.drawable.toBitmap()
            it.setImageBitmap(
                contrastFilterRepositoryImpl.contrast(
                    previousValue ?: 0f,
                    bitmap.copy(bitmap.config, true)
                )
            )
        }
    }

    private fun implementPreviousStateRotate(rotateData: EditorRotateModel) {
        viewBinding?.imgUcropPreview?.let {
            data.rotateData?.let { rotationData ->
                val bitmapResult = it.getProcessedBitmap(
                    it.cropImageView.drawable.toBitmap(),
                    rotationData.leftRectPos,
                    rotationData.topRectPos,
                    rotationData.rightRectPos,
                    rotationData.bottomRectPos,
                    finalRotationDegree = ((rotateData.orientationChangeNumber * RotateToolUiComponent.ROTATE_BTN_DEGREE) + rotationData.rotateDegree),
                    sliderValue = rotationData.rotateDegree,
                    rotateNumber = rotateData.orientationChangeNumber,
                    null
                )

                it.cropImageView.setImageBitmap(bitmapResult)
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

        // === Rotate ===
        if (previousState.rotateData != null) {
            implementPreviousStateRotate(previousState.rotateData!!)
        }

        // image that already implemented previous filter
        implementedBaseBitmap = viewBinding?.imgUcropPreview?.cropImageView?.drawable?.toBitmap()

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

        // if current tools editor not rotate then skip crop data set by sent empty object on data
        viewBinding?.btnSave?.setOnClickListener {
            viewBinding?.imgUcropPreview?.cropRotate(
                finalRotationDegree = rotateFilterRepositoryImpl.getFinalRotationDegree(),
                sliderValue = rotateFilterRepositoryImpl.sliderValue,
                rotateNumber = rotateFilterRepositoryImpl.rotateNumber,
                if (data.isToolRotate()) data else EditorDetailUiModel()
            ) {
                saveImage(it)
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