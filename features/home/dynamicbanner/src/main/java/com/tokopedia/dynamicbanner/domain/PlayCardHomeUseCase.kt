package com.tokopedia.dynamicbanner.domain

import com.tokopedia.dynamicbanner.QUERY_PLAY_CARD
import com.tokopedia.dynamicbanner.entity.PlayCardHome
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject
import javax.inject.Named

class PlayCardHomeUseCase @Inject constructor(
        @Named(QUERY_PLAY_CARD) val query: String,
        repository: GraphqlRepository
): GraphqlUseCase<PlayCardHome>(repository) {

    init {
        setTypeClass(PlayCardHome::class.java)
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).apply {
            setSessionIncluded(true)
        }.build())
    }

}