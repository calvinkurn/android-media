package com.tokopedia.editor.ui.main.fragment.video

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentVodMainEditorBinding
import com.tokopedia.editor.ui.main.MainEditorViewModel
import com.tokopedia.editor.ui.player.EditorVideoPlayer
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class VideoMainEditorFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : BaseEditorFragment(R.layout.fragment_vod_main_editor) {

    private val binding: FragmentVodMainEditorBinding? by viewBinding()
    private val viewModel: MainEditorViewModel by activityViewModels { viewModelFactory }

    private val videoPlayer by lazy {
        EditorVideoPlayer(requireContext())
    }

    override fun initView() {
        setupViewPlayer(viewModel.filePath)
    }

    override fun initObserver() = Unit

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
}
