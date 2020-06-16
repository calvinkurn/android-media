package com.tokopedia.topchat.chatsearch.usecase

import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.data.GetMultiChatSearchResponse
import com.tokopedia.topchat.chatsearch.view.uimodel.ContactLoadMoreUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetSearchQueryUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GetMultiChatSearchResponse>,
        private var dispatchers: TopchatCoroutineContextProvider
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    var isSearching: Boolean = false
    var hasNext: Boolean = false

    private val paramKeyword = "keyword"
    private val paramPage = "page"
    private val paramIsSeller = "isSeller"

    fun doSearch(
            onSuccess: (GetChatSearchResponse, ContactLoadMoreUiModel?) -> Unit,
            onError: (Throwable) -> Unit,
            keyword: String,
            page: Int
    ) {
        launchCatchError(dispatchers.IO,
                {
                    isSearching = true
                    val params = generateSearchParams(keyword, page)
                    val response = gqlUseCase.apply {
                        setTypeClass(GetMultiChatSearchResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val contactLoadMore = createContactLoadMore(response)
                    val searchContactResponse = GetChatSearchResponse(response.searchByName)
                    isSearching = false
                    hasNext = response.contactHasNext
                    withContext(dispatchers.Main) {
                        onSuccess(searchContactResponse, contactLoadMore)
                    }
                },
                {
                    isSearching = false
                    withContext(dispatchers.Main) {
                        onError(it)
                    }
                }
        )
    }

    private fun createContactLoadMore(response: GetMultiChatSearchResponse): ContactLoadMoreUiModel? {
        val contactCount = response.contactSearchResults.size
        if (contactCount > 5) {
            return ContactLoadMoreUiModel(response.contactCount)
        }
        return null
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
            query chatSearchByContactAndReply($$paramKeyword: String, $$paramPage: Int, $$paramIsSeller: Int){
              searchByName: chatSearch(
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
              searchByReply: chatSearch(
                keyword: $$paramKeyword, 
                page: $$paramPage, 
                isSeller: $$paramIsSeller, 
                by:"reply"
              ){
                replies{
                data {
                  createBy
                  createTimeStr
                  lastMessage
                  msgId
                  oppositeId
                  oppositeType
                  replyId
                  roomId
                }
                hasNext
              }
              }
            }
        """.trimIndent()
}