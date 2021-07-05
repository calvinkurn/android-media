package com.tokopedia.shop.settings.basicinfo.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChanges
import javax.inject.Inject

class GetAllowShopNameDomainChanges @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<AllowShopNameDomainChanges>(graphqlRepository) {

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(QUERY)
        setTypeClass(AllowShopNameDomainChanges::class.java)
    }

    companion object {
        private const val QUERY = """
            query allowShopNameDomainChanges() {
                allowShopNameDomainChanges {
                    isDomainAllowed
                    reasonDomainNotAllowed
                    isNameAllowed
                    reasonNameNotAllowed
                    error {
                        message
                    }
                }
            }
        """
    }
}