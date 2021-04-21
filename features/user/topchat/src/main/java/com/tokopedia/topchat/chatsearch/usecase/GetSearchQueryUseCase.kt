package com.tokopedia.topchat.chatsearch.usecase

import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatsearch.data.GetMultiChatSearchResponse
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchListHeaderUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class GetSearchQueryUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GetMultiChatSearchResponse>,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    var isSearching: Boolean = false
    var hasNext: Boolean = false

    private val paramKeyword = "keyword"
    private val paramPage = "page"
    private val paramIsSeller = "isSeller"

    fun doSearch(
            onSuccess: (GetMultiChatSearchResponse, SearchListHeaderUiModel?, SearchListHeaderUiModel?) -> Unit,
            onError: (Throwable) -> Unit,
            keyword: String,
            page: Int,
            isReplyOnly: Boolean = false
    ) {
        launchCatchError(dispatchers.io,
                {
                    isSearching = true
                    val params = generateSearchParams(keyword, page)
                    val response = gqlUseCase.apply {
                        setTypeClass(GetMultiChatSearchResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(if (isReplyOnly) querySearchReply else query)
                    }.executeOnBackground()
                    val contactLoadMore = createContactLoadMore(response, page)
                    val replyHeader = createReplyHeader(response, page)
                    isSearching = false
                    hasNext = response.replyHasNext
                    withContext(dispatchers.main) {
                        onSuccess(response, contactLoadMore, replyHeader)
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

    private fun createReplyHeader(response: GetMultiChatSearchResponse, page: Int): SearchListHeaderUiModel? {
        if (page != 1) return null
        val contactCount = response.replySearchResults.size
        val replyCount = if (contactCount > 5) response.replyCount else ""
        if (contactCount > 0) {
            return SearchListHeaderUiModel(SearchListHeaderUiModel.TITLE_REPLY, replyCount, true)
        }
        return null
    }

    private fun createContactLoadMore(response: GetMultiChatSearchResponse, page: Int): SearchListHeaderUiModel? {
        if (page != 1) return null
        val contactCount = response.contactSearchResults.size
        val replyCount = if (contactCount > 5) response.contactCount else ""
        val hideCta =  contactCount <= 5
        if (contactCount > 0) {
            return SearchListHeaderUiModel(SearchListHeaderUiModel.TITLE_CONTACT, replyCount, hideCta)
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
                  roomId
                  productId
                }
                hasNext
              }
              }
            }
        """.trimIndent()

    private val querySearchReply = """
            query chatSearchByReply($$paramKeyword: String, $$paramPage: Int, $$paramIsSeller: Int){
              searchByReply: chatSearch(
                keyword: $$paramKeyword, 
                page: $$paramPage, 
                isSeller: $$paramIsSeller, 
                by:"reply"
              ){
                replies{
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
                  roomId
                  productId
                }
                hasNext
              }
              }
            }
        """.trimIndent()
}