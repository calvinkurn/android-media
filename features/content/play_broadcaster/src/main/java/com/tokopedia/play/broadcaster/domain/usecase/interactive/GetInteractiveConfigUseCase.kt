package com.tokopedia.play.broadcaster.domain.usecase.interactive

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.interactive.GetInteractiveConfigResponse
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Created by mzennis on 06/07/21.
 */
class GetInteractiveConfigUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers,
) : GraphqlUseCase<GetInteractiveConfigResponse>(gqlRepository) {

    private val query = """
        query GetInteractiveGetInteractiveConfig(${"$$PARAM_SHOP_ID"}: String!) {
          playInteractiveGetInteractiveConfig(req:{shopID: ${"$$PARAM_SHOP_ID"}}){
            interactiveConfig {
              isActive
              interactiveNamingGuidelineHeader
              interactiveNamingGuidelineDetail
              interactiveTimeGuidelineHeader
              interactiveTimeGuidelineDetail
              interactiveDuration
              countdownPickerTime
            }
          }
        }
    """

    init {
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetInteractiveConfigResponse::class.java)
    }

    suspend fun execute(shopId: String): GetInteractiveConfigResponse = withContext(dispatchers.io) {
        setRequestParams(createParams(shopId))
        return@withContext executeOnBackground()
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopID"

        fun createParams(shopId: String): Map<String, Any> = mapOf(
            PARAM_SHOP_ID to shopId
        )
    }
}