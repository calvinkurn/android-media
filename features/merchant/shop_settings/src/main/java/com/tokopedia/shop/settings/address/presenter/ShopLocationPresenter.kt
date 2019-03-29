package com.tokopedia.shop.settings.address.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.DeleteShopLocationUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.GetShopLocationUseCase
import com.tokopedia.shop.settings.address.data.ShopLocationViewModel
import com.tokopedia.shop.settings.address.view.listener.ShopLocationView
import rx.Subscriber
import javax.inject.Inject

class ShopLocationPresenter @Inject
    constructor(private val getShopLocationUseCase: GetShopLocationUseCase, private val deleteShopLocationUseCase: DeleteShopLocationUseCase)
    : BaseDaggerPresenter<ShopLocationView>(){

    override fun detachView() {
        getShopLocationUseCase.unsubscribe()
        deleteShopLocationUseCase.unsubscribe()
        super.detachView()
    }

    fun getShopAddress() {
        getShopLocationUseCase.execute(object : Subscriber<List<ShopLocationModel>>() {
            override fun onNext(addresses: List<ShopLocationModel>?) {
                fun ShopLocationModel.toViewModel() = ShopLocationViewModel(
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

    fun deleteItem(item: ShopLocationViewModel) {
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