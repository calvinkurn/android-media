package com.tokopedia.explore.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.explore.domain.interactor.ExploreDataUseCase
import com.tokopedia.explore.view.subscriber.EmptySubscriber
import com.tokopedia.explore.view.uimodel.PostKolUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class HashtagLandingPageViewModel @Inject constructor(coroutineThread: CoroutineDispatchers,
                                                      private val exploreDataUseCase: ExploreDataUseCase,
                                                      private val trackClickAffiliateClickUseCase: TrackAffiliateClickUseCase)
    : BaseViewModel(coroutineThread.main) {

    val canLoadMore: Boolean
        get() = cursor.isNotBlank()
    var hashtag: String = ""
    private var cursor = ""

    private val postResponse = MutableLiveData<Result<List<PostKolUiModel>>>()

    fun getPostResponse(): LiveData<Result<List<PostKolUiModel>>> = postResponse

    fun getContentByHashtag(isForceRefresh: Boolean = false) {
        if (isForceRefresh) cursor = ""
        launchCatchError(block = {
            exploreDataUseCase.setParams(cursor = cursor, search = hashtag)
            val exploreData = exploreDataUseCase.executeOnBackground()
            exploreData.getDiscoveryKolData?.let { discoveryKolData ->
                cursor = discoveryKolData.lastCursor
                postResponse.value = Success(
                        discoveryKolData.postKol
                                .map { PostKolUiModel(it) }
                )
            }
        }) {
            postResponse.value = Fail(it)
        }
    }

    fun trackAffiliate(url: String) {
        trackClickAffiliateClickUseCase.execute(
                TrackAffiliateClickUseCase.createRequestParams(url),
                EmptySubscriber()
        )
    }
}