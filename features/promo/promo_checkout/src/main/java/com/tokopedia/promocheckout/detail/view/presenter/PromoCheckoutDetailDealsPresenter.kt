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
        it.showLoading()

        getDetailCouponMarketplaceUseCase.execute(getDetailCouponMarketplaceUseCase.createRequestParams(slug),
                object : Subscriber<GraphqlResponse>() {

                    override fun onError(e: Throwable) {
                        if (isViewAttached) {
                            it.hideLoading()
                            it.onErroGetDetail(e)
                        }
                    }

                    override fun onCompleted() {

                    }

                    override fun onNext(response: GraphqlResponse?) {
                        it.hideLoading()
                        val dataDetailCheckoutPromo = response?.getData<DataPromoCheckoutDetail>(DataPromoCheckoutDetail::class.java)
                        it.onSuccessGetDetailPromo(dataDetailCheckoutPromo?.promoCheckoutDetailModel
                                ?: throw RuntimeException())
                    }
                })
            }
    }

    override fun processCheckDealPromoCode(code: List<String>, categoryName: String, metaData: String, grandTotal: Int) {
        view?.let {
            it.showProgressLoading()
            dealsCheckVoucherUseCase.execute(dealsCheckVoucherUseCase.createRequestParams(code, categoryName, metaData, grandTotal), object : Subscriber<GraphqlResponse>() {
                override fun onNext(objects: GraphqlResponse) {
                    it.hideProgressLoading()
                    val checkDealsData = objects.getSuccessData<DealsPromoCheckResponse>()
                    if (checkDealsData.eventValidateUsePromo.data.global_success) {
                        it.onSuccessCheckPromo(DealsCheckoutMapper.mapDataNew(checkDealsData.eventValidateUsePromo.data))
                    } else {
                        it.onErrorCheckPromo(com.tokopedia.network.exception.MessageErrorException(checkDealsData.eventValidateUsePromo.data.usage_details.firstOrNull()?.message?.text))
                    }
                }

                override fun onError(e: Throwable) {
                    if (isViewAttached) {
                        it.hideProgressLoading()
                        it.onErrorCheckPromo(e)
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