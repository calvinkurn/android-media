package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.ThanksPageData
import kotlinx.android.synthetic.main.thank_fragment_instant_payment.*
import javax.inject.Inject

class InstantPaymentFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var thanksPageData: ThanksPageData

    override fun getScreenName(): String = "Pembayaran Berhasil"

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_instant_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!::thanksPageData.isInitialized)
            activity?.finish()
        bindDataToUI()
        checkCreditCardRegisteredForRBA()
    }

    private fun bindDataToUI() {
        ImageLoader.LoadImage(iv_instant_payment, thanksPageData.gatewayImage)
        tv_instant_payment_method_name.text = thanksPageData.gatewayName
        tv_instant_payment_amount.text = getString(R.string.thankyou_rp, thanksPageData.amountStr)
    }

    private fun checkCreditCardRegisteredForRBA() {

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