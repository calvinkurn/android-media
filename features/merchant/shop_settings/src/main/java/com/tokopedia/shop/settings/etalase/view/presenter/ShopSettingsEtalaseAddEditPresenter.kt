package com.tokopedia.shop.settings.etalase.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.AddShopEtalaseUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.UpdateShopEtalaseUseCase
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.view.listener.ShopSettingsEtalaseAddEditView
import com.tokopedia.usecase.UseCase
import rx.Subscriber
import javax.inject.Inject

class ShopSettingsEtalaseAddEditPresenter @Inject constructor(private val addShopEtalaseUseCase: AddShopEtalaseUseCase,
                                                              private val updateShopEtalaseUseCase: UpdateShopEtalaseUseCase)
    : BaseDaggerPresenter<ShopSettingsEtalaseAddEditView>(){

    override fun detachView() {
        super.detachView()
        addShopEtalaseUseCase.unsubscribe()
        updateShopEtalaseUseCase.unsubscribe()
    }

    fun saveEtalase(etalaseModel: ShopEtalaseViewModel, isEdit: Boolean = false){
        val useCase: UseCase<String> = if (!isEdit) addShopEtalaseUseCase else updateShopEtalaseUseCase
        val requestParams = AddShopEtalaseUseCase.createRequestParams(etalaseModel.name)

        if (isEdit){
            requestParams.putString(ID, etalaseModel.id)
        }

        useCase.execute(requestParams, object : Subscriber<String>() {
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