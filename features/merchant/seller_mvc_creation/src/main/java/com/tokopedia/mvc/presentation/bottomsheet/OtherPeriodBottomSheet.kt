package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetOtherPeriodVoucherBinding
import com.tokopedia.mvc.presentation.bottomsheet.adapter.OtherPeriodAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class OtherPeriodBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheetOtherPeriodVoucherBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            rvVoucherChild.setupOtherPeriodList()
            tickerHeaderInfo.setHtmlDescription(getString(R.string.smvc_bottomsheet_other_period_header_info_text))
        }
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SmvcBottomsheetOtherPeriodVoucherBinding.inflate(inflater, container, false)
        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding?.root)
    }

    private fun RecyclerView.setupOtherPeriodList() {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = OtherPeriodAdapter().apply {
            setDataList(listOf("hohoh", "hihihih", "hehehehe"))
        }
    }

    fun show(fragment: Fragment, totalVoucherChild: Int) {
        setTitle(fragment.context?.getString(
            R.string.smvc_bottomsheet_other_period_voucher_title, totalVoucherChild).orEmpty())
        show(fragment.childFragmentManager, "")
    }

}


