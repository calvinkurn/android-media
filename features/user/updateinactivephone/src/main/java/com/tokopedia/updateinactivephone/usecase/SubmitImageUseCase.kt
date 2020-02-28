package com.tokopedia.updateinactivephone.usecase

import android.content.Context

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.ObservableFactory
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.EMAIL
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.FILE_UPLOADED
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.PHONE
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.USER_ID
import com.tokopedia.updateinactivephone.data.model.response.GqlUpdatePhoneStatusResponse
import com.tokopedia.usecase.RequestParams

import java.util.ArrayList
import java.util.HashMap

import rx.Observable

class SubmitImageUseCase(private val context: Context) {

    private val graphqlUseCase: GraphqlUseCase?

    init {
        graphqlUseCase = GraphqlUseCase()
    }

    fun unsubscribe() {
        graphqlUseCase?.unsubscribe()
    }

    fun getObservable(requestParams: RequestParams): Observable<GraphqlResponse> {
        val variables = HashMap<String, Any>()
        variables[PHONE] = requestParams.getString(PHONE, "")
        variables[EMAIL] = requestParams.getString(EMAIL, "")
        variables[USER_ID] = requestParams.getInt(USER_ID, 0)
        variables[FILE_UPLOADED] = requestParams.getObject(PARAM_FILE_UPLOADED)

        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.query_update_phone_email),
                GqlUpdatePhoneStatusResponse::class.java,
                variables)

        val graphqlRequestList = ArrayList<GraphqlRequest>()
        graphqlRequestList.add(graphqlRequest)

        val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        return ObservableFactory.create(graphqlRequestList,
                graphqlCacheStrategy)
    }

    companion object {
        const val PARAM_FILE_UPLOADED = "file_uploaded"
    }
}
