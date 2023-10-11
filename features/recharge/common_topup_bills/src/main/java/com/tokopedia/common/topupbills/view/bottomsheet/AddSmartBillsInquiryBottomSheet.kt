package com.tokopedia.common.topupbills.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryAttribute
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import com.tokopedia.common.topupbills.view.adapter.AddSmartBillsInquiryAdapter
import com.tokopedia.common.topupbills.view.bottomsheet.callback.AddSmartBillsInquiryCallBack
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class AddSmartBillsInquiryBottomSheet : BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
    }

    private val smartBillsInquiryAdapter = AddSmartBillsInquiryAdapter()
    private lateinit var smartBillInquiryRecycleView: RecyclerView
    private lateinit var btnAddInquiry: UnifyButton
    private var inquiryCallback: AddSmartBillsInquiryCallBack? = null
    private var attribute: TopupBillsEnquiryAttribute? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arguments ->
            attribute = arguments.getParcelable(ATTRIBUTE_EXTRA)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val itemView = inflater.inflate(R.layout.bottom_sheets_add_bills_inquiry, container)
        smartBillInquiryRecycleView = itemView.findViewById(R.id.rv_inquiry_result)
        btnAddInquiry = itemView.findViewById(R.id.btn_sbm_add_inquiry)
        setChild(itemView)
        setTitle(getString(R.string.common_topup_add_sbm_inqiry_title))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        smartBillInquiryRecycleView.apply {
            adapter = smartBillsInquiryAdapter
            layoutManager = LinearLayoutManager(context)
        }
        super.onViewCreated(view, savedInstanceState)
        updateSBMInquiry()
        btnAddInquiry.setOnClickListener{
            btnAddInquiry.isLoading = true
            dismiss()
            attribute?.let { attribute ->
                inquiryCallback?.onInquiryClicked(attribute)
            }
        }
    }

    override fun dismiss() {
        inquiryCallback?.onInquiryClose()
        super.dismiss()
    }

    private fun updateSBMInquiry(){
        attribute?.let { attribute ->
            val listInquiry = attribute.mainInfoList.toMutableList()
            if (!attribute.additionalMainInfo.isNullOrEmpty()) {
                attribute.additionalMainInfo.forEach {
                    listInquiry.addAll(it.detail.map {
                        TopupBillsEnquiryMainInfo(it.label, it.value)
                    })
                }
            }
            smartBillsInquiryAdapter.listInquiry = listInquiry
        }
    }

    fun setCallback(inquiryCallback: AddSmartBillsInquiryCallBack) {
        this.inquiryCallback = inquiryCallback
    }

    companion object {
        private const val ATTRIBUTE_EXTRA = "ATTRIBUTE_EXTRA"

        fun newInstance(attribute: TopupBillsEnquiryAttribute): AddSmartBillsInquiryBottomSheet {
            val bottomSheet = AddSmartBillsInquiryBottomSheet()
            val bundle = Bundle()
            bundle.putParcelable(ATTRIBUTE_EXTRA, attribute)
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }
}
