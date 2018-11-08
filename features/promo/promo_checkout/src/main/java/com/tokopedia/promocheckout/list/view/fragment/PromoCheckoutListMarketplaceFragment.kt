package com.tokopedia.promocheckout.list.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.list.di.DaggerPromoCheckoutListComponent
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailMarketplaceActivity
import com.tokopedia.promocheckout.list.di.PromoCheckoutListModule
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListMarketplaceContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListMarketplacePresenter
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import javax.inject.Inject

class PromoCheckoutListMarketplaceFragment : BasePromoCheckoutListFragment(), PromoCheckoutListMarketplaceContract.View {

    @Inject
    lateinit var promoCheckoutListMarketplacePresenter: PromoCheckoutListMarketplacePresenter

    private var isOneClickShipment: Boolean = false
    lateinit var progressDialog : ProgressDialog

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        isCouponActive = arguments?.getBoolean(IS_COUPON_ACTIVE)?:true
        promoCode = arguments?.getString(PROMO_CODE)?:""
        isOneClickShipment = arguments?.getBoolean(ONE_CLICK_SHIPMENT)?:false
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
        startActivityForResult(PromoCheckoutDetailMarketplaceActivity.createIntent(activity, promoCheckoutListModel?.code,  isOneClickShipment), REQUEST_CODE_DETAIL_PROMO)
    }

    override fun onPromoCodeUse(promoCode: String) {
        promoCheckoutListMarketplacePresenter.checkPromoCode(promoCode, isOneClickShipment)
    }

    override fun showProgressLoading() {
        progressDialog.show()
    }

    override fun hideProgressLoading() {
        progressDialog.show()
    }

    override fun onErrorCheckPromoCode(e: Throwable) {
        textInputLayoutCoupon.error = ErrorHandler.getErrorMessage(activity, e)
    }

    override fun onSuccessCheckPromoCode(dataVoucher: DataVoucher) {
        val intent = Intent()
        val typePromo = if(dataVoucher.isCoupon == PromoData.TYPE_COUPON ) PromoData.TYPE_COUPON else PromoData.TYPE_VOUCHER
        val promoData = PromoData(typePromo, dataVoucher.code?:"",
                dataVoucher.message?.text?:"", dataVoucher.titleDescription?:"",
                dataVoucher.cashbackAmount, dataVoucher.message?.state?.mapToStatePromoCheckout()?:TickerCheckoutView.State.EMPTY)
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_DETAIL_PROMO){
            activity?.setResult(Activity.RESULT_OK, data)
            activity?.finish()
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

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, oneClickShipment: Boolean?): PromoCheckoutListMarketplaceFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListMarketplaceFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_COUPON_ACTIVE, isCouponActive?:true)
            bundle.putString(PROMO_CODE, promoCode?:"")
            bundle.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment?:false)
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }

}
