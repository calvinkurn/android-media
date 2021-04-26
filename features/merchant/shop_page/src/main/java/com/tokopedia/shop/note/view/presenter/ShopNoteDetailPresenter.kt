package com.tokopedia.shop.note.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.note.domain.interactor.GetShopNoteDetailUseCase
import com.tokopedia.shop.note.domain.interactor.GetShopNoteDetailUseCase.Companion.createRequestParams
import com.tokopedia.shop.note.view.listener.ShopNoteDetailView
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by normansyahputa on 2/8/18.
 */
class ShopNoteDetailPresenter @Inject constructor(private val getShopNoteDetailUseCase: GetShopNoteDetailUseCase) : BaseDaggerPresenter<ShopNoteDetailView?>() {
    fun getShopNoteList(shopId: String, noteId: String) {
        getShopNoteDetailUseCase.execute(createRequestParams(
                shopId,
                noteId
        ), object : Subscriber<ShopNoteModel>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view?.onErrorGetShopNoteList(e)
                }
            }

            override fun onNext(shopNoteModel: ShopNoteModel) {
                view?.onSuccessGetShopNoteList(shopNoteModel)
            }
        })
    }

    override fun detachView() {
        super.detachView()
        getShopNoteDetailUseCase.unsubscribe()
    }
}