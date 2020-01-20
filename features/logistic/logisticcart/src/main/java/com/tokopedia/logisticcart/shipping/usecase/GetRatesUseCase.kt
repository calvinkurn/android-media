package com.tokopedia.logisticcart.shipping.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.GetRatesCourierRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.PromoStacking
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.RatesDetailData
import com.tokopedia.remoteconfig.GraphqlHelper
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

private typealias RatesGqlResponse = GetRatesCourierRecommendationData
private typealias RatesModel = ShippingRecommendationData

class GetRatesUseCase @Inject constructor(@ApplicationContext val context: Context, val gql: GraphqlUseCase) {

    fun execute(param: RatesParam): Observable<RatesModel> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.ratesv3)
        val gqlRequest = GraphqlRequest(query, RatesGqlResponse::class.java, param.toMap())

        gql.clearRequest()
        gql.addRequest(gqlRequest)
        return gql.getExecuteObservable(null)
                .map { graphqlResponse: GraphqlResponse ->
                    val response = graphqlResponse.getData<RatesGqlResponse>(RatesGqlResponse::class.java)
                    val model = RatesModel()
                    val details: RatesDetailData? = response.ratesData?.ratesDetailData
                    // Check has service / duration list
                    if (details?.services?.isNotEmpty() == true) {

                        // Check if has info
                        if (details.error?.errorMessage?.isNotEmpty() == true) {
                            model.errorMessage = details.error.errorMessage
                            model.errorId = details.error.errorId
                        }

                        // Setting for Promo Stacking
                        val promoStacking: PromoStacking? = details.promoStacking
                        var promoModel: LogisticPromoViewModel? = null
                        if (promoStacking != null && promoStacking.isPromo == 1) {
                            val applied = promoStacking.isApplied == 1
                            promoModel = LogisticPromoViewModel(
                                    promoStacking.getPromoCode(), promoStacking.getTitle(),
                                    promoStacking.getBenefitDesc(), promoStacking.getShipperName(),
                                    promoStacking.getServiceId(), promoStacking.getShipperId(),
                                    promoStacking.getShipperProductId(),
                                    promoStacking.getShipperDesc(),
                                    promoStacking.getShipperDisableText(),
                                    promoStacking.getPromoTncHtml(), applied,
                                    promoStacking.getImageUrl(), promoStacking.getDiscontedRate(),
                                    promoStacking.getShippingRate(),
                                    promoStacking.getBenefitAmount(), promoStacking.isDisabled(),
                                    promoStacking.isHideShipperName())
                        }
                        model.logisticPromo = promoModel

                        model.shippingDurationViewModels = details.services.map { service ->
                            val durationViewModel = ShippingDurationViewModel()
                            durationViewModel.serviceData = service
                            durationViewModel.shippingCourierViewModelList = service.products.map { product ->
                                ShippingCourierViewModel().apply {
                                    productData = product
                                    blackboxInfo = details.info.blackboxInfo.textInfo
                                    serviceData = durationViewModel.serviceData
                                    ratesId = details.ratesId
                                }
                            }
                            durationViewModel
                        }
                    }
                    model
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}