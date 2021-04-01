package com.tokopedia.shop.settings.address.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationOldUiModel
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.DeleteShopLocationUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.GetShopLocationUseCase
import com.tokopedia.shop.settings.address.view.listener.ShopLocationOldView
import rx.Subscriber
import javax.inject.Inject

class ShopLocationOldPresenter @Inject
    constructor(private val getShopLocationUseCase: GetShopLocationUseCase, private val deleteShopLocationUseCase: DeleteShopLocationUseCase)
    : BaseDaggerPresenter<ShopLocationOldView>(){

    override fun detachView() {
        getShopLocationUseCase.unsubscribe()
        deleteShopLocationUseCase.unsubscribe()
        super.detachView()
    }

    fun getShopAddress() {
        getShopLocationUseCase.execute(object : Subscriber<List<ShopLocationModel>>() {
            override fun onNext(addresses: List<ShopLocationModel>?) {
                fun ShopLocationModel.toViewModel() = ShopLocationOldUiModel(
                        this.id, this.name, this.address, this.districtId, this.districtName, this.cityId,
                        this.cityName, this.stateId, this.stateName, this.postalCode, this.email, this.phone,
                        this.fax)

                view?.onSuccessLoadAddresses(addresses?.map { it.toViewModel() })
            }

            override fun onCompleted() {}

            override fun onError(throwable: Throwable?) {
                view?.onErrorLoadAddresses(throwable)
            }

        })
    }

    fun deleteItem(item: ShopLocationOldUiModel) {
        deleteShopLocationUseCase.execute(DeleteShopLocationUseCase.createRequestParams(item.id), object : Subscriber<String>(){
            override fun onNext(string: String?) {
                view?.onSuccessDeleteAddress(string)
            }

            override fun onCompleted() {}

            override fun onError(throwable: Throwable?) {
                view?.onErrorDeleteAddress(throwable)
            }

        })
    }


}