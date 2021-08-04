package com.tokopedia.affiliatecommon.domain

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliatecommon.R
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.ContentSubmitInput
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author by milhamj on 10/15/18.
 */
class DeletePostUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                            private val graphqlUseCase: GraphqlUseCase)
    : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val query: String = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.mutation_af_submit_post
        )

        val variables = HashMap<String, Any>()
        variables[PARAM_INPUT] = getContentSubmitInput(requestParams)

        val graphqlRequest = GraphqlRequest(query, SubmitPostData::class.java, variables)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val data: SubmitPostData? = it.getData(SubmitPostData::class.java)
            if (data == null) {
                throw RuntimeException()
            } else if (TextUtils.isEmpty(data.feedContentSubmit.error).not()) {
                throw MessageErrorException(data.feedContentSubmit.error)
            }
            data.feedContentSubmit.success == SubmitPostData.SUCCESS
        }
    }

    private fun getContentSubmitInput(requestParams: RequestParams): ContentSubmitInput {
        return ContentSubmitInput(
                activityId = requestParams.getString(PARAM_ID, ""),
                type = requestParams.getString(PARAM_TYPE, ""),
                action = requestParams.getString(PARAM_ACTION, "")
        )
    }

    companion object {
        private const val PARAM_INPUT = "input"
        private const val PARAM_ID = "ID"
        private const val PARAM_TYPE = "type"
        private const val PARAM_ACTION = "action"
        private const val TYPE_CONTENT = "content"
        private const val ACTION_DELETE = "delete"

        fun createRequestParams(id: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_ID, id)
            requestParams.putString(PARAM_TYPE, TYPE_CONTENT)
            requestParams.putString(PARAM_ACTION, ACTION_DELETE)
            return requestParams
        }
    }
}