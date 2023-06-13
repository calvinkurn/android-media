package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.databinding.BottomsheetLsFinishOrderUohBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by fwidjaja on 02/10/21.
 */
class UohLsFinishOrderBottomSheet : BottomSheetUnify() {
    private var listener: UohLsFinishOrderBottomSheetListener? = null
    private var binding by autoClearedNullable<BottomsheetLsFinishOrderUohBinding>()

    companion object {
        private const val TAG: String = "UohLsFinishOrderBottomSheet"
        private const val INDEX = "index"
        private const val ORDER_ID = "order_id"

        @JvmStatic
        fun newInstance(index: Int, orderId: String): UohLsFinishOrderBottomSheet {
            return UohLsFinishOrderBottomSheet().apply {
                val bundle = Bundle()
                bundle.putInt(INDEX, index)
                bundle.putString(ORDER_ID, orderId)
                arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index = arguments?.getInt(INDEX) ?: 0
        val orderId = arguments?.getString(ORDER_ID) ?: ""
        binding = BottomsheetLsFinishOrderUohBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            btnLsFinishOrder.setOnClickListener {
                dismiss()
                listener?.onClickLsFinishOrder(index, orderId)
            }
            btnLsKembali.setOnClickListener { dismiss() }
        }
        setChild(binding?.root)
        showCloseIcon = true
        showHeader = true
        setTitle(UohConsts.FINISH_ORDER_BOTTOMSHEET_TITLE)
        setCloseClickListener { dismiss() }
    }

    interface UohLsFinishOrderBottomSheetListener {
        fun onClickLsFinishOrder(index: Int, orderId: String)
    }

    fun setListener(listener: UohLsFinishOrderBottomSheetListener) {
        this.listener = listener
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}
