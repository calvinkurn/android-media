package com.tokopedia.media.editor.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.base.BaseEditorFragment
import com.tokopedia.media.editor.data.repository.ContrastFilterRepositoryImpl
import com.tokopedia.media.editor.databinding.FragmentDetailEditorBinding
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorViewModel
import com.tokopedia.media.editor.ui.component.BrightnessToolUiComponent
import com.tokopedia.media.editor.ui.component.ContrastToolsUiComponent
import com.tokopedia.media.editor.ui.component.RemoveBackgroundToolUiComponent
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.utils.view.binding.viewBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DetailEditorFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory,
) : BaseEditorFragment()
    , BrightnessToolUiComponent.Listener
    , ContrastToolsUiComponent.Listener
    , RemoveBackgroundToolUiComponent.Listener {

    private val viewBinding: FragmentDetailEditorBinding? by viewBinding()
    private val viewModel: DetailEditorViewModel by activityViewModels { viewModelFactory }

    @Inject lateinit var contrastFilterRepositoryImpl: ContrastFilterRepositoryImpl

    private val brightnessComponent by uiComponent { BrightnessToolUiComponent(it, this) }
    private val removeBgComponent by uiComponent { RemoveBackgroundToolUiComponent(it, this) }
    private val contrastComponent by uiComponent { ContrastToolsUiComponent(it, this) }

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
        data.removeBackgroundUrl?.let {
            viewModel.setRemoveBackground(it)
        }
    }

    override fun initObserver() {
        observeIntentUiModel()
        observeLoader()
        observeBrightness()
        observerContrast()
        observeRemoveBackground()
    }

    private fun observeBrightness() {
        viewModel.brightnessFilter.observe(viewLifecycleOwner) {
            viewBinding?.imgPreview?.colorFilter = it
        }
    }

    private fun observerContrast() {
        viewModel.contrastFilter.observe(viewLifecycleOwner) {
            viewBinding?.imgPreview?.apply {
                originalBitmap?.let { itBitmap ->
                    val cloneOriginal = itBitmap.copy(itBitmap.config, true)
                    setImageBitmap(contrastFilterRepositoryImpl.contrast(it, cloneOriginal))
                }
            }
        }
    }

    private fun observeRemoveBackground() {
        viewModel.removeBackground.observe(viewLifecycleOwner) {
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

            renderUiComponent(it.editorToolType)
            renderImagePreview(it.originalUrl)
        }
    }

    private fun observeLoader() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            viewBinding?.ldrPreview?.showWithCondition(it)
        }
    }

    private fun renderUiComponent(@EditorToolType type: Int) {
        when (type) {
            EditorToolType.BRIGHTNESS -> brightnessComponent.setupView()
            EditorToolType.REMOVE_BACKGROUND -> removeBgComponent.setupView()
            EditorToolType.CONTRAST -> contrastComponent.setupView()
        }
    }

    private fun renderImagePreview(imageUrl: String) {
        viewBinding?.imgPreview?.loadImage(imageUrl) {
            centerCrop()
            this.listener(onSuccess = {bitmap, _ ->
                originalBitmap = bitmap
            })
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
        intent.putExtra(DetailEditorActivity.EDITOR_RESULT_PARAM, data)
        activity?.setResult(DetailEditorActivity.EDITOR_RESULT_CODE, intent)
        activity?.finish()
    }

    private fun saveImage(){
        viewBinding?.let {
            try {
                val bitmap = (it.imgPreview.drawable as BitmapDrawable).bitmap

                val file = getDestinationUri().toFile()
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
            } catch (e: Exception){}
        }
    }

    private fun getEditorSaveFolderDir(): String {
        return "${requireContext().externalCacheDir?.path}/editor-cache/"
    }

    private fun getDestinationUri(): Uri {
        val dir = File(getEditorSaveFolderDir())
        if(!dir.exists()) dir.mkdir()

        return Uri.fromFile(File("${getEditorSaveFolderDir()}/${generateFileName()}.png"))
    }

    @SuppressLint("SimpleDateFormat")
    fun generateFileName(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Detail Editor"
    }

}