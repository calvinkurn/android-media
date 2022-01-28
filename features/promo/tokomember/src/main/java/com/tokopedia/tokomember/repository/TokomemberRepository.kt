package com.tokopedia.tokomember.repository

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokomember.model.MembershipRegisterResponse
import com.tokopedia.tokomember.model.ShopRegisterResponse
import com.tokopedia.tokomember.usecase.MembershipRegisterParams
import com.tokopedia.tokomember.usecase.TokomemberShopParams
import com.tokopedia.tokomember.util.MEMBERSHIP_REGISTER
import com.tokopedia.tokomember.util.TM_REGISTRATION_SHOP_DATA
import java.lang.IllegalArgumentException
import javax.inject.Inject

class TokomemberRepository @Inject constructor(private val gql: GraphqlRepository) {

    @GqlQuery("TmMembershipRegisterQuery", MEMBERSHIP_REGISTER)
    suspend fun registerMembership(cardId: String?): MembershipRegisterResponse {
        val variables = HashMap<String, Any>()
        if (!cardId.isNullOrEmpty()) {
            variables[MembershipRegisterParams.CARD_ID] = cardId.toInt()
        } else {
            throw IllegalArgumentException("Card Id must not be empty")
        }
        val request = GraphqlRequest(
            TmMembershipRegisterQuery.GQL_QUERY,
            MembershipRegisterResponse::class.java, variables
        )
        return gql.getResponse(request)
    }

    @GqlQuery("TmMembershipShopData", TM_REGISTRATION_SHOP_DATA)
    suspend fun getTokomemberData(shopId: Int , amount:Float): ShopRegisterResponse {
        val variables = HashMap<String, Any>()
            variables[TokomemberShopParams.SHOP_ID] = shopId
            variables[TokomemberShopParams.AMOUNT] = amount
        val request = GraphqlRequest(
            TmMembershipShopData.GQL_QUERY,
            ShopRegisterResponse::class.java, variables
        )
        return gql.getResponse(request)
    }
}

internal suspend inline fun <reified T> GraphqlRepository.getResponse(request: GraphqlRequest): T {
    return response(listOf(request)).getSuccessData<T>()
        ?: throw NullPointerException("Data with your type might not exist")
}