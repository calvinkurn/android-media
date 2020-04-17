package com.tokopedia.thankyou_native.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.getMaskedNumberSubStringPayment
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.viewModel.CheckWhiteListViewModel
import com.tokopedia.thankyou_native.recommendation.presentation.view.PDPThankYouPageView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_fragment_instant_payment.*
import javax.inject.Inject

class InstantPaymentFragment : ThankYouBaseFragment() {

    private lateinit var dialogUnify: DialogUnify

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
        return inflater.inflate(R.layout.thank_fragment_instant_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionMenu()
        pdp_recommendation_instant.fragment = this
        if (!::thanksPageData.isInitialized)
            activity?.finish()
        bindDataToUI()
        context?.let {
            checkCreditCardRegisteredForRBA(it)
        }
        observeViewModel()
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

    private fun observeViewModel() {
        checkWhiteListViewModel.whiteListResultLiveData.observe(this, Observer {
            when (it) {
                is Success -> onSuccessFullyRegister()
                is Fail -> onSingleAuthRegisterFail()

            }
        })
    }

    private fun onSingleAuthRegisterFail() {
        loadingView.gone()
        showErrorOnUI(getString(R.string.thank_enable_single_authentication_error)) { enableSingleAuthentication() }
    }

    private fun onSuccessFullyRegister() {
        loadingView.gone()
        showToaster(getString(R.string.thank_enable_single_authentication_success))
    }

    private fun bindDataToUI() {
        ImageLoader.LoadImage(iv_instant_payment, thanksPageData.gatewayImage)
        if (thanksPageData.additionalInfo.maskedNumber.isNotBlank()) {
            tv_instant_payment_method_name.text = thanksPageData.additionalInfo.maskedNumber.getMaskedNumberSubStringPayment()
            if (thanksPageData.additionalInfo.installmentInfo.isNotBlank()) {
                tv_instant_payment_interest.text = thanksPageData.additionalInfo.installmentInfo
                tv_instant_payment_interest.visible()
            }
        } else
            tv_instant_payment_method_name.text = thanksPageData.gatewayName
        tv_instant_payment_amount.text = getString(R.string.thankyou_rp_without_space, thanksPageData.amountStr)
        btn_instant_see_transaction_list.setOnClickListener { gotoOrderList() }
    }

    private fun checkCreditCardRegisteredForRBA(context: Context) {
        if (::dialogUnify.isInitialized)
            dialogUnify.cancel()
        if (!thanksPageData.whitelistedRBA)
            dialogUnify = DialogUnify(context = context, actionType = DialogUnify.HORIZONTAL_ACTION,
                    imageType = DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.thank_single_authentication))
                setDescription(getString(R.string.thank_enable_single_authentication_easy))
                setPrimaryCTAText(getString(R.string.thank_activate_it))
                setSecondaryCTAText(getString(R.string.thank_next_time))
                setPrimaryCTAClickListener { enableSingleAuthentication() }
                setSecondaryCTAClickListener { dialogUnify.cancel() }
                show()
            }
    }

    private fun enableSingleAuthentication() {
        if (::dialogUnify.isInitialized)
            dialogUnify.cancel()
        loadingView.visible()
        checkWhiteListViewModel.registerForSingleAuth()
    }


    companion object {
        const val SCREEN_NAME = "Pembayaran Berhasil"
        private const val ARG_THANK_PAGE_DATA = "arg_thank_page_data"

        fun getLoaderFragmentInstance(bundle: Bundle?, thanksPageData: ThanksPageData)
                : InstantPaymentFragment = InstantPaymentFragment().apply {

            bundle?.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
            }
        }
    }

}
