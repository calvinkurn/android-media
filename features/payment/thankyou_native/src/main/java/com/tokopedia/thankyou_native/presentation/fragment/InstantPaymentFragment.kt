package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.ThanksPageData
import javax.inject.Inject

class InstantPaymentFragment : BaseThankYouPageFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var thanksPageData: ThanksPageData

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ThankYouPageComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            if (it.containsKey(ARG_THANK_PAGE_DATA)) {
                thanksPageData = it.getParcelable(ARG_THANK_PAGE_DATA)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater): View? {
        return inflater.inflate(R.layout.thank_fragment_instant_payment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!::thanksPageData.isInitialized)
            activity?.finish()
        showPurchasedProductList()
        loadRecommendations()
        checkCreditCardRegisteredForRBA()
        //todo temporary
        openPurchasedItemBottomSheet()
    }

    private fun checkCreditCardRegisteredForRBA() {
        TODO("API required from backend side")
    }

    private fun loadRecommendations() {

    }

    private fun showPurchasedProductList() {
        if (thanksPageData.orderList.isNotEmpty()) {
            TODO("ADD product item to bottom sheet of wishList")
        }
    }

    private fun openPurchasedItemBottomSheet() {
        Handler().postDelayed(Runnable {
            showProductBottomSheet(thanksPageData.orderList)
        }, 2000L)
    }


    companion object {

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