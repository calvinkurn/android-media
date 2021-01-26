package com.tokopedia.promocheckout.detail.view.presenter

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.deals.PromoCheckoutDealsRepository
import com.tokopedia.promocheckout.common.domain.mapper.DealsCheckoutMapper
import com.tokopedia.promocheckout.common.domain.model.deals.DealsErrorResponse
import com.tokopedia.promocheckout.common.domain.model.deals.DealsVerifyResponse
import com.tokopedia.promocheckout.detail.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import retrofit2.HttpException
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class PromoCheckoutDetailDealsPresenter(private val getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                        private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                        private val dealsCheckRepository: PromoCheckoutDealsRepository,
                                        private val compositeSubscription: CompositeSubscription) :
        BaseDaggerPresenter<PromoCheckoutDetailContract.View>(), PromoCheckoutDetailDealsContract.Presenter {

    override fun getDetailPromo(slug: String) {
        view?.let {
        view.showLoading()

        getDetailCouponMarketplaceUseCase.execute(getDetailCouponMarketplaceUseCase.createRequestParams(slug),
                object : Subscriber<GraphqlResponse>() {

                    override fun onError(e: Throwable) {
                        if (isViewAttached) {
                            view.hideLoading()
                            view.onErroGetDetail(e)
                        }
                    }

                    override fun onCompleted() {

                    }

                    override fun onNext(response: GraphqlResponse?) {
                        view.hideLoading()
                        val dataDetailCheckoutPromo = response?.getData<DataPromoCheckoutDetail>(DataPromoCheckoutDetail::class.java)
                        view.onSuccessGetDetailPromo(dataDetailCheckoutPromo?.promoCheckoutDetailModel
                                ?: throw RuntimeException())
                    }
                })
            }
    }

    override fun processCheckDealPromoCode(promoCode: String, flag: Boolean, requestBody: JsonObject) {
        compositeSubscription.add(
                dealsCheckRepository.postVerify(flag, requestBody)
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
                            }

                            override fun onError(e: Throwable) {
                                if (e is HttpException) {
                                    try {
                                        val body = (e as HttpException).response()?.errorBody()?.string()
                                        if (!body.isNullOrEmpty()) {
                                            val gson = Gson()
                                            val testModel = gson.fromJson(body, DealsErrorResponse::class.java)
                                            if (isViewAttached) {
                                                view.hideProgressLoading()
                                                if (testModel.data.message.isNotEmpty()) {
                                                    view.onErrorCheckPromo(MessageErrorException(testModel.data.message))
                                                } else {
                                                    view.onErrorCheckPromo(e)
                                                }
                                            }
                                        } else {
                                            view.hideProgressLoading()
                                            view.onErrorCheckPromo(e)
                                        }
                                    } catch (exception: Exception) {
                                        view.hideProgressLoading()
                                        view.onErrorCheckPromo(e)
                                    }
                                } else {
                                    view.hideProgressLoading()
                                    view.onErrorCheckPromo(e)
                                }
                            }

                        })
        )
    }

    override fun detachView() {
        getDetailCouponMarketplaceUseCase.unsubscribe()
        clearCacheAutoApplyStackUseCase.unsubscribe()
        compositeSubscription.unsubscribe()
        super.detachView()
    }
}