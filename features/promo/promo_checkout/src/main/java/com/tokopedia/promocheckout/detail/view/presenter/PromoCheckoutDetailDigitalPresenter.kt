package com.tokopedia.promocheckout.detail.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.CheckVoucherDigitalUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckVoucherDigitalMapper
import com.tokopedia.promocheckout.common.domain.model.CheckVoucherDigital
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class PromoCheckoutDetailDigitalPresenter(private val getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                          private val checkVoucherDigitalUseCase: CheckVoucherDigitalUseCase,
                                          val checkVoucherDigitalMapper: CheckVoucherDigitalMapper,
                                          private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase) :
        BaseDaggerPresenter<PromoCheckoutDetailContract.View>(), PromoCheckoutDetailDigitalContract.Presenter {

    override fun cancelPromo(codeCoupon: String) {
        view.showProgressLoading()
        val promoCodes = arrayListOf(codeCoupon)
        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, promoCodes)
        clearCacheAutoApplyStackUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorCancelPromo(e)
                }
            }

            override fun onNext(response: GraphqlResponse) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    val responseData = response.getData<ClearCacheAutoApplyStackResponse>(ClearCacheAutoApplyStackResponse::class.java)
                    if (responseData.successData.success) {
                        view.onSuccessCancelPromoStacking()
                    } else {
                        view.onErrorCancelPromo(RuntimeException())
                    }
                }
            }

        })

    }

    override fun checkVoucher(promoCode: String, promoDigitalModel: PromoDigitalModel) {
        view.showProgressLoading()

        checkVoucherDigitalUseCase.execute(checkVoucherDigitalUseCase.createRequestParams(promoCode, promoDigitalModel), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                val checkVoucherData = objects.getData<CheckVoucherDigital.Response>(CheckVoucherDigital.Response::class.java).response
                if (checkVoucherData.voucherData.success) {
                    view.onSuccessValidatePromoStacking(checkVoucherDigitalMapper.mapData(checkVoucherData.voucherData))
                } else {
                    view.onErrorValidatePromoStacking(com.tokopedia.network.exception.MessageErrorException(checkVoucherData.errors.getOrNull(0)?.status))
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorValidatePromoStacking(e)
                }
            }

        })

    }

    override fun getDetailPromo(codeCoupon: String) {
        view.showLoading()
        getDetailCouponMarketplaceUseCase.execute(getDetailCouponMarketplaceUseCase.createRequestParams(codeCoupon),
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

    override fun detachView() {
        getDetailCouponMarketplaceUseCase.unsubscribe()
        checkVoucherDigitalUseCase.unsubscribe()
        clearCacheAutoApplyStackUseCase.unsubscribe()
        super.detachView()
    }
}