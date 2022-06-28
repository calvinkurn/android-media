package com.tokopedia.media.editor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.databinding.FragmentDetailEditorBinding
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorViewModel
import com.tokopedia.media.editor.ui.component.BrightnessToolUiComponent
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class DetailEditorFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory
) : TkpdBaseV4Fragment()
    , BrightnessToolUiComponent.Listener {

    private val viewBinding: FragmentDetailEditorBinding? by viewBinding()
    private val viewModel: DetailEditorViewModel by activityViewModels { viewModelFactory }

    private val brightnessComponent by uiComponent { BrightnessToolUiComponent(it, this) }

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

    private fun initObservable() {
        viewModel.intentUiModel.observe(viewLifecycleOwner) {
            viewBinding?.imgPreview?.loadImage(it.imageUrl) {
                centerCrop()
            }

            if (it.editorToolType == EditorToolType.BRIGHTNESS) {
                brightnessComponent.setupView()
            }
        }

        viewModel.brightnessValue.observe(viewLifecycleOwner) {
            viewBinding?.imgPreview?.colorFilter = it
        }
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Detail Editor"
    }

}