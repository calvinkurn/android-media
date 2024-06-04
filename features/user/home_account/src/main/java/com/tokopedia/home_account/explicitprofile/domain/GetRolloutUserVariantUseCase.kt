package com.tokopedia.home_account.explicitprofile.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.explicitprofile.data.RolloutUserVariantResponse
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetRolloutUserVariantUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val userSessionInterface: UserSessionInterface,
    private val irisSession: IrisSession,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<List<String>, RolloutUserVariantResponse>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query RolloutUserVariant(${'$'}input: GetUserVariant!) {
              RolloutUserVariant(input: ${'$'}input) {
                featureVariants {
                  feature
                  variant
                }
                message
              }
            }
        """.trimIndent()

    override suspend fun execute(params: List<String>): RolloutUserVariantResponse {
        val id = if (userSessionInterface.isLoggedIn) {
            userSessionInterface.userId
        } else {
            userSessionInterface.deviceId
        }
        val parameter = RolloutUserVariantParams(
            listExpName = params,
            id = id,
            clientType = CLIENT_TYPE_ANDROID,
            irisSessionID = irisSession.getSessionId()
        )
        return repository.request(graphqlQuery(), GetUserVariantParams(input = parameter))
    }

    companion object {
        private const val CLIENT_TYPE_ANDROID = "1"
    }
}

data class RolloutUserVariantParams(
    @SerializedName("listExpName")
    var listExpName: List<String>,
    @SerializedName("ID")
    var id: String,
    @SerializedName("clientType")
    var clientType: String,
    @SerializedName("irisSessionID")
    var irisSessionID: String,
) : GqlParam

data class GetUserVariantParams(
    @SerializedName("input")
    val input: RolloutUserVariantParams
) : GqlParam
