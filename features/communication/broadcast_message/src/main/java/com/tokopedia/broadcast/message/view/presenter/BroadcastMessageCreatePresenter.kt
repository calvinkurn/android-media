package com.tokopedia.broadcast.message.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.broadcast.message.view.listener.BroadcastMessageCreateView
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import rx.Subscriber
import javax.inject.Inject

class BroadcastMessageCreatePresenter @Inject constructor(private val userSession: UserSessionInterface,
                                                          private val getShopInfoUseCase: GetShopInfoUseCase)
    : BaseDaggerPresenter<BroadcastMessageCreateView>() {

    fun getShopInfo(){
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(userSession.shopId), object : Subscriber<ShopInfo>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                view?.onErrorGetShopInfo(e)
            }

            override fun onNext(shopInfo: ShopInfo) {
                view?.onSuccessGetShopInfo(shopInfo)
            }
        })
    }

    override fun detachView() {
        super.detachView()
        getShopInfoUseCase.unsubscribe()
    }

}