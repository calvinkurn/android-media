package com.tokopedia.shop.settings.etalase.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.ReorderShopEtalaseUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.ReorderShopNoteUseCase

import java.util.ArrayList

import javax.inject.Inject

import rx.Subscriber

/**
 * Created by hendry on 16/08/18.
 */
class ShopSettingEtalaseListReorderPresenter @Inject
constructor(private val reorderShopEtalaseUseCase: ReorderShopEtalaseUseCase) : BaseDaggerPresenter<ShopSettingEtalaseListReorderPresenter.View>() {

    interface View : CustomerView {
        fun onSuccessReorderShopEtalase(successMessage: String)
        fun onErrorReorderShopEtalase(throwable: Throwable)
    }

    fun reorderShopNotes(etalaseIdList: ArrayList<String>) {
        reorderShopEtalaseUseCase.unsubscribe()
        reorderShopEtalaseUseCase.execute(ReorderShopEtalaseUseCase.createRequestParams(etalaseIdList),
                object : Subscriber<String>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view?.onErrorReorderShopEtalase(e)
                    }

                    override fun onNext(successMessage: String) {
                        view?.onSuccessReorderShopEtalase(successMessage)
                    }
                })
    }

    override fun detachView() {
        super.detachView()
        reorderShopEtalaseUseCase.unsubscribe()
    }
}
