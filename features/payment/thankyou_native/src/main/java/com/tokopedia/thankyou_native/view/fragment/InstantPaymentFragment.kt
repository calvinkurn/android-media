package com.tokopedia.thankyou_native.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.ThankYouPageComponent
import javax.inject.Inject

class InstantPaymentFragment : BaseThankYouPageFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater): View? {
        return inflater.inflate(R.layout.thank_fragment_instant_payment, null)
    }

    override fun getScreenName(): String {
        return "Thank You"
    }

    override fun initInjector() {
        getComponent(ThankYouPageComponent::class.java).inject(this)
    }

    companion object {
        fun getLoaderFragmentInstance(bundle: Bundle?): InstantPaymentFragment = InstantPaymentFragment().apply {
            bundle?.let {
                arguments = bundle
            }
        }
    }

}