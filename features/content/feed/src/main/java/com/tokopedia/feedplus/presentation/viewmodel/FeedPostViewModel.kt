package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateAtcSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.content.common.comment.usecase.GetCountCommentsUseCase
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.report_content.model.UserReportOptions
import com.tokopedia.content.common.usecase.BroadcasterReportTrackViewerUseCase
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.content.common.usecase.GetUserReportListUseCase
import com.tokopedia.content.common.usecase.PostUserReportUseCase
import com.tokopedia.content.common.usecase.TrackVisitChannelBroadcasterUseCase
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.createpost.common.domain.entity.SubmitPostData
import com.tokopedia.feed.component.product.FeedTaggedProductUiModel
import com.tokopedia.feedcomponent.domain.mapper.ProductMapper
import com.tokopedia.feedcomponent.domain.usecase.FeedXGetActivityProductsUseCase
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.people.usecase.ProfileUnfollowedUseCase
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TOPADS_HEADLINE_VALUE_SRC
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.domain.FeedRepository
import com.tokopedia.feedplus.domain.mapper.MapperTopAdsXFeed.transformCpmToFeedTopAds
import com.tokopedia.feedplus.domain.usecase.FeedCampaignCheckReminderUseCase
import com.tokopedia.feedplus.domain.usecase.FeedCampaignReminderUseCase
import com.tokopedia.feedplus.domain.usecase.FeedXRecomWidgetUseCase
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import com.tokopedia.feedplus.presentation.model.FeedLikeModel
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.feedplus.presentation.model.FeedPaginationModel
import com.tokopedia.feedplus.presentation.model.FeedPostEvent
import com.tokopedia.feedplus.presentation.model.FeedReminderResultModel
import com.tokopedia.feedplus.presentation.model.FollowShopModel
import com.tokopedia.feedplus.presentation.model.LikeFeedDataModel
import com.tokopedia.feedplus.presentation.model.PostSourceModel
import com.tokopedia.feedplus.presentation.uiview.FeedCampaignRibbonType
import com.tokopedia.feedplus.presentation.util.common.FeedLikeAction
import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.PARAM_DEVICE
import com.tokopedia.topads.sdk.utils.PARAM_EP
import com.tokopedia.topads.sdk.utils.PARAM_HEADLINE_PRODUCT_COUNT
import com.tokopedia.topads.sdk.utils.PARAM_ITEM
import com.tokopedia.topads.sdk.utils.PARAM_PAGE
import com.tokopedia.topads.sdk.utils.PARAM_SRC
import com.tokopedia.topads.sdk.utils.PARAM_TEMPLATE_ID
import com.tokopedia.topads.sdk.utils.PARAM_USER_ID
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import com.tokopedia.topads.sdk.utils.UrlParamHelper
import com.tokopedia.topads.sdk.utils.VALUE_DEVICE
import com.tokopedia.topads.sdk.utils.VALUE_EP
import com.tokopedia.topads.sdk.utils.VALUE_HEADLINE_PRODUCT_COUNT
import com.tokopedia.topads.sdk.utils.VALUE_ITEM
import com.tokopedia.topads.sdk.utils.VALUE_TEMPLATE_ID
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.HttpURLConnection
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 23/02/23
 */
class FeedPostViewModel @Inject constructor(
    private val repository: FeedRepository,
    private val addToCartUseCase: AddToCartUseCase,
    private val likeContentUseCase: SubmitLikeContentUseCase,
    private val deletePostUseCase: SubmitActionContentUseCase,
    private val userSession: UserSessionInterface,
    private val shopFollowUseCase: ShopFollowUseCase,
    private val userFollowUseCase: ProfileFollowUseCase,
    private val userUnfollowUseCase: ProfileUnfollowedUseCase,
    private val setCampaignReminderUseCase: FeedCampaignReminderUseCase,
    private val checkCampaignReminderUseCase: FeedCampaignCheckReminderUseCase,
    private val topAdsHeadlineUseCase: GetTopAdsHeadlineUseCase,
    private val mvcSummaryUseCase: MVCSummaryUseCase,
    private val topAdsAddressHelper: TopAdsAddressHelper,
    private val getCountCommentsUseCase: GetCountCommentsUseCase,
    private val affiliateCookieHelper: AffiliateCookieHelper,
    private val trackVisitChannelUseCase: TrackVisitChannelBroadcasterUseCase,
    private val trackReportTrackViewerUseCase: BroadcasterReportTrackViewerUseCase,
    private val submitReportUseCase: FeedComplaintSubmitReportUseCase,
    private val getReportUseCase: GetUserReportListUseCase,
    private val postReportUseCase: PostUserReportUseCase,
    private val feedXRecomWidgetUseCase: FeedXRecomWidgetUseCase,
    private val uiEventManager: UiEventManager<FeedPostEvent>,
    private val feedXGetActivityProductsUseCase: FeedXGetActivityProductsUseCase,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val _feedHome = MutableLiveData<Result<FeedModel>?>()
    val feedHome: LiveData<Result<FeedModel>?>
        get() = _feedHome

    private val _followResult = MutableLiveData<Result<String>>()
    val followResult: LiveData<Result<String>>
        get() = _followResult

    private val _unfollowResult = MutableLiveData<Result<String>>()
    val unfollowResult: LiveData<Result<String>>
        get() = _unfollowResult

    private val _likeKolResp = MutableLiveData<FeedResult<LikeFeedDataModel>>()
    val getLikeKolResp: LiveData<FeedResult<LikeFeedDataModel>>
        get() = _likeKolResp

    private val _deletePostResult = MutableLiveData<Result<Int>>()
    val deletePostResult: LiveData<Result<Int>>
        get() = _deletePostResult

    private val _reminderResult = MutableLiveData<Result<FeedReminderResultModel>>()
    val reminderResult: LiveData<Result<FeedReminderResultModel>>
        get() = _reminderResult

    private val _feedTagProductList = MutableLiveData<Result<List<FeedTaggedProductUiModel>>?>()
    val feedTagProductList: LiveData<Result<List<FeedTaggedProductUiModel>>?>
        get() = _feedTagProductList

    private val _followRecommendationResult = MutableLiveData<Result<String>>()
    val followRecommendationResult: LiveData<Result<String>>
        get() = _followRecommendationResult

    private val _suspendedFollowData = MutableLiveData<FollowShopModel>()
    private val _suspendedLikeData = MutableLiveData<LikeFeedDataModel>()

    private var fetchPostJob: Job? = null

    private var cursor = ""
    private var currentTopAdsPage = 0
    private var shouldFetchTopAds = true

    private var _shouldShowNoMoreContent = false
    private var _hasNext = true
    val shouldShowNoMoreContent: Boolean
        get() = _shouldShowNoMoreContent

    val hasNext: Boolean
        get() = _hasNext

    val uiEvent: Flow<FeedPostEvent?>
        get() = uiEventManager.event

    private val _userReport = MutableLiveData<Result<List<PlayUserReportReasoningUiModel>>>()
    val userReportList
        get() =
            _userReport.value ?: Success(emptyList())

    private val _selectedReport = MutableLiveData<PlayUserReportReasoningUiModel.Reasoning>()
    val selectedReport get() = _selectedReport.value
    private val _isReported = MutableLiveData<Result<Unit>>()
    val isReported: LiveData<Result<Unit>> get() = _isReported

    fun fetchFeedPosts(
        source: String,
        isNewData: Boolean = false,
        postSource: PostSourceModel? = null
    ) {
        if (fetchPostJob?.isActive == true) return
        if (!isNewData && !_hasNext) return

        _shouldShowNoMoreContent = false
        if (isNewData) _feedHome.value = null

        fetchPostJob = viewModelScope.launch {
            if (cursor == "" || cursor != _feedHome.value?.cursor.orEmpty()) {
                shouldFetchTopAds = true
                cursor = _feedHome.value?.cursor.orEmpty()

                val relevantPostsDeferred = async {
                    try {
                        requireNotNull(postSource)
                        require(isNewData)

                        if (postSource.source != FeedBaseFragment.TAB_TYPE_CDP) {
                            repository.getRelevantPosts(postSource)
                        } else {
                            FeedModel.Empty
                        }
                    } catch (_: Throwable) {
                        FeedModel.Empty
                    }.items
                }

                val feedPostsDeferred = async {
                    try {
                        Success(
                            getFeedPosts(
                                source = source,
                                cursor = _feedHome.value?.cursor.orEmpty(),
                                postSourceModel = postSource
                            )
                        )
                    } catch (e: Throwable) {
                        Fail(e)
                    }
                }

                val relevantPostData = relevantPostsDeferred.await().map {
                    when (it) {
                        is FeedCardImageContentModel -> if (it.campaign.id.isNotEmpty()) {
                            it.copy(
                                campaign = it.campaign.copy(
                                    isReminderActive = getCampaignReminderStatus(it.campaign.id.toLongOrZero())
                                )
                            )
                        } else {
                            it
                        }
                        is FeedCardVideoContentModel -> if (it.campaign.id.isNotEmpty()) {
                            it.copy(
                                campaign = it.campaign.copy(
                                    isReminderActive = getCampaignReminderStatus(it.campaign.id.toLongOrZero())
                                )
                            )
                        } else {
                            it
                        }
                        else -> it
                    }
                }

                val feedPosts = feedPostsDeferred.await()

                uiEventManager.emitEvent(
                    FeedPostEvent.PreCacheVideos(
                        feedPosts.items.mapNotNull {
                            if (it is FeedCardVideoContentModel && it.videoUrl.isNotEmpty()) {
                                it.videoUrl
                            } else {
                                null
                            }
                        }
                    )
                )

                _feedHome.value = when (feedPosts) {
                    is Success -> {
                        val items = feedPosts.items.map {
                            when (it) {
                                is FeedCardImageContentModel -> if (it.campaign.id.isNotEmpty()) {
                                    it.copy(
                                        campaign = it.campaign.copy(
                                            isReminderActive = getCampaignReminderStatus(it.campaign.id.toLongOrZero())
                                        )
                                    )
                                } else {
                                    it
                                }
                                is FeedCardVideoContentModel -> if (it.campaign.id.isNotEmpty()) {
                                    it.copy(
                                        campaign = it.campaign.copy(
                                            isReminderActive = getCampaignReminderStatus(it.campaign.id.toLongOrZero())
                                        )
                                    )
                                } else {
                                    it
                                }
                                else -> it
                            }
                        }.toList()

                        _shouldShowNoMoreContent = items.isEmpty() &&
                            source == FeedBaseFragment.TAB_TYPE_FOLLOWING

                        _hasNext = feedPosts.pagination.hasNext

                        Success(
                            data = feedPosts.data.copy(
                                items = relevantPostData + _feedHome.value?.items.orEmpty() + items
                            )
                        )
                    }
                    else -> feedPosts
                }

                feedPosts.items.filterIsInstance<FeedFollowRecommendationModel>().forEach { item ->
                    fetchFollowRecommendation(item.id)
                }
            }
        }
    }

    fun setUnsetReminder(
        campaignId: Long,
        setReminder: Boolean,
        type: FeedCampaignRibbonType
    ) {
        viewModelScope.launch {
            try {
                val response = setCampaignReminderUseCase(
                    setCampaignReminderUseCase.createParams(
                        campaignId,
                        setReminder
                    )
                )

                if (!response.success || response.errorMessage.isNotEmpty()) {
                    throw MessageErrorException(response.errorMessage)
                }

                _feedHome.value?.let {
                    if (it is Success) {
                        val newData = it.data.items.map { item ->
                            when {
                                item is FeedCardImageContentModel && item.campaign.id.equals(
                                    campaignId.toString(),
                                    true
                                ) -> item.copy(
                                    campaign = item.campaign.copy(
                                        isReminderActive = setReminder
                                    )
                                )
                                item is FeedCardVideoContentModel && item.campaign.id.equals(
                                    campaignId.toString(),
                                    true
                                ) -> item.copy(
                                    campaign = item.campaign.copy(
                                        isReminderActive = setReminder
                                    )
                                )
                                else -> item
                            }
                        }

                        _feedHome.value = it.copy(
                            data = it.data.copy(
                                items = newData
                            )
                        )
                    }
                }
                _reminderResult.value = Success(
                    FeedReminderResultModel(
                        isSetReminder = setReminder,
                        reminderType = type
                    )
                )
            } catch (e: Throwable) {
                _reminderResult.value = Fail(e)
            }
        }
    }

    fun isFollowing(id: String): Boolean = feedHome.value?.let {
        return when (it) {
            is Success -> {
                val filteredData = it.data.items.firstOrNull { item ->
                    (item is FeedCardImageContentModel && item.id == id) || (item is FeedCardVideoContentModel && item.id == id) || (item is FeedCardLivePreviewContentModel && item.id == id)
                }
                return filteredData?.let { item ->
                    when {
                        item is FeedCardImageContentModel && item.id == id -> item.followers.isFollowed
                        item is FeedCardVideoContentModel && item.id == id -> item.followers.isFollowed
                        item is FeedCardLivePreviewContentModel && item.id == id -> item.followers.isFollowed
                        else -> false
                    }
                } ?: false
            }
            else -> false
        }
    } ?: false

    fun fetchTopAdsData() {
        viewModelScope.launch {
            feedHome.value?.let {
                if (it is Success && shouldFetchTopAds) {
                    shouldFetchTopAds = false

                    val defaultTopAdsUrlParams: MutableMap<String, Any> = getTopAdsParams()
                    val topAdsAddressData = topAdsAddressHelper.getAddressData()

                    val newItems = it.data.items.mapNotNull { item ->
                        when {
                            item is FeedCardImageContentModel && item.isTopAds && !item.isFetched -> {
                                val topAdsDeferred = async {
                                    topAdsHeadlineUseCase.setParams(
                                        UrlParamHelper.generateUrlParamString(
                                            defaultTopAdsUrlParams.apply {
                                                put(PARAM_PAGE, ++currentTopAdsPage)
                                            }
                                        ),
                                        topAdsAddressData
                                    )
                                    val data = topAdsHeadlineUseCase.executeOnBackground()
                                    if (data.displayAds.data.isNotEmpty()) {
                                        val cpmModel = data.displayAds
                                        transformCpmToFeedTopAds(item, cpmModel)
                                    } else {
                                        // Error fetch TopAds, should remove the view
                                        null
                                    }
                                }
                                topAdsDeferred.await()
                            }
                            else -> item
                        }
                    }

                    _feedHome.value = Success(
                        it.data.copy(
                            items = newItems
                        )
                    )
                }
            }
        }
    }

    fun fetchFollowRecommendation(position: Int) {
        _feedHome.value?.let {
            if (it is Success) {
                val followRecomData = it.data.items.getOrNull(position)

                if (followRecomData is FeedFollowRecommendationModel)
                    viewModelScope.launch {
                        fetchFollowRecommendation(followRecomData.id)
                    }
            }
        }
    }

    private suspend fun fetchFollowRecommendation(widgetId: String) {
        val followRecomData = getFollowRecomModel(widgetId)

        try {
            feedHome.value?.let {
                if (it is Success && isAllowFetchFollowRecommendation(followRecomData)) {
                    updateFollowRecom(followRecomData.id) { followRecom ->
                        followRecom.copy(status = FeedFollowRecommendationModel.Status.Loading)
                    }

                    val request = feedXRecomWidgetUseCase.createFeedFollowRecomParams(followRecomData.cursor, followRecomData.id)
                    val response = feedXRecomWidgetUseCase(request)

                    updateFollowRecom(followRecomData.id) { followRecom ->
                        response.copy(
                            data = followRecom.data + response.data
                        )
                    }
                }
            }
        } catch (throwable: Throwable) {
            updateFollowRecom(followRecomData.id) { followRecom ->
                followRecom.copy(
                    status = if (followRecom.data.isEmpty()) {
                        FeedFollowRecommendationModel.Status.getErrorStatus(throwable)
                    } else {
                        _followRecommendationResult.value = Fail(throwable)
                        FeedFollowRecommendationModel.Status.Success
                    }
                )
            }
        }
    }

    private fun isAllowFetchFollowRecommendation(followRecomData: FeedFollowRecommendationModel): Boolean {
        return followRecomData.isError ||
            // load for the first time
            (followRecomData.data.isEmpty() && !followRecomData.hasNext && !followRecomData.isLoading) ||
            // load next page
            (followRecomData.hasNext && !followRecomData.isLoading)
    }

    fun suspendFollow(id: String, encryptedId: String, isShop: Boolean) {
        _suspendedFollowData.value = FollowShopModel(
            id = id,
            encryptedId = encryptedId,
            success = false,
            isFollowing = false,
            isShop = isShop
        )
    }

    fun processSuspendedFollow() {
        _suspendedFollowData.value?.let {
            doFollow(it.id, it.encryptedId, it.isShop)
        }
    }

    fun doFollow(id: String, encryptedId: String, isShop: Boolean) {
        viewModelScope.launch {
            try {
                val response = followProfile(id, encryptedId, isShop)

                updateFollowStatus(response.id, response.isFollowing)
                _followResult.value = Success(if (isShop) SHOP else USER)
            } catch (it: Throwable) {
                _followResult.value = Fail(it)
            }
        }
    }

    fun doFollowProfileRecommendation(id: String, encryptedId: String, isShop: Boolean) {
        viewModelScope.launch {
            try {
                updateFollowStatus(id, true)

                val response = followProfile(id, encryptedId, isShop)

                if (response.success) {
                    _followResult.value = Success(if (isShop) SHOP else USER)
                } else {
                    throw Exception()
                }
            } catch (throwable: Throwable) {
                updateFollowStatus(id, false)
                _followResult.value = Fail(throwable)
            }
        }
    }

    fun doUnfollowProfileRecommendation(id: String, encryptedId: String, isShop: Boolean) {
        viewModelScope.launch {
            try {
                updateFollowStatus(id, false)

                val response = unfollowProfile(id, encryptedId, isShop)

                if (response.success) {
                    _unfollowResult.value = Success(if (isShop) SHOP else USER)
                } else {
                    throw Exception()
                }
            } catch (throwable: Throwable) {
                updateFollowStatus(id, true)
                _unfollowResult.value = Fail(throwable)
            }
        }
    }

    private suspend fun followProfile(id: String, encryptedId: String, isShop: Boolean): FollowShopModel {
        return withContext(dispatchers.io) {
            if (isShop) {
                val response = shopFollowUseCase(shopFollowUseCase.createParams(id))
                FollowShopModel(
                    id = id,
                    encryptedId = encryptedId,
                    success = response.followShop.success,
                    isFollowing = response.followShop.isFollowing,
                    isShop = true
                )
            } else {
                val response = userFollowUseCase.executeOnBackground(encryptedId)
                if (response.profileFollowers.errorCode.isNotEmpty()) {
                    throw MessageErrorException(
                        response.profileFollowers.messages.firstOrNull() ?: ""
                    )
                }
                FollowShopModel(
                    id = id,
                    encryptedId = encryptedId,
                    success = true,
                    isFollowing = true,
                    isShop = false
                )
            }
        }
    }

    private suspend fun unfollowProfile(id: String, encryptedId: String, isShop: Boolean): FollowShopModel {
        return withContext(dispatchers.io) {
            if (isShop) {
                val response = shopFollowUseCase(shopFollowUseCase.createParams(id, ShopFollowAction.UnFollow))
                FollowShopModel(
                    id = id,
                    encryptedId = encryptedId,
                    success = response.followShop.success,
                    isFollowing = response.followShop.isFollowing,
                    isShop = true
                )
            } else {
                val response = userUnfollowUseCase.executeOnBackground(encryptedId)
                if (response.profileFollowers.errorCode.isNotEmpty()) {
                    throw MessageErrorException(
                        response.profileFollowers.messages.firstOrNull() ?: ""
                    )
                }
                FollowShopModel(
                    id = id,
                    encryptedId = encryptedId,
                    success = true,
                    isFollowing = false,
                    isShop = false
                )
            }
        }
    }

    fun suspendLikeContent(contentId: String, rowNumber: Int) {
        _suspendedLikeData.value = LikeFeedDataModel(
            contentId,
            rowNumber,
            FeedLikeAction.Like
        )
    }

    fun processSuspendedLike() {
        _suspendedLikeData.value?.let {
            likeContent(it.contentId, it.rowNumber, shouldLike = true)
        }
    }

    fun likeContent(contentId: String, rowNumber: Int) {
        val likeStatus = getIsLikedStatus(contentId) ?: return
        likeContent(contentId, rowNumber, shouldLike = !likeStatus)
    }

    fun consumeEvent(event: FeedPostEvent) {
        viewModelScope.launch {
            uiEventManager.clearEvent(event.id)
        }
    }

    private fun likeContent(contentId: String, rowNumber: Int, shouldLike: Boolean) {
        _likeKolResp.value = FeedResult.Loading
        viewModelScope.launch {
            val action = if (shouldLike) FeedLikeAction.Like else FeedLikeAction.UnLike
            updateLikeStatus(contentId, shouldLike)

            try {
                likeContentUseCase.setRequestParams(
                    SubmitLikeContentUseCase.createParam(contentId, action.value)
                )
                val response = likeContentUseCase.executeOnBackground()

                if (response.doLikeKolPost.error.isNotEmpty()) {
                    throw MessageErrorException(response.doLikeKolPost.error)
                }
                if (response.doLikeKolPost.data.success != SubmitLikeContentUseCase.SUCCESS) {
                    throw CustomUiMessageThrowable(R.string.feed_like_error_message)
                }
                val mappedResponse = mapLikeResponse(contentId, action, rowNumber)
                _likeKolResp.value = FeedResult.Success(mappedResponse)
            } catch (it: Throwable) {
                _likeKolResp.value = FeedResult.Failure(it)
            }
        }
    }

    fun doDeletePost(id: String, rowNumber: Int) {
        viewModelScope.launch {
            try {
                deletePostUseCase.setRequestParams(
                    SubmitActionContentUseCase.paramToDeleteContent(
                        id
                    )
                )

                val isSuccess = withContext(dispatchers.io) {
                    deletePostUseCase.executeOnBackground().content.success == SubmitPostData.SUCCESS
                }

                _deletePostResult.value = if (isSuccess) {
                    Success(rowNumber)
                } else {
                    throw MessageErrorException()
                }

                if (feedHome.value is Success) {
                    val currentFeedData = (feedHome.value as Success).data
                    _feedHome.value = Success(
                        currentFeedData.copy(
                            items = currentFeedData.items.filter {
                                when (it) {
                                    is FeedCardImageContentModel -> it.id != id
                                    is FeedCardVideoContentModel -> it.id != id
                                    else -> true
                                }
                            }
                        )
                    )
                }
            } catch (t: Throwable) {
                _deletePostResult.value = Fail(t)
            }
        }
    }

    fun updateCommentsCount(contentId: String, isPlayContent: Boolean) {
        viewModelScope.launch {
            try {
                val result = withContext(dispatchers.io) {
                    getCountCommentsUseCase(
                        GetCountCommentsUseCase.Param(
                            sourceId = listOf(
                                contentId
                            ),
                            sourceType = if (isPlayContent) TYPE_COMMENT_PLAY_CHANNEL_ID else TYPE_COMMENT_ACTIVITY_ID
                        )
                    ).parent.child.data.firstOrNull {
                        it.contentId == contentId
                    }
                }

                result?.let {
                    updateItems { item ->
                        when {
                            item is FeedCardImageContentModel && item.id == contentId && !isPlayContent -> item.copy(
                                comments = item.comments.copy(
                                    count = it.count.toIntSafely(),
                                    countFmt = it.countFmt
                                )
                            )
                            item is FeedCardVideoContentModel && ((item.id == contentId && !isPlayContent) || (item.playChannelId == contentId && isPlayContent)) -> item.copy(
                                comments = item.comments.copy(
                                    count = it.count.toIntSafely(),
                                    countFmt = it.countFmt
                                )
                            )
                            else -> item
                        }
                    }
                }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    private suspend fun getFeedPosts(
        source: String,
        cursor: String = "",
        postSourceModel: PostSourceModel?
    ): FeedModel {
        var response = FeedModel(emptyList(), FeedPaginationModel.Empty)
        var thresholdGet = 3
        var nextCursor = cursor
        val detailId = if (postSourceModel?.source == FeedBaseFragment.TAB_TYPE_CDP) postSourceModel.id else ""

        while (response.items.isEmpty() && --thresholdGet >= 0) {
            response = repository.getPost(source, nextCursor, detailId = detailId)
            nextCursor = response.pagination.cursor
        }

        return if (response.items.isEmpty()) {
            FeedModel(emptyList(), FeedPaginationModel.Empty)
        } else {
            response
        }
    }

    private suspend fun getCampaignReminderStatus(campaignId: Long): Boolean = try {
        checkCampaignReminderUseCase(
            checkCampaignReminderUseCase.createParams(campaignId)
        ).isAvailable
    } catch (e: Throwable) {
        Timber.e(e)
        false
    }

    private fun mapLikeResponse(contentId: String, likeAction: FeedLikeAction, rowNumber: Int) =
        LikeFeedDataModel(
            contentId = contentId,
            rowNumber = rowNumber,
            action = likeAction
        )

    fun updateFollowStatus(id: String, isFollowing: Boolean) {
        updateItems { item ->
            when {
                item is FeedCardImageContentModel && item.author.id == id -> item.copy(
                    followers = item.followers.copy(
                        isFollowed = isFollowing
                    )
                )
                item is FeedCardVideoContentModel && item.author.id == id -> item.copy(
                    followers = item.followers.copy(
                        isFollowed = isFollowing
                    )
                )
                item is FeedFollowRecommendationModel -> {
                    item.copy(
                        data = item.data.map {
                            if (it.id == id) {
                                it.copy(isFollowed = isFollowing)
                            } else {
                                it
                            }
                        }
                    )
                }
                else -> item
            }
        }
    }

    private fun updateLikeStatus(id: String, isLiked: Boolean) {
        val newLike: (FeedLikeModel) -> FeedLikeModel = { like ->
            val newCount = if (isLiked) like.count + 1 else (like.count - 1).coerceAtLeast(0)
            like.copy(
                isLiked = isLiked,
                count = newCount,
                countFmt = if (like.countFmt.contains(Regex("[a-zA-Z]"))) {
                    like.countFmt
                } else {
                    newCount.toString()
                }
            )
        }

        updateItems { item ->
            when {
                item is FeedCardImageContentModel && item.id == id -> item.copy(
                    like = newLike(item.like)
                )
                item is FeedCardVideoContentModel && item.id == id -> item.copy(
                    like = newLike(item.like)
                )
                else -> item
            }
        }
    }

    private fun getIsLikedStatus(id: String): Boolean? {
        val item = getItemById(id) ?: return null
        return when (item) {
            is FeedCardImageContentModel -> item.like.isLiked
            is FeedCardVideoContentModel -> item.like.isLiked
            else -> null
        }
    }

    private fun getItemById(id: String): Visitable<FeedAdapterTypeFactory>? {
        val currentValue = feedHome.value ?: return null
        if (currentValue !is Success) return null
        return currentValue.items.firstOrNull {
            when (it) {
                is FeedCardImageContentModel -> it.id == id
                is FeedCardVideoContentModel -> it.id == id
                else -> false
            }
        }
    }

    private fun updateItems(
        onUpdate: (Visitable<FeedAdapterTypeFactory>) -> Visitable<FeedAdapterTypeFactory>
    ) {
        val currentValue = feedHome.value

        currentValue?.let {
            when (it) {
                is Success -> {
                    _feedHome.value = Success(
                        it.data.copy(
                            items = it.data.items.map { item ->
                                onUpdate(item)
                            }
                        )
                    )
                }
                else -> {}
            }
        }
    }

    private fun getFollowRecomModel(widgetId: String): FeedFollowRecommendationModel {
        var followRecom = FeedFollowRecommendationModel.Empty

        feedHome.value?.let {
            if (it is Success) {
                followRecom = it.data.items
                    .filterIsInstance<FeedFollowRecommendationModel>()
                    .firstOrNull { item -> item.id == widgetId }
                    ?: FeedFollowRecommendationModel.Empty
            }
        }

        return followRecom
    }

    private fun updateFollowRecom(
        widgetId: String,
        onUpdate: (FeedFollowRecommendationModel) -> FeedFollowRecommendationModel
    ) {
        val currentValue = feedHome.value

        currentValue?.let {
            when (it) {
                is Success -> {
                    _feedHome.value = Success(
                        it.data.copy(
                            items = it.data.items.map { item ->
                                when {
                                    item is FeedFollowRecommendationModel && item.id == widgetId -> {
                                        onUpdate(item)
                                    }
                                    else -> item
                                }
                            }
                        )
                    )
                }
                else -> {}
            }
        }
    }

    private fun getTopAdsParams(): MutableMap<String, Any> = mutableMapOf(
        PARAM_DEVICE to VALUE_DEVICE,
        PARAM_EP to VALUE_EP,
        PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
        PARAM_ITEM to VALUE_ITEM,
        PARAM_SRC to TOPADS_HEADLINE_VALUE_SRC,
        PARAM_TEMPLATE_ID to VALUE_TEMPLATE_ID,
        PARAM_USER_ID to userSession.userId
    )

    /**
     * Add to Cart & Buy
     */
    val observeAddProductToCart: LiveData<Result<AddToCartDataModel>>
        get() = _observeAddProductToCart
    private val _observeAddProductToCart = MutableLiveData<Result<AddToCartDataModel>>()

    val observeBuyProduct: LiveData<Result<AddToCartDataModel>>
        get() = _observeBuyProduct
    private val _observeBuyProduct = MutableLiveData<Result<AddToCartDataModel>>()

    private val _suspendedAddProductToCartData = MutableLiveData<FeedTaggedProductUiModel>()
    private val _suspendedBuyProductData = MutableLiveData<FeedTaggedProductUiModel>()

    fun suspendAddProductToCart(product: FeedTaggedProductUiModel) {
        _suspendedAddProductToCartData.value = product
    }

    fun suspendBuyProduct(product: FeedTaggedProductUiModel) {
        _suspendedBuyProductData.value = product
    }

    fun processSuspendedAddProductToCart() {
        _suspendedAddProductToCartData.value?.let { product ->
            addProductToCart(product)
        }
    }

    fun processSuspendedBuyProduct() {
        _suspendedBuyProductData.value?.let { product ->
            buyProduct(product)
        }
    }

    fun addProductToCart(product: FeedTaggedProductUiModel) {
        viewModelScope.launchCatchError(block = {
            val response = addToCart(product)
            if (response.isDataError()) {
                _observeAddProductToCart.value =
                    Fail(ResponseErrorException(response.getAtcErrorMessage()))
            } else {
                _observeAddProductToCart.value = Success(response)
            }
        }) {
            _observeAddProductToCart.value = Fail(it)
        }
    }

    fun buyProduct(product: FeedTaggedProductUiModel) {
        viewModelScope.launchCatchError(block = {
            val response = addToCart(product)
            if (response.isDataError()) {
                _observeBuyProduct.value =
                    Fail(ResponseErrorException(response.getAtcErrorMessage()))
            } else {
                _observeBuyProduct.value = Success(response)
            }
        }) {
            _observeBuyProduct.value = Fail(it)
        }
    }

    private suspend fun addToCart(product: FeedTaggedProductUiModel) = withContext(dispatchers.io) {
        product.affiliate.let { affiliate ->
            if (affiliate.id.isNotEmpty() && affiliate.channel.isNotEmpty()) {
                affiliateCookieHelper.initCookie(
                    affiliate.id,
                    affiliate.channel,
                    AffiliatePageDetail(
                        pageId = product.id,
                        source = AffiliateSdkPageSource.DirectATC(
                            atcSource = AffiliateAtcSource.SHOP_PAGE,
                            shopId = product.shop.id,
                            null
                        )
                    )
                )
            }
        }

        addToCartUseCase.apply {
            setParams(
                AddToCartUseCase.getMinimumParams(
                    productId = product.id,
                    shopId = product.shop.id,
                    productName = product.title,
                    userId = userSession.userId
                )
            )
        }.executeOnBackground()
    }

    /**
     * Merchant Voucher
     */
    private val _merchantVoucherLiveData = MutableLiveData<Result<TokopointsCatalogMVCSummary>?>()
    val merchantVoucherLiveData: LiveData<Result<TokopointsCatalogMVCSummary>?>
        get() = _merchantVoucherLiveData

    fun getMerchantVoucher(shopId: String) {
        viewModelScope.launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                mvcSummaryUseCase.getResponse(mvcSummaryUseCase.getQueryParams(shopId))
            }
            val result = response.data
            if (result == null) {
                _merchantVoucherLiveData.value = Fail(IllegalStateException())
            } else {
                if (result.resultStatus?.code == HttpURLConnection.HTTP_OK.toString()) {
                    _merchantVoucherLiveData.value = Success(result)
                } else {
                    _merchantVoucherLiveData.value = Fail(
                        ResponseErrorException(
                            response.data?.resultStatus?.message?.firstOrNull().orEmpty()
                        )
                    )
                }
            }
        }) {
            _merchantVoucherLiveData.value = Fail(it)
        }
    }

    fun clearMerchantVoucher() {
        _merchantVoucherLiveData.value = null
    }

    fun getReport() {
        viewModelScope.launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                getReportUseCase.executeOnBackground()
            }
            val mapped = response.data.map { reasoning ->
                PlayUserReportReasoningUiModel.Reasoning(
                    reasoningId = reasoning.id,
                    title = reasoning.value,
                    detail = reasoning.detail,
                    submissionData = if (reasoning.additionalField.isNotEmpty()) reasoning.additionalField.first() else UserReportOptions.OptionAdditionalField()
                )
            }
            _userReport.value = Success(mapped)
        }) {
            _userReport.value = Fail(it)
        }
    }

    fun selectReport(item: PlayUserReportReasoningUiModel.Reasoning) {
        _selectedReport.value = item
    }

    fun submitReport(desc: String, timestamp: Long, item: FeedCardVideoContentModel) {
        viewModelScope.launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                val request = postReportUseCase.createParam(
                    channelId = item.playChannelId.toLongOrZero(),
                    mediaUrl = item.media.firstOrNull()?.mediaUrl.orEmpty(),
                    reasonId = selectedReport?.reasoningId.orZero(),
                    timestamp = timestamp,
                    reportDesc = desc,
                    partnerId = item.author.id.toLongOrZero(),
                    partnerType = PostUserReportUseCase.PartnerType.getTypeFromFeed(item.author.type.value),
                    reporterId = userSession.userId.toLongOrZero()
                )
                postReportUseCase.setRequestParams(request.parameters)
                postReportUseCase.executeOnBackground()
            }
            val isSuccess = response.submissionReport.status == "success"
            _isReported.value = if (isSuccess) Success(Unit) else Fail(MessageErrorException())
        }) {
            _isReported.value = Fail(it)
        }
    }

    /**
     * Track Visit Channel
     */
    fun trackVisitChannel(model: FeedCardVideoContentModel) {
        val playChannelId = model.playChannelId
        if (playChannelId.isBlank()) return

        viewModelScope.launchCatchError(dispatchers.io, block = {
            trackVisitChannelUseCase.apply {
                setRequestParams(
                    TrackVisitChannelBroadcasterUseCase.createParams(
                        playChannelId,
                        TrackVisitChannelBroadcasterUseCase.FEED_ENTRY_POINT_VALUE
                    )
                )
            }.executeOnBackground()
        }) {
        }
    }

    fun trackChannelPerformance(model: FeedCardVideoContentModel) {
        val playChannelId = model.playChannelId
        if (playChannelId.isBlank()) return

        val productIds = model.products.map { it.id }
        viewModelScope.launchCatchError(dispatchers.io, block = {
            trackReportTrackViewerUseCase.apply {
                setRequestParams(
                    BroadcasterReportTrackViewerUseCase.createParams(
                        playChannelId,
                        productIds
                    )
                )
            }.executeOnBackground()
        }) {
        }
    }

    /**
     * Report
     */
    private val _reportResponse = MutableLiveData<Result<FeedComplaintSubmitReportResponse>>()
    val reportResponse: LiveData<Result<FeedComplaintSubmitReportResponse>>
        get() = _reportResponse

    fun reportContent(feedReportRequestParamModel: FeedComplaintSubmitReportUseCase.Param) {
        viewModelScope.launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                submitReportUseCase(feedReportRequestParamModel)
            }
            if (response.data.success.not()) {
                throw MessageErrorException("Error in Reporting")
            } else {
                _reportResponse.value = Success(response)
            }
        }) {
            _reportResponse.value = Fail(it)
        }
    }

    fun fetchFeedProduct(
        activityId: String,
        products: List<FeedTaggedProductUiModel>,
        sourceType: FeedTaggedProductUiModel.SourceType
    ) {
        viewModelScope.launch {
            try {
                _feedTagProductList.value = null

                val currentList: List<FeedTaggedProductUiModel> = when {
                    products.isNotEmpty() -> products
                    else -> emptyList()
                }

                val response = withContext(dispatchers.io) {
                    feedXGetActivityProductsUseCase(
                        feedXGetActivityProductsUseCase.getFeedDetailParam(
                            activityId,
                            cursor
                        )
                    ).data
                }

                val mappedData = response.products.map {
                    ProductMapper.transform(it, response.campaign, sourceType)
                }
                val distinctData = (currentList + mappedData).distinctBy {
                    it.id
                }
                _feedTagProductList.value = Success(distinctData)
            } catch (t: Throwable) {
                _feedTagProductList.value = Fail(t)
            }
        }
    }

    /**
     * Follow Recommendation
     */
    fun removeProfileRecommendation(profile: FeedFollowRecommendationModel.Profile) {
        viewModelScope.launch {
            val feedHome = if (_feedHome.value != null && _feedHome.value is Success)
                _feedHome.value
            else
                return@launch

            if (feedHome == null) return@launch

            _feedHome.value = Success(
                data = FeedModel(
                    items = feedHome.items.map { model ->
                        when (model) {
                            is FeedFollowRecommendationModel -> {
                                model.copy(
                                    data = model.data.filter { item ->
                                        item != profile
                                    }
                                )
                            }
                            else -> model
                        }
                    },
                    pagination = feedHome.pagination
                )
            )
        }
    }

    private val Result<FeedModel>.cursor: String
        get() = pagination.cursor

    private val Result<FeedModel>.items: List<Visitable<FeedAdapterTypeFactory>>
        get() = when (this) {
            is Success -> data.items
            else -> emptyList()
        }

    private val Result<FeedModel>.pagination: FeedPaginationModel
        get() = when (this) {
            is Success -> data.pagination
            else -> FeedPaginationModel.Empty
        }

    companion object {
        private const val SHOP = "toko"
        private const val USER = "akun"

        const val TYPE_COMMENT_ACTIVITY_ID = "ActivityID"
        const val TYPE_COMMENT_PLAY_CHANNEL_ID = "PlayChannelID"
    }
}
