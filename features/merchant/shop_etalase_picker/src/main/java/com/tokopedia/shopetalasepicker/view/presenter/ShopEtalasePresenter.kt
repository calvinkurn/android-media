package com.tokopedia.shopetalasepicker.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shopetalasepicker.view.listener.ShopEtalaseView
import com.tokopedia.shopetalasepicker.view.model.ShopEtalaseViewModel
import rx.Subscriber
import java.util.*
import javax.inject.Inject

/**
 * Created by normansyahputa on 2/28/18.
 */

class ShopEtalasePresenter @Inject
constructor(private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
            private val userSession: UserSession) : BaseDaggerPresenter<ShopEtalaseView>() {

    fun getShopEtalase(shopId: String?) {
        if (shopId.isNullOrEmpty()) {
            return
        }
        val params = GetShopEtalaseByShopUseCase.createRequestParams(
                shopId!!, true, false, shopId.equals(userSession.shopId, ignoreCase = true))
        getShopEtalaseByShopUseCase.execute(params, object : Subscriber<ArrayList<ShopEtalaseModel>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view?.showGetListError(e)
            }

            override fun onNext(shopEtalaseModels: ArrayList<ShopEtalaseModel>) {
                view?.renderList(map(shopEtalaseModels), false)
            }

            private fun map(shopEtalaseModels: ArrayList<ShopEtalaseModel>): List<ShopEtalaseViewModel> {
                if (shopEtalaseModels.size == 0) {
                    return listOf()
                }
                return shopEtalaseModels.map { ShopEtalaseViewModel(it) }
            }
        })
    }

    fun clearEtalaseCache() {
        getShopEtalaseByShopUseCase.clearCache()
    }

}
