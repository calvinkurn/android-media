package com.tokopedia.mvcwidget.usecases

import com.google.gson.Gson
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.mvcwidget.FakeResponse
import com.tokopedia.mvcwidget.GqlUseCaseWrapper
import com.tokopedia.mvcwidget.MEMBERSHIP_REGISTER
import com.tokopedia.mvcwidget.MembershipRegisterResponse
import kotlinx.coroutines.delay
import java.lang.IllegalArgumentException
import javax.inject.Inject
import kotlin.Throws

@GqlQuery("MvcMembershipRegisterQuery", MEMBERSHIP_REGISTER)
class MembershipRegisterUseCase @Inject constructor(val gqlWrapper: GqlUseCaseWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): MembershipRegisterResponse? {
        return getFakeResponseSuccess()
//        return gqlWrapper.getResponse(MembershipRegisterResponse::class.java, MvcMembershipRegisterQuery.GQL_QUERY, map)
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

    suspend fun getFakeResponseSuccess(): MembershipRegisterResponse {
        delay(1000)
        return Gson().fromJson(FakeResponse.FakeMemberShipRegisterSuccess, MembershipRegisterResponse::class.java)
    }

    suspend fun getFakeResponseFail(): MembershipRegisterResponse {
        delay(1000)
        return Gson().fromJson(FakeResponse.FakeMemberShipRegisterFail, MembershipRegisterResponse::class.java)
    }
}

object MembershipRegisterParams {
    const val CARD_ID = "cardId"
//    const val REFERENCE_ID = "referenceID"
//    const val SOURCE = "source"
//    const val NAME = "name"
}