package com.tokopedia.stories.widget.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 16/08/23
 */
@GqlQuery(GetShopStoriesStatusUseCase.QUERY_NAME, GetShopStoriesStatusUseCase.QUERY)
internal class GetShopStoriesStatusUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<GetShopStoriesStatusUseCase.Request, GetShopStoriesStatusResponse>(dispatchers.io) {

    private val query = GetShopStoriesStatusUseCaseQuery()

    override fun graphqlQuery(): String {
        return query.getQuery()
    }

    override suspend fun execute(params: Request): GetShopStoriesStatusResponse {
        return graphqlRepository.request(graphqlQuery(), params)
    }

    companion object {
        private const val request = "\$request"

        internal const val QUERY_NAME = "GetShopStoriesStatusUseCaseQuery"
        internal const val QUERY = """
            query getShopStoriesStatus(
                $request: ContentAuthorStatusesRequest!
            ) {
                contentAuthorStatuses(request: $request) {
                    authors {
                        id
                        hasStory
                        isUnseenStoryExist
                        appLink
                    }
                    meta {
                        coachmark
                    }
                }
            }
        """
    }

    data class Request private constructor(
        @SerializedName("request")
        private val data: Data,
    ) : GqlParam {

        companion object {
            fun create(
                key: StoriesEntrySource,
                authors: List<Author> = emptyList(),
            ) = Request(
                Data(
                    source = key.sourceName,
                    authors = authors,
                )
            )
        }

        data class Data(
            @SerializedName("source")
            private val source: String,

            @SerializedName("authors")
            private val authors: List<Author>,
        )

        data class Author private constructor(
            @SerializedName("id")
            private val id: String,

            @SerializedName("type")
            private val type: Int,
        ) {
            companion object {
                fun create(
                    id: String,
                    type: Type,
                ) = Author(
                    id = id,
                    type = type.value,
                )
            }

            enum class Type(val value: Int) {
                Tokopedia(1),
                Shop(2),
                User(3),
            }
        }
    }
}
