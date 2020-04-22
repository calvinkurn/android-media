package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.getMaskedNumberSubStringPayment
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.viewModel.CheckWhiteListViewModel
import com.tokopedia.thankyou_native.recommendation.presentation.view.PDPThankYouPageView
import kotlinx.android.synthetic.main.thank_fragment_success_payment.*
import javax.inject.Inject

class CashOnDeliveryFragment  : ThankYouBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var checkWhiteListViewModel: CheckWhiteListViewModel

    private lateinit var thanksPageData: ThanksPageData

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(ThankYouPageComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_THANK_PAGE_DATA)) {
                thanksPageData = it.getParcelable(ARG_THANK_PAGE_DATA)
            }
        }
        initViewModels()
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        checkWhiteListViewModel = viewModelProvider.get(CheckWhiteListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_success_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionMenu()
        pdp_recommendation_instant.fragment = this
        if (!::thanksPageData.isInitialized)
            activity?.finish()
        bindDataToUI()
    }

    private fun setActionMenu() {
        val headerUnify = (activity as ThankYouPageActivity).getHeader()
        headerUnify.actionText = getString(R.string.thank_menu_detail)
        headerUnify.actionTextView?.setOnClickListener { openInvoiceDetail(thanksPageData) }
    }

    override fun getThankPageData(): ThanksPageData {
        return thanksPageData
    }

    override fun getRecommendationView(): PDPThankYouPageView? {
        return pdp_recommendation_instant
    }

    private fun bindDataToUI() {
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
    }


    companion object {
        const val SCREEN_NAME = "Pembayaran Berhasil"
        private const val ARG_THANK_PAGE_DATA = "arg_thank_page_data"

        fun getFragmentInstance(bundle: Bundle?, thanksPageData: ThanksPageData)
                : CashOnDeliveryFragment = CashOnDeliveryFragment().apply {
            bundle?.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
            }
        }
    }

}
