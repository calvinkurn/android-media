package com.tokopedia.shop.settings.etalase.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.DeleteShopEtalaseUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.DeleteShopNoteUseCase
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel

import java.util.ArrayList

import javax.inject.Inject

import rx.Subscriber

/**
 * Created by hendry on 16/08/18.
 */
class ShopSettingEtalaseListPresenter @Inject
constructor(private val getShopEtalaseUseCase: GetShopEtalaseUseCase,
            private val deleteShopEtalaseUseCase: DeleteShopEtalaseUseCase) : BaseDaggerPresenter<ShopSettingEtalaseListPresenter.View>() {

    interface View : CustomerView {
        fun onSuccessGetShopEtalase(shopEtalaseViewModels: ArrayList<ShopEtalaseViewModel>)
        fun onErrorGetShopEtalase(throwable: Throwable)
        fun onSuccessDeleteShopEtalase(successMessage: String)
        fun onErrorDeleteShopEtalase(throwable: Throwable)
    }

    fun getShopEtalase() {
        getShopEtalaseUseCase.unsubscribe()
        getShopEtalaseUseCase.execute(GetShopEtalaseUseCase.createRequestParams(true),
                object : Subscriber<ArrayList<ShopEtalaseModel>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view?.onErrorGetShopEtalase(e)
                    }

                    override fun onNext(shopEtalaseModels: ArrayList<ShopEtalaseModel>) {
                        view?.run {
                            val shopEtalaseViewModels = ArrayList<ShopEtalaseViewModel>()
                            var countPrimaryEtalase = 0
                            for (shopEtalaseModel in shopEtalaseModels) {
                                shopEtalaseViewModels.add(ShopEtalaseViewModel(shopEtalaseModel,
                                        countPrimaryEtalase < PRIMARY_ETALASE_LIMIT))
                                countPrimaryEtalase++
                            }
                            onSuccessGetShopEtalase(shopEtalaseViewModels)
                        }
                    }
                })
    }

    fun deleteShopEtalase(etalaseId: String) {
        deleteShopEtalaseUseCase.unsubscribe()
        deleteShopEtalaseUseCase.execute(DeleteShopEtalaseUseCase.createRequestParams(etalaseId),
                object : Subscriber<String>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view?.onErrorDeleteShopEtalase(e)
                    }

                    override fun onNext(successMessage: String) {
                        view?.onSuccessDeleteShopEtalase(successMessage)
                    }
                })
    }

    override fun detachView() {
        super.detachView()
        getShopEtalaseUseCase.unsubscribe()
        deleteShopEtalaseUseCase.unsubscribe()
    }

    companion object {
        val PRIMARY_ETALASE_LIMIT = 5
    }
}
