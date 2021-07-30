package com.tokopedia.smartbills.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapterItemInquiry
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class SmartBillsInquiryBottomSheet(private val getInquiryCallback: SmartBillAddInquiryCallback): BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
    }

    private val smartBillsInquiryAdapter = SmartBillsAdapterItemInquiry()
    private lateinit var smartBillInquiryRecycleView: RecyclerView
    private lateinit var btnAddInquiry: UnifyButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val itemView = inflater.inflate(R.layout.bottomsheet_smart_bills_inquiry, container)
        smartBillInquiryRecycleView = itemView.findViewById(R.id.rv_inquiry_result)
        btnAddInquiry = itemView.findViewById(R.id.btn_sbm_add_inquiry)
        setChild(itemView)
        setTitle(getString(R.string.smart_bills_add_bills_title_bottom_sheet_inquiry))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        smartBillInquiryRecycleView.apply {
            adapter = smartBillsInquiryAdapter
            layoutManager = LinearLayoutManager(context)
        }
        super.onViewCreated(view, savedInstanceState)
        btnAddInquiry.setOnClickListener{
            btnAddInquiry.isLoading = true
            dismiss()
            getInquiryCallback.onInquiryClicked()
        }
    }

    fun addSBMInquiry(sbmInquiryList: List<TopupBillsEnquiryMainInfo>){
        smartBillsInquiryAdapter.listInquiry = sbmInquiryList
    }
}