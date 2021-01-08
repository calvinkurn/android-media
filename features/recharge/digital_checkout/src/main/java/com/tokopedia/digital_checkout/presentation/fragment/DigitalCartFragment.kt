package com.tokopedia.digital_checkout.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.data.response.atc.DigitalSubscriptionParams
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponent
import com.tokopedia.digital_checkout.presentation.viewmodel.DigitalCartViewModel
import com.tokopedia.digital_checkout.utils.DeviceUtil
import javax.inject.Inject

/**
 * @author by jessica on 07/01/21
 */

class DigitalCartFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(DigitalCartViewModel::class.java) }

    private var cartPassData: DigitalCheckoutPassData? = null
    private var digitalSubscriptionParams: DigitalSubscriptionParams = DigitalSubscriptionParams()


    override fun getScreenName(): String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cartPassData = arguments?.getParcelable(ARG_PASS_DATA)
        val subParams: DigitalSubscriptionParams? = arguments?.getParcelable(ARG_SUBSCRIPTION_PARAMS)
        if (subParams != null) {
            digitalSubscriptionParams = subParams
        }
    }

    override fun initInjector() {
        getComponent(DigitalCheckoutComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_digital_checkout_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartPassData?.let {
            if (it.needGetCart) {
                viewModel.getCart(it)
            } else {
                viewModel.addToCart(it, getDigitalIdentifierParam(), digitalSubscriptionParams)
            }
        }
    }

    private fun getDigitalIdentifierParam(): RequestBodyIdentifier = DeviceUtil.getDigitalIdentifierParam(requireActivity())

    companion object {
        private const val ARG_PASS_DATA = "ARG_PASS_DATA"
        private const val ARG_SUBSCRIPTION_PARAMS = "ARG_SUBSCRIPTION_PARAMS"
    }
}