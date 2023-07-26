package com.tokopedia.editor.ui.fragment.text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentInputTextBinding
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class InputTextFragment @Inject constructor() : BaseEditorFragment() {

    private val binding: FragmentInputTextBinding? by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input_text, container, false)
    }

    override fun initView() {
        binding?.btnBackButton?.setOnClickListener {

        }
    }

    override fun initObserver() {

    }

}
