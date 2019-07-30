package com.tokopedia.promocheckout.list.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeException
import com.tokopedia.promocheckout.common.util.*
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailMarketplaceActivity
import com.tokopedia.promocheckout.list.di.PromoCheckoutListModule
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListMarketplaceContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListMarketplacePresenter
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import com.tokopedia.promocheckout.list.di.DaggerPromoCheckoutListComponent
import javax.inject.Inject

class PromoCheckoutListMarketplaceFragment : BasePromoCheckoutListFragment(), PromoCheckoutListMarketplaceContract.View {

    @Inject
    lateinit var promoCheckoutListMarketplacePresenter: PromoCheckoutListMarketplacePresenter

    private var isOneClickShipment: Boolean = false
    private var promo: Promo? = null

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        isCouponActive = arguments?.getBoolean(IS_COUPON_ACTIVE) ?: true
        promoCode = arguments?.getString(PROMO_CODE) ?: ""
        isOneClickShipment = arguments?.getBoolean(ONE_CLICK_SHIPMENT) ?: false
        pageTracking = arguments?.getInt(PAGE_TRACKING) ?: 1
        promo = arguments?.getParcelable(CHECK_PROMO_FIRST_STEP_PARAM)
        super.onCreate(savedInstanceState)
        promoCheckoutListMarketplacePresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.title_loading))
        textInputCoupon.setText(promoCode)
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        super.onItemClicked(promoCheckoutListModel)
        startActivityForResult(PromoCheckoutDetailMarketplaceActivity.createIntent(
                activity, promoCheckoutListModel?.code, oneClickShipment = isOneClickShipment, pageTracking = pageTracking, promo = promo), REQUEST_CODE_DETAIL_PROMO)
    }

    override fun onPromoCodeUse(promoCode: String) {
        promoCheckoutListMarketplacePresenter.checkPromoStackingCode(promoCode, isOneClickShipment, promo)
    }

    override fun onSuccessCheckPromoStackingCode(data: DataUiModel) {
        trackSuccessCheckPromoCode(data)
        val intent = Intent()
        val typePromo = if (data.isCoupon == PromoStackingData.VALUE_COUPON) PromoStackingData.TYPE_COUPON else PromoStackingData.TYPE_VOUCHER
        val promoStackingData = PromoStackingData(
                typePromo = typePromo,
                promoCode = data.codes[0],
                description = data.message.text,
                title = data.titleDescription,
                counterLabel = "",
                amount = data.cashbackWalletAmount,
                state = data.message.state.mapToStatePromoStackingCheckout(),
                trackingDetailUiModels = data.trackingDetailUiModel)
        intent.putExtra(EXTRA_PROMO_DATA, promoStackingData)
        intent.putExtra(EXTRA_INPUT_TYPE, INPUT_TYPE_PROMO_CODE)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onClashCheckPromo(clasingInfoDetailUiModel: ClashingInfoDetailUiModel) {
        val intent = Intent()
        intent.putExtra(EXTRA_CLASHING_DATA, clasingInfoDetailUiModel)
        activity?.setResult(RESULT_CLASHING, intent)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_DETAIL_PROMO) {
            if (resultCode == Activity.RESULT_OK) {
                activity?.setResult(Activity.RESULT_OK, data)
                activity?.finish()
            } else {
                val intent = Intent()
                val bundle = data?.getExtras()
                val clashingInfoDetailUiModel: ClashingInfoDetailUiModel? = bundle?.getParcelable(EXTRA_CLASHING_DATA);
                intent.putExtra(EXTRA_CLASHING_DATA, clashingInfoDetailUiModel)
                activity?.setResult(RESULT_CLASHING, intent)

                if (clashingInfoDetailUiModel != null) {
                    activity?.finish()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initInjector() {
        super.initInjector()
        DaggerPromoCheckoutListComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutListModule(PromoCheckoutListModule())
                .build()
                .inject(this)
    }

    override fun onDestroyView() {
        promoCheckoutListMarketplacePresenter.detachView()
        super.onDestroyView()
    }

    companion object {
        val REQUEST_CODE_DETAIL_PROMO = 231
        val IS_COUPON_ACTIVE = "IS_COUPON_ACTIVE"
        val PROMO_CODE = "PROMO_CODE"
        val ONE_CLICK_SHIPMENT = "ONE_CLICK_SHIPMENT"
        val PAGE_TRACKING = "PAGE_TRACKING"
        val CHECK_PROMO_FIRST_STEP_PARAM = "CHECK_PROMO_FIRST_STEP_PARAM"

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, oneClickShipment: Boolean?, pageTracking: Int,
                           promo: Promo): PromoCheckoutListMarketplaceFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListMarketplaceFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putString(PROMO_CODE, promoCode ?: "")
            bundle.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment ?: false)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            bundle.putParcelable(CHECK_PROMO_FIRST_STEP_PARAM, promo)
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }

}
