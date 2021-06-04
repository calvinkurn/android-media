package com.tokopedia.feedplus.view.presenter

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import javax.inject.Inject

class FeedPlusContainerViewModel @Inject constructor(baseDispatcher: CoroutineDispatcher,
                                                     private val useCase: GraphqlUseCase<FeedTabs.Response>,
                                                     private val getWhitelistUseCase: GetWhitelistUseCase)
    : BaseViewModel(baseDispatcher){

    val tabResp = MutableLiveData<Result<FeedTabs>>()
    val whitelistResp = MutableLiveData<Result<WhitelistDomain>>()

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

    fun getWhitelist(authorListEmpty: Boolean) {
        getWhitelistUseCase.clearRequest()
        getWhitelistUseCase.setCacheStrategy(authorListEmpty)
        getWhitelistUseCase.addRequest(getWhitelistUseCase.getRequest(
                GetWhitelistUseCase.createRequestParams(GetWhitelistUseCase.WHITELIST_ENTRY_POINT))
        )
        getWhitelistUseCase.execute(RequestParams.EMPTY, object: Subscriber<GraphqlResponse>() {
            override fun onNext(t: GraphqlResponse) {
                val query = t.getData<WhitelistQuery>(WhitelistQuery::class.java)
                whitelistResp.value = Success(getWhitelistDomain(query))
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                whitelistResp.value = Fail(e)
            }
        })

    }

    private fun getWhitelistDomain(query: WhitelistQuery?): WhitelistDomain {
        return if (query == null) {
            WhitelistDomain()
        } else {
            WhitelistDomain().apply {
                error = query.whitelist.error
                url = query.whitelist.url
                isWhitelist = query.whitelist.isWhitelist
                title = query.whitelist.title
                desc = query.whitelist.description
                titleIdentifier = query.whitelist.titleIdentifier
                postSuccessMessage = query.whitelist.postSuccessMessage
                image = query.whitelist.imageUrl
                authors = ArrayList(query.whitelist.authors)
            }
        }
    }

}