package com.tokopedia.promocheckout.common.domain

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.detail.domain.DetailCouponMarkeplaceModel
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func2
import java.util.*

class GetDetailCouponMarketplaceUseCase(val resources: Resources, val getDetailUseCase: GraphqlUseCase, val checkPromoCodeUseCase: CheckPromoCodeUseCase) : UseCase<DetailCouponMarkeplaceModel>() {

    val ONE_CLICK_SHIPMENT = "oneClickShipment"
    val INPUT_CODE = "code"

    override fun createObservable(requestParams: RequestParams?): Observable<DetailCouponMarkeplaceModel> {

        val variables = HashMap<String, Any>()
        variables[INPUT_CODE] = requestParams?.getString(INPUT_CODE, "") ?: ""
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.promo_checkout_detail_marketplace), DataPromoCheckoutDetail::class.java, variables)
        getDetailUseCase.clearRequest()
        getDetailUseCase.addRequest(graphqlRequest)
        return Observable.zip(getDetailUseCase.createObservable(requestParams), checkPromoCodeUseCase.createObservable(
                checkPromoCodeUseCase.createRequestParams(requestParams?.getString(INPUT_CODE, "")
                        ?: "", true, oneClickShipment = requestParams?.getBoolean(ONE_CLICK_SHIPMENT, false) ?: false)),
                object : Func2<GraphqlResponse, DataVoucher, DetailCouponMarkeplaceModel> {
                    override fun call(t1: GraphqlResponse?, t2: DataVoucher?): DetailCouponMarkeplaceModel {
                        val dataDetailCheckoutPromo = t1?.getData<DataPromoCheckoutDetail>(DataPromoCheckoutDetail::class.java)
                        dataDetailCheckoutPromo?.promoCheckoutDetailModel?:throw RuntimeException()
                        return DetailCouponMarkeplaceModel(dataDetailCheckoutPromo, t2)
                    }

                })
    }

    fun createRequestParams(promoCode: String, skipApply: Boolean = false, suggestedPromo: Boolean = false, oneClickShipment: Boolean = false): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(INPUT_CODE, promoCode)
        requestParams.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment)
        return requestParams
    }

}