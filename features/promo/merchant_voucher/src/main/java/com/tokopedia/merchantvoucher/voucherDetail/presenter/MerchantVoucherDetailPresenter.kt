package com.tokopedia.merchantvoucher.voucherDetail.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import javax.inject.Inject

/**
 * Created by hendry on 01/10/18.
 */
class MerchantVoucherDetailPresenter @Inject
constructor(private val userSession: UserSession)
    : BaseDaggerPresenter<MerchantVoucherDetailView>(){

    fun isLogin() = (userSession.isLoggedIn)
    fun isMyShop(shopId: String) = (userSession.shopId == shopId)

    fun getVoucherDetail(voucherId: Int) {
        // currently the api do not support get voucher detail
    }

    override fun detachView() {
        super.detachView()
    }
}