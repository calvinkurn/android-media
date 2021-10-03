package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.databinding.BottomsheetFinishOrderUohBinding
import com.tokopedia.unifyorderhistory.databinding.BottomsheetLsFinishOrderUohBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment

/**
 * Created by fwidjaja on 02/10/21.
 */
class UohLsFinishOrderBottomSheet {
    private var actionListener: ActionListener? = null

    fun show(context: Context,
             fragmentManager: FragmentManager,
             index: Int, orderId: String) {
        val bottomSheet = BottomSheetUnify()
        bottomSheet.showCloseIcon = true
        bottomSheet.showHeader = true

        val binding = BottomsheetLsFinishOrderUohBinding.inflate(LayoutInflater.from(context))
        binding.run {
            btnLsFinishOrder.setOnClickListener {
                bottomSheet.dismiss()
                actionListener?.onClickLsFinishOrder(index, orderId)
            }
            btnLsKembali.setOnClickListener { bottomSheet.dismiss() }
        }
        bottomSheet.setChild(binding.root)
        bottomSheet.setTitle(UohConsts.FINISH_ORDER_BOTTOMSHEET_TITLE)
        bottomSheet.show(fragmentManager, "")
        bottomSheet.setCloseClickListener { bottomSheet.dismiss() }
    }

    interface ActionListener {
        fun onClickLsFinishOrder(index: Int, orderId: String)
    }

    fun setActionListener(fragment: UohListFragment) {
        this.actionListener = fragment
    }
}