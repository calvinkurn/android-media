package com.tokopedia.editor.ui.main.fragment.image.main

import android.view.View
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
import com.tokopedia.editor.ui.main.uimodel.MainEditorEvent
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.ui.widget.DynamicTextCanvasLayout
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageMainEditorFragment @Inject constructor(
    private val param: EditorParamFetcher
) : BaseEditorFragment(R.layout.fragment_image_main_editor), DynamicTextCanvasLayout.Listener {

    private val binding: FragmentImageMainEditorBinding? by viewBinding()
    private val viewModel: MainEditorViewModel by activityViewModels()

    override fun initView() {
        binding?.canvas?.setListener(this)

        lifecycleScope.launchWhenCreated {
            val file = param.get().firstFile.path
            binding?.imgSample?.loadImage(file)
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.inputTextState.collect(::addOrEditTextOnLayout)
            }
        }
    }

    override fun onTextClick(text: View, model: InputTextModel?) {
        if (model == null) return

        viewModel.onEvent(MainEditorEvent.EditInputTextPage(text.id, model))
    }

    private fun addOrEditTextOnLayout(state: InputTextUiModel) {
        val (typographyId, model) = state
        if (model == null) return

        if (typographyId != -1) {
            binding?.canvas?.modifySelectedText(id, model)
        } else {
            binding?.canvas?.addNewText(model)
        }

        // reset active every input text invoked
        viewModel.onEvent(MainEditorEvent.ResetActiveInputText)
    }
}
