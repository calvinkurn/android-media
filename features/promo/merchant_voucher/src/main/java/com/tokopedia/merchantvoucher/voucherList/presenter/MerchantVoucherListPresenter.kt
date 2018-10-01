package com.tokopedia.merchantvoucher.voucherList.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by hendry on 01/10/18.
 */
class MerchantVoucherListPresenter @Inject
constructor(private val getShopInfoUseCase: GetShopInfoUseCase,
            private val deleteShopInfoUseCase: DeleteShopInfoCacheUseCase,
            private val userSession: UserSession)
    : BaseDaggerPresenter<MerchantVoucherListView>(){

    fun isMyShop(shopId: String) = (userSession.shopId == shopId)

    fun getShopInfo(shopId: String) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), object : Subscriber<ShopInfo>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                e.printStackTrace()
                view?.onErrorGetShopInfo(e)
            }

            override fun onNext(shopInfo: ShopInfo) {
                view?.onSuccessGetShopInfo(shopInfo)
            }
        })
    }

    fun clearCache() {
        deleteShopInfoUseCase.executeSync()
    }

    override fun detachView() {
        super.detachView()
        getShopInfoUseCase.unsubscribe()
    }
}