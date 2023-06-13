package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.databinding.BottomsheetKebabMenuUohBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.view.adapter.UohBottomSheetKebabMenuAdapter
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by fwidjaja on 01/10/21.
 */
class UohKebabMenuBottomSheet : BottomSheetUnify() {
    private var listener: UohKebabMenuBottomSheetListener? = null
    private var adapter: UohBottomSheetKebabMenuAdapter? = null
    private var binding by autoClearedNullable<BottomsheetKebabMenuUohBinding>()

    companion object {
        private const val TAG: String = "UohKebabMenuBottomSheet"

        @JvmStatic
        fun newInstance(): UohKebabMenuBottomSheet { return UohKebabMenuBottomSheet() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomsheetKebabMenuUohBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            listener?.let { adapter?.setActionListener(it) }
            rvKebab.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvKebab.adapter = adapter
        }
        showCloseIcon = true
        showHeader = true
        setChild(binding?.root)
        setTitle(UohConsts.OTHERS)
        setCloseClickListener { dismiss() }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    fun setListener(listener: UohKebabMenuBottomSheetListener) {
        this.listener = listener
    }

    fun setAdapter(adapter: UohBottomSheetKebabMenuAdapter) {
        this.adapter = adapter
    }

    interface UohKebabMenuBottomSheetListener {
        fun onKebabItemClick(index: Int, orderData: UohListOrder.UohOrders.Order, orderIndex: Int)
    }
}
