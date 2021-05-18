package com.tokopedia.editshipping.domain

import android.content.Context
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.ValidateShippingModel
import com.tokopedia.editshipping.domain.response.ValidateShippingResponse
import com.tokopedia.editshipping.util.EditShippingConstant.PARAM_VALIDATE_SHIPPING
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.GraphqlHelper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ValidateShippingUseCase (val context: Context, val useCase: GraphqlUseCase, val mapper: ValidateShippingMapper) : UseCase<ValidateShippingModel>() {

   override fun createObservable(requestParams: RequestParams): Observable<ValidateShippingModel> {
       val params = mapOf(PARAM_VALIDATE_SHIPPING to requestParams.parameters)

       val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_shippingeditor_popup)
       val graphqlRequest = GraphqlRequest(query, ValidateShippingResponse::class.java, params)
       useCase.clearRequest()
       useCase.addRequest(graphqlRequest)
       return useCase.createObservable(RequestParams.EMPTY)
               .map {
                   val response = it.getData<ValidateShippingResponse>(ValidateShippingResponse::class.java)
                   if (response != null) {
                       if (response.response.status == "OK") {
                           mapper.call(response)
                       } else {
                           throw MessageErrorException(it.getError(ValidateShippingResponse::class.java)[0].message)
                       }
                   } else {
                       throw MessageErrorException(it.getError(ValidateShippingResponse::class.java)[0].message)
                   }
               }
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
   }

    companion object{
        const val SHOP_ID = "shop_id"
        const val SHIPMENT_IDS = "shipment_ids"

    }

}

