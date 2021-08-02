package com.tokopedia.promocheckout.detail.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel
import com.tokopedia.promocheckout.util.PromoCheckoutQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.HashMap
import javax.inject.Inject

/**
 * @author: astidhiyaa on 02/08/21.
 */
class GetDetailPromoCheckoutUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository
) :
    GraphqlUseCase<DataPromoCheckoutDetail>(graphqlRepository) {

    suspend fun execute(requestParams: RequestParams?): Result<PromoCheckoutDetailModel> {
        return try{
            val variables = HashMap<String, Any>()
            variables[INPUT_CODE] = requestParams?.getString(INPUT_CODE, EMPTY_STRING) ?: EMPTY_STRING
            variables[API_VERSION] = API_VERSION_2

            this.setTypeClass(DataPromoCheckoutDetail::class.java)
            this.setGraphqlQuery(PromoCheckoutQuery.promoCheckoutDetailMarketPlace())
            this.setRequestParams(variables)

            val data = this.executeOnBackground()
            Success(data.promoCheckoutDetailModel ?: PromoCheckoutDetailModel())
        }catch (t: Throwable){
            Fail(t)
        }
    }

    fun createRequestParams(voucherCode: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(INPUT_CODE, voucherCode)
        return requestParams
    }

    companion object{
        const val ONE_CLICK_SHIPMENT = "oneClickShipment"
        const val INPUT_CODE = "code"
        const val API_VERSION = "apiVersion"
        const val API_VERSION_2 = "2.0.0"
        const val EMPTY_STRING = ""
    }
}