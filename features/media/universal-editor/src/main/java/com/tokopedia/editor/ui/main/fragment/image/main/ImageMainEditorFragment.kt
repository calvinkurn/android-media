package com.tokopedia.editor.ui.main.fragment.image.main

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentImageMainEditorBinding
import com.tokopedia.editor.ui.main.EditorParamFetcher
import com.tokopedia.editor.ui.main.MainEditorViewModel
import com.tokopedia.editor.ui.main.uimodel.InputTextUiModel
import com.tokopedia.editor.ui.main.uimodel.IsEdited
import com.tokopedia.editor.ui.main.uimodel.MainEditorEvent
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.ui.widget.DynamicTextCanvasView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageMainEditorFragment @Inject constructor(
    private val param: EditorParamFetcher
) : BaseEditorFragment(R.layout.fragment_image_main_editor), DynamicTextCanvasView.Listener {

    private val binding: FragmentImageMainEditorBinding? by viewBinding()
    private val viewModel: MainEditorViewModel by activityViewModels()

    override fun initView() {
        lifecycleScope.launchWhenCreated {
            val file = param.get().firstFile.path
            binding?.imgSample?.loadImage(file)
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.inputTextState.collect {
                    addOrEditTextOnCanvas(it)
                }
            }
        }
    }

    override fun onTextClick(text: Typography, model: InputTextModel) {
        viewModel.onEvent(MainEditorEvent.ClickInputTextTool(model, IsEdited(true)))
    }

    private fun addOrEditTextOnCanvas(state: InputTextUiModel) {
        if (state.model == null) return

        binding?.btnDelete?.let {
            binding?.canvas?.addButtonView(it)
        }

        binding?.canvas?.setListener(this)

        if (state.isEdited) {
            binding?.canvas?.editText(state.previousString, state.model)
        } else {
            binding?.canvas?.addText(state.model)
        }
    }

}
