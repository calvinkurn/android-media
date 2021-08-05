package com.tokopedia.promocheckout.list.view.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailDealsActivity
import com.tokopedia.promocheckout.detail.view.fragment.PromoCheckoutDetailDealsFragment
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.viewmodel.PromoCheckoutListDealsViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import timber.log.Timber


/**
 * Abrar
 */

class PromoCheckoutListDealsFragment() : BasePromoCheckoutListFragment() {

    private val dealsPromoCheckoutListViewModel: PromoCheckoutListDealsViewModel by lazy { viewModelProvider.get(PromoCheckoutListDealsViewModel::class.java) }

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING
    var categoryID: Int = 1
    var checkoutData: String = ""
    var promoCodeApplied: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryID = arguments?.getInt(EXTRA_CATEGORY_ID, 1) ?: 1
        checkoutData = arguments?.getString(EXTRA_CHECKOUT_DATA) ?: ""
        promoCodeApplied = arguments?.getString(EXTRA_PROMO_CODE) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (promoCodeApplied != null) {
            textInputCoupon.textFieldInput.setText(promoCodeApplied)
        }
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dealsPromoCheckoutListViewModel.showLoadingPromoDeals.observe(viewLifecycleOwner, {
            if (it) {
                showProgressLoading()
            } else {
                hideProgressLoading()
            }
        })

        dealsPromoCheckoutListViewModel.dealsCheckVoucherResult.observe(viewLifecycleOwner, {
            when(it){
                is Success ->{
                    onSuccessCheckPromo(it.data)
                }
                is Fail->{
                    onErrorCheckPromo(it.throwable)
                }
            }
        })

        dealsPromoCheckoutListViewModel.listTravelCollectiveBanner.observe(viewLifecycleOwner, {
            when(it){
                is Success ->{
                    if(it.data.isNotEmpty()){
                        changeTitle(getString(R.string.promo_title_for_this_category))
                    }
                    renderListLastSeen(it.data, true)
                }
                is Fail->{
                    showGetListLastSeenError(it.throwable)
                }
            }
        })
    }

    override fun onPromoCodeUse(promoCode: String) {
        if (promoCode.isNotEmpty()) {
            var requestBody: JsonObject? = null
            if (checkoutData.isNotBlank() || checkoutData.length > 0) {
                val jsonElement: JsonElement = JsonParser().parse(checkoutData)
                requestBody = jsonElement.asJsonObject
                requestBody.addProperty(PROMOCODE, promoCode)
                dealsPromoCheckoutListViewModel.processCheckDealPromoCode(false, requestBody)
            }
        }
    }

    override fun onClickItemLastSeen(promoCheckoutLastSeenModel: PromoCheckoutLastSeenModel) {
        textInputCoupon.textFieldInput.setText(promoCheckoutLastSeenModel.promoCode)
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        super.onItemClicked(promoCheckoutListModel)
        navigateToPromoDetail(promoCheckoutListModel)
    }

    open fun navigateToPromoDetail(promoCheckoutListModel: PromoCheckoutListModel?) {
        startActivityForResult(PromoCheckoutDetailDealsActivity.newInstance(
                activity, promoCheckoutListModel?.code
                ?: "", false, checkoutData), REQUEST_CODE_PROMO_DETAIL)
    }

    fun onSuccessCheckPromo(data: DataUiModel) {
        val intent = Intent()
        val promoData = PromoData(PromoData.VOUCHER_RESULT_CODE, data.codes[0],
                data.message.text, data.titleDescription, state = data.message.state.mapToStatePromoCheckout())
        intent.putExtra(PromoCheckoutDetailDealsFragment.IS_CANCEL, false)
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        intent.putExtra(VOUCHER_CODE, data.codes[0])
        intent.putExtra(VOUCHER_MESSAGE, data.message.text)
        intent.putExtra(VOUCHER_DISCOUNT_AMOUNT, data.discountAmount)
        activity?.setResult(PromoData.VOUCHER_RESULT_CODE, intent)
        activity?.finish()
    }

    override fun initInjector() {
        getComponent(PromoCheckoutListComponent::class.java).inject(this)
    }

    override fun hideProgressLoading() {
        progressDialog?.hide()
    }

    override fun showProgressLoading() {
        try {
            progressDialog?.show()
        } catch (exception: UnsupportedOperationException) {
            Timber.d(exception)
        }
    }

    override fun loadData(page: Int) {
        if (isCouponActive) {
            promoCheckoutListViewModel.getPromoList(serviceId, categoryId, page)
        }
        promoCheckoutListViewModel.getPromoLastSeen(listOf(categoryId))
    }

    companion object {
        const val EXTRA_CATEGORY_ID = "EXTRA_CATEGORYID"
        const val EXTRA_PRODUCTID = "EXTRA_PRODUCTID"
        const val VOUCHER_DISCOUNT_AMOUNT = "VOUCHER_DISCOUNT_AMOUNT"
        const val EXTRA_CHECKOUT_DATA = "checkoutdata"
        const val VOUCHER_CODE = "voucher_code"
        const val VOUCHER_MESSAGE = "voucher_message"
        const val PROMOCODE = "promocode"

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