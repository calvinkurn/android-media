package com.tokopedia.editor.ui.main.fragment.image.placement

import android.widget.Toast
import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentImagePlacementBinding
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class ImagePlacementFragment @Inject constructor() : BaseEditorFragment(R.layout.fragment_image_placement) {

    private val binding: FragmentImagePlacementBinding? by viewBinding()

    override fun initView() {
        Toast.makeText(requireContext(), "image scale and crop!", Toast.LENGTH_SHORT).show()
    }

    override fun initObserver() {

    }
}
