package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetFilterVoucherBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class OtherPeriodBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheetFilterVoucherBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SmvcBottomsheetFilterVoucherBinding.inflate(inflater, container, false)
        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding?.root)
        // TODO: Implement layout and behavior
    }

    fun show(fragment: Fragment, totalVoucherChild: Int) {
        setTitle(fragment.context?.getString(
            R.string.smvc_bottomsheet_other_period_voucher_title, totalVoucherChild).orEmpty())
        show(fragment.childFragmentManager, "")
    }

}
