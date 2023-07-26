package com.tokopedia.editor.ui.fragment.image.placement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentImagePlacementBinding
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class ImagePlacementFragment @Inject constructor() : BaseEditorFragment() {

    private val binding: FragmentImagePlacementBinding? by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_placement, container, false)
    }

    override fun initView() {
        Toast.makeText(requireContext(), "image scale and crop!", Toast.LENGTH_SHORT).show()
    }

    override fun initObserver() {

    }

}
