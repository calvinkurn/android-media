package com.tokopedia.media.editor.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.scale
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.view.drawToBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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
import com.tokopedia.media.editor.ui.component.EditorDetailPreviewImage
import com.tokopedia.media.editor.ui.component.RemoveBackgroundToolUiComponent
import com.tokopedia.media.editor.ui.component.RotateToolUiComponent
import com.tokopedia.media.editor.ui.component.WatermarkToolUiComponent
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorRotateModel
import com.tokopedia.media.editor.utils.getDestinationUri
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.loadImageWithTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.utils.MediaTarget
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.util.RectUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.Exception
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.min

class DetailEditorFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory,
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
    private var originalBitmap: Bitmap? = null

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
        val target = data.resultUrl ?: data.originalUrl
        val uri = Uri.parse(target)
        uri.path?.let { it ->
            viewModel.setRemoveBackground(it) {
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
    }

    override fun onWatermarkChanged(value: Int) {
        viewModel.setWatermark(value)
        data.watermarkMode = value
    }

    override fun onRotateValueChanged(value: Float) {
        rotateFilterRepositoryImpl.rotate(viewBinding?.imgUcropPreview, value, false)
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
            val originalBitmap = viewBinding?.imgUcropPreview?.cropImageView?.drawable?.toBitmap()
            originalBitmap?.let { itBitmap ->
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
            viewBinding?.imgPreview?.let { imgPreview ->
                Glide
                    .with(requireContext())
                    .asBitmap()
                    .load(it)
                    .into(imgPreview)
            }
        }
    }

    private fun observeIntentUiModel() {
        viewModel.intentUiModel.observe(viewLifecycleOwner) {
            // make this ui model as global variable
            data = it

            renderImagePreview(it.removeBackgroundUrl ?: it.originalUrl)
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
            originalBitmap?.let { bitmap ->
                val text = "Toko Maju Jaya Perkasa Abadi Bangunan"
                val result = watermarkFilterRepositoryImpl.watermark(
                    requireContext(),
                    bitmap,
                    watermarkType,
                    text,
                    false
                )
                viewBinding?.imgPreview?.setImageBitmap(result)
            }
        }
    }

    private fun renderUiComponent(@EditorToolType type: Int) {
        val uri = Uri.fromFile(File(data.originalUrl))

        when (type) {
            EditorToolType.BRIGHTNESS -> {
                val brightnessValue = data.brightnessValue ?: DEFAULT_VALUE_BRIGHTNESS
                viewBinding?.imgUcropPreview?.apply {
                    initializeBrightness(uri)
                    onLoadComplete = {
                        val newColorFilter = brightnessFilterRepositoryImpl.brightness(brightnessValue)
                        this.cropImageView.colorFilter = newColorFilter
                    }
                }
                brightnessComponent.setupView(brightnessValue)
            }
            // ==========
            EditorToolType.REMOVE_BACKGROUND -> removeBgComponent.setupView()
            // ==========
            EditorToolType.CONTRAST -> {
                val contrastValue = data.contrastValue ?: DEFAULT_VALUE_CONTRAST
                viewBinding?.imgUcropPreview?.apply {
                    initializeContrast(uri)
                    onLoadComplete = {
                        val originalBitmap = cropImageView.drawable.toBitmap()
                        val contrastBitmap = contrastFilterRepositoryImpl.contrast(
                            contrastValue,
                            originalBitmap.copy(originalBitmap.config, true)
                        )

                        this.cropImageView.setImageBitmap(contrastBitmap)
                    }
                }
                contrastComponent.setupView(contrastValue)
            }
            // ==========
            EditorToolType.WATERMARK -> watermarkComponent.setupView()
            // ==========
            EditorToolType.ROTATE -> {
                viewBinding?.imgUcropPreview?.apply {
                    initializeRotate(uri)
                    disabledTouchEvent()
                    onLoadComplete = {
                        data.rotateData?.let {
                            if(it.scaleX < 0f){
                                rotateFilterRepositoryImpl.mirror(this)
                            }
                            if(it.scaleY < 0f){
                                rotateFilterRepositoryImpl.rotate(this, RotateToolUiComponent.ROTATE_BTN_DEGREE, true)
                                rotateFilterRepositoryImpl.mirror(this)
                            }

                            this.cropImageView.post {
                                if(it.orientationChangeNumber > 0){
                                    rotateFilterRepositoryImpl.rotate(this, it.orientationChangeNumber * RotateToolUiComponent.ROTATE_BTN_DEGREE, true)
                                }
                                rotateFilterRepositoryImpl.rotate(this, it.rotateDegree, false)
                            }
                        }
                    }
                }
                rotateComponent.setupView(data)
            }
            // ==========
            EditorToolType.CROP -> {
                val uri = Uri.fromFile(File(data.originalUrl))
//                viewBinding?.imgUcropPreview?.initialize(uri)
                cropComponent.setupView()
            }
        }
    }

    private fun renderImagePreview(imageUrl: String) {
        viewBinding?.imgPreview?.let {
            loadImageWithTarget(requireContext(), imageUrl, {},
                MediaTarget(
                    viewComponent = it,
                    onReady = { imageView, resource ->
                        originalBitmap = resource

                        imageView.setImageBitmap(resource)
                        imageView.post {
                            if (data.brightnessValue != null
                                && data.editorToolType != EditorToolType.BRIGHTNESS
                            ) viewModel.setBrightness(data.brightnessValue!!)
                            if (data.contrastValue != null
                                && data.editorToolType != EditorToolType.CONTRAST
                            ) viewModel.setContrast(data.contrastValue!!)
                            if (data.watermarkMode != null
                                && data.editorToolType != EditorToolType.WATERMARK
                            ) viewModel.setWatermark(data.watermarkMode!!)

                            originalBitmap = it.drawToBitmap()

                            when (data.editorToolType) {
                                EditorToolType.WATERMARK -> viewModel.setWatermark(data.watermarkMode)
                                EditorToolType.BRIGHTNESS -> viewModel.setBrightness(data.brightnessValue)
                                EditorToolType.CONTRAST -> viewModel.setContrast(data.contrastValue)
                            }

                            // render result for watermark drawer item
                            if (data.editorToolType == EditorToolType.WATERMARK) setWatermarkDrawerItem()
                        }
                    }
                )
            )
        }
    }

    private fun setWatermarkDrawerItem() {
        originalBitmap?.let { bitmap ->
            // todo: implement text from user data
            val text = "Toko Maju Jaya Perkasa Abadi Bangunan"
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
            if(data.editorToolType == EditorToolType.ROTATE){
                viewBinding?.imgUcropPreview?.cropRotate(
                    finalRotationDegree = rotateFilterRepositoryImpl.getFinalRotationDegree(),
                    sliderValue = rotateFilterRepositoryImpl.sliderValue,
                    rotateNumber = rotateFilterRepositoryImpl.rotateNumber,
                    data
                ) {
                    saveImage(it)
                }
            } else {
                viewBinding?.imgUcropPreview?.let {
                    saveImage(it.cropImageView.drawable.toBitmap())
                }
            }
        }
    }

    private fun editingSave() {
        val intent = Intent()

        if (data.editorToolType == EditorToolType.REMOVE_BACKGROUND) data.clearValue()

        intent.putExtra(DetailEditorActivity.EDITOR_RESULT_PARAM, data)
        activity?.setResult(DetailEditorActivity.EDITOR_RESULT_CODE, intent)
        activity?.finish()
    }

    private fun saveImage(bitmapParam: Bitmap, filename: String? = null) {
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

                editingSave()
            } catch (e: Exception) {
            }
        }
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Detail Editor"

        private const val DEFAULT_VALUE_CONTRAST = 0f
        private const val DEFAULT_VALUE_BRIGHTNESS = 0f
    }
}