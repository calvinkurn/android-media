package com.tokopedia.shop.settings.basicinfo.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.usecase.RequestParams

import javax.inject.Inject

import rx.Subscriber

class UpdateShopShedulePresenter @Inject
constructor(private val updateShopScheduleUseCase: UpdateShopScheduleUseCase) : BaseDaggerPresenter<UpdateShopShedulePresenter.View>() {

    interface View : CustomerView {
        fun onSuccessUpdateShopSchedule(successMessage: String)
        fun onErrorUpdateShopSchedule(throwable: Throwable)
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

    override fun detachView() {
        super.detachView()
        updateShopScheduleUseCase.unsubscribe()
    }


}
