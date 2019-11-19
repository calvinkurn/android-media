package com.tokopedia.explore.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.explore.data.CoroutineThread
import com.tokopedia.explore.domain.entity.GetExploreData
import com.tokopedia.explore.view.subscriber.EmptySubscriber
import com.tokopedia.explore.view.uimodel.PostKolUiModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HashtagLandingPageViewModel
    @Inject constructor(private val coroutineThread: CoroutineThread,
                        private val graphqlUseCase: GraphqlUseCase<GetExploreData>,
                        private val trackClickAffiliateClickUseCase: TrackAffiliateClickUseCase)
    : BaseViewModel(coroutineThread.MAIN) {

    val canLoadMore: Boolean
        get() = cursor.isNotBlank()
    var hashtag: String = ""
    private var cursor = ""

    val postResponse = MutableLiveData<Result<List<PostKolUiModel>>>()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.`val`())
                .setSessionIncluded(true)
                .build())
    }

    fun getContentByHashtag(isForceRefresh: Boolean = false) {
        if (isForceRefresh) cursor = ""
        launchCatchError(block = {
            graphqlUseCase.setRequestParams(mapOf(PARAM_CATEGORY_ID to DEFAULT_CATEGORY_ID,
                    PARAM_CURSOR to cursor,
                    PARAM_SEARCH to hashtag,
                    PARAM_LIMIT to LIMIT))
            val exploreData = withContext(coroutineThread.IO){graphqlUseCase.executeOnBackground()}
            cursor = exploreData.getDiscoveryKolData.lastCursor
            postResponse.value = Success(
                    exploreData.getDiscoveryKolData.postKol
                            .map { PostKolUiModel(it) }
            )
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

    companion object{
        private const val DEFAULT_CATEGORY_ID = 0
        private const val PARAM_CATEGORY_ID = "idcategory"
        private const val PARAM_CURSOR = "cursor"
        private const val PARAM_SEARCH = "search"
        private const val PARAM_LIMIT = "limit"
        private const val LIMIT = 18
    }
}