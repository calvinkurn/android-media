package com.tokopedia.feedplus.view.presenter

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FeedPlusContainerViewModel @Inject constructor(baseDispatcher: CoroutineDispatcher,
                                                    private val useCase: GraphqlUseCase<FeedTabs.Response>)
    : BaseViewModel(baseDispatcher){

    val tabResp = MutableLiveData<Result<FeedTabs>>()

    init {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
    }

    fun getDynamicTabs(){
        launchCatchError(block = {
            tabResp.value = Success(useCase.executeOnBackground().feedTabs)
        }){
            tabResp.value = Fail(it)
        }
    }
}