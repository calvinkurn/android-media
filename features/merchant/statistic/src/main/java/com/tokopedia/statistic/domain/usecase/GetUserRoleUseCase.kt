package com.tokopedia.statistic.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhomecommon.domain.usecase.BaseGqlUseCase
import com.tokopedia.statistic.di.StatisticScope
import com.tokopedia.statistic.domain.model.GetUserRoleModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 30/07/20
 */

@StatisticScope
class GetUserRoleUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : BaseGqlUseCase<List<String>>() {

    override suspend fun executeOnBackground(): List<String> {
        val gqlRequest = GraphqlRequest(QUERY, GetUserRoleModel::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))
        val errors: List<GraphqlError>? = gqlResponse.getError(GetUserRoleModel::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetUserRoleModel>()
            return data.goldGetUserShopInfo?.data?.roles.orEmpty()
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val USER_ID = "userID"
        private val QUERY = """
            query UserRole(${'$'}userID: Int!) {
              GoldGetUserShopInfo(userID: ${'$'}userID) {
                Data {
                  Roles
                }
              }
            }
        """.trimIndent()

        fun createParam(userId: Int): RequestParams {
            return RequestParams.create().apply {
                putInt(USER_ID, userId)
            }
        }
    }
}