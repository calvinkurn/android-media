package com.tokopedia.feedplus.view.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.createpost.common.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.createpost.common.data.pojo.getcontentform.FeedContentResponse
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedplus.domain.usecase.GetContentFormForFeedUseCase
import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class FeedPlusContainerViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val repo: FeedPlusRepository,
    private val userSession: UserSessionInterface,
) : BaseViewModel(dispatchers.main){

    val tabResp = MutableLiveData<Result<FeedTabs>>()
    val whitelistResp = MutableLiveData<Result<WhitelistDomain>>()

    val isShowPostButton: Boolean
        get() = when(val whitelist = whitelistResp.value) {
            is Success -> whitelist.data.isShopAccountExists || whitelist.data.isUserAccountPostEligible
            else -> false
        }

    fun getDynamicTabs() {
        launchCatchError(block = {
            val feedTabs: FeedTabs = repo.getDynamicTabs()
            if (feedTabs.feedData.isNotEmpty()) {
                tabResp.value = Success(feedTabs)
            } else {
                tabResp.value = Fail(RuntimeException())
                repo.clearDynamicTabCache()
            }
        }) {
            tabResp.value = Fail(it)
            repo.clearDynamicTabCache()
        }
    }

    fun getWhitelist() {
        viewModelScope.launchCatchError(block = {
            if(!userSession.isLoggedIn) return@launchCatchError

            val response = repo.getWhitelist()
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
