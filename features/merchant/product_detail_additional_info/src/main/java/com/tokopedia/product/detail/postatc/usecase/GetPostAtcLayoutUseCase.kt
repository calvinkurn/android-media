package com.tokopedia.product.detail.postatc.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.detail.postatc.model.PostAtcLayout
import com.tokopedia.product.detail.postatc.query.GetPostAtcLayoutQuery
import javax.inject.Inject

class GetPostAtcLayoutUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<PostAtcLayout>(graphqlRepository) {

    init {
        setGraphqlQuery(GetPostAtcLayoutQuery)
        setTypeClass(PostAtcLayout::class.java)
    }

    suspend fun execute(
        productId: String,
        cartId: String,
        layoutId: String
    ): PostAtcLayout {
        setRequestParams(
            GetPostAtcLayoutQuery.createParams(
                productId = productId,
                cartId = cartId,
                layoutId = layoutId
            )
        )
        return executeOnBackground()
    }
}
