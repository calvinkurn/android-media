package com.tokopedia.report.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.report.data.model.SubmitReportResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

@Suppress("UNCHECKED_CAST")
class SubmitReportUseCase @Inject constructor(@Named("product_report_submit") private val gqlMutationQuery: String,
                                              private val graphqlUseCase: GraphqlUseCase): UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams?): Observable<Boolean> {
        if (requestParams == null) return Observable.error(Throwable("Invalid Params"))
        val graphqlRequest = GraphqlRequest(gqlMutationQuery, SubmitReportResponse.Data::class.java,
                mapOf("input" to requestParams.parameters), false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(null).flatMap { gqlResponse ->
            val gqlError = gqlResponse.getError(SubmitReportResponse.Data::class.java)
                    ?.filter { it.message.isNotBlank() }?.map { it.message } ?: listOf()
            if (gqlError.isNotEmpty()){
                Observable.error(MessageErrorException(gqlError.joinToString(", ")))
            } else {
                Observable.just(gqlResponse.getData<SubmitReportResponse.Data>(SubmitReportResponse.Data::class.java).response.isSuccess)
            }
        }
    }

    companion object{
        private const val PARAM_FIELDS = "additional_fields"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_CATEGORY_ID = "category_id"

        fun createRequestParam(categoryId: Int, productId: Int, fields: Map<String, Any>): RequestParams = RequestParams.create()
                .apply {
                    putInt(PARAM_PRODUCT_ID, productId)
                    putInt(PARAM_CATEGORY_ID, categoryId)
                    putObject(PARAM_FIELDS, fields)
                }
    }
}