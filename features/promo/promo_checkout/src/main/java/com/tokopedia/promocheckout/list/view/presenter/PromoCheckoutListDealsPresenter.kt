package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.deals.DealsCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.DealsCheckoutMapper
import com.tokopedia.promocheckout.common.domain.model.deals.DealsPromoCheckResponse
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.promocheckout.list.model.listtravelcollectivebanner.PromoChekoutDealsBannerModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListDealsPresenter.Mapper.mapToLastSeen
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.*


class PromoCheckoutListDealsPresenter(
        private val dealsCheckVoucherUseCase: DealsCheckVoucherUseCase,
        private val dealsPromoUseCase: GraphqlUseCase
) : BaseDaggerPresenter<PromoCheckoutListContract.View>(), PromoCheckoutListDealsContract.Presenter {


    override fun processCheckDealPromoCode(code: List<String>, categoryName: String, metaData: String, grandTotal: Int) {
        view?.let {
            view.showProgressLoading()
            dealsCheckVoucherUseCase.execute(dealsCheckVoucherUseCase.createRequestParams(code, categoryName, metaData, grandTotal), object : Subscriber<GraphqlResponse>(){
                override fun onNext(objects: GraphqlResponse) {
                    it.hideProgressLoading()
                    val checkDealsData = objects.getSuccessData<DealsPromoCheckResponse>()
                    if (checkDealsData.eventValidateUsePromo.data.global_success){
                        it.onSuccessCheckPromo(DealsCheckoutMapper.mapDataNew(checkDealsData.eventValidateUsePromo.data))
                    } else {
                        it.onErrorCheckPromo(com.tokopedia.network.exception.MessageErrorException(checkDealsData.eventValidateUsePromo.data.usage_details.firstOrNull()?.message?.text))
                    }
                }

                override fun onError(e: Throwable) {
                    if (isViewAttached){
                        it.hideProgressLoading()
                        it.onErrorCheckPromo(e)
                    }
                }

                override fun onCompleted() {

                }
            })
        }
    }

    override fun getListTravelCollectiveBanner(resources: Resources) {
        val variables = HashMap<String, Any>()
        val graphqlRequest = GraphqlRequest(PromoQuery.promoDealsQuery(), PromoChekoutDealsBannerModel.Response::class.java, variables, false)
        dealsPromoUseCase.clearRequest()
        dealsPromoUseCase.addRequest(graphqlRequest)
        dealsPromoUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                val promoData = objects.getData<PromoChekoutDealsBannerModel.Response>(PromoChekoutDealsBannerModel.Response::class.java)
                if (!promoData.response.banners.isNullOrEmpty()) view.changeTitle(resources.getString(R.string.promo_title_for_this_category))
                val data = mapToLastSeen(promoData.response.banners)
                view.renderListLastSeen(data, true)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.showGetListLastSeenError(e)
                }
            }

        })
    }

    object Mapper {
        fun mapToLastSeen(data: List<PromoChekoutDealsBannerModel.Banner>?): List<PromoCheckoutLastSeenModel> {
            val mapResult = mutableListOf<PromoCheckoutLastSeenModel>()
            data?.forEachIndexed { index, banner ->
                if (!banner.attribute.promoCode.isBlank() && !banner.attribute.promoCode.equals("-")) {
                    val dataMapper = PromoCheckoutLastSeenModel(
                            id = banner.id.toInt(),
                            title = banner.attribute.description,
                            promoCode = banner.attribute.promoCode,
                            subtitle = banner.product
                    )
                    mapResult.add(dataMapper)
                }
            }
            return mapResult
        }
    }

}