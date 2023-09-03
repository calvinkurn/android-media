package com.tokopedia.editor.ui.main.fragment.video

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.R
import com.tokopedia.editor.databinding.FragmentVodMainEditorBinding
import com.tokopedia.editor.ui.main.EditorParamFetcher
import com.tokopedia.editor.ui.main.MainEditorViewModel
import com.tokopedia.editor.ui.main.uimodel.InputTextParam
import com.tokopedia.editor.ui.main.uimodel.MainEditorEvent
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.ui.player.EditorVideoPlayer
import com.tokopedia.editor.ui.widget.DynamicTextCanvasLayout
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoMainEditorFragment @Inject constructor(
    private val param: EditorParamFetcher
) : BaseEditorFragment(R.layout.fragment_vod_main_editor), DynamicTextCanvasLayout.Listener {

    private val binding: FragmentVodMainEditorBinding? by viewBinding()
    private val viewModel: MainEditorViewModel by activityViewModels()

    private val videoPlayer by lazy {
        EditorVideoPlayer(requireContext())
    }

    override fun initView() {
        lifecycleScope.launchWhenCreated {
            val file = param.get().firstFile
            if (file.isImage()) return@launchWhenCreated

            file.path?.let {
                setupViewPlayer(it)
            }
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.inputTextState.collect(::addOrEditTextOnLayout)
            }
        }
    }

    override fun onTextClick(text: View, model: InputTextModel) {
        viewModel.onEvent(MainEditorEvent.EditInputTextPage(text.id, model))
    }

    override fun onResume() {
        super.onResume()
        videoPlayer.resume()
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.pause()
    }

    override fun onStart() {
        super.onStart()
        videoPlayer.start()
    }

    override fun onStop() {
        super.onStop()
        videoPlayer.stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoPlayer.release()
    }

    private fun setupViewPlayer(url: String) {
        binding?.playerView?.player = videoPlayer.player()

        videoPlayer.videoUrl = url
        videoPlayer.start()
    }

    private fun addOrEditTextOnLayout(state: InputTextParam) {
        val (typographyId, model) = state
        if (model == null) return

        binding?.container?.addOrEditText(typographyId, model)
        viewModel.onEvent(MainEditorEvent.ResetActiveInputText)
    }
}
