package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.databinding.BottomsheetFinishOrderUohBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by fwidjaja on 01/10/21.
 */
class UohFinishOrderBottomSheet : BottomSheetUnify() {
    private var actionListener: ActionListener? = null
    private var bottomSheetFinishOrder : BottomSheetUnify? = null
    private var binding by autoClearedNullable<BottomsheetFinishOrderUohBinding>()

    fun show(context: Context, fragmentManager: FragmentManager, index: Int, status: String, orderId: String){
        bottomSheetFinishOrder = BottomSheetUnify()
        binding = BottomsheetFinishOrderUohBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            icFinishDetail1.background = ContextCompat.getDrawable(context, R.drawable.ic_uoh_bound_icon)
            icFinishDetail2.background = ContextCompat.getDrawable(context, R.drawable.ic_uoh_bound_icon)
            btnFinishOrder.setOnClickListener {
                actionListener?.onClickFinishOrder(index, status, orderId)
            }
            btnAjukanKomplain.setOnClickListener {
                actionListener?.onClickAjukanKomplain(orderId)
            }
        }

        bottomSheetFinishOrder?.run {
            showCloseIcon = true
            showHeader = true
            setChild(binding?.root)
            setTitle(UohConsts.FINISH_ORDER_BOTTOMSHEET_TITLE)
            setCloseClickListener { dismiss() }
        }
        bottomSheetFinishOrder?.show(fragmentManager, "")
    }

    interface ActionListener {
        fun onClickFinishOrder(index: Int, status: String, orderId: String)
        fun onClickAjukanKomplain(orderId: String)
    }

    fun setActionListener(fragment: UohListFragment) {
        this.actionListener = fragment
    }
}