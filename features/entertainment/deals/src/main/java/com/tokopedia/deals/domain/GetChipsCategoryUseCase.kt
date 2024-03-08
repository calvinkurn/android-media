package com.tokopedia.deals.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.data.entity.CuratedData
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class GetChipsCategoryUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, CuratedData>(dispatcher.io) {

    private fun cacheStrategy() = GraphqlCacheStrategy
        .Builder(CacheType.CACHE_FIRST)
        .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`()).build()

    override suspend fun execute(params: Unit): CuratedData {
        return repository.request(graphqlQuery(), params, cacheStrategy())
    }

    override fun graphqlQuery(): String = """
     {
        event_child_category(categoryName: "deal", categoryID: 15, verticalID: 2)
          {
            categories{
                id,
                parent_id,
                name,
                title,
                description,
                meta_title,
                meta_description,
                url,
                media_url,
                seo_url,
                priority,
                status,
                message,
                code,
                message_error,
                web_url,
                app_url,
                is_card,
                is_hidden,
                inactiveMediaUrl,
            }
          }
     }
    """.trimIndent()
}
