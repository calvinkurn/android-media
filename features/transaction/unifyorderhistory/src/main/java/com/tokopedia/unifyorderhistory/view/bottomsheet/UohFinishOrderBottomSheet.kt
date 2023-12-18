package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.databinding.BottomsheetFinishOrderUohBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.utils.lifecycle.autoClearedNullable

class UohFinishOrderBottomSheet : BottomSheetUnify() {
    private var listener: UohFinishOrderBottomSheetListener? = null
    private var binding by autoClearedNullable<BottomsheetFinishOrderUohBinding>()

    companion object {
        private const val TAG: String = "UohFinishOrderBottomSheet"
        private const val UUID = "UUID"
        private const val STATUS = "status"
        private const val ORDER_ID = "order_id"

        @JvmStatic
        fun newInstance(uuid: String, status: String, orderId: String): UohFinishOrderBottomSheet {
            return UohFinishOrderBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(UUID, uuid)
                bundle.putString(STATUS, status)
                bundle.putString(ORDER_ID, orderId)
                arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uuid = arguments?.getString(UUID) ?: ""
        val status = arguments?.getString(STATUS) ?: ""
        val orderId = arguments?.getString(ORDER_ID) ?: ""
        binding = BottomsheetFinishOrderUohBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            context?.let { context ->
                val icBound = ContextCompat.getDrawable(context, R.drawable.ic_uoh_bound_icon)
                icFinishDetail1.background = icBound
                icFinishDetail2.background = icBound
            }

            btnFinishOrder.setOnClickListener {
                listener?.onClickFinishOrder(uuid, status, orderId)
            }
            btnAjukanKomplain.setOnClickListener {
                listener?.onClickAjukanKomplain(orderId)
            }
        }
        showCloseIcon = true
        showHeader = true
        setChild(binding?.root)
        setTitle(UohConsts.FINISH_ORDER_BOTTOMSHEET_TITLE)
        setCloseClickListener { dismiss() }
    }

    interface UohFinishOrderBottomSheetListener {
        fun onClickFinishOrder(uuid: String, status: String, orderId: String)
        fun onClickAjukanKomplain(orderId: String)
    }

    fun setListener(listener: UohFinishOrderBottomSheetListener) {
        this.listener = listener
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}
