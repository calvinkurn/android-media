package com.tokopedia.kol.feature.report.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.data.raw.GQL_MUTATION_SEND_REPORT
import com.tokopedia.feedcomponent.domain.model.report.entity.SendReportResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 14/11/18.
 */
class SendReportUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase) : UseCase<SendReportResponse>() {

    override fun createObservable(params: RequestParams?): Observable<SendReportResponse> {
        val query = GQL_MUTATION_SEND_REPORT
        val request = GraphqlRequest(query, SendReportResponse::class.java, params?.parameters)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            it.getData<SendReportResponse>(SendReportResponse::class.java)
        }
    }

    companion object {
        private const val PARAM_CONTENT_ID = "content"
        private const val PARAM_CONTENT_TYPE = "contentType"
        private const val PARAM_REASON_TYPE = "reasonType"
        private const val PARAM_REASON_MESSAGE = "reasonMessage"
        const val REPORT_SUCCESS = 1
        const val ERROR_REPORT_DUPLICATE = "error_duplicate_report"

        fun createRequestParams(contentId: Int, reasonType: String, reasonMessage: String,contentType: String )
                : RequestParams {
            val params = RequestParams.create()
            params.putInt(PARAM_CONTENT_ID, contentId)
            params.putString(PARAM_CONTENT_TYPE, contentType)
            params.putString(PARAM_REASON_TYPE, reasonType)
            params.putString(PARAM_REASON_MESSAGE, reasonMessage)
            return params
        }
    }
}