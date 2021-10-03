package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.databinding.BottomsheetFinishOrderUohBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment

/**
 * Created by fwidjaja on 01/10/21.
 */
class UohFinishOrderBottomSheet {
    private var actionListener: ActionListener? = null

    fun show(context: Context,
             fragmentManager: FragmentManager,
             index: Int,
             status: String,
             orderId: String
    ){
        val bottomSheet = BottomSheetUnify()
        bottomSheet.showCloseIcon = true
        bottomSheet.showHeader = true

        val binding = BottomsheetFinishOrderUohBinding.inflate(LayoutInflater.from(context))
        binding.run {
            icFinishDetail1.background = ContextCompat.getDrawable(context, R.drawable.ic_uoh_bound_icon)
            icFinishDetail2.background = ContextCompat.getDrawable(context, R.drawable.ic_uoh_bound_icon)
            btnFinishOrder.setOnClickListener {
                actionListener?.onClickFinishOrder(index, status, orderId)
            }
            btnAjukanKomplain.setOnClickListener {
                actionListener?.onClickAjukanKomplain(orderId)
            }
        }
        bottomSheet.setChild(binding.root)
        bottomSheet.setTitle(UohConsts.FINISH_ORDER_BOTTOMSHEET_TITLE)
        bottomSheet.show(fragmentManager, "")
        bottomSheet.setCloseClickListener { bottomSheet.dismiss() }
    }

    interface ActionListener {
        fun onClickFinishOrder(index: Int, status: String, orderId: String)
        fun onClickAjukanKomplain(orderId: String)
    }

    fun setActionListener(fragment: UohListFragment) {
        this.actionListener = fragment
    }
}