package com.tokopedia.digital_checkout.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.data.response.atc.DigitalSubscriptionParams
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponent
import com.tokopedia.digital_checkout.presentation.adapter.DigitalCartDetailInfoAdapter
import com.tokopedia.digital_checkout.presentation.viewmodel.DigitalCartViewModel
import com.tokopedia.digital_checkout.utils.DeviceUtil
import kotlinx.android.synthetic.main.fragment_digital_checkout_page.*
import javax.inject.Inject

/**
 * @author by jessica on 07/01/21
 */

class DigitalCartFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(DigitalCartViewModel::class.java) }

    private var cartPassData: DigitalCheckoutPassData? = null
    private var digitalSubscriptionParams: DigitalSubscriptionParams = DigitalSubscriptionParams()

    lateinit var cartDetailInfoAdapter: DigitalCartDetailInfoAdapter

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

        initViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.cartTitle.observe(viewLifecycleOwner, Observer {
            productTitle.text = it
        })

        viewModel.cartItemDigitalList.observe(viewLifecycleOwner, Observer {
            cartDetailInfoAdapter.setInfoItems(it)
        })

        viewModel.cartAdditionalInfoList.observe(viewLifecycleOwner, Observer {
            cartDetailInfoAdapter.setAdditionalInfoItems(it)
        })
    }

    private fun getDigitalIdentifierParam(): RequestBodyIdentifier = DeviceUtil.getDigitalIdentifierParam(requireActivity())

    private fun initViews() {
        cartDetailInfoAdapter = DigitalCartDetailInfoAdapter()
        rvDetails.layoutManager = LinearLayoutManager(context)
        rvDetails.isNestedScrollingEnabled = false
        rvDetails.adapter = cartDetailInfoAdapter

        tvSeeDetailToggle.setOnClickListener {
            cartDetailInfoAdapter.toggleIsExpanded()
            if (cartDetailInfoAdapter.isExpanded) {
                tvSeeDetailToggle.text = getString(R.string.digital_cart_detail_close_label)
            } else {
                tvSeeDetailToggle.text = getString(R.string.digital_cart_detail_see_detail_label)
            }
        }
    }

    companion object {
        private const val ARG_PASS_DATA = "ARG_PASS_DATA"
        private const val ARG_SUBSCRIPTION_PARAMS = "ARG_SUBSCRIPTION_PARAMS"

        fun newInstance(passData: DigitalCheckoutPassData?,
                        subParams: DigitalSubscriptionParams?): DigitalCartFragment {
            val fragment = DigitalCartFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_PASS_DATA, passData)
            bundle.putParcelable(ARG_SUBSCRIPTION_PARAMS, subParams)
            fragment.arguments = bundle
            return fragment
        }
    }
}