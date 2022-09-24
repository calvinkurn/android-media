package com.tokopedia.feedplus.view.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.createpost.common.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.createpost.common.data.pojo.getcontentform.FeedContentResponse
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase.Companion.WHITELIST_ENTRY_POINT
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain
import com.tokopedia.feedplus.domain.usecase.GetContentFormForFeedUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import rx.Subscriber
import javax.inject.Inject

class FeedPlusContainerViewModel @Inject constructor(
    private val baseDispatcher: CoroutineDispatchers,
    private val useCase: GraphqlUseCase<FeedTabs.Response>,
    private val getWhitelistUseCase: GetWhitelistNewUseCase,
    private val getContentFormForFeedUseCase: GetContentFormForFeedUseCase
) : BaseViewModel(baseDispatcher.main) {

    val tabResp = MutableLiveData<Result<FeedTabs>>()
    val whitelistResp = MutableLiveData<Result<WhitelistDomain>>()
    var feedContentForm = FeedContentForm()

    val isShowPostButton: Boolean
        get() = when (val whitelist = whitelistResp.value) {
            is Success -> whitelist.data.isShopAccountExists || whitelist.data.isUserAccountPostEligible
            else -> false
        }

    init {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
    }

    fun getDynamicTabs() {
        launchCatchError(block = {
            val feedTabs: FeedTabs = useCase.executeOnBackground().feedTabs
            if (feedTabs.feedData.isNotEmpty()) {
                tabResp.value = Success(feedTabs)
            } else {
                tabResp.value = Fail(RuntimeException())
                useCase.clearCache()
            }
        }) {
            tabResp.value = Fail(it)
            useCase.clearCache()
        }
    }

    fun getContentForm() {
        getContentFormForFeedUseCase.clearRequest()
        getContentFormForFeedUseCase.execute(GetContentFormForFeedUseCase.createRequestParams(
            mutableListOf(), "", ""
        ),
            object : Subscriber<GraphqlResponse>() {
                override fun onNext(t: GraphqlResponse) {
                    val query = t.getData<FeedContentResponse>(FeedContentResponse::class.java)
                    feedContentForm = query.feedContentForm
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                }
            })

    }

    fun getWhitelist(authorListEmpty: Boolean) {
        viewModelScope.launchCatchError(context = baseDispatcher.io, block = {
            getWhitelistUseCase.createRequestParams(WHITELIST_ENTRY_POINT)
            val response = getWhitelistUseCase.executeOnBackground()
            whitelistResp.value = Success(getWhitelistDomain(response))
        }) {
            whitelistResp.value = Fail(it)
        }
    }

    private fun getWhitelistDomain(query: WhitelistQuery?): WhitelistDomain {
        return if (query == null) {
            WhitelistDomain.Empty
        } else {
            WhitelistDomain(
                error = query.whitelist.error,
                url = query.whitelist.url,
                isWhitelist = query.whitelist.isWhitelist,
                title = query.whitelist.title,
                desc = query.whitelist.description,
                titleIdentifier = query.whitelist.titleIdentifier,
                postSuccessMessage = query.whitelist.postSuccessMessage,
                image = query.whitelist.imageUrl,
                authors = query.whitelist.authors
            )
        }
    }
}
