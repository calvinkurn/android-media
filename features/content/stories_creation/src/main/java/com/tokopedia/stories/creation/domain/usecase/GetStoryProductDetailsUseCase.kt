package com.tokopedia.stories.creation.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.creation.model.GetStoryProductDetailsRequest
import com.tokopedia.stories.creation.model.GetStoryProductDetailsResponse
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 09, 2023
 */
class GetStoryProductDetailsUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<GetStoryProductDetailsRequest, GetStoryProductDetailsResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: GetStoryProductDetailsRequest): GetStoryProductDetailsResponse {
        return repository.request(graphqlQuery(), params.buildRequestParam())
    }

    companion object {
        private const val PARAM_REQ = "req"

        private const val QUERY = """
            query contentCreatorStoryGetProductDetails(
                ${"$$PARAM_REQ"}: ContentCreatorStoryGetProductDetailsRequest!
            ) {
                contentCreatorStoryGetProductDetails(
                    $PARAM_REQ: ${"$$PARAM_REQ"}
                ) {
                    products {
                        productID
                        productName
                        originalPriceFmt
                        originalPrice
                        priceFmt
                        price
                        discount
                        quantity
                        imageURL
                    }
                }
            }
        """
    }
}
