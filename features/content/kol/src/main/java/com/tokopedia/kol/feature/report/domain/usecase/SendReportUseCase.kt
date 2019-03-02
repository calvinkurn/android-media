package com.tokopedia.kol.feature.report.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.report.data.entity.SendReportResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 14/11/18.
 */
class SendReportUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase): UseCase<SendReportResponse>() {

    override fun createObservable(params: RequestParams?): Observable<SendReportResponse> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.mutation_send_report)
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

        private const val CONTENT_TYPE_CONTENT = "content"

        const val REPORT_SUCCESS = 1
        const val ERROR_REPORT_DUPLICATE = "error_duplicate_report"

        fun createRequestParams(contentId: Int, reasonType: String, reasonMessage: String)
                : RequestParams {
            val params = RequestParams.create()
            params.putInt(PARAM_CONTENT_ID, contentId)
            params.putString(PARAM_CONTENT_TYPE, CONTENT_TYPE_CONTENT)
            params.putString(PARAM_REASON_TYPE, reasonType)
            params.putString(PARAM_REASON_MESSAGE, reasonMessage)
            return params
        }
    }
}