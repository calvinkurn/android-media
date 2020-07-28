package com.tokopedia.editshipping.domain

import android.content.Context
import android.util.Log
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.ValidateShippingModel
import com.tokopedia.editshipping.domain.model.ValidateShippingParams
import com.tokopedia.editshipping.domain.response.ValidateShippingResponse
import com.tokopedia.editshipping.util.PARAM_VALIDATE_SHIPPING
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.GraphqlHelper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class ValidateShippingUseCase @Inject constructor(@ApplicationContext val context: Context, val useCase: GraphqlUseCase, val mapper: ValidateShippingMapper) : UseCase<ValidateShippingModel>() {

   override fun createObservable(requestParams: RequestParams): Observable<ValidateShippingModel> {
       val params = mapOf(PARAM_VALIDATE_SHIPPING to requestParams.parameters)

       val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_shippingeditor_popup)
       val graphqlRequest = GraphqlRequest(query, ValidateShippingResponse::class.java, params)
//       Log.d("PARAM_Final", getParams(validateShippingParams).toString())
       Log.d("PARAM_Final_2", params.toString())
       useCase.clearRequest()
       useCase.addRequest(graphqlRequest)
       return useCase.createObservable(RequestParams.EMPTY)
               .map {
                   val response = it.getData<ValidateShippingResponse>(ValidateShippingResponse::class.java)
                   Log.d("RESPONSE_DATA", response.toString())
                   val result = mapper.call(response)
                   result
               }
   }

    companion object{
        const val SHOP_ID = "shop_id"
        const val SHIPMENT_IDS = "shipment_ids"

        private const val PARAM = "inputShippingEditorMobilePopup"

    }

}

