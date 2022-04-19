package com.tokopedia.topchat.chatlist.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatlist.pojo.whitelist.ChatWhitelistFeatureResponse
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetChatWhitelistFeature @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatWhitelistFeatureResponse>,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    private val paramFeature = "feature"

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun getWhiteList(
            feature: String,
            onSuccess: (ChatWhitelistFeatureResponse) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(dispatchers.io,
                {
                    val params = generateParams(feature)
                    val response = gqlUseCase.apply {
                        setTypeClass(ChatWhitelistFeatureResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    withContext(dispatchers.main) {
                        onSuccess(response)
                    }
                },
                {
                    withContext(dispatchers.main) {
                        onError(it)
                    }
                }
        )
    }

    private fun generateParams(feature: String): Map<String, Any?> {
        return mapOf(
                paramFeature to feature
        )
    }

    private val query = """
        query chatWhitelistFeature($$paramFeature: String){
          chatWhitelistFeature(feature: $$paramFeature){
            isWhitelist
          }
        }
    """.trimIndent()

    companion object {
        const val PARAM_VALUE_FEATURE_TOPBOT = "topbot"
    }

}