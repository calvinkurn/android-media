package com.tokopedia.broadcast.message.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.broadcast.message.view.listener.BroadcastMessageCreateView
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class BroadcastMessageCreatePresenter @Inject constructor(private val userSession: UserSessionInterface,
                                                          private val gqlGetShopInfoUseCase: GQLGetShopInfoUseCase)
    : BaseDaggerPresenter<BroadcastMessageCreateView>() {

    fun getShopInfo(){
        gqlGetShopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(listOf(userSession.shopId.toIntOrZero()))
        gqlGetShopInfoUseCase.execute(
                {
                    if (isViewAttached) {
                        view?.onSuccessGetShopInfo(it)
                    }
                },
                {
                    if (isViewAttached) {
                        view?.onErrorGetShopInfo(it)
                    }
                }
        )
    }

    override fun detachView() {
        super.detachView()
        gqlGetShopInfoUseCase.cancelJobs()
    }

}