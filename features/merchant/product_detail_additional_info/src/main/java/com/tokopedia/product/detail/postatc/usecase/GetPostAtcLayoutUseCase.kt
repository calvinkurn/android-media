package com.tokopedia.product.detail.postatc.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.postatc.data.model.PostAtcLayout
import com.tokopedia.product.detail.postatc.data.model.PostAtcLayoutResponse
import com.tokopedia.product.detail.postatc.data.query.GetPostAtcLayoutQuery
import javax.inject.Inject

class GetPostAtcLayoutUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<PostAtcLayoutResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetPostAtcLayoutQuery)
        setTypeClass(PostAtcLayoutResponse::class.java)
    }

    suspend fun execute(
        productId: String,
        cartId: String,
        layoutId: String,
        pageSource: String,
        postAtcSession: String,
        userLocationRequest: UserLocationRequest
    ): PostAtcLayout {
        setRequestParams(
            GetPostAtcLayoutQuery.createParams(
                productId = productId,
                cartId = cartId,
                layoutId = layoutId,
                pageSource = pageSource,
                postAtcSession = postAtcSession,
                userLocationRequest = userLocationRequest
            )
        )
        return executeOnBackground().postAtcLayout
    }
}
