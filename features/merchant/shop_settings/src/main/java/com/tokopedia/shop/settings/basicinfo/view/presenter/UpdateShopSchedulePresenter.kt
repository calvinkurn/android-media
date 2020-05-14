package com.tokopedia.shop.settings.basicinfo.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class UpdateShopSchedulePresenter @Inject
constructor(private val updateShopScheduleUseCase: UpdateShopScheduleUseCase,
            private val getShopBasicDataUseCase: GetShopBasicDataUseCase) : BaseDaggerPresenter<UpdateShopSchedulePresenter.View>() {

    interface View : CustomerView {
        fun onSuccessUpdateShopSchedule(successMessage: String)
        fun onErrorUpdateShopSchedule(throwable: Throwable)
        fun onSuccessGetShopBasicData(shopBasicDataModel: ShopBasicDataModel)
        fun onErrorGetShopBasicData(throwable: Throwable)
    }

    fun updateShopSchedule(@ShopScheduleActionDef action: Int,
                           closeNow: Boolean,
                           closeStart: String?,
                           closeEnd: String?,
                           closeNote: String) {
        updateShopScheduleUseCase.unsubscribe()
        updateShopScheduleUseCase.execute(UpdateShopScheduleUseCase.createRequestParams(
                action, closeNow, closeStart, closeEnd, closeNote
        ), createUpdateShopScheduleSubscriber())
    }

    fun getShopBasicData() {
        getShopBasicDataUseCase.unsubscribe()
        getShopBasicDataUseCase.execute(RequestParams.EMPTY, createGetShopBasicDataSubscriber())
    }

    private fun createUpdateShopScheduleSubscriber(): Subscriber<String> {
        return object : Subscriber<String>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view?.onErrorUpdateShopSchedule(e)
            }

            override fun onNext(successMessage: String) {
                view?.onSuccessUpdateShopSchedule(successMessage)
            }
        }
    }

    private fun createGetShopBasicDataSubscriber(): Subscriber<ShopBasicDataModel> {
        return object : Subscriber<ShopBasicDataModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view?.onErrorGetShopBasicData(e)
                }
            }

            override fun onNext(shopBasicDataModel: ShopBasicDataModel) {
                if (isViewAttached) {
                    view?.onSuccessGetShopBasicData(shopBasicDataModel)
                }
            }
        }
    }

    override fun detachView() {
        super.detachView()
        updateShopScheduleUseCase.unsubscribe()
        getShopBasicDataUseCase.unsubscribe()
    }
}
