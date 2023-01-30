package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.mvc.R
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.mvc.databinding.SmvcBottomSheetForbiddenWordsBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ForbiddenWordsBottomSheet : BottomSheetUnify() {

    companion object {
        private const val TAG = "ForbiddenWordsBottomSheet"
    }

    private var binding by autoClearedNullable<SmvcBottomSheetForbiddenWordsBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcBottomSheetForbiddenWordsBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        setupContent()
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupView() {
        setTitle(getString(R.string.smvc_forbidden_words_info_title))
        showCloseIcon = true
    }

    private fun setupContent() {
        binding?.run {
            tgForbiddenWords.text = getString(R.string.smvc_forbidden_words_info_content)
        }
    }

}
