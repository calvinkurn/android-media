package com.tokopedia.topchat.chatsearch.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import javax.inject.Inject

class GetSearchQueryUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GetChatSearchResponse>
) {

    var isSearching: Boolean = false
    var hasNext: Boolean = false

    private val paramKeyword = "keyword"
    private val paramPage = "page"

    fun doSearch(
            onSuccess: (GetChatSearchResponse) -> Unit,
            onError: (Throwable) -> Unit,
            keyword: String,
            page: Int
    ) {
        isSearching = true
        val params = generateSearchParams(keyword, page)
        gqlUseCase.apply {
            setTypeClass(GetChatSearchResponse::class.java)
            setRequestParams(params)
            setGraphqlQuery(query)
            execute({ result ->
                onSuccess(result)
                isSearching = false
                hasNext = result.chatSearch.contact.hasNext
            }, { error ->
                onError(error)
                isSearching = false
            })
        }
    }

    private fun generateSearchParams(keyword: String, page: Int): Map<String, Any> {
        return mapOf(
                paramKeyword to keyword,
                paramPage to page
        )
    }

    fun cancelRunningSearch() {
        gqlUseCase.cancelJobs()
        isSearching = false
    }

    private val query by lazy {
        val keyword = "\$$paramKeyword"
        val page = "\$$paramPage"
        """
            query chatSearch($keyword: String, $page: Int){
              chatSearch(keyword:$keyword, page: $page, isSeller: 1, by:"name") {
                contact{
                  hasNext
                  data {
                    contact {
                      id
                      role
                      attributes {
                        domain
                        name
                        tag
                        thumbnail
                      }
                    }
                    createBy
                    createTimeStr
                    lastMessage
                    msgId
                    oppositeId
                    oppositeType
                  }
                }
              }
            }
        """.trimIndent()
    }
}