package com.tokopedia.cod.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.cod.R
import com.tokopedia.cod.di.DaggerCodComponent
import com.tokopedia.logisticanalytics.CodAnalytics
import com.tokopedia.transactionanalytics.data.EnhancedECommerceActionField
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCartMapData
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCheckout
import com.tokopedia.transactionanalytics.data.EnhancedECommerceProductCartMapData
import com.tokopedia.transactiondata.entity.request.CheckoutRequest
import com.tokopedia.transactiondata.entity.response.cod.Data
import kotlinx.android.synthetic.main.fragment_cod_confirmation.*
import java.util.*
import javax.inject.Inject

/**
 * Created by fajarnuha on 17/12/18.
 */
class CodFragment : BaseDaggerFragment(), CodContract.View {

    @Inject
    lateinit var presenter: CodContract.Presenter
    @Inject
    lateinit var mTracker: CodAnalytics

    companion object {

        const val ARGUMENT_COD_DATA = "ARGUMENT_COD_DATA"
        const val ARGUMENT_CHECKOUT_REQUEST = "ARGUMENT_CHECKOUT_REQUEST"

        fun newInstance(data: Data, checkoutRequest: CheckoutRequest): Fragment = CodFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARGUMENT_COD_DATA, data)
                putParcelable(ARGUMENT_CHECKOUT_REQUEST, checkoutRequest)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cod_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun initInjector() {
        activity?.let {
            val baseApp = it.application
            if (baseApp is BaseMainApplication) {
                DaggerCodComponent.builder()
                        .baseAppComponent(baseApp.baseAppComponent)
                        .build().inject(this)
            }
        }
    }

    override fun getScreenName(): String {
        return getString(R.string.cod_brand_tag)
    }

    override fun showLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressbar.visibility = View.GONE
    }

    override fun showError(message: String?) {
        NetworkErrorHelper.showCloseSnackbar(activity, message)
    }

    override fun initView() {
        arguments?.getParcelable<Data>(ARGUMENT_COD_DATA)?.let {
            textview_ticker_message.setText(it.message.messageInfo)
            textview_counter_info.text = MethodChecker.fromHtml(it.counterInfo)
            recycler_view_summary.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = CodSummaryAdapter(it.priceSummary)
            }
            button_confirm.setOnClickListener(this::onPayClicked)
        }
    }

    override fun onPayClicked(view: View) {
        mTracker.eventClickBayarDiTempatCod()
        presenter.confirmPayment()
    }

    override fun navigateToThankYouPage(applink: String) {
        val intent = RouteManager.getIntent(context, applink)
        startActivity(intent)
        activity?.finish()
    }

    override fun sendEventEECod() {
        mTracker.eventEEClickBayarDiTempat(generateCheckoutAnalyticsStep2DataLayer(arguments?.getParcelable(ARGUMENT_CHECKOUT_REQUEST)))
     }

    private fun generateCheckoutAnalyticsStep2DataLayer(checkoutRequest: CheckoutRequest?): Map<String, Any>? {
        if (checkoutRequest != null) {
            val checkoutMapData = HashMap<String, Any>()
            val enhancedECommerceActionField = EnhancedECommerceActionField()
            enhancedECommerceActionField.setStep(EnhancedECommerceActionField.STEP_2)
            enhancedECommerceActionField.setOption(EnhancedECommerceActionField.OPTION_CLICK_PAYMENT_OPTION_BUTTON)

            val enhancedECommerceCheckout = EnhancedECommerceCheckout()
            for (dataCheckoutRequest in checkoutRequest.data) {
                for (shopProductCheckoutRequest in dataCheckoutRequest.shopProducts) {
                    for (productDataCheckoutRequest in shopProductCheckoutRequest.productData) {
                        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData()
                        enhancedECommerceProductCartMapData.setProductName(productDataCheckoutRequest.productName)
                        enhancedECommerceProductCartMapData.setProductID(productDataCheckoutRequest.getProductId().toString())
                        enhancedECommerceProductCartMapData.setPrice(productDataCheckoutRequest.productPrice)
                        enhancedECommerceProductCartMapData.setBrand(productDataCheckoutRequest.productBrand)
                        enhancedECommerceProductCartMapData.setCategory(productDataCheckoutRequest.productCategory)
                        enhancedECommerceProductCartMapData.setVariant(productDataCheckoutRequest.productVariant)
                        enhancedECommerceProductCartMapData.setQty(productDataCheckoutRequest.getProductQuantity())
                        enhancedECommerceProductCartMapData.setShopId(productDataCheckoutRequest.productShopId)
                        enhancedECommerceProductCartMapData.setShopName(productDataCheckoutRequest.productShopName)
                        enhancedECommerceProductCartMapData.setShopType(productDataCheckoutRequest.productShopType)
                        enhancedECommerceProductCartMapData.setCategoryId(productDataCheckoutRequest.productCategoryId)
                        enhancedECommerceProductCartMapData.setDimension38(productDataCheckoutRequest.productAttribution)
                        enhancedECommerceProductCartMapData.setDimension40(productDataCheckoutRequest.productListName)
                        enhancedECommerceProductCartMapData.setDimension45(productDataCheckoutRequest.cartId.toString())
                        enhancedECommerceCheckout.addProduct(enhancedECommerceProductCartMapData.product)
                    }
                }
            }
            enhancedECommerceCheckout.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
            enhancedECommerceCheckout.setActionField(enhancedECommerceActionField.actionFieldMap)

            checkoutMapData[EnhancedECommerceCheckout.KEY_CHECKOUT] = enhancedECommerceCheckout.checkoutMap

            return checkoutMapData
        }
        return null
    }

}
