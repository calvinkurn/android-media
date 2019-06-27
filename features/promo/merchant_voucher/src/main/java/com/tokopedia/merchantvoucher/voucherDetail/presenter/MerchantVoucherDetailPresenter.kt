package com.tokopedia.merchantvoucher.voucherDetail.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.UseMerchantVoucherUseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by hendry on 01/10/18.
 */
class MerchantVoucherDetailPresenter @Inject
constructor(private val userSessionInterface: UserSessionInterface,
            private val useMerchantVoucherUseCase: UseMerchantVoucherUseCase )
    : BaseDaggerPresenter<MerchantVoucherDetailView>(){

    var voucherCodeInProgress:String = ""

    fun isLogin() = (userSessionInterface.isLoggedIn)
    fun isMyShop(shopId: String?) :Boolean{
        if (shopId == null) {
            return false
        }
        return (userSessionInterface.shopId == shopId)
    }

    fun getVoucherDetail(voucherId: Int) {
        // currently the api do not support get voucher detail
    }

    fun useMerchantVoucher(voucherCode: String, voucherId:Int) {
        if (voucherCodeInProgress.equals(voucherCode)) {
            return;
        }
        voucherCodeInProgress = voucherCode
        useMerchantVoucherUseCase.unsubscribe()
        useMerchantVoucherUseCase.execute(UseMerchantVoucherUseCase.createRequestParams(voucherCode, voucherId),
                object : Subscriber<UseMerchantVoucherQueryResult>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        voucherCodeInProgress = ""
                        view?.onErrorUseVoucher(e)
                    }

                    override fun onNext(useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult) {
                        // success should be true
                        voucherCodeInProgress = ""
                        view?.onSuccessUseVoucher(useMerchantVoucherQueryResult)
                    }
                })
    }

    override fun detachView() {
        super.detachView()
        useMerchantVoucherUseCase.unsubscribe()
    }
}