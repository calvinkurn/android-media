package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetFilterStatusVoucherBinding
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherStatusFilter
import com.tokopedia.mvc.presentation.bottomsheet.adapter.FilterVoucherStatusAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class FilterVoucherStatusBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheetFilterStatusVoucherBinding>()
    private var listener: FilterVoucherStatusBottomSheetListener? = null
    private val adapter = FilterVoucherStatusAdapter()

    interface FilterVoucherStatusBottomSheetListener {
        fun onFilterVoucherStatusChanged(status: List<VoucherStatus>, statusText: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SmvcBottomsheetFilterStatusVoucherBinding.inflate(inflater, container, false)
        adapter.apply {
            setDataList(VoucherStatusFilter.values().toList())
            setOnClickListener { status: List<VoucherStatus>, title: String ->
                listener?.onFilterVoucherStatusChanged(status, title)
                dismiss()
            }
        }
        binding?.rvStatus?.adapter = adapter
        binding?.rvStatus?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(getString(R.string.smvc_bottomsheet_filter_status_voucher_title))
    }

    fun setListener(listener: FilterVoucherStatusBottomSheetListener) {
        this.listener = listener
    }

    fun setSelected(selectedItem: List<String>) {
        selectedItem.firstOrNull()?.let {
            adapter.setSelected(it)
        }
    }
}
