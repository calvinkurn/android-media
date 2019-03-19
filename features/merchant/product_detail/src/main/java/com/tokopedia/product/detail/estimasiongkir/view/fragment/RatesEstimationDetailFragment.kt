package com.tokopedia.product.detail.estimasiongkir.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.KG
import com.tokopedia.product.detail.data.util.LABEL_GRAM
import com.tokopedia.product.detail.data.util.LABEL_KG
import com.tokopedia.product.detail.data.util.numberFormatted
import com.tokopedia.product.detail.estimasiongkir.data.constant.RatesEstimationConstant
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.product.detail.estimasiongkir.di.RatesEstimationComponent
import com.tokopedia.product.detail.estimasiongkir.view.adapter.RatesEstimationServiceAdapter
import com.tokopedia.product.detail.estimasiongkir.view.viewmodel.RatesEstimationDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_rates_estimation_detail.*
import kotlinx.android.synthetic.main.partial_header_rate_estimation.*

import javax.inject.Inject

class RatesEstimationDetailFragment : BaseDaggerFragment(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: RatesEstimationDetailViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var shopDomain: String = ""
    private var productWeightUnit: String = LABEL_GRAM
    private var productWeight: Float = 0f
    private var origin: String? = null

    private val adapter = RatesEstimationServiceAdapter()

    override fun initInjector() {
        getComponent(RatesEstimationComponent::class.java).inject(this)
    }

    override fun getScreenName(): String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(RatesEstimationDetailViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.rateEstResp.observe(this, Observer {
            when(it){
                is Success -> onSuccesLoadRateEstimaion(it.data)
                is Fail -> onErrorLoadRateEstimaion(it.throwable)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rates_estimation_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            shopDomain = it.getString(RatesEstimationConstant.PARAM_SHOP_DOMAIN, "")
            productWeightUnit = it.getString(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT_UNIT, LABEL_GRAM)
            productWeight = it.getFloat(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, 0f)
            productWeight
            origin = it.getString(RatesEstimationConstant.PARAM_ORIGIN)
        }

        recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = adapter
        recycler_view.isNestedScrollingEnabled = false

        shipping_weight.text = "${productWeight.numberFormatted()} ${if (productWeightUnit.toLowerCase() == KG)
            LABEL_KG else LABEL_GRAM}"

        getCostEstimation()
    }

    private fun getCostEstimation() {
        setViewState(VIEW_LOADING)
        val weightInKg: Float = if (productWeightUnit.toLowerCase() == KG) productWeight else (productWeight/1000)
        viewModel.getCostEstimation(weightInKg, shopDomain, origin)
    }

    private fun setViewState(viewLoading: Int) {
        when (viewLoading) {
            VIEW_LOADING -> {
                loading_state_view.visibility = View.VISIBLE
                app_bar_layout.visibility = View.INVISIBLE
                recycler_view.visibility = View.INVISIBLE
            }
            VIEW_CONTENT -> {
                loading_state_view.visibility = View.GONE
                app_bar_layout.visibility = View.VISIBLE
                recycler_view.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        viewModel.clear()
        super.onDestroyView()
    }

    private fun onErrorLoadRateEstimaion(throwable: Throwable) {
        setViewState(VIEW_CONTENT)
    }

    private fun onSuccesLoadRateEstimaion(ratesEstimationModel: RatesEstimationModel) {
        val address = ratesEstimationModel.address
        val ratesEstimation = ratesEstimationModel.rates
        val shop = ratesEstimationModel.shop

        shipping_destination.text = shop.districtName
        val title = userSession.name
        val spannableString = SpannableString(title)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, userSession.name.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        shipping_receiver_name.text = spannableString
        if(address.phone.isNotEmpty()) {
            shipping_receiver_phone.visibility = View.VISIBLE
            shipping_receiver_phone.text = address.phone
        }
        shipping_receiver_address.text = MethodChecker.fromHtml("${address.address}, ${address.districtName}, ${address.provinceName}")
        if (ratesEstimationModel.isBlackbox){
            (activity as? BaseSimpleActivity)?.updateTitle(getString(R.string.product_detail_courier))
            if (recycler_view.itemDecorationCount > 0)
                recycler_view.removeItemDecorationAt(recycler_view.itemDecorationCount - 1)
        } else {
            (activity as? BaseSimpleActivity)?.updateTitle(getString(R.string.label_rates_estimation_detail))
            recycler_view.addItemDecoration(DividerItemDecoration(activity))
        }
        adapter.isBlackbox = ratesEstimationModel.isBlackbox
        adapter.updateShippingServices(ratesEstimation.services)
        setViewState(VIEW_CONTENT)
    }

    companion object {
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2

        fun createInstance(shopDomain: String, productWeight: Float, productWeightUnit: String, origin: String? = null) =
                RatesEstimationDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(RatesEstimationConstant.PARAM_SHOP_DOMAIN, shopDomain)
                        putFloat(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, productWeight)
                        putString(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT_UNIT, productWeightUnit)
                        putString(RatesEstimationConstant.PARAM_ORIGIN, origin)
                    }
                }
    }
}
