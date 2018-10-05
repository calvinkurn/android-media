package com.tokopedia.merchantvoucher.voucherDetail.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.UseMerchantVoucherUseCase
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by hendry on 01/10/18.
 */
class MerchantVoucherDetailPresenter @Inject
constructor(private val userSession: UserSession,
            private val useMerchantVoucherUseCase: UseMerchantVoucherUseCase )
    : BaseDaggerPresenter<MerchantVoucherDetailView>(){

    fun isLogin() = (userSession.isLoggedIn)
    fun isMyShop(shopId: String?) :Boolean{
        if (shopId == null) {
            return false
        }
        return (userSession.shopId == shopId)
    }

    fun getVoucherDetail(voucherId: Int) {
        // currently the api do not support get voucher detail
    }

    fun useMerchantVoucher(voucherCode: String, voucherId:Int) {
        useMerchantVoucherUseCase.execute(UseMerchantVoucherUseCase.createRequestParams(voucherCode, voucherId),
                object : Subscriber<Boolean>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        view?.onErrorUseVoucher(e)
                    }

                    override fun onNext(success: Boolean) {
                        // success should be true
                        view?.onSuccessUseVoucher()
                    }
                })
    }

    override fun detachView() {
        super.detachView()
        useMerchantVoucherUseCase.unsubscribe()
    }
}