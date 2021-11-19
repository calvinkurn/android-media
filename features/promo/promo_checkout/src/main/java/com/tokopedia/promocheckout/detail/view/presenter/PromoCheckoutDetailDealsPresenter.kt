package com.tokopedia.promocheckout.detail.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.deals.DealsCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.DealsCheckoutMapper
import com.tokopedia.promocheckout.common.domain.model.deals.DealsPromoCheckResponse
import com.tokopedia.promocheckout.detail.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import rx.Subscriber

class PromoCheckoutDetailDealsPresenter(private val getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                        private val dealsCheckVoucherUseCase: DealsCheckVoucherUseCase) :
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

    override fun processCheckDealPromoCode(code: List<String>, categoryName: String, metaData: String, grandTotal: Int) {
        view?.let {
            view.showProgressLoading()
            dealsCheckVoucherUseCase.execute(dealsCheckVoucherUseCase.createRequestParams(code, categoryName, metaData, grandTotal), object : Subscriber<GraphqlResponse>() {
                override fun onNext(objects: GraphqlResponse) {
                    view.hideProgressLoading()
                    val checkDealsData = objects.getSuccessData<DealsPromoCheckResponse>()
                    if (checkDealsData.eventValidateUsePromo.data.global_success) {
                        view.onSuccessCheckPromo(DealsCheckoutMapper.mapDataNew(checkDealsData.eventValidateUsePromo.data))
                    } else {
                        view.onErrorCheckPromo(com.tokopedia.network.exception.MessageErrorException(checkDealsData.eventValidateUsePromo.data.usage_details.firstOrNull()?.message?.text))
                    }
                }

                override fun onError(e: Throwable) {
                    if (isViewAttached) {
                        view.hideProgressLoading()
                        view.onErrorCheckPromo(e)
                    }
                }

                override fun onCompleted() {

                }
            })
        }
    }

    override fun detachView() {
        getDetailCouponMarketplaceUseCase.unsubscribe()
        super.detachView()
    }
}