package com.tokopedia.manageaddress.ui.shoplocation.shopaddress

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationOldUiModel
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.DeleteShopLocationUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shoplocation.GetShopLocationUseCase
import com.tokopedia.manageaddress.ui.shoplocation.shopaddress.listener.ShopLocationOldView
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
                        this.id, this.name, this.address, this.districtId.toLongOrZero(), this.districtName, this.cityId.toLongOrZero(),
                        this.cityName, this.stateId.toLongOrZero(), this.stateName, this.postalCode, this.email, this.phone,
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