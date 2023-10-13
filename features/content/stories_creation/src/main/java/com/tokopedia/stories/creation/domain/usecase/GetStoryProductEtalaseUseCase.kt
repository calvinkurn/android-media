package com.tokopedia.stories.creation.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stories.creation.model.GetStoryProductEtalaseRequest
import com.tokopedia.stories.creation.model.GetStoryProductEtalaseResponse
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
class GetStoryProductEtalaseUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<GetStoryProductEtalaseRequest, GetStoryProductEtalaseResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: GetStoryProductEtalaseRequest): GetStoryProductEtalaseResponse {
        return repository.request(graphqlQuery(), params.buildRequestParam())
    }

    companion object {
        private const val PARAM_REQ = "req"

        const val QUERY = """
            query getStoryProductEtalase(
                ${"$$PARAM_REQ"}: GetStoryProductEtalaseRequest!
            ) {
                getStoryProductEtalase(
                    $PARAM_REQ: ${"$$PARAM_REQ"}
                ) {
                    products {
                        pictures {
                            urlThumbnail
                        }
                        ID
                        name
                        stock
                        price {
                            min
                            max
                            minFmt
                            maxFmt
                        }
                    }
                    pagerCursor {
                        limit
                        cursor
                        hasNext
                    }
                }
            }
        """
    }
}
