package com.tokopedia.shop.settings.notes.view.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.AddShopNoteUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.UpdateShopNoteUseCase
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.shop.settings.notes.view.listener.ShopSettingsNotesAddEditView
import com.tokopedia.usecase.UseCase
import rx.Subscriber
import javax.inject.Inject

class ShopSettingsNoteAddEditPresenter @Inject constructor(private val addShopNoteUseCase: AddShopNoteUseCase,
                                                           private val editShopNoteUseCase: UpdateShopNoteUseCase):
        BaseDaggerPresenter<ShopSettingsNotesAddEditView>() {

    override fun detachView() {
        addShopNoteUseCase.unsubscribe()
        editShopNoteUseCase.unsubscribe()
        super.detachView()
    }

    fun saveNote(shopNoteModel: ShopNoteUiModel, isEdit: Boolean){
        val useCase: UseCase<String> = if (!isEdit) addShopNoteUseCase else editShopNoteUseCase
        if (TextUtils.isEmpty(shopNoteModel.title)||
                TextUtils.isEmpty(shopNoteModel.content)){
            return;
        }

        val requestParam = AddShopNoteUseCase.createRequestParams(shopNoteModel.title!!,
                shopNoteModel.content!!, shopNoteModel.terms)

        if (isEdit)
            requestParam.putString(ID, shopNoteModel.id)

        useCase.execute(requestParam, object : Subscriber<String>() {
            override fun onNext(string: String?) {
                view?.onSuccesAddEdit(string)
            }

            override fun onCompleted() {}

            override fun onError(throwable: Throwable?) {
                view?.onErrorAddEdit(throwable)
            }
        })
    }

    companion object {
        private const val ID = "id"
    }
}