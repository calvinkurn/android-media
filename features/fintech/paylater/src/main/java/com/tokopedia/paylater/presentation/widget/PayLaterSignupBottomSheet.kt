package com.tokopedia.paylater.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.paylater.R
import com.tokopedia.paylater.data.mapper.PayLaterPartnerTypeMapper
import com.tokopedia.paylater.data.mapper.ProcessingApplicationPartnerType
import com.tokopedia.paylater.data.mapper.RegisterStepsPartnerType
import com.tokopedia.paylater.data.mapper.UsageStepsPartnerType
import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.paylater.presentation.adapter.PayLaterPaymentMethodAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.paylater_signup_bottomsheet_widget.*

class PayLaterSignupBottomSheet: BottomSheetUnify() {


    init {
        setShowListener {
            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }
    }

    private val childLayoutRes = R.layout.paylater_signup_bottomsheet_widget
    private var payLaterDataList : ArrayList<PayLaterItemProductData> = arrayListOf()
    private var payLaterApplicationStatusList: ArrayList<PayLaterApplicationDetail> = arrayListOf()
    private var listener: Listener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }


    private fun getArgumentData() {
        arguments?.let {
            payLaterDataList = it.getParcelableArrayList(PAY_LATER_PARTNER_DATA) ?: arrayListOf()
            payLaterApplicationStatusList = it.getParcelableArrayList(PAY_LATER_APPLICATION_DATA) ?: arrayListOf()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
    }


    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
    }

    private fun initAdapter() {
        rvPayLaterPaymentMethods.adapter = PayLaterPaymentMethodAdapter(payLaterDataList, payLaterApplicationStatusList) { payLaterData, payLaterApplicationStatus ->
            listener?.onPayLaterSignupClicked(payLaterData, payLaterApplicationStatus)
            dismiss()
            //openBottomSheet(payLaterData, payLaterApplicationStatus)
        }
        rvPayLaterPaymentMethods.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setDefaultParams() {
        setTitle(DIALOG_TITLE)
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    fun setActionListener(listener: Listener) {
        this.listener = listener
    }

    companion object {
        private const val DIALOG_TITLE = "Mau daftar PayLater apa?"
        const val PAY_LATER_PARTNER_DATA = "payLaterPartnerData"
        const val PAY_LATER_APPLICATION_DATA = "payLaterApplicationData"

        private const val TAG = "FT_TAG"
        fun show(bundle: Bundle, childFragmentManager: FragmentManager): PayLaterSignupBottomSheet {
            val payLaterSignupBottomSheet =  PayLaterSignupBottomSheet().apply {
                arguments = bundle
            }
            payLaterSignupBottomSheet.show(childFragmentManager, TAG)
            return payLaterSignupBottomSheet
        }
    }

    interface Listener {
        fun onPayLaterSignupClicked(productItemData: PayLaterItemProductData, partnerApplicationDetail: PayLaterApplicationDetail?)
    }
}