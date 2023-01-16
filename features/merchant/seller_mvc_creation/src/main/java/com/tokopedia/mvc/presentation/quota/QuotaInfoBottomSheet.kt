package com.tokopedia.mvc.presentation.quota

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetQuotaInfoBinding
import com.tokopedia.mvc.presentation.quota.fragment.QuotaInfoFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class QuotaInfoBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheetQuotaInfoBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun initBottomSheet() {
        clearContentPadding = true
        val title = context?.getString(R.string.smvc_quota_info_bottomsheet_title).orEmpty()
        setTitle(title)
        setAction(getString(R.string.smvc_quota_info_bottomsheet_action_text)) { dismiss() }
        binding = SmvcBottomsheetQuotaInfoBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
    }

    private fun setupView() {
        binding?.apply {
            val newFragment = QuotaInfoFragment.newInstance(showToolbar = false)
            childFragmentManager.beginTransaction()
                .replace(containerFragment.id, newFragment)
                .commit()
        }
    }

    fun show(fm: FragmentManager) {
        showNow(fm, this::class.java.simpleName)
    }
}
