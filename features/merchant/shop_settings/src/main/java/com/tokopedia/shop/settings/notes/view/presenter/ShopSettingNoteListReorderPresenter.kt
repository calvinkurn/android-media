package com.tokopedia.shop.settings.notes.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.ReorderShopNoteUseCase

import java.util.ArrayList

import javax.inject.Inject

import rx.Subscriber

/**
 * Created by hendry on 16/08/18.
 */
class ShopSettingNoteListReorderPresenter @Inject
constructor(private val reorderShopNoteUseCase: ReorderShopNoteUseCase) : BaseDaggerPresenter<ShopSettingNoteListReorderPresenter.View>() {

    interface View : CustomerView {
        fun onSuccessReorderShopNote(successMessage: String)
        fun onErrorReorderShopNote(throwable: Throwable)
    }

    fun reorderShopNotes(noteIdList: ArrayList<String>) {
        reorderShopNoteUseCase.unsubscribe()
        reorderShopNoteUseCase.execute(ReorderShopNoteUseCase.createRequestParams(noteIdList),
                object : Subscriber<String>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view?.onErrorReorderShopNote(e)
                    }

                    override fun onNext(successMessage: String) {
                        view?.onSuccessReorderShopNote(successMessage)
                    }
                })
    }

    override fun detachView() {
        super.detachView()
        reorderShopNoteUseCase.unsubscribe()
    }
}
