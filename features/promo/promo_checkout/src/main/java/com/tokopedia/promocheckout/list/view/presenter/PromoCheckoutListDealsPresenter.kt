package com.tokopedia.promocheckout.list.view.presenter

import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.deals.PromoCheckoutDealsRepository
import com.tokopedia.promocheckout.common.domain.mapper.DealsCheckoutMapper
import com.tokopedia.promocheckout.common.domain.model.deals.DealsVerifyResponse
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription


class PromoCheckoutListDealsPresenter(
        private val repository: PromoCheckoutDealsRepository,
        private val compositeSubscription: CompositeSubscription
) : BaseDaggerPresenter<PromoCheckoutListContract.View>(), PromoCheckoutListDealsContract.Presenter {

    override fun processCheckDealPromoCode(promoCode: String, flag: Boolean, requestParams: JsonObject) {
        compositeSubscription.add(
                repository.postVerify(false, requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Subscriber<DealsVerifyResponse>() {
                            override fun onNext(objects: DealsVerifyResponse) {
                                view.showProgressLoading()
                                if (objects.message_error.isNotEmpty()) {
                                    view.onErrorCheckPromo(MessageErrorException(objects.data.message))
                                } else {
                                    view.onSuccessCheckPromo(DealsCheckoutMapper.mapData(objects))
                                }
                            }

                            override fun onCompleted() {
                                //
                            }

                            override fun onError(e: Throwable) {
                                if (isViewAttached) {
                                    view.hideProgressLoading()
                                    view.onErrorCheckPromo(e)
                                }
                            }

                        })
        )
    }
}