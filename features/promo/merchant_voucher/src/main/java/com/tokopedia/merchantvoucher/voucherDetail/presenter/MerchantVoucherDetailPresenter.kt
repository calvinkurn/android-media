package com.tokopedia.merchantvoucher.voucherDetail.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherDetailUseCase
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase
import rx.Subscriber
import java.util.*
import javax.inject.Inject

/**
 * Created by hendry on 01/10/18.
 */
class MerchantVoucherDetailPresenter @Inject
constructor(private val getMerchantVoucherDetailUseCase: GetMerchantVoucherDetailUseCase,
            private val userSession: UserSession)
    : BaseDaggerPresenter<MerchantVoucherDetailView>(){

    fun isMyShop(shopId: String) = (userSession.shopId == shopId)

    fun getVoucherDetail(voucherId: Int) {
        getMerchantVoucherDetailUseCase.execute(GetMerchantVoucherListUseCase.createRequestParams(),
                object : Subscriber<MerchantVoucherModel>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                view?.onErrorGetMerchantVoucherDetail(e)
            }

            override fun onNext(merchantVoucherModel: MerchantVoucherModel) {
                view?.onSuccessGetMerchantVoucherDetail(MerchantVoucherViewModel(merchantVoucherModel))
            }
        })
    }

    override fun detachView() {
        super.detachView()
        getMerchantVoucherDetailUseCase.unsubscribe()
    }
}