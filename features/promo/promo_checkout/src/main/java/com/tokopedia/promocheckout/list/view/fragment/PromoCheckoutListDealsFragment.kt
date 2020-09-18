package com.tokopedia.promocheckout.list.view.fragment

import android.content.Intent
import android.os.Bundle
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.domain.model.TravelCollectiveBanner
import com.tokopedia.promocheckout.common.domain.model.deals.DealsVerifyBody
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailDealsActivity
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListDealsPresenter
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import javax.inject.Inject


/**
 * Abrar
 */

class PromoCheckoutListDealsFragment() : BasePromoCheckoutListFragment(), PromoCheckoutListContract.View {

    @Inject
    lateinit var promoCheckoutListDealsPresenter: PromoCheckoutListDealsPresenter

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DEALS_STRING
    var categoryID: Int = 1
    var checkoutData: String = ""
    var dealsVerify = DealsVerifyBody()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryID = arguments?.getInt(EXTRA_CATEGORY_ID, 1) ?: 1
        checkoutData = arguments?.getString(EXTRA_CHECKOUT_DATA) ?: ""
        promoCheckoutListDealsPresenter.attachView(this)
    }

    override fun onPromoCodeUse(promoCode: String) {
        var requestBody: JsonObject? = null
        if (checkoutData.isNotBlank() || checkoutData.length > 0) {
            val jsonElement: JsonElement = JsonParser().parse(checkoutData)
            requestBody = jsonElement.asJsonObject
            dealsVerify.promocode = promoCode
            promoCheckoutListDealsPresenter.processCheckDealPromoCode(promoCode, false, requestBody)
        }
    }

    override fun onClickItemPromo(promoCheckoutDealsPromoCode: TravelCollectiveBanner.Banner) {
        textInputCoupon.setText(promoCheckoutDealsPromoCode.attributes.promoCode)
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        super.onItemClicked(promoCheckoutListModel)
        navigateToPromoDetail(promoCheckoutListModel)
    }

    open fun navigateToPromoDetail(promoCheckoutListModel: PromoCheckoutListModel?) {
        var requestBody: JsonObject? = null
        val jsonElement: JsonElement = JsonParser().parse(checkoutData).asJsonObject
        requestBody = jsonElement.asJsonObject
        startActivityForResult(PromoCheckoutDetailDealsActivity.newInstance(
                activity, promoCheckoutListModel?.code
                ?: "", false, requestBody), REQUEST_CODE_PROMO_DETAIL)
    }

    override fun onSuccessCheckPromo(data: DataUiModel) {
        val intent = Intent()
        val promoData = PromoData(PromoData.VOUCHER_RESULT_CODE, data.codes[0],
                data.message.text, data.titleDescription, state = data.message.state.mapToStatePromoCheckout())
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        intent.putExtra(VOUCHER_CODE, data.codes[0])
        intent.putExtra(VOUCHER_MESSAGE, data.titleDescription)
        intent.putExtra(VOUCHER_AMOUNT, data.discountAmount)
        activity?.setResult(PromoData.VOUCHER_RESULT_CODE, intent)
        activity?.finish()
    }

    override fun initInjector() {
        getComponent(PromoCheckoutListComponent::class.java).inject(this)
    }

    override fun onDestroyView() {
        promoCheckoutListDealsPresenter.detachView()
        super.onDestroyView()
    }

    override fun loadData(page: Int) {
        if (isCouponActive) {
            promoCheckoutListPresenter.getListPromo(serviceId, categoryID, page, resources)
        }
        promoCheckoutListPresenter.getListTravelCollectiveBanner(resources)
    }

    companion object {

        val EXTRA_CATEGORY_ID = "EXTRA_CATEGORYID"
        val EXTRA_PRODUCTID = "EXTRA_PRODUCTID"
        val EXTRA_CHECKOUT_DATA = "checkoutdata"
        val VOUCHER_CODE = "voucher_code"
        val VOUCHER_MESSAGE = "voucher_message"
        val VOUCHER_AMOUNT = "voucher_amount"

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, categoryId: Int?, pageTracking: Int?, productId: String?, checkoutData: String?): PromoCheckoutListDealsFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListDealsFragment()
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putString(EXTRA_PROMO_CODE, promoCode ?: "")
            bundle.putInt(EXTRA_CATEGORY_ID, categoryId ?: 1)
            bundle.putInt(PAGE_TRACKING, pageTracking ?: 1)
            bundle.putString(EXTRA_PRODUCTID, productId ?: "")
            bundle.putString(EXTRA_CHECKOUT_DATA, checkoutData ?: "")
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }
}