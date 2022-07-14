package com.tokopedia.media.editor.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.view.drawToBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.base.BaseEditorFragment
import com.tokopedia.media.editor.data.repository.ContrastFilterRepositoryImpl
import com.tokopedia.media.editor.data.repository.WatermarkFilterRepositoryImpl
import com.tokopedia.media.editor.databinding.FragmentDetailEditorBinding
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorViewModel
import com.tokopedia.media.editor.ui.component.BrightnessToolUiComponent
import com.tokopedia.media.editor.ui.component.ContrastToolsUiComponent
import com.tokopedia.media.editor.ui.component.RemoveBackgroundToolUiComponent
import com.tokopedia.media.editor.ui.component.WatermarkToolUiComponent
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.utils.getDestinationUri
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.media.loader.loadImageWithTarget
import com.tokopedia.media.loader.utils.MediaTarget
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.lang.Exception
import javax.inject.Inject

class DetailEditorFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory,
) : BaseEditorFragment(), BrightnessToolUiComponent.Listener, ContrastToolsUiComponent.Listener,
    RemoveBackgroundToolUiComponent.Listener, WatermarkToolUiComponent.Listener {

    private val viewBinding: FragmentDetailEditorBinding? by viewBinding()
    private val viewModel: DetailEditorViewModel by activityViewModels { viewModelFactory }

    @Inject
    lateinit var contrastFilterRepositoryImpl: ContrastFilterRepositoryImpl
    @Inject
    lateinit var watermarkFilterRepositoryImpl: WatermarkFilterRepositoryImpl

    private val brightnessComponent by uiComponent { BrightnessToolUiComponent(it, this) }
    private val removeBgComponent by uiComponent { RemoveBackgroundToolUiComponent(it, this) }
    private val contrastComponent by uiComponent { ContrastToolsUiComponent(it, this) }
    private val watermarkComponent by uiComponent { WatermarkToolUiComponent(it, this) }

    private var data = EditorDetailUiModel()

    private var originalBitmap: Bitmap? = null

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
        saveImage()
        data.resultUrl?.let {
            val uri = Uri.parse(it)
            uri.path?.let { it1 -> viewModel.setRemoveBackground(it1) }
        }
    }

    override fun onWatermarkChanged(value: Int) {
        viewModel.setWatermark(value)
        data.watermarkMode = value
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
            viewBinding?.imgPreview?.colorFilter = it
        }
    }

    private fun observeContrast() {
        viewModel.contrastFilter.observe(viewLifecycleOwner) {
            originalBitmap?.let { itBitmap ->
                val cloneOriginal = itBitmap.copy(itBitmap.config, true)
                viewBinding?.imgPreview?.setImageBitmap(
                    contrastFilterRepositoryImpl.contrast(
                        it,
                        cloneOriginal
                    )
                )
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
        when (type) {
            EditorToolType.BRIGHTNESS -> {
                brightnessComponent.setupView(data.brightnessValue ?: 0f)
            }
            EditorToolType.REMOVE_BACKGROUND -> removeBgComponent.setupView()
            EditorToolType.CONTRAST -> {
                contrastComponent.setupView(data.contrastValue ?: DEFAULT_VALUE_CONTRAST)
            }
            EditorToolType.WATERMARK -> {
                watermarkComponent.setupView()
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
                                && data.editorToolType != EditorToolType.BRIGHTNESS) viewModel.setBrightness(data.brightnessValue!!)
                            if (data.contrastValue != null
                                && data.editorToolType != EditorToolType.CONTRAST) viewModel.setContrast(data.contrastValue!!)
                            if (data.watermarkMode != null
                                && data.editorToolType != EditorToolType.WATERMARK) viewModel.setWatermark(data.watermarkMode!!)

                            originalBitmap = it.drawToBitmap()

                            when(data.editorToolType){
                                EditorToolType.WATERMARK -> viewModel.setWatermark(data.watermarkMode)
                                EditorToolType.BRIGHTNESS -> viewModel.setBrightness(data.brightnessValue)
                                EditorToolType.CONTRAST -> viewModel.setContrast(data.contrastValue)
                            }

                            // render result for watermark drawer item
                            if(data.editorToolType == EditorToolType.WATERMARK) setWatermarkDrawerItem()
                        }
                    }
                )
            )
        }
    }

    private fun setWatermarkDrawerItem(){
        originalBitmap?.let { bitmap ->
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
                val roundedCorner = context?.resources?.getDimension(R.dimen.editor_watermark_rounded) ?: 0f
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
            saveImage()
            editingSave()
        }
    }

    private fun editingSave() {
        val intent = Intent()

        if(data.editorToolType == EditorToolType.REMOVE_BACKGROUND) data.clearValue()

        intent.putExtra(DetailEditorActivity.EDITOR_RESULT_PARAM, data)
        activity?.setResult(DetailEditorActivity.EDITOR_RESULT_CODE, intent)
        activity?.finish()
    }

    private fun saveImage() {
        viewBinding?.let {
            try {
                val bitmap = it.imgPreview.drawToBitmap()

                val file = getDestinationUri(requireContext()).toFile()
                file.createNewFile()

                val bos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 0, bos)
                val bitmapData = bos.toByteArray()

                val fos = FileOutputStream(file)
                fos.write(bitmapData)
                fos.flush()
                fos.close()

                val uri = file.toUri()
                data.resultUrl = uri.path
            } catch (e: Exception) {
            }
        }
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Detail Editor"

        private const val DEFAULT_VALUE_CONTRAST = 10f
    }

}