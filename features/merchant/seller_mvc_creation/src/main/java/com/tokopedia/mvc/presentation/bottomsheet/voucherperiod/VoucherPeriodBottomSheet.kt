package com.tokopedia.mvc.presentation.bottomsheet.voucherperiod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.mvc.databinding.SmvcBottomsheetPeriodsBinding
import com.tokopedia.mvc.presentation.bottomsheet.voucherperiod.adapter.VoucherPeriodBottomSheetAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class VoucherPeriodBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheetPeriodsBinding>()

    private var sheetTitle: String = ""
    private var dateList: List<DateStartEndData> = emptyList()

    init {
        clearContentPadding = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcBottomsheetPeriodsBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(sheetTitle)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding?.periodListRecyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = VoucherPeriodBottomSheetAdapter()
        adapter.setList(dateList)
        binding?.periodListRecyclerView?.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String="", dateList: List<DateStartEndData> = emptyList()): VoucherPeriodBottomSheet {
            return VoucherPeriodBottomSheet().apply {
                this.sheetTitle = title
                this.dateList = dateList
            }
        }
    }

}
