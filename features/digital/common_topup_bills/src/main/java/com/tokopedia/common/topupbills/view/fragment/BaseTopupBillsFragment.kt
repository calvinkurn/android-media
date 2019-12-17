package com.tokopedia.common.topupbills.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
abstract class BaseTopupBillsFragment: BaseDaggerFragment()  {

    lateinit var checkoutPassData: DigitalCheckoutPassData

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var topupBillsViewModel: TopupBillsViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        topupBillsViewModel.enquiryData.observe(this, Observer {
            it.run {
                when (it) {
                    is Success -> processEnquiry(it.data)
                    is Fail -> showEnquiryError(it.throwable)
                }
            }
        })
        topupBillsViewModel.menuDetailData.observe(this, Observer {
            it.run {
                when (it) {
                    is Success -> processMenuDetail(it.data)
                    is Fail -> showMenuDetailError(it.throwable)
                }
            }
        })
        topupBillsViewModel.favNumberData.observe(this, Observer {
            it.run {
                when (it) {
                    is Success -> processFavoriteNumbers(it.data)
                    is Fail -> showFavoriteNumbersError(it.throwable)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_LOGIN) {
                processToCart()
            } else if (requestCode == REQUEST_CODE_CART_DIGITAL) {
                data?.getStringExtra(DigitalExtraParam.EXTRA_MESSAGE)?.let {
                    NetworkErrorHelper.showSnackbar(activity, it)
                }
            }
        }
    }

    fun getEnquiry(operatorId: String, productId: String, inputData: Array<Map<String, String>>) {
        topupBillsViewModel.getEnquiry(GraphqlHelper.loadRawString(resources, R.raw.query_recharge_inquiry),
                topupBillsViewModel.createEnquiryParams(operatorId, productId, inputData))
    }

    fun getMenuDetail(menuId: Int) {
        topupBillsViewModel.getMenuDetail(GraphqlHelper.loadRawString(resources, R.raw.query_menu_detail),
                topupBillsViewModel.createMenuDetailParams(menuId))
    }

    fun getFavoriteNumbers(categoryId: Int) {
        topupBillsViewModel.getFavoriteNumbers(GraphqlHelper.loadRawString(resources, R.raw.query_fav_number_digital),
                topupBillsViewModel.createFavoriteNumbersParams(categoryId))
    }

    abstract fun processEnquiry(data: TopupBillsEnquiryData)

    abstract fun processMenuDetail(data: TopupBillsMenuDetail)

    abstract fun processFavoriteNumbers(data: TopupBillsFavNumber)

    abstract fun showEnquiryError(t: Throwable)

    abstract fun showMenuDetailError(t: Throwable)

    abstract fun showFavoriteNumbersError(t: Throwable)

    fun processToCart() {
        if (userSession.isLoggedIn) {
            navigateToCart()
        } else {
            navigateToLoginPage()
        }
    }

    private fun navigateToCart() {
        if (::checkoutPassData.isInitialized) {
            checkoutPassData.idemPotencyKey = userSession.userId.generateRechargeCheckoutToken()
            val intent = RouteManager.getIntent(context, ApplinkConsInternalDigital.CART_DIGITAL)
            intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, checkoutPassData)
            startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
        }
    }

    fun navigateToLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

//    fun navigateToPromoPage() {
//        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_DIGITAL)
//        intent.putExtra("EXTRA_COUPON_ACTIVE", cartDigitalInfoData.attributes!!.isCouponActive)
//        intent.putExtra("EXTRA_PROMO_DIGITAL_MODEL", getPromoDigitalModel())
//        startActivityForResult(intent, REQUEST_CODE_LIST_PROMO)
//    }
//
//    private fun getPromoDigitalModel(): PromoDigitalModel {
//        return PromoDigitalModel(
//                Integer.parseInt(cartPassData.categoryId!!),
//                getProductId(),
//                cartPassData.clientNumber!!,
//                cartDigitalInfoData.attributes!!.pricePlain
//        )
//    }

    companion object {
        const val REQUEST_CODE_LOGIN = 1010
        const val REQUEST_CODE_CART_DIGITAL = 1090
        const val REQUEST_CODE_LIST_PROMO = 232
    }

}