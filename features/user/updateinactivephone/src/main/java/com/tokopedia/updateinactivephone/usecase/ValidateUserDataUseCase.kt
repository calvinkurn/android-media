package com.tokopedia.updateinactivephone.usecase

import android.content.Context

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.OPERATION_NAME.Companion.VALIDATE_USER_DATA
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.EMAIL
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.PHONE
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.USER_ID
import com.tokopedia.updateinactivephone.data.model.response.GqlValidateUserDataResponse
import com.tokopedia.updateinactivephone.viewmodel.subscriber.ValidateUserDataSubscriber

import java.util.HashMap


class ValidateUserDataUseCase(private val context: Context) {

    private val graphqlUseCase: GraphqlUseCase?

    init {
        graphqlUseCase = GraphqlUseCase()
    }

    fun unsubscribe() {
        graphqlUseCase?.unsubscribe()
    }

    fun execute(phone: String, email: String, userId: String, validateUserDataSubscriber: ValidateUserDataSubscriber) {
        graphqlUseCase!!.clearRequest()
        val variables = HashMap<String, Any>()

        variables[PHONE] = phone
        variables[EMAIL] = email
        variables[USER_ID] = Integer.parseInt(if (userId != "") userId else "0")

        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.query_validate_user_data),
                GqlValidateUserDataResponse::class.java,
                variables, VALIDATE_USER_DATA, false)

        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(validateUserDataSubscriber)
    }
}
