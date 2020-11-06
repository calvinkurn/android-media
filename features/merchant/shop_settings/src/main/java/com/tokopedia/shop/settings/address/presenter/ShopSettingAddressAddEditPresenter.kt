package com.tokopedia.shop.settings.address.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.AddShopLocationUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.UpdateShopLocationUseCase
import com.tokopedia.shop.settings.address.data.ShopLocationUiModel
import com.tokopedia.shop.settings.address.view.listener.ShopSettingAddressAddEditView
import com.tokopedia.usecase.UseCase
import rx.Subscriber
import javax.inject.Inject

class ShopSettingAddressAddEditPresenter @Inject constructor(private val addShopLocationUseCase: AddShopLocationUseCase,
                                                             private val updateShopLocationUseCase: UpdateShopLocationUseCase)
    : BaseDaggerPresenter<ShopSettingAddressAddEditView>(){

    override fun detachView() {
        super.detachView()
        addShopLocationUseCase.unsubscribe()
        updateShopLocationUseCase.unsubscribe()
    }

    fun saveAddress(shopLocationUiModel: ShopLocationUiModel, isNew: Boolean){
        val useCase: UseCase<String> = if (isNew) addShopLocationUseCase else updateShopLocationUseCase
        val requestParam = AddShopLocationUseCase.createRequestParams(shopLocationUiModel.name,
                shopLocationUiModel.address, shopLocationUiModel.districtId, shopLocationUiModel.cityId,
                shopLocationUiModel.stateId, shopLocationUiModel.postalCode, shopLocationUiModel.email,
                shopLocationUiModel.phone, shopLocationUiModel.fax)

        if (!isNew)
            requestParam.putString(ID, shopLocationUiModel.id)

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