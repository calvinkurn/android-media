package com.tokopedia.shop.info.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.shop.extension.transformToVisitable
import com.tokopedia.shop.info.view.listener.ShopInfoView
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote
import com.tokopedia.shop.note.domain.interactor.GetShopNoteListUseCase
import rx.Subscriber
import javax.inject.Inject

class ShopInfoPresenter
    @Inject constructor(private val getShopNoteListUseCase: GetShopNoteListUseCase,
                        private val userSession: UserSession): BaseDaggerPresenter<ShopInfoView>(){

    fun isMyshop(shopId: String) = userSession.shopId == shopId

    fun getShopNoteList(shopId: String) = getShopNoteListUseCase
            .execute(GetShopNoteListUseCase.createRequestParam(shopId), object : Subscriber<List<ShopNote>>() {
                override fun onNext(shopNotes: List<ShopNote>?) {
                    view?.renderListNote((shopNotes ?: listOf()).map { it.transformToVisitable() })
                }

                override fun onCompleted() {}

                override fun onError(throwable: Throwable?) {
                    view?.showListNoteError(throwable)
                }

            })

    override fun detachView() {
        super.detachView()
        getShopNoteListUseCase.unsubscribe()
    }
}