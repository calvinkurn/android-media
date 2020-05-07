package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.event.repository.EventCheckRepository
import com.tokopedia.promocheckout.common.domain.mapper.EventCheckVoucherMapper.mapDataEvent
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class PromoCheckoutListEventPresenter(private val eventCheckRepository: EventCheckRepository,
                                      private val compositeSubscription: CompositeSubscription) :
        BaseDaggerPresenter<PromoCheckoutListContract.View>(),
        PromoCheckoutListEventContract.Presenter {

    override fun checkPromoCode(promoCode: String, book: Boolean, eventVerifyBody: EventVerifyBody) {
        view.showProgressLoading()
        compositeSubscription.add(
        eventCheckRepository.postVerify(book, eventVerifyBody).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        object : Subscriber<EventVerifyResponse>() {
                            override fun onNext(objects: EventVerifyResponse) {
                                view.hideProgressLoading()
                                if (objects.message_error.isNotEmpty()) {
                                    view.onErrorCheckPromo(MessageErrorException(objects.data.message))
                                } else {
                                    view.onSuccessCheckPromo(mapDataEvent(objects))
                                }
                            }

                            override fun onCompleted() {

                            }

                            override fun onError(e: Throwable) {
                                if (isViewAttached) {
                                    view.hideProgressLoading()
                                    view.onErrorCheckPromo(e)
                                }
                            }

                        }))
    }

    override fun detachView() {
        compositeSubscription.unsubscribe()
        super.detachView()
    }
}