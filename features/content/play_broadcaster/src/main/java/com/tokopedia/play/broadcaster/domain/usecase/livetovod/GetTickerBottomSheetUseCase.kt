package com.tokopedia.play.broadcaster.domain.usecase.livetovod

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

@GqlQuery(GetTickerBottomSheetUseCase.QUERY_NAME, GetTickerBottomSheetUseCase.QUERY)
class GetTickerBottomSheetUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<GetTickerBottomSheetRequest, GetTickerBottomSheetResponse>(dispatchers.io) {

    override suspend fun execute(params: GetTickerBottomSheetRequest): GetTickerBottomSheetResponse {
        val request = mapOf(PARAMS_PAGE to params.page.value)
        return repository.request(graphqlQuery(), request)
    }

    override fun graphqlQuery(): String = BroadcasterGetTickerBottomSheetConfigQuery().getQuery()

    companion object {
        private const val PARAMS_PAGE = "page"
        const val QUERY_NAME = "BroadcasterGetTickerBottomSheetConfigQuery"
        const val QUERY = """
            query BroadcasterGetTickerBottomsheetConfig(
                ${"$${PARAMS_PAGE}"}: String!
            ) {
              broadcasterGetTickerBottomsheetConfig(
                $PARAMS_PAGE: ${"$$PARAMS_PAGE"}
              ) {
                page
                type
                imageURL
                mainText {
                  title
                  description
                  action {
                    index
                    text
                    link
                  }
                }
                bottomText {
                  description
                  action {
                    index
                    text
                    link
                  }
                }
              }
            }
        """
    }
}
