package com.tokopedia.seller.search.common.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.seller.search.common.domain.model.SellerSearchPlaceholderResponse
import javax.inject.Inject

class GetSellerSearchPlaceholderUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<SellerSearchPlaceholderResponse>(graphqlRepository) {

    companion object {
        private const val QUERY = """
            query searchPlaceholder {
                placeholder {
                    sentence
                }
            }
        """
    }

    init {
        setGraphqlQuery(QUERY)
        setTypeClass(SellerSearchPlaceholderResponse::class.java)
    }
}