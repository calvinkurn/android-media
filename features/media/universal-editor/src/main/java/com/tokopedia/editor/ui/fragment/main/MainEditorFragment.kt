package com.tokopedia.editor.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentMainEditorBinding
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class MainEditorFragment @Inject constructor() : BaseEditorFragment() {

    private val binding: FragmentMainEditorBinding? by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_editor, container, false)
    }

    override fun initView() {
        Toast.makeText(requireContext(), "hi!", Toast.LENGTH_SHORT).show()

        binding?.btnTest?.setOnClickListener {

        }
    }

    override fun initObserver() {

    }

}
