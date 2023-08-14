package com.tokopedia.smartbills.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class SmartBillsDeleteBottomSheet : BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
    }

    private lateinit var textDelete: Typography
    private var listener: DeleteProductSBMListener? = null
    private var bill: RechargeBills? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arguments ->
            bill = arguments.getParcelable(BILL_EXTRA) ?: RechargeBills()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val itemView = inflater.inflate(R.layout.bottomsheet_smart_bills_delete, container)
        textDelete = itemView.findViewById(R.id.tg_delete_sbm)
        textDelete.setOnClickListener {
            dismiss()
            bill?.let { bill ->
                listener?.onDeleteProductClicked(bill)
            }
        }
        setChild(itemView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun dismiss() {
        listener?.onCloseBottomSheet()
        super.dismiss()
    }

    fun setListener(listener: DeleteProductSBMListener) {
        this.listener = listener
    }

    interface DeleteProductSBMListener{
        fun onDeleteProductClicked(bill: RechargeBills)
        fun onCloseBottomSheet()
    }

    companion object {
        private const val BILL_EXTRA = "BILL_EXTRA"
        fun newInstance(bill: RechargeBills): SmartBillsDeleteBottomSheet {
            val bottomsheet = SmartBillsDeleteBottomSheet()
            val bundle = Bundle()
            bundle.putParcelable(BILL_EXTRA, bill)
            bottomsheet.arguments = bundle
            return bottomsheet
        }
    }
}
