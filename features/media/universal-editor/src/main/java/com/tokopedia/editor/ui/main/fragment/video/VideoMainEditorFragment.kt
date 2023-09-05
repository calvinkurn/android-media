package com.tokopedia.editor.ui.main.fragment.video

import androidx.lifecycle.lifecycleScope
import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentVodMainEditorBinding
import com.tokopedia.editor.ui.main.EditorParamFetcher
import com.tokopedia.editor.ui.player.EditorVideoPlayer
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class VideoMainEditorFragment @Inject constructor(
    private val param: EditorParamFetcher
) : BaseEditorFragment(R.layout.fragment_vod_main_editor) {

    private val binding: FragmentVodMainEditorBinding? by viewBinding()

    private val videoPlayer by lazy {
        EditorVideoPlayer(requireContext())
    }

    override fun initView() {
        lifecycleScope.launchWhenCreated {
            val file = param.get().firstFile
            if (file.isImage()) return@launchWhenCreated
            setupViewPlayer(file.path)
        }
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
