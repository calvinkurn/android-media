package com.tokopedia.shop.settings.notes.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.DeleteShopNoteUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.GetShopNotesUseCase
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.usecase.RequestParams

import java.util.ArrayList

import javax.inject.Inject

import rx.Subscriber

/**
 * Created by hendry on 16/08/18.
 */
class ShopSettingNoteListPresenter @Inject
constructor(private val getShopNotesUseCase: GetShopNotesUseCase,
            private val deleteShopNoteUseCase: DeleteShopNoteUseCase) : BaseDaggerPresenter<ShopSettingNoteListPresenter.View>() {

    interface View : CustomerView {
        fun onSuccessGetShopNotes(shopNoteModels: ArrayList<ShopNoteUiModel>)
        fun onErrorGetShopNotes(throwable: Throwable)
        fun onSuccessDeleteShopNote(successMessage: String)
        fun onErrorDeleteShopNote(throwable: Throwable)
    }

    fun getShopNotes() {
        getShopNotesUseCase.unsubscribe()
        getShopNotesUseCase.execute(RequestParams.EMPTY, object : Subscriber<ArrayList<ShopNoteModel>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view?.onErrorGetShopNotes(e)
            }

            override fun onNext(shopNoteModels: ArrayList<ShopNoteModel>) {
                view?.run{
                    val shopNoteViewModelArrayList = ArrayList<ShopNoteUiModel>()
                    for (shopNoteModel in shopNoteModels) {
                        shopNoteViewModelArrayList.add(ShopNoteUiModel(shopNoteModel))
                    }
                    onSuccessGetShopNotes(shopNoteViewModelArrayList)
                }
            }
        })
    }

    fun deleteShopNote(noteId: String) {
        deleteShopNoteUseCase.unsubscribe()
        deleteShopNoteUseCase.execute(DeleteShopNoteUseCase.createRequestParams(noteId),
                object : Subscriber<String>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view?.onErrorDeleteShopNote(e)
                    }

                    override fun onNext(successMessage: String) {
                        view?.onSuccessDeleteShopNote(successMessage)
                    }
                })
    }

    override fun detachView() {
        super.detachView()
        getShopNotesUseCase.unsubscribe()
        deleteShopNoteUseCase.unsubscribe()
    }
}
