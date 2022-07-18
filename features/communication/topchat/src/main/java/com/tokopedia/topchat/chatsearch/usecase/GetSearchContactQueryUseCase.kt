package com.tokopedia.topchat.chatsearch.usecase

import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetSearchContactQueryUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GetChatSearchResponse>,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    var isSearching: Boolean = false

    private val paramKeyword = "keyword"
    private val paramPage = "page"
    private val paramIsSeller = "isSeller"

    fun doSearch(
            onSuccess: (GetChatSearchResponse) -> Unit,
            onError: (Throwable) -> Unit,
            keyword: String,
            page: Int,
            firstResponse: GetChatSearchResponse
    ) {
        launchCatchError(dispatchers.io,
                {
                    if (firstResponse.searchResults.isNotEmpty() && page == 1) {
                        withContext(dispatchers.main) {
                            onSuccess(firstResponse)
                        }
                        return@launchCatchError
                    }
                    isSearching = true
                    val params = generateSearchParams(keyword, page)
                    val response = gqlUseCase.apply {
                        setTypeClass(GetChatSearchResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    isSearching = false
                    withContext(dispatchers.main) {
                        onSuccess(response)
                    }
                },
                {
                    isSearching = false
                    withContext(dispatchers.main) {
                        onError(it)
                    }
                }
        )
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
                  count
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