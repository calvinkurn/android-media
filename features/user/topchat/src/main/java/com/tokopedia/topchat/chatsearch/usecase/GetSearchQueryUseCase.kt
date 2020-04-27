package com.tokopedia.topchat.chatsearch.usecase

import com.tokopedia.config.GlobalConfig
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
    private val paramIsSeller = "isSeller"

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
                paramPage to page,
                paramIsSeller to isSellerOnly()
        )
    }

    private fun isSellerOnly(): Int {
        return if (GlobalConfig.isSellerApp()) 1 else 0
    }

    fun cancelRunningSearch() {
        gqlUseCase.cancelJobs()
        isSearching = false
    }

    private val query = """
            query chatSearch($$paramKeyword: String, $$paramPage: Int, $$paramIsSeller: Int){
              chatSearch(
                keyword:$$paramKeyword, 
                page: $$paramPage, 
                isSeller: $$paramIsSeller, 
                by:"name"
              ) {
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