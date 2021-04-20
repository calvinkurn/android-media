package com.tokopedia.mvcwidget.usecases

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.mvcwidget.GqlUseCaseWrapper
import com.tokopedia.mvcwidget.MEMBERSHIP_REGISTER
import com.tokopedia.mvcwidget.MembershipRegisterResponse
import java.lang.IllegalArgumentException
import javax.inject.Inject

@GqlQuery("MvcMembershipRegisterQuery", MEMBERSHIP_REGISTER)
class MembershipRegisterUseCase @Inject constructor(val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): MembershipRegisterResponse? {
        return gqlWrapper.getResponse(MembershipRegisterResponse::class.java, MvcMembershipRegisterQuery.GQL_QUERY, map)
    }

    @Throws(Exception::class)
    fun getQueryParams(cardId: String?): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        if(!cardId.isNullOrEmpty()) {
            variables[MembershipRegisterParams.CARD_ID] = cardId.toInt()
        }else {
            throw IllegalArgumentException("Card Id must not be empty")
        }
        return variables
    }

}

object MembershipRegisterParams {
    const val CARD_ID = "cardID"
}