package com.tokopedia.mvcwidget.usecases

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.mvcwidget.GqlUseCaseWrapper
import com.tokopedia.mvcwidget.MEMBERSHIP_REGISTER
import com.tokopedia.mvcwidget.MembershipRegisterResponse
import javax.inject.Inject

@GqlQuery("MvcMembershipRegisterQuery", MEMBERSHIP_REGISTER)
class MembershipRegisterUseCase @Inject constructor(val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): MembershipRegisterResponse {
        return gqlWrapper.getResponse(MembershipRegisterResponse::class.java, MvcMembershipRegisterQuery.GQL_QUERY, map)
    }

    fun getQueryParams(cardId: Int, referenceId: String, name: String, source: Int): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[MembershipRegisterParams.CARD_ID] = cardId
        variables[MembershipRegisterParams.REFERENCE_ID] = referenceId
        variables[MembershipRegisterParams.SOURCE] = source
        variables[MembershipRegisterParams.NAME] = name
        return variables
    }
}

object MembershipRegisterParams {
    const val CARD_ID = "cardId"
    const val REFERENCE_ID = "referenceID"
    const val SOURCE = "source"
    const val NAME = "name"
}