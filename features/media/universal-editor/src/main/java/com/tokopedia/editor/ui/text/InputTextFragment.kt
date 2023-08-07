package com.tokopedia.editor.ui.text

import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentInputTextBinding
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class InputTextFragment @Inject constructor() : BaseEditorFragment(R.layout.fragment_input_text) {

    private val binding: FragmentInputTextBinding? by viewBinding()

    override fun initView() {
        binding?.btnBackButton?.setOnClickListener {

        }
    }

    override fun initObserver() {

    }
}
