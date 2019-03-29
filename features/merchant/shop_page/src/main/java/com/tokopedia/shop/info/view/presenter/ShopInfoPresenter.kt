package com.tokopedia.shop.info.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeedV2
import com.tokopedia.reputation.common.domain.interactor.GetReputationSpeedDailyUseCase
import com.tokopedia.reputation.common.domain.interactor.GetReputationSpeedUseCase
import com.tokopedia.shop.extension.transformToVisitable
import com.tokopedia.shop.info.view.listener.ShopInfoView
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote
import com.tokopedia.shop.note.domain.interactor.GetShopNoteListUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class ShopInfoPresenter
    @Inject constructor(private val getShopNoteListUseCase: GetShopNoteListUseCase,
                        private val getReputationSpeedDailyUseCase: GetReputationSpeedDailyUseCase,
                        private val userSession: UserSessionInterface): BaseDaggerPresenter<ShopInfoView>(){

    fun isMyshop(shopId: String) = userSession.shopId == shopId

    fun getShopNoteList(shopId: String) {
        getShopNoteListUseCase
                .execute(GetShopNoteListUseCase.createRequestParam(shopId), object : Subscriber<List<ShopNote>>() {
                    override fun onNext(shopNotes: List<ShopNote>?) {
                        view?.renderListNote((shopNotes
                                ?: listOf()).map { it.transformToVisitable() })
                    }

                    override fun onCompleted() {}

                    override fun onError(throwable: Throwable?) {
                        view?.showListNoteError(throwable)
                    }

                })
    }

    fun getShopReputationSpeed(shopId: String) {
        getReputationSpeedDailyUseCase
                .execute(GetReputationSpeedUseCase.createRequestParam(shopId), object : Subscriber<ReputationSpeedV2>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                view?.onErrorGetReputation(e)
            }

            override fun onNext(reputationSpeed: ReputationSpeedV2) {
                view?.onSuccessGetReputation(reputationSpeed)
            }
        })
    }

    override fun detachView() {
        super.detachView()
        getShopNoteListUseCase.unsubscribe()
        getReputationSpeedDailyUseCase.unsubscribe()
    }
}