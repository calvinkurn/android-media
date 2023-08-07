package com.tokopedia.editor.ui.main.fragment.image.main

import androidx.lifecycle.lifecycleScope
import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentImageMainEditorBinding
import com.tokopedia.editor.ui.main.EditorParamFetcher
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class ImageMainEditorFragment @Inject constructor(
    private val param: EditorParamFetcher
) : BaseEditorFragment(R.layout.fragment_image_main_editor) {

    private val binding: FragmentImageMainEditorBinding? by viewBinding()

    override fun initView() {
        lifecycleScope.launchWhenCreated {
            val file = param.get().firstFile.path
            binding?.imgSample?.loadImage(file)
        }
    }

    override fun initObserver() {}

}
