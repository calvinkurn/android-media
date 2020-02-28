package com.tokopedia.updateinactivephone.usecase

import android.content.Context

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.data.model.response.GqlCheckPhoneStatusResponse
import com.tokopedia.updateinactivephone.viewmodel.subscriber.CheckPhoneNumberStatusSubscriber

import java.util.HashMap

import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.OPERATION_NAME.Companion.CHECK_USER_STATUS
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.PHONE

class CheckPhoneNumberStatusUsecase(private val context: Context) {

    private val graphqlUseCase: GraphqlUseCase?

    init {
        graphqlUseCase = GraphqlUseCase()
    }

    fun unsubscribe() {
        graphqlUseCase?.unsubscribe()
    }

    fun execute(phone: String, checkPhoneNumberStatusSubscriber: CheckPhoneNumberStatusSubscriber) {
        graphqlUseCase?.clearRequest()
        val variables = HashMap<String, Any>()

        variables[PHONE] = phone
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.query_check_phone_number_status),
                GqlCheckPhoneStatusResponse::class.java,
                variables, CHECK_USER_STATUS)

        graphqlUseCase?.addRequest(graphqlRequest)
        graphqlUseCase?.execute(checkPhoneNumberStatusSubscriber)
    }
}
