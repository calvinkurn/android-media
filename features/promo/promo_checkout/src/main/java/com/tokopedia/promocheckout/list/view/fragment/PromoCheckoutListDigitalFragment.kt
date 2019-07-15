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
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailDigitalActivity
import com.tokopedia.promocheckout.list.di.PromoCheckoutListModule
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListMarketplaceContract
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import com.tokopedia.promocheckout.list.di.DaggerPromoCheckoutListComponent
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListDigitalContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListDigitalPresenter
import javax.inject.Inject

class PromoCheckoutListDigitalFragment : BasePromoCheckoutListFragment(), PromoCheckoutListDigitalContract.View {

    @Inject
    lateinit var promoCheckoutListDigitalPresenter: PromoCheckoutListDigitalPresenter

    @Inject
    lateinit var trackingPromoCheckoutUtil: TrackingPromoCheckoutUtil

    lateinit var progressDialog: ProgressDialog
    var pageTracking: Int = 1
    private var promo: Promo? = Promo()

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        isCouponActive = arguments?.getBoolean(IS_COUPON_ACTIVE) ?: true
        promoCode = arguments?.getString(PROMO_CODE) ?: ""
        pageTracking = arguments?.getInt(PAGE_TRACKING) ?: 1
        super.onCreate(savedInstanceState)
        promoCheckoutListDigitalPresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.title_loading))
        textInputCoupon.setText(promoCode)
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickCoupon(promoCheckoutListModel?.code ?: "")
        } else {
            trackingPromoCheckoutUtil.checkoutClickCoupon(promoCheckoutListModel?.code ?: "")
        }
        startActivityForResult(PromoCheckoutDetailDigitalActivity.createIntent(
                activity, promoCheckoutListModel?.code, pageTracking = pageTracking, promo = promo), REQUEST_CODE_DETAIL_PROMO)
    }

    override fun onPromoCodeUse(promoCode: String) {
        promoCheckoutListDigitalPresenter.checkPromoStackingCode(promoCode, promo)
    }

    override fun showProgressLoading() {
        progressDialog.show()
    }

    override fun hideProgressLoading() {
        progressDialog.hide()
    }

    override fun onErrorCheckPromoCode(e: Throwable) {
        if (e is CheckPromoCodeException || e is MessageErrorException) {
            textInputLayoutCoupon.error = e.message
        } else {
            NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
        }
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCodeFailed()
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCodeFailed()
        }
    }

    override fun onErrorEmptyPromoCode() {
        textInputLayoutCoupon.error = getString(R.string.promostacking_checkout_label_error_empty_voucher_code)
    }

    override fun onSuccessCheckPromoStackingCode(data: DataUiModel) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCodeSuccess(data.codes[0])
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCodeSuccess(data.codes[0])
        }
        val intent = Intent()
        val typePromo = if (data.isCoupon == PromoStackingData.VALUE_COUPON) PromoStackingData.TYPE_COUPON else PromoStackingData.TYPE_VOUCHER
        val promoStackingData = PromoStackingData(typePromo, data.codes[0],
                data.message.text, data.titleDescription, "",
                data.cashbackWalletAmount, data.message.state.mapToStatePromoStackingCheckout())
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

    override fun onImpressionCoupon(promoCheckoutListModel: PromoCheckoutListModel?) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartImpressionCoupon(promoCheckoutListModel?.code ?: "")
        } else {
            trackingPromoCheckoutUtil.checkoutImpressionCoupon(promoCheckoutListModel?.code ?: "")
        }
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
        promoCheckoutListDigitalPresenter.detachView()
        super.onDestroyView()
    }

    companion object {
        val REQUEST_CODE_DETAIL_PROMO = 231
        val IS_COUPON_ACTIVE = "IS_COUPON_ACTIVE"
        val PROMO_CODE = "PROMO_CODE"
        val PAGE_TRACKING = "PAGE_TRACKING"
        val CHECK_PROMO_FIRST_STEP_PARAM = "CHECK_PROMO_FIRST_STEP_PARAM"

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, pageTracking: Int): PromoCheckoutListDigitalFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListDigitalFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putString(PROMO_CODE, promoCode ?: "")
            bundle.putInt(PAGE_TRACKING, pageTracking)
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }

}
