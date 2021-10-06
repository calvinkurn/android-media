package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.databinding.BottomsheetLsFinishOrderUohBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by fwidjaja on 02/10/21.
 */
class UohLsFinishOrderBottomSheet : BottomSheetUnify() {
    private var actionListener: ActionListener? = null
    private var binding by autoClearedNullable<BottomsheetLsFinishOrderUohBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        binding = BottomsheetLsFinishOrderUohBinding.inflate(LayoutInflater.from(context), null, false)
    }

    fun show(context: Context, fragmentManager: FragmentManager, index: Int, orderId: String) {
        val bottomSheet = BottomSheetUnify()
        bottomSheet.showCloseIcon = true
        bottomSheet.showHeader = true

        binding?.run {
            btnLsFinishOrder.setOnClickListener {
                bottomSheet.dismiss()
                actionListener?.onClickLsFinishOrder(index, orderId)
            }
            btnLsKembali.setOnClickListener { bottomSheet.dismiss() }
        }
        bottomSheet.setChild(binding?.root)
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