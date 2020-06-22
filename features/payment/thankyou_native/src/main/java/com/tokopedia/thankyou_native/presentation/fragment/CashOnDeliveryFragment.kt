package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.getMaskedNumberSubStringPayment
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import kotlinx.android.synthetic.main.thank_fragment_success_payment.*

class CashOnDeliveryFragment : ThankYouBaseFragment() {

    override fun getScreenName(): String = SCREEN_NAME

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_success_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionMenu()
    }

    private fun setActionMenu() {
        val headerUnify = (activity as ThankYouPageActivity).getHeader()
        headerUnify.actionText = getString(R.string.thank_menu_detail)
        headerUnify.actionTextView?.setOnClickListener { openInvoiceDetail(thanksPageData) }
    }

    override fun getRecommendationContainer(): LinearLayout? = recommendationContainer


    override fun bindThanksPageDataToUI(thanksPageData: ThanksPageData) {
        tv_payment_success.text = getString(R.string.thank_cod_payment_successful)
        tv_payment_success_check_order.text = getString(R.string.thank_cod_payment_check_order)
        ImageLoader.LoadImage(iv_payment, thanksPageData.gatewayImage)
        if (thanksPageData.additionalInfo.maskedNumber.isNotBlank()) {
            tv_payment_method_name.text = thanksPageData.additionalInfo.maskedNumber.getMaskedNumberSubStringPayment()
            if (thanksPageData.additionalInfo.installmentInfo.isNotBlank()) {
                tv_payment_interest.text = thanksPageData.additionalInfo.installmentInfo
                tv_payment_interest.visible()
            }
        } else
            tv_payment_method_name.text = thanksPageData.gatewayName
        tv_payment_amount.text = getString(R.string.thankyou_rp_without_space, thanksPageData.amountStr)
        btn_see_transaction_list.setOnClickListener { gotoOrderList() }
        btnShopAgain.setOnClickListener { gotoHomePage() }
    }

    override fun getLoadingView(): View? = null

    override fun onThankYouPageDataReLoaded(data: ThanksPageData) {
        //not required
    }

    companion object {
        const val SCREEN_NAME = "Pembayaran Berhasil"

        fun getFragmentInstance(bundle: Bundle?, thanksPageData: ThanksPageData)
                : CashOnDeliveryFragment = CashOnDeliveryFragment().apply {
            bundle?.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
            }
        }
    }

}
