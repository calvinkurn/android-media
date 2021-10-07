package com.tokopedia.promocheckout.list.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailDigitalActivity
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListDigitalPresenter
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import javax.inject.Inject

open class PromoCheckoutListDigitalFragment : BasePromoCheckoutListFragment(), PromoCheckoutListContract.View {

    @Inject
    lateinit var promoCheckoutListDigitalPresenter: PromoCheckoutListDigitalPresenter

    @Inject
    lateinit var userSession: UserSession
    lateinit var promoDigitalModel: PromoDigitalModel

    private var categoryName = ""
    private var operatorName = ""

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCouponActive = arguments?.getBoolean(EXTRA_COUPON_ACTIVE) ?: true
        promoCode = arguments?.getString(EXTRA_PROMO_CODE) ?: ""
        promoDigitalModel = arguments?.getParcelable(EXTRA_PROMO_DIGITAL_MODEL)
                ?: PromoDigitalModel()
        categoryId = promoDigitalModel.categoryId
        categoryName = promoDigitalModel.categoryName
        operatorName = promoDigitalModel.operatorName
        pageTracking = arguments?.getInt(PAGE_TRACKING) ?: 1
        promoCheckoutListDigitalPresenter.attachView(this)
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        super.onItemClicked(promoCheckoutListModel)
        promoCheckoutAnalytics.clickDigitalMyPromo(categoryName, operatorName, userSession.userId)
        navigateToPromoDetail(promoCheckoutListModel)
    }

    open fun navigateToPromoDetail(promoCheckoutListModel: PromoCheckoutListModel?) {
        startActivityForResult(PromoCheckoutDetailDigitalActivity.newInstance(
                activity, promoCheckoutListModel?.code
                ?: "", false, promoDigitalModel, pageTracking), REQUEST_CODE_PROMO_DETAIL)
    }

    override fun onPromoCodeUse(promoCode: String) {
        if (promoCheckoutListDigitalPresenter.isViewNotAttached) promoCheckoutListDigitalPresenter.attachView(this)
        if (promoCode.isNotEmpty()) promoCheckoutListDigitalPresenter.checkPromoCode(promoCode, promoDigitalModel)
    }

    override fun onSuccessCheckPromo(data: DataUiModel) {
        trackSuccessCheckPromoCode(data)
        val intent = Intent()
        val promoData = PromoData(data.isCoupon, data.codes[0],
                data.message.text, data.titleDescription, data.discountAmount, state = data.message.state.mapToStatePromoCheckout())
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onClickItemLastSeen(promoCheckoutLastSeenModel: PromoCheckoutLastSeenModel) {
        textInputCoupon.textFieldInput.setText(promoCheckoutLastSeenModel.promoCode)
        promoCheckoutAnalytics.clickDigitalLastSeenPromo(promoCheckoutLastSeenModel.promoCode, userSession.userId)
    }

    override fun loadData(page: Int) {
        if (isCouponActive) {
            promoCheckoutListPresenter.getListPromo(serviceId, categoryId, page, resources)
        }
        promoCheckoutListPresenter.getListLastSeen(listOf(categoryId), resources)
    }

    override fun initInjector() {
        getComponent(PromoCheckoutListComponent::class.java).inject(this)
    }

    override fun onDestroyView() {
        promoCheckoutListDigitalPresenter.detachView()
        super.onDestroyView()
    }

    companion object {
        const val EXTRA_PROMO_DIGITAL_MODEL = "EXTRA_PROMO_DIGITAL_MODEL"

        private val promoCheckoutAnalytics: PromoCheckoutAnalytics by lazy { PromoCheckoutAnalytics() }

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, promoDigitalModel: PromoDigitalModel, pageTracking: Int): PromoCheckoutListDigitalFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListDigitalFragment()
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putString(EXTRA_PROMO_CODE, promoCode ?: "")
            bundle.putParcelable(EXTRA_PROMO_DIGITAL_MODEL, promoDigitalModel)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }

}
