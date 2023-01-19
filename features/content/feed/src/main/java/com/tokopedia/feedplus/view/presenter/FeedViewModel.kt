package com.tokopedia.feedplus.view.presenter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.content.common.usecase.GetWhiteListNewUseCase
import com.tokopedia.content.common.usecase.GetWhiteListNewUseCase.Companion.WHITELIST_INTEREST
import com.tokopedia.createpost.common.domain.entity.SubmitPostData
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.data.feedrevamp.FeedASGCUpcomingReminderStatus
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXData
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.feedrevamp.reversed
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.CheckUpcomingCampaignReminderUseCase
import com.tokopedia.feedcomponent.domain.usecase.FeedBroadcastTrackerUseCase
import com.tokopedia.feedcomponent.domain.usecase.FeedXTrackViewerUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetFollowingUseCase
import com.tokopedia.feedcomponent.domain.usecase.PostUpcomingCampaignReminderUseCase
import com.tokopedia.feedcomponent.domain.usecase.SCREEN_NAME_UPDATE_TAB
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction.Follow
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction.UnFollow
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase.Companion.VAL_LIMIT
import com.tokopedia.feedcomponent.domain.usecase.shoprecom.ShopRecomUseCase.Companion.VAL_SCREEN_NAME_FEED_UPDATE
import com.tokopedia.feedcomponent.people.mapper.ProfileMutationMapper
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.people.usecase.ProfileUnfollowedUseCase
import com.tokopedia.feedcomponent.shoprecom.mapper.ShopRecomUiMapper
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState.FOLLOW
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState.LOADING_FOLLOW
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState.LOADING_UNFOLLOW
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState.UNFOLLOW
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomWidgetModel
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.AtcModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.DeletePostModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FavoriteShopModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FeedAsgcCampaignResponseModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FeedWidgetData
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.TrackAffiliateModel
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.view.viewmodel.FeedPromotedShopModel
import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitReportContentUseCase
import com.tokopedia.kolcommon.view.viewmodel.FollowKolViewModel
import com.tokopedia.kolcommon.view.viewmodel.LikeKolViewModel
import com.tokopedia.kolcommon.view.viewmodel.ViewsKolModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-09-18
 */
class FeedViewModel @Inject constructor(
    private val baseDispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val likeKolPostUseCase: SubmitLikeContentUseCase,
    private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase,
    private val deletePostUseCase: SubmitActionContentUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val sendTopAdsUseCase: SendTopAdsUseCase,
    private val playWidgetTools: PlayWidgetTools,
    private val shopRecomUseCase: ShopRecomUseCase,
    private val shopRecomMapper: ShopRecomUiMapper,
    private val getDynamicFeedNewUseCase: GetDynamicFeedNewUseCase,
    private val sendReportUseCase: SubmitReportContentUseCase,
    private val getWhiteListNewUseCase: GetWhiteListNewUseCase,
    private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
    private val trackVisitChannelBroadcasterUseCase: FeedBroadcastTrackerUseCase,
    private val feedXTrackViewerUseCase: FeedXTrackViewerUseCase,
    private val checkUpcomingCampaignReminderUseCase: CheckUpcomingCampaignReminderUseCase,
    private val postUpcomingCampaignReminderUseCase: PostUpcomingCampaignReminderUseCase,
    private val shopFollowUseCase: ShopFollowUseCase,
    private val doFollowUseCase: ProfileFollowUseCase,
    private val doUnfollowUseCase: ProfileUnfollowedUseCase,
    private val profileMutationMapper: ProfileMutationMapper,
    private val getFollowingUseCase: GetFollowingUseCase,
) : BaseViewModel(baseDispatcher.main) {

    companion object {
        private const val ERROR_FOLLOW_MESSAGE = "“Oops, gagal mem-follow."
        private const val ERROR_UNFOLLOW_MESSAGE = "Oops, gagal meng-unfollow."
        const val PARAM_SOURCE_RECOM_PROFILE_CLICK = "click_recom_profile"
        const val PARAM_SOURCE_SEE_ALL_CLICK = "click_see_all"
        private const val ERROR_CUSTOM_MESSAGE = "Terjadi kesalahan koneksi. Silakan coba lagi."
        private const val FOLLOW_TYPE_SHOP = 2
        private const val FOLLOW_TYPE_BUYER = 3
    }

    val getFeedFirstPageResp = MutableLiveData<Result<DynamicFeedFirstPageDomainModel>>()
    val getFeedNextPageResp = MutableLiveData<Result<DynamicFeedDomainModel>>()
    val doFavoriteShopResp = MutableLiveData<Result<FeedPromotedShopModel>>()
    val followKolResp = MutableLiveData<Result<FollowKolViewModel>>()
    val followKolRecomResp = MutableLiveData<Result<FollowKolViewModel>>()
    val likeKolResp = MutableLiveData<Result<LikeKolViewModel>>()
    val deletePostResp = MutableLiveData<Result<DeletePostModel>>()
    val atcResp = MutableLiveData<Result<AtcModel>>()
    val toggleFavoriteShopResp = MutableLiveData<Result<FavoriteShopModel>>()
    val trackAffiliateResp = MutableLiveData<Result<TrackAffiliateModel>>()
    val reportResponse = MutableLiveData<Result<DeletePostModel>>()
    val viewTrackResponse = MutableLiveData<Result<ViewsKolModel>>()
    val longVideoViewTrackResponse = MutableLiveData<Result<ViewsKolModel>>()

    private val _playWidgetModel = MutableLiveData<Result<CarouselPlayCardModel>>()
    val playWidgetModel: LiveData<Result<CarouselPlayCardModel>>
        get() = _playWidgetModel

    private val _shopRecom = MutableStateFlow(ShopRecomWidgetModel())
    val shopRecom: StateFlow<ShopRecomWidgetModel>
        get() = _shopRecom

    private val _asgcReminderButtonInitialStatus =
        MutableLiveData<Result<FeedAsgcCampaignResponseModel>>()
    val asgcReminderButtonInitialStatus: LiveData<Result<FeedAsgcCampaignResponseModel>>
        get() = _asgcReminderButtonInitialStatus

    private val _asgcReminderButtonStatus = MutableLiveData<Result<FeedAsgcCampaignResponseModel>>()
    val asgcReminderButtonStatus: LiveData<Result<FeedAsgcCampaignResponseModel>>
        get() = _asgcReminderButtonStatus

    private val _feedWidgetLatestData = MutableLiveData<Result<FeedWidgetData>>()
    val feedWidgetLatestData: LiveData<Result<FeedWidgetData>>
        get() = _feedWidgetLatestData

    private val _shopIdsFollowStatusToUpdateData = MutableLiveData<Result<Map<String, Boolean>>>()
    val shopIdsFollowStatusToUpdateData: LiveData<Result<Map<String, Boolean>>>
        get() = _shopIdsFollowStatusToUpdateData

    private var currentCursor = ""
    private val pagingHandler: PagingHandler = PagingHandler()

    private val currentFollowState: MutableMap<String, Boolean> = mutableMapOf()

    fun updateCurrentFollowState(list: List<Visitable<*>>) {
        list.map { item ->
            if (item is DynamicPostModel) {
                currentFollowState[item.header.followCta.authorID] =
                    item.header.followCta.isFollow
            } else if (item is DynamicPostUiModel) {
                currentFollowState[item.feedXCard.author.id] = item.feedXCard.followers.isFollowed
            }
        }
    }

    fun updateFollowStatus() {
        val authorIds = currentFollowState.keys.toList()
        if (authorIds.isNotEmpty())
            viewModelScope.launchCatchError(block = {
                val shopIdsToUpdate = mutableMapOf<String, Boolean>()
                val response = withContext(baseDispatcher.io) {
                    getFollowingUseCase(authorIds)
                }
                response.shopInfoById.result.map { item ->
                    if (currentFollowState[item.shopCore.shopID] != null &&
                        item.favoriteData.isFollowing != currentFollowState[item.shopCore.shopID]
                    ) {
                        shopIdsToUpdate[item.shopCore.shopID] = item.favoriteData.isFollowing
                        currentFollowState[item.shopCore.shopID] = item.favoriteData.isFollowing
                    }
                }
                response.feedXProfileIsFollowing.isUserFollowing.map { item ->
                    if (currentFollowState[item.userId] != null &&
                        response.shopInfoById.result.firstOrNull { it.shopCore.shopID == item.userId } == null &&
                        item.status != currentFollowState[item.userId]
                    ) {
                        shopIdsToUpdate[item.userId] = item.status
                        currentFollowState[item.userId] = item.status
                    }
                }
                _shopIdsFollowStatusToUpdateData.value = Success(shopIdsToUpdate)
            }) {
                _shopIdsFollowStatusToUpdateData.value = Fail(it)
            }
    }

    fun clearFollowIdToUpdate() {
        if (_shopIdsFollowStatusToUpdateData.value != null &&
            _shopIdsFollowStatusToUpdateData.value is Success &&
            (_shopIdsFollowStatusToUpdateData.value as Success).data.isNotEmpty()
        ) {
            _shopIdsFollowStatusToUpdateData.value = Success(mapOf())
        }
    }

    fun sendReport(
        positionInFeed: Int,
        contentId: String,
        reasonType: String,
        reasonMessage: String
    ) {
        viewModelScope.launchCatchError(block = {
            sendReportUseCase.setRequestParams(
                SubmitReportContentUseCase.createParam(
                    contentId = contentId,
                    reasonType = reasonType,
                    reasonMessage = reasonMessage
                )
            )
            val response = withContext(baseDispatcher.io) {
                sendReportUseCase.executeOnBackground()
            }
            if (response.content.errorMessage.isEmpty()) {
                reportResponse.value = Success(
                    DeletePostModel(
                        contentId,
                        positionInFeed,
                        response.content.errorMessage,
                        true
                    )
                )
            } else {
                reportResponse.value = Fail(MessageErrorException(response.content.errorMessage))
            }
        }) {
            reportResponse.value = Fail(it)
        }
    }

    fun trackVisitChannel(channelId: String, rowNumber: Int) {
        viewModelScope.launchCatchError(block = {
            trackVisitChannelBroadcasterUseCase.setRequestParams(
                FeedBroadcastTrackerUseCase.createParams(
                    channelId
                )
            )
            val trackResponse = withContext(baseDispatcher.io) {
                trackVisitChannelBroadcasterUseCase.executeOnBackground()
            }
            val data = ViewsKolModel()
            data.rowNumber = rowNumber
            data.isSuccess = trackResponse.reportVisitChannelTracking.success
            viewTrackResponse.postValue(Success(data))
        }) {
            viewTrackResponse.postValue(Fail(it))
        }
    }

    fun fetchLatestFeedPostWidgetData(detailId: String, rowNumber: Int) {
        viewModelScope.launchCatchError(
            baseDispatcher.io,
            block = {
                val response = getFeedWidgetUpdatedData(detailId)

                if (response?.feedXHome?.items?.isNotEmpty() == true) {
                    val updatedData = FeedWidgetData(
                        rowNumber = rowNumber,
                        feedXCard = response.feedXHome.items.first()
                    )
                    _feedWidgetLatestData.postValue(Success(updatedData))
                } else {
                    _feedWidgetLatestData.postValue(
                        Fail(CustomUiMessageThrowable(com.tokopedia.feedplus.R.string.feed_result_empty))
                    )
                }
            }
        ) {
            _feedWidgetLatestData.value = Fail(it)
        }
    }

    private suspend fun getFeedWidgetUpdatedData(detailId: String): FeedXData? {
        try {
            return getDynamicFeedNewUseCase.executeForCDP(
                cursor = currentCursor,
                detailId = detailId
            )
        } catch (e: Throwable) {
            Timber.e(e)
        }
        return null
    }

    fun trackLongVideoView(activityId: String, rowNumber: Int) {
        viewModelScope.launchCatchError(
            baseDispatcher.io,
            block = {
                feedXTrackViewerUseCase.setRequestParams(
                    FeedXTrackViewerUseCase.createParams(
                        activityId
                    )
                )
                val trackResponse = feedXTrackViewerUseCase.executeOnBackground()
                val data = ViewsKolModel()
                data.rowNumber = rowNumber
                data.isSuccess = trackResponse.feedXTrackViewerResponse.success
                longVideoViewTrackResponse.postValue(Success(data))
            }
        ) {
            longVideoViewTrackResponse.postValue(Fail(it))
        }
    }

    fun checkUpcomingCampaignInitialReminderStatus(campaign: FeedXCampaign, rowNumber: Int) {
        viewModelScope.launchCatchError(
            baseDispatcher.io,
            block = {
                val data = checkUpcomingCampaign(campaignId = campaign.campaignId)
                val reminderStatusRes =
                    if (data) {
                        FeedASGCUpcomingReminderStatus.On(campaign.campaignId)
                    } else {
                        FeedASGCUpcomingReminderStatus.Off(
                            campaign.campaignId
                        )
                    }
                _asgcReminderButtonInitialStatus.postValue(
                    Success(
                        FeedAsgcCampaignResponseModel(
                            rowNumber = rowNumber,
                            campaignId = campaign.campaignId,
                            reminderStatus = reminderStatusRes
                        )
                    )
                )
            }
        ) {
            _asgcReminderButtonInitialStatus.postValue(Fail(it))
        }
    }

    private suspend fun checkUpcomingCampaign(campaignId: Long): Boolean =
        withContext(baseDispatcher.io) {
            val response = checkUpcomingCampaignReminderUseCase.apply {
                setRequestParams(CheckUpcomingCampaignReminderUseCase.createParam(campaignId))
            }.executeOnBackground()
            return@withContext response.response.isAvailable
        }

    fun setUnsetReminder(campaign: FeedXCampaign, rowNumber: Int) {
        viewModelScope.launchCatchError(
            block = {
                val data = subscribeUpcomingCampaign(
                    campaignId = campaign.campaignId,
                    reminderType = campaign.reminder
                )
                if (data.first) {
                    val reminderStatusRes = campaign.reminder.reversed(campaign.campaignId)
                    _asgcReminderButtonStatus.value = Success(
                        FeedAsgcCampaignResponseModel(
                            rowNumber = rowNumber,
                            campaignId = campaign.campaignId,
                            reminderStatus = reminderStatusRes
                        )
                    )
                } else {
                    _asgcReminderButtonStatus.value = Fail(Throwable(data.second))
                }
            }
        ) {
            _asgcReminderButtonStatus.value = Fail(it)
        }
    }

    suspend fun subscribeUpcomingCampaign(
        campaignId: Long,
        reminderType: FeedASGCUpcomingReminderStatus
    ): Pair<Boolean, String> = withContext(baseDispatcher.io) {
        val response = postUpcomingCampaignReminderUseCase.apply {
            setRequestParams(
                PostUpcomingCampaignReminderUseCase.createParam(
                    campaignId,
                    reminderType
                ).parameters
            )
        }.executeOnBackground()
        return@withContext Pair(
            response.response.success,
            if (response.response.errorMessage.isNotEmpty()) response.response.errorMessage else response.response.message
        )
    }

    fun getFeedFirstPage() {
        pagingHandler.resetPage()
        currentCursor = ""
        launchCatchError(
            block = {
                val results = withContext(baseDispatcher.io) {
                    getFeedFirstDataResult()
                }
                currentCursor = results.dynamicFeedDomainModel.cursor
                getFeedFirstPageResp.value = Success(results)

                if (shouldGetPlayWidget(results.dynamicFeedDomainModel)) {
                    try {
                        val newCarouselModel = processPlayWidget()
                        _playWidgetModel.value = Success(newCarouselModel)
                    } catch (e: Throwable) {
                        _playWidgetModel.value = Fail(e)
                    }
                }

                if (shouldGetShopRecomWidget(results.dynamicFeedDomainModel)) getShopRecomWidget()
            }
        ) {
            getFeedFirstPageResp.value = Fail(it)
        }
    }

    fun getFeedNextPage() {
        pagingHandler.nextPage()
        if (currentCursor.isEmpty()) {
            return
        }
        launchCatchError(
            block = {
                val results = withContext(baseDispatcher.io) {
                    getFeedDataResult()
                }
                if (results.hasNext) {
                    currentCursor = results.cursor
                }
                getFeedNextPageResp.value = Success(results)

                if (shouldGetPlayWidget(results)) {
                    try {
                        val newCarouselModel = processPlayWidget()
                        _playWidgetModel.value = Success(newCarouselModel)
                    } catch (e: Throwable) {
                        _playWidgetModel.value = Fail(e)
                    }
                }
            }
        ) {
            getFeedNextPageResp.value = Fail(it)
        }
    }

    fun doFavoriteShop(promotedShopViewModel: Data, adapterPosition: Int) {
        launchCatchError(block = {
            val response = withContext(baseDispatcher.io) {
                shopFollowUseCase.executeOnBackground(
                    shopId = promotedShopViewModel.shop.id
                )
            }
            val result = shopRecomMapper.mapShopFollow(response)
            doFavoriteShopResp.value = Success(
                FeedPromotedShopModel(
                    isSuccess = result is MutationUiModel.Success,
                    promotedShopViewModel = promotedShopViewModel,
                    adapterPosition = adapterPosition
                )
            )
        }) {
            doFavoriteShopResp.value = Fail(it)
        }
    }

    fun doFollowKol(id: String, rowNumber: Int, isFollowedFromFollowRestrictionBottomSheet: Boolean = false) {
        launchCatchError(block = {
            val response = withContext(baseDispatcher.io) {
                doFollowUseCase.executeOnBackground(id)
            }
            val result = profileMutationMapper.mapFollow(response)
            followKolResp.value = Success(
                FollowKolViewModel(
                    id = id,
                    rowNumber = rowNumber,
                    status = 1, // todo: revamp the whole
                    isFollowedFromFollowRestrictionBottomSheet = isFollowedFromFollowRestrictionBottomSheet,
                    isSuccess = result is MutationUiModel.Success,
                    isFollow = true
                )
            )
        }) {
            followKolResp.value = Fail(Exception(ERROR_FOLLOW_MESSAGE))
        }
    }

    fun doUnfollowKol(id: String, rowNumber: Int) {
        launchCatchError(block = {
            val response = withContext(baseDispatcher.io) {
                doUnfollowUseCase.executeOnBackground(id)
            }
            val result = profileMutationMapper.mapUnfollow(response)
            followKolResp.value = Success(
                FollowKolViewModel(
                    id = id,
                    rowNumber = rowNumber,
                    status = 0,
                    isSuccess = result is MutationUiModel.Success,
                    isFollow = false
                )
            )
        }) {
            followKolResp.value = Fail(Exception(ERROR_UNFOLLOW_MESSAGE))
        }
    }

    fun doLikeKol(id: Long, rowNumber: Int) {
        viewModelScope.launchCatchError(block = {
            likeKolPostUseCase.setRequestParams(
                SubmitLikeContentUseCase.createParam(
                    contentId = id.toString(),
                    action = SubmitLikeContentUseCase.ACTION_LIKE
                )
            )
            val isSuccess = withContext(baseDispatcher.io) {
                likeKolPostUseCase.executeOnBackground().doLikeKolPost.data.success == SubmitLikeContentUseCase.SUCCESS
            }
            likeKolResp.value =
                if (isSuccess) {
                    Success(LikeKolViewModel(id = id, rowNumber = rowNumber, isSuccess = true))
                } else {
                    Fail(CustomUiMessageThrowable(R.string.feed_like_error_message))
                }
        }) {
            likeKolResp.value = Fail(it)
        }
    }

    fun doUnlikeKol(id: Long, rowNumber: Int) {
        viewModelScope.launchCatchError(block = {
            likeKolPostUseCase.setRequestParams(
                SubmitLikeContentUseCase.createParam(
                    contentId = id.toString(),
                    action = SubmitLikeContentUseCase.ACTION_UNLIKE
                )
            )
            val isSuccess = withContext(baseDispatcher.io) {
                likeKolPostUseCase.executeOnBackground().doLikeKolPost.data.success == SubmitLikeContentUseCase.SUCCESS
            }
            likeKolResp.value = if (isSuccess) {
                Success(LikeKolViewModel(id = id, rowNumber = rowNumber, isSuccess = isSuccess))
            } else {
                throw MessageErrorException()
            }
        }) {
            likeKolResp.value = Fail(it)
        }
    }

    fun doFollowKolFromRecommendation(id: String, rowNumber: Int, position: Int) {
        launchCatchError(block = {
            val response = withContext(baseDispatcher.io) {
                doFollowUseCase.executeOnBackground(id)
            }
            val result = profileMutationMapper.mapFollow(response)
            followKolRecomResp.value = Success(
                FollowKolViewModel(
                    status = 1,
                    position = position,
                    rowNumber = rowNumber,
                    isSuccess = result is MutationUiModel.Success,
                    isFollow = true
                )
            )
        }) {
            followKolRecomResp.value = Fail(it)
        }
    }

    fun doUnfollowKolFromRecommendation(id: String, rowNumber: Int, position: Int) {
        launchCatchError(block = {
            val response = withContext(baseDispatcher.io) {
                doUnfollowUseCase.executeOnBackground(id)
            }
            val result = profileMutationMapper.mapUnfollow(response)
            followKolRecomResp.value = Success(
                FollowKolViewModel(
                    status = 0,
                    position = position,
                    rowNumber = rowNumber,
                    isSuccess = result is MutationUiModel.Success,
                    isFollow = false
                )
            )
        }) {
            followKolRecomResp.value = Fail(it)
        }
    }

    fun doDeletePost(id: String, rowNumber: Int) {
        viewModelScope.launchCatchError(block = {
            deletePostUseCase.setRequestParams(
                SubmitActionContentUseCase.paramToDeleteContent(
                    contentId = id
                )
            )
            val isSuccess = withContext(baseDispatcher.io) {
                deletePostUseCase.executeOnBackground().content.success == SubmitPostData.SUCCESS
            }
            deletePostResp.value = if (isSuccess) {
                Success(DeletePostModel(id = id, rowNumber = rowNumber, isSuccess = isSuccess))
            } else {
                throw MessageErrorException()
            }
        }) {
            deletePostResp.value = Fail(it)
        }
    }

    fun addtoCartProduct(
        postTagItem: FeedXProduct,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        activityId: String
    ) {
        launchCatchError(
            block = {
                val results = withContext(baseDispatcher.io) {
                    addToCart(postTagItem, shopId, type, isFollowed, activityId)
                }
                atcResp.value = Success(results)
            }
        ) {
            atcResp.value = Fail(it)
        }
    }

    fun doTrackAffiliate(url: String) {
        launchCatchError(
            block = {
                val results = withContext(baseDispatcher.io) {
                    trackAffiliate(url)
                }
                trackAffiliateResp.value = Success(results)
            }
        ) {
        }
    }

    fun doToggleFavoriteShop(
        rowNumber: Int,
        adapterPosition: Int,
        shopId: String,
        follow: Boolean = true,
        isFollowedFromFollowRestrictionBottomSheet: Boolean = false
    ) {
        launchCatchError(block = {
            val response = withContext(baseDispatcher.io) {
                shopFollowUseCase.executeOnBackground(
                    shopId = shopId
                )
            }
            val result = shopRecomMapper.mapShopFollow(response)
            toggleFavoriteShopResp.value = Success(
                FavoriteShopModel(
                    rowNumber = rowNumber,
                    adapterPosition = adapterPosition,
                    shopId = shopId,
                    isFollowedFromFollowRestrictionBottomSheet = isFollowedFromFollowRestrictionBottomSheet,
                    isSuccess = result is MutationUiModel.Success
                )
            )
        }) {
            if (follow) {
                toggleFavoriteShopResp.value =
                    Fail(CustomUiMessageThrowable(R.string.feed_unfollow_error_message))
            } else {
                toggleFavoriteShopResp.value = Fail(Exception(ERROR_FOLLOW_MESSAGE))
            }
        }
    }

    fun doTopAdsTracker(
        url: String,
        shopId: String,
        shopName: String,
        imageUrl: String,
        isClick: Boolean
    ) {
        if (isClick) {
            sendTopAdsUseCase.hitClick(url, shopId, shopName, imageUrl)
        } else {
            sendTopAdsUseCase.hitImpressions(url, shopId, shopName, imageUrl)
        }
    }

    fun doAutoRefreshPlayWidget() {
        launchCatchError(
            block = {
                val newCarouselModel = processPlayWidget(isAutoRefresh = true)
                _playWidgetModel.value = Success(newCarouselModel)
            },
            onError = {
                _playWidgetModel.value = Fail(it)
            }
        )
    }

    fun addWishlistV2(
        activityId: String,
        productId: String,
        shopId: String,
        positionInFeed: Int,
        position: Int,
        type: String,
        isFollowed: Boolean,
        onFail: (String) -> Unit,
        onSuccess: (String, String, String, Boolean, Int, Int, AddToWishlistV2Response.Data.WishlistAddV2) -> Unit,
        context: Context
    ) {
        launch(baseDispatcher.main) {
            addToWishlistV2UseCase.setParams(productId, userSession.userId)
            val result =
                withContext(baseDispatcher.io) { addToWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                onSuccess.invoke(
                    activityId,
                    shopId,
                    type,
                    isFollowed,
                    position,
                    positionInFeed,
                    result.data
                )
            } else if (result is Fail) {
                val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                onFail.invoke(errorMessage)
            }
        }
    }

    private suspend fun getFeedFirstDataResult(): DynamicFeedFirstPageDomainModel {
        return try {
            val feedResponseModel = getFeedDataResult()
            if (userSession.isLoggedIn) {
                val whiteListModel = getWhiteListNewUseCase.execute(type = WHITELIST_INTEREST)
                DynamicFeedFirstPageDomainModel(
                    feedResponseModel,
                    (whiteListModel.whitelist.error.isEmpty() && whiteListModel.whitelist.isWhitelist)
                )
            } else {
                DynamicFeedFirstPageDomainModel(feedResponseModel, false)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    private suspend fun getFeedDataResult(): DynamicFeedDomainModel {
        try {
            return getDynamicFeedNewUseCase.execute(
                cursor = currentCursor,
                screenName = SCREEN_NAME_UPDATE_TAB
            )
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    private suspend fun addToCart(
        postTagItem: FeedXProduct,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        activityId: String
    ): AtcModel =
        withContext(baseDispatcher.io) {
            val params = AddToCartUseCase.getMinimumParams(
                postTagItem.id,
                shopId,
                productName = postTagItem.productName,
                price = postTagItem.price.toString(),
                userId = userSession.userId
            )
            try {
                val data = AtcModel()
                data.applink = postTagItem.appLink
                data.activityId = activityId
                data.postType = type
                data.isFollowed = isFollowed
                data.shopId = shopId

                addToCartUseCase.setParams(params)
                val response = addToCartUseCase.executeOnBackground()
                if (response.isDataError()) throw MessageErrorException(response.getAtcErrorMessage())
                data.isSuccess = !response.isStatusError()
                if (response.isStatusError()) {
                    data.errorMsg = response.errorMessage.firstOrNull() ?: ""
                }
                return@withContext data
            } catch (e: Throwable) {
                if (e is ResponseErrorException) {
                    throw MessageErrorException(e.localizedMessage)
                } else {
                    throw e
                }
            }
        }

    private fun trackAffiliate(url: String): TrackAffiliateModel {
        try {
            val data = TrackAffiliateModel()
            data.url = url
            val params = TrackAffiliateClickUseCase.createRequestParams(url)
            val isSuccess = trackAffiliateClickUseCase.createObservable(params).toBlocking().first()
            if (isSuccess) {
                data.isSuccess = isSuccess
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    /**
     * Play Widget
     */
    fun updatePlayWidgetTotalView(channelId: String?, totalView: String?) {
        if (channelId == null || totalView == null) return

        val currentValue = _playWidgetModel.value
        if (currentValue is Success) {
            val model = currentValue.data.playWidgetState
            _playWidgetModel.value = Success(
                data = currentValue.data.copy(
                    playWidgetState = playWidgetTools.updateTotalView(model, channelId, totalView)
                )
            )
        }
    }

    private fun shouldGetPlayWidget(model: DynamicFeedDomainModel): Boolean {
        return model.postList.any { it is CarouselPlayCardModel }
    }

    private suspend fun processPlayWidget(isAutoRefresh: Boolean = false): CarouselPlayCardModel {
        val response = playWidgetTools.getWidgetFromNetwork(
            widgetType = PlayWidgetUseCase.WidgetType.Feeds,
            coroutineContext = baseDispatcher.io
        )
        val uiModel = playWidgetTools.mapWidgetToModel(response)
        return CarouselPlayCardModel(uiModel, isAutoRefresh)
    }

    /**
     * Shop Recommendation Widget
     */
    fun getShopRecomWidget(nextCursor: String = "") {
        launchCatchError(
            baseDispatcher.io,
            block = {
                val request = requestShopRecomWidget(nextCursor)
                if (request.shopRecomUiModel.isShown) {
                    val items = if (nextCursor.isEmpty()) {
                        request.shopRecomUiModel.items
                    } else {
                        _shopRecom.value.shopRecomUiModel.items + request.shopRecomUiModel.items
                    }

                    _shopRecom.update {
                        it.copy(
                            shopRecomUiModel = it.shopRecomUiModel.copy(
                                isShown = request.shopRecomUiModel.isShown,
                                nextCursor = request.shopRecomUiModel.nextCursor,
                                title = request.shopRecomUiModel.title,
                                loadNextPage = request.shopRecomUiModel.loadNextPage,
                                items = items,
                                isRefresh = nextCursor.isEmpty()
                            ),
                            onError = ""
                        )
                    }
                } else {
                    _shopRecom.update { ShopRecomWidgetModel() }
                }
            },
            onError = {
                if (_shopRecom.value.onError.isNotEmpty()) return@launchCatchError
                _shopRecom.update { data ->
                    data.copy(
                        shopRecomUiModel = data.shopRecomUiModel.copy(loadNextPage = false),
                        onError = it.message.orEmpty()
                    )
                }
            }
        )
    }

    private fun shouldGetShopRecomWidget(model: DynamicFeedDomainModel): Boolean {
        return model.postList.any { it is ShopRecomWidgetModel }
    }

    private suspend fun requestShopRecomWidget(cursor: String): ShopRecomWidgetModel {
        val response = shopRecomUseCase.executeOnBackground(
            screenName = VAL_SCREEN_NAME_FEED_UPDATE,
            limit = VAL_LIMIT,
            cursor = cursor
        )
        val uiModel = shopRecomMapper.mapShopRecom(response, VAL_LIMIT)
        return ShopRecomWidgetModel(uiModel)
    }

    fun handleClickFollowButtonShopRecom(itemId: Long) {
        val currentItem = _shopRecom.value.shopRecomUiModel.items.find { it.id == itemId } ?: return
        val currentState =
            if (currentItem.state == LOADING_FOLLOW || currentItem.state == LOADING_UNFOLLOW) {
                return
            } else {
                currentItem.state
            }
        val isCurrentStateFollow = currentState == FOLLOW
        val loadingState = if (isCurrentStateFollow) LOADING_FOLLOW else LOADING_UNFOLLOW

        viewModelScope.launchCatchError(
            block = {
                updateLoadingStateFollowShopRecom(itemId, loadingState)

                val result = when (currentItem.type) {
                    FOLLOW_TYPE_SHOP -> {
                        val request = shopFollowUseCase.executeOnBackground(
                            shopId = currentItem.id.toString(),
                            action = if (currentState == FOLLOW) UnFollow else Follow
                        )
                        shopRecomMapper.mapShopFollow(request)
                    }
                    FOLLOW_TYPE_BUYER -> {
                        if (currentState == FOLLOW) {
                            val request =
                                doUnfollowUseCase.executeOnBackground(currentItem.encryptedID)
                            profileMutationMapper.mapUnfollow(request)
                        } else {
                            val request =
                                doFollowUseCase.executeOnBackground(currentItem.encryptedID)
                            profileMutationMapper.mapFollow(request)
                        }
                    }
                    else -> return@launchCatchError
                }

                when (result) {
                    is MutationUiModel.Success -> {
                        updateItemFollowStatusShopRecom(currentItem, currentState)
                    }
                    is MutationUiModel.Error -> throw Throwable(result.message)
                }
            },
            onError = {
                updateLoadingStateFollowShopRecom(itemId, currentState)
                _shopRecom.update { data ->
                    data.copy(onError = it.message.orEmpty())
                }
            }
        )
    }

    fun handleClickRemoveButtonShopRecom(itemID: Long) {
        _shopRecom.update { data ->
            data.copy(
                shopRecomUiModel = data.shopRecomUiModel.copy(
                    items = data.shopRecomUiModel.items.filterNot { it.id == itemID }
                ),
                onError = ""
            )
        }
    }

    private fun updateItemFollowStatusShopRecom(
        currentItem: ShopRecomUiModelItem,
        currentState: ShopRecomFollowState
    ) {
        _shopRecom.update { data ->
            data.copy(
                shopRecomUiModel = data.shopRecomUiModel.copy(
                    items = data.shopRecomUiModel.items.map {
                        if (currentItem.id == it.id) {
                            it.copy(state = if (currentState == FOLLOW) UNFOLLOW else FOLLOW)
                        } else {
                            it
                        }
                    }
                ),
                onError = ""
            )
        }
    }

    private fun updateLoadingStateFollowShopRecom(
        itemID: Long,
        state: ShopRecomFollowState
    ) {
        _shopRecom.update { data ->
            data.copy(
                shopRecomUiModel = data.shopRecomUiModel.copy(
                    items = data.shopRecomUiModel.items.map {
                        if (itemID == it.id) {
                            it.copy(state = state)
                        } else {
                            it
                        }
                    }
                ),
                onError = ""
            )
        }
    }
}
