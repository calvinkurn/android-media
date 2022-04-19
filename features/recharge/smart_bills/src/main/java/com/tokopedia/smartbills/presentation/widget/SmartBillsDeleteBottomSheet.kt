package com.tokopedia.smartbills.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.smartbills.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class SmartBillsDeleteBottomSheet(private val listener: DeleteProductSBMListener): BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
    }

    private lateinit var textDelete: Typography

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val itemView = inflater.inflate(R.layout.bottomsheet_smart_bills_delete, container)
        textDelete = itemView.findViewById(R.id.tg_delete_sbm)
        textDelete.setOnClickListener {
            dismiss()
            listener.onDeleteProductClicked()
        }
        setChild(itemView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun dismiss() {
        listener.onCloseBottomSheet()
        super.dismiss()
    }

    interface DeleteProductSBMListener{
        fun onDeleteProductClicked()
        fun onCloseBottomSheet()
    }
}