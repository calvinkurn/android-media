package com.tokopedia.media.editor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.databinding.FragmentDetailEditorBinding
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorViewModel
import com.tokopedia.media.editor.ui.component.BrightnessToolUiComponent
import com.tokopedia.media.editor.ui.component.RemoveBackgroundToolUiComponent
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class DetailEditorFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory
) : TkpdBaseV4Fragment()
    , BrightnessToolUiComponent.Listener
    , RemoveBackgroundToolUiComponent.Listener {

    private val viewBinding: FragmentDetailEditorBinding? by viewBinding()
    private val viewModel: DetailEditorViewModel by activityViewModels { viewModelFactory }

    private val brightnessComponent by uiComponent { BrightnessToolUiComponent(it, this) }
    private val removeBgComponent by uiComponent { RemoveBackgroundToolUiComponent(it, this) }

    private var data = EditorDetailUiModel()

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
        initObservable()
    }

    override fun onBrightnessValueChanged(value: Float) {
        viewModel.setBrightness(value)
    }

    override fun onRemoveBackgroundClicked() {
        viewModel.setRemoveBackground(data.imageUrl)
    }

    private fun initObservable() {
        observeIntentUiModel()
        observeLoader()
        observeBrightness()
        observeRemoveBackground()
    }

    private fun observeBrightness() {
        viewModel.brightnessValue.observe(viewLifecycleOwner) {
            viewBinding?.imgPreview?.colorFilter = it
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
            data = it

            renderImagePreview(it.imageUrl)
            renderUiComponent(it.editorToolType)
        }
    }

    private fun observeLoader() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            viewBinding?.ldrPreview?.showWithCondition(it)
        }
    }

    private fun renderImagePreview(imageUrl: String) {
        viewBinding?.imgPreview?.loadImage(imageUrl) {
            centerCrop()
        }
    }

    private fun renderUiComponent(@EditorToolType type: Int) {
        when (type) {
            EditorToolType.BRIGHTNESS -> brightnessComponent.setupView()
            EditorToolType.REMOVE_BACKGROUND -> removeBgComponent.setupView()
        }
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Detail Editor"
    }

}