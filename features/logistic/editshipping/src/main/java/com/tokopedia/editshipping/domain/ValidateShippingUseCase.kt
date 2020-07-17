package com.tokopedia.editshipping.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.ValidateShippingModel
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

    override fun createObservable(p0: RequestParams?): Observable<ValidateShippingModel> {
        val param = mapOf<String, Any>()
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_shippingEditorPopup)
        val graphqlUseCase = GraphqlRequest(query, ValidateShippingResponse::class.java, param)

        useCase.clearCache()
        useCase.addRequest(graphqlUseCase)
        return useCase.createObservable(null)
                .map {
                    graphqlResponse ->
                    val response: ValidateShippingResponse? =
                            graphqlResponse.getData(ValidateShippingResponse::class.java)
                    response ?: throw MessageErrorException(
                            graphqlResponse.getError(ValidateShippingResponse::class.java)[0].message)
                }
                .map(mapper)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    companion object{
        val QUERY = """
            query shippingEditorMobilePopup(${"$"}inputShippingEditorMobilrPopup : KeroShippingEditorMobilePopupInput!) {
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
