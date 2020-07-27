package com.tokopedia.editshipping.domain

import android.content.Context
import android.util.Log
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.ValidateShippingModel
import com.tokopedia.editshipping.domain.model.ValidateShippingParams
import com.tokopedia.editshipping.domain.response.ValidateShippingResponse
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

//class ValidateShippingUseCase @Inject constructor(@ApplicationContext val context: Context, val useCase: GraphqlUseCase, val mapper: ValidateShippingMapper) : UseCase<ValidateShippingModel>() {
class ValidateShippingUseCase @Inject constructor(@ApplicationContext val context: Context, val useCase: GraphqlUseCase, val mapper: ValidateShippingMapper) : UseCase<ValidateShippingModel>() {

    /*fun execute (onSuccess: (ValidateShippingResponse) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
//        graphqlUseCase.setRequestParams()
        graphqlUseCase.setTypeClass(ValidateShippingResponse::class.java)
        graphqlUseCase.execute({ response: ValidateShippingResponse ->
            if(response.response.status.equals("OK", true)) {
                onSuccess(response)
            } else {
                onError(MessageErrorException("Error"))
            }
        }, {
            throwable: Throwable -> onError(throwable)
        })
    }
    */

/*    fun execute(shopId: String, shipmentId: String): Observable<ValidateShippingResponse> {
        val request = ValidateShippingRequest(shopId = shopId.toInt(), shipmentId = shipmentId)
        val param = mapOf<String, Any>(PARAM_VALIDATE_SHIPPING to request)
        Log.d("STRING_PARAM", param.toString());
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_shippingeditor_popup)
        val graphqlUseCase = GraphqlRequest(query, ValidateShippingResponse::class.java, param)

        useCase.clearCache()
        useCase.addRequest(graphqlUseCase)
        return useCase.createObservable(null)
                .map {
                    val response = it.getData<ValidateShippingResponse>(ValidateShippingResponse::class.java)
                    if (response != null) {
                        if(response.response.status == "OK") {
                            it.getData(ValidateShippingResponse::class.java)
                        } else {
                            throw MessageErrorException(it.getError(ValidateShippingResponse::class.java)[0].message)
                        }
                    } else {
                        throw MessageErrorException(it.getError(ValidateShippingResponse::class.java)[0].message)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }*/

    private fun getParams(validateShippingParams: ValidateShippingParams): Map<String, Any> {
        return mapOf(
                PARAM to validateShippingParams
        )
    }

   override fun createObservable(requestParams: RequestParams?): Observable<ValidateShippingModel> {
       val validateShippingParams  = requestParams?.getObject(REQUEST_PARAM_VALIDATE_BO) as ValidateShippingParams

       val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_shippingeditor_popup)
       val graphqlUseCase = GraphqlRequest(query, ValidateShippingResponse::class.java, getParams(validateShippingParams))
       Log.d("PARAM", validateShippingParams.toString())
       useCase.clearCache()
       useCase.addRequest(graphqlUseCase)
       return useCase.createObservable(null)
               .map {
                   val response = it.getData<ValidateShippingResponse>(ValidateShippingResponse::class.java)
                   if (response != null) {
                       if(response.response.status == "OK") {
                           it.getData(ValidateShippingResponse::class.java)
                       } else {
                           throw MessageErrorException(it.getError(ValidateShippingResponse::class.java)[0].message)
                       }
                   } else {
                       throw MessageErrorException(it.getError(ValidateShippingResponse::class.java)[0].message)
                   }
               }
               .map(mapper)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
   }

    companion object{
        const val SHOP_ID = "shop_id"
        const val SHIPMENT_ID = "shipment_ids"
        const val REQUEST_PARAM_VALIDATE_BO = "REQUEST_PARAM_VALIDATE_BO"

        private const val PARAM = "inputShippingEditorMobilePopup"

        val QUERY = """
            query shippingEditorMobilePopup(${"$"}inputShippingEditorMobilePopup : KeroShippingEditorMobilePopupInput!) {
              kero_shipping_editor_mobile_popup(input: ${"$"}inputShippingEditorMobilePopup ) {
                status
                config
                server_process_time
                message_status
                data {
                  show_popup
                  ticker_title
                  ticker_content
                  popup_content
                }
              }
            }
        """.trimIndent()
    }

}

