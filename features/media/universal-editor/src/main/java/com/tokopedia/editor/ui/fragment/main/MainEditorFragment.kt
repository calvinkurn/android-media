package com.tokopedia.editor.ui.fragment.main

import android.os.Bundle
import android.widget.Toast
import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentMainEditorBinding
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class MainEditorFragment @Inject constructor() : BaseEditorFragment(R.layout.fragment_main_editor) {

    private val binding: FragmentMainEditorBinding? by viewBinding()

    override fun initView() {
        Toast.makeText(requireContext(), "hi!", Toast.LENGTH_SHORT).show()
    }

    override fun initObserver() {

    }

    override fun onLoadSavedState(bundle: Bundle?) {

    }

}
