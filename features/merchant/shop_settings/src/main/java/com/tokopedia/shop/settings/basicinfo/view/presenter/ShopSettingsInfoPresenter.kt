package com.tokopedia.shop.settings.basicinfo.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable

import javax.inject.Inject

import rx.Subscriber
import rx.functions.Func2

class ShopSettingsInfoPresenter @Inject
constructor(private val getShopBasicDataAndStatusUseCase: GetShopBasicDataAndStatusUseCase,
            private val updateShopScheduleUseCase: UpdateShopScheduleUseCase,
            private val userSession: UserSessionInterface) : BaseDaggerPresenter<ShopSettingsInfoPresenter.View>() {

    interface View : CustomerView {
        fun onSuccessGetShopBasicData(result: Pair<ShopBasicDataModel?, ShopStatusModel?>)
        fun onErrorGetShopBasicData(throwable: Throwable)
        fun onSuccessUpdateShopSchedule(successMessage: String)
        fun onErrorUpdateShopSchedule(throwable: Throwable)
    }

    fun getShopData() {
        getShopBasicDataAndStatusUseCase.unsubscribe()
        getShopBasicDataAndStatusUseCase.execute(
            GetShopBasicDataAndStatusUseCase.createRequestParams(userSession.shopId),
            object : Subscriber<Pair<ShopBasicDataModel?, ShopStatusModel?>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view?.onErrorGetShopBasicData(e)
            }

            override fun onNext(result: Pair<ShopBasicDataModel?, ShopStatusModel?>) {
                view?.onSuccessGetShopBasicData(result)
            }
        })
    }

    fun updateShopSchedule(@ShopScheduleActionDef action: Int,
                           closeNow: Boolean,
                           closeStart: String,
                           closeEnd: String,
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
        getShopBasicDataAndStatusUseCase.unsubscribe()
        updateShopScheduleUseCase.unsubscribe()
    }
}
