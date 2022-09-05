package com.tokopedia.feedplus.view.presenter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.data.feedrevamp.FeedASGCUpcomingReminderStatus
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.*
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardViewModel
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.domain.model.DynamicFeedFirstPageDomainModel
import com.tokopedia.feedplus.view.constants.Constants.FeedConstants.NON_LOGIN_USER_ID
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.*
import com.tokopedia.feedplus.view.viewmodel.FeedPromotedShopViewModel
import com.tokopedia.kolcommon.data.pojo.FollowKolDomain
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.view.viewmodel.FollowKolViewModel
import com.tokopedia.kolcommon.view.viewmodel.LikeKolViewModel
import com.tokopedia.kolcommon.view.viewmodel.ViewsKolModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import kotlinx.coroutines.withContext
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-09-18
 */

private const val PARAM_SHOP_DOMAIN = "shop_domain"
private const val PARAM_SRC = "src"
private const val PARAM_AD_KEY = "ad_key"
private const val DEFAULT_VALUE_SRC = "fav_shop"

class FeedViewModel @Inject constructor(
    private val baseDispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val doFavoriteShopUseCase: ToggleFavouriteShopUseCase,
    private val followKolPostGqlUseCase: FollowKolPostGqlUseCase,
    private val likeKolPostUseCase: LikeKolPostUseCase,
    private val atcUseCase: AddToCartUseCase,
    private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val sendTopAdsUseCase: SendTopAdsUseCase,
    private val playWidgetTools: PlayWidgetTools,
    private val getDynamicFeedNewUseCase: GetDynamicFeedNewUseCase,
    private val getWhitelistNewUseCase: GetWhitelistNewUseCase,
    private val sendReportUseCase: SendReportUseCase,
    private val addWishListUseCase: AddWishListUseCase,
    private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
    private val trackVisitChannelBroadcasterUseCase: FeedBroadcastTrackerUseCase,
    private val feedXTrackViewerUseCase: FeedXTrackViewerUseCase,
    private val checkUpcomingCampaignReminderUseCase: CheckUpcomingCampaignReminderUseCase,
    private val postUpcomingCampaignReminderUseCase: PostUpcomingCampaignReminderUseCase

) : BaseViewModel(baseDispatcher.main) {

    companion object {
        private const val ERROR_UNFOLLOW_MESSAGE = "Oops, gagal meng-unfollow."
        private const val ERROR_FOLLOW_MESSAGE = "“Oops, gagal mem-follow."
        const val PARAM_SOURCE_RECOM_PROFILE_CLICK = "click_recom_profile"
        const val PARAM_SOURCE_SEE_ALL_CLICK = "click_see_all"
        private const val ERROR_CUSTOM_MESSAGE = "Terjadi kesalahan koneksi. Silakan coba lagi."
    }

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else NON_LOGIN_USER_ID

    val getFeedFirstPageResp = MutableLiveData<Result<DynamicFeedFirstPageDomainModel>>()
    val getFeedNextPageResp = MutableLiveData<Result<DynamicFeedDomainModel>>()
    val doFavoriteShopResp = MutableLiveData<Result<FeedPromotedShopViewModel>>()
    val followKolResp = MutableLiveData<Result<FollowKolViewModel>>()
    val followKolRecomResp = MutableLiveData<Result<FollowKolViewModel>>()
    val likeKolResp = MutableLiveData<Result<LikeKolViewModel>>()
    val deletePostResp = MutableLiveData<Result<DeletePostViewModel>>()
    val atcResp = MutableLiveData<Result<AtcViewModel>>()
    val toggleFavoriteShopResp = MutableLiveData<Result<FavoriteShopViewModel>>()
    val trackAffiliateResp = MutableLiveData<Result<TrackAffiliateViewModel>>()
    val reportResponse = MutableLiveData<Result<DeletePostViewModel>>()
    val viewTrackResponse = MutableLiveData<Result<ViewsKolModel>>()
    val longVideoViewTrackResponse = MutableLiveData<Result<ViewsKolModel>>()


    private val _playWidgetModel = MutableLiveData<Result<CarouselPlayCardViewModel>>()
    val playWidgetModel: LiveData<Result<CarouselPlayCardViewModel>>
        get() = _playWidgetModel

    private val _asgcReminderButtonInitialStatus = MutableLiveData<Result<FeedAsgcCampaignResponseModel>>()
    val asgcReminderButtonInitialStatus: LiveData<Result<FeedAsgcCampaignResponseModel>>
        get() = _asgcReminderButtonInitialStatus
    private val _asgcReminderButtonStatus = MutableLiveData<Result<FeedAsgcCampaignResponseModel>>()
    val asgcReminderButtonStatus: LiveData<Result<FeedAsgcCampaignResponseModel>>
        get() = _asgcReminderButtonStatus

    private var currentCursor = ""
    private val pagingHandler: PagingHandler = PagingHandler()

    fun sendReport(
        positionInFeed: Int,
        contentId: Int,
        reasonType: String,
        reasonMessage: String,
        contentType: String
    ) {
        sendReportUseCase.createRequestParams(contentId, reasonType, reasonMessage, contentType)
        sendReportUseCase.execute(
            {
                val deleteModel = DeletePostViewModel(
                    contentId,
                    positionInFeed,
                    it.feedReportSubmit.errorMessage,
                    true
                )
                if (it.feedReportSubmit.errorMessage.isEmpty()) {
                    reportResponse.value = Success(deleteModel)
                } else {
                    reportResponse.value = Fail(Exception(it.feedReportSubmit.errorMessage))
                }
            },
            {
                reportResponse.value = Fail(it)
            }
        )
    }

    fun trackVisitChannel(channelId: String,rowNumber: Int) {

        viewModelScope.launchCatchError(baseDispatcher.io, block = {
            trackVisitChannelBroadcasterUseCase.setRequestParams(FeedBroadcastTrackerUseCase.createParams(channelId))
            val trackResponse = trackVisitChannelBroadcasterUseCase.executeOnBackground()
            val data = ViewsKolModel()
            data.rowNumber = rowNumber
            data.isSuccess = trackResponse.reportVisitChannelTracking.success
            viewTrackResponse.postValue(Success(data))
        }) {
            viewTrackResponse.postValue(Fail(it))
        }
    }
    fun trackLongVideoView(activityId: String, rowNumber: Int) {

        viewModelScope.launchCatchError(baseDispatcher.io, block = {
            feedXTrackViewerUseCase.setRequestParams(FeedXTrackViewerUseCase.createParams(activityId))
            val trackResponse = feedXTrackViewerUseCase.executeOnBackground()
            val data = ViewsKolModel()
            data.rowNumber = rowNumber
            data.isSuccess = trackResponse.feedXTrackViewerResponse.success
            longVideoViewTrackResponse.postValue(Success(data))
        }) {
            longVideoViewTrackResponse.postValue(Fail(it))
        }
    }

     fun checkUpcomingCampaignInitialReminderStatus(campaign: FeedXCampaign, rowNumber: Int) {
        viewModelScope.launchCatchError(block = {
            val data = checkUpcomingCampaign(campaignId = campaign.id.toLongOrZero())
            val reminderStatusRes = if (data) FeedASGCUpcomingReminderStatus.On(campaign.id.toLongOrZero()) else FeedASGCUpcomingReminderStatus.Off(campaign.id.toLongOrZero())
                _asgcReminderButtonInitialStatus.value = Success(FeedAsgcCampaignResponseModel(rowNumber = rowNumber, campaignId = campaign.id.toLongOrZero(), reminderStatus = reminderStatusRes))
        }) {
            _asgcReminderButtonInitialStatus.value = Fail(it)
        }
    }

    private suspend fun checkUpcomingCampaign(campaignId: Long): Boolean = withContext(baseDispatcher.io) {
        val response = checkUpcomingCampaignReminderUseCase.apply {
            setRequestParams(CheckUpcomingCampaignReminderUseCase.createParam(campaignId).parameters)
        }.executeOnBackground()
        return@withContext response.response.isAvailable
    }

     fun setUnsetReminder(campaign: FeedXCampaign, rowNumber: Int) {
        viewModelScope.launchCatchError(block = {
            val data = subscribeUpcomingCampaign(
                campaignId = campaign.id.toLongOrZero(),
                reminderType = campaign.reminder
            )
            val reminderStatusRes = if (data.first) FeedASGCUpcomingReminderStatus.On(campaign.id.toLongOrZero()) else FeedASGCUpcomingReminderStatus.Off(campaign.id.toLongOrZero())
            _asgcReminderButtonStatus.value = Success(FeedAsgcCampaignResponseModel(rowNumber = rowNumber, campaignId = campaign.id.toLongOrZero(), reminderStatus = reminderStatusRes))
        }) {
            _asgcReminderButtonStatus.value = Fail(it)
        }
    }

    suspend fun subscribeUpcomingCampaign(campaignId: Long, reminderType: FeedASGCUpcomingReminderStatus): Pair<Boolean, String> = withContext(baseDispatcher.io)  {
        val response = postUpcomingCampaignReminderUseCase.apply {
            setRequestParams(PostUpcomingCampaignReminderUseCase.createParam(campaignId, reminderType).parameters)
        }.executeOnBackground()
        return@withContext Pair(response.response.success, if(response.response.errorMessage.isNotEmpty()) response.response.errorMessage else response.response.message)
    }


    fun getFeedFirstPage() {
        pagingHandler.resetPage()
        currentCursor = ""
        launchCatchError(block = {
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
        }) {
            getFeedFirstPageResp.value = Fail(it)
        }
    }

    fun getFeedNextPage() {
        pagingHandler.nextPage()
        if (currentCursor.isEmpty()) {
            return
        }
        launchCatchError(block = {
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
        }) {
            getFeedNextPageResp.value = Fail(it)
        }
    }

    fun doFavoriteShop(promotedShopViewModel: Data, adapterPosition: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                doFavoriteShopResult(promotedShopViewModel)
            }
            results.adapterPosition = adapterPosition
            doFavoriteShopResp.value = Success(results)
        }) {
            doFavoriteShopResp.value = Fail(it)
        }
    }

    fun doFollowKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                followKol(id, rowNumber)
            }
            followKolResp.value = Success(results)
        }) {
            followKolResp.value = Fail(Exception(ERROR_FOLLOW_MESSAGE))
        }
    }

    fun doUnfollowKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                unfollowKol(id, rowNumber)
            }
            followKolResp.value = Success(results)
        }) {
            followKolResp.value = Fail(Exception(ERROR_UNFOLLOW_MESSAGE))
        }
    }

    fun doLikeKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                likeKol(id, rowNumber)
            }
            likeKolResp.value = Success(results)
        }) {
            likeKolResp.value = Fail(it)
        }
    }

    fun doUnlikeKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                unlikeKol(id, rowNumber)
            }
            likeKolResp.value = Success(results)
        }) {
            likeKolResp.value = Fail(it)
        }
    }

    fun doFollowKolFromRecommendation(id: Int, rowNumber: Int, position: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                followKolFromRecom(id, rowNumber, position)
            }
            followKolRecomResp.value = Success(results)
        }) {
            followKolRecomResp.value = Fail(it)
        }
    }

    fun doUnfollowKolFromRecommendation(id: Int, rowNumber: Int, position: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                unfollowKolFromRecom(id, rowNumber, position)
            }
            followKolRecomResp.value = Success(results)
        }) {
            followKolRecomResp.value = Fail(it)
        }
    }

    fun doDeletePost(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                deletePost(id, rowNumber)
            }
            deletePostResp.value = Success(results)
        }) {
            deletePostResp.value = Fail(it)
        }
    }

    fun doAtc(postTagItem: FeedXProduct, shopId: String, type: String, isFollowed: Boolean, activityId: String) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                atc(postTagItem, shopId, type, isFollowed, activityId)
            }
            atcResp.value = Success(results)
        }) {
            atcResp.value = Fail(it)
        }
    }

    fun doTrackAffiliate(url: String) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                trackAffiliate(url)
            }
            trackAffiliateResp.value = Success(results)
        }) {
        }
    }

    fun doToggleFavoriteShop(
        rowNumber: Int,
        adapterPosition: Int,
        shopId: String,
        follow: Boolean = true,
        isUnfollowFromBottomSheetMenu: Boolean = false
    ) {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                toggleFavoriteShop(rowNumber, adapterPosition, shopId, isUnfollowFromBottomSheetMenu)
            }
            toggleFavoriteShopResp.value = Success(results)
        }) {
            if (follow)
                toggleFavoriteShopResp.value = Fail(CustomUiMessageThrowable(R.string.feed_unfollow_error_message))
            else
                toggleFavoriteShopResp.value = Fail(Exception(ERROR_FOLLOW_MESSAGE))

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

        launchCatchError(block = {
            val newCarouselModel = processPlayWidget(isAutoRefresh = true)
            _playWidgetModel.value = Success(newCarouselModel)
        }, onError = {
            _playWidgetModel.value = Fail(it)
        })
    }

    fun addWishlist(
        activityId: String,
        productId: String,
        shopId: String,
        position: Int,
        type: String,
        isFollowed: Boolean,
        onFail: (String) -> Unit,
        onSuccess: (String, String, String, Boolean) -> Unit,
        context: Context
    ) {
        addWishListUseCase.createObservable(
            productId, userSession.userId,
            object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                    onFail.invoke(errorMessage ?: ERROR_CUSTOM_MESSAGE)
                }

                override fun onSuccessAddWishlist(productId: String?) {
                    if (productId != null) {
                        onSuccess.invoke(activityId, shopId, type, isFollowed)
                    }
                }

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}

                override fun onSuccessRemoveWishlist(productId: String?) {}

            })
    }

    fun addWishlistV2(
        activityId: String,
        productId: String,
        shopId: String,
        position: Int,
        type: String,
        isFollowed: Boolean,
        onFail: (String) -> Unit,
        onSuccess: (String, String, String, Boolean, AddToWishlistV2Response.Data.WishlistAddV2) -> Unit,
        context: Context
    ) {
        launch(baseDispatcher.main) {
            addToWishlistV2UseCase.setParams(productId, userSession.userId)
            val result = withContext(baseDispatcher.io) { addToWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                onSuccess.invoke(activityId, shopId, type, isFollowed, result.data)
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
                val whiteListModel = getWhitelistNewUseCase.execute(type = WHITELIST_INTEREST)
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
            return getDynamicFeedNewUseCase.execute(cursor = currentCursor)
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    private fun doFavoriteShopResult(promotedShopViewModel: Data): FeedPromotedShopViewModel {
        try {
            val result = FeedPromotedShopViewModel()
            val params =
                ToggleFavouriteShopUseCase.createRequestParam(promotedShopViewModel.shop.id)

            params.putString(PARAM_SHOP_DOMAIN, promotedShopViewModel.shop.domain)
            params.putString(PARAM_SRC, DEFAULT_VALUE_SRC)
            params.putString(PARAM_AD_KEY, promotedShopViewModel.adRefKey)
            val requestSuccess =
                doFavoriteShopUseCase.createObservable(params).toBlocking().single()
            result.isSuccess = requestSuccess
            result.promotedShopViewModel = promotedShopViewModel
            return result
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun followKol(id: Int, rowNumber: Int): FollowKolViewModel {
        try {
            val data = FollowKolViewModel()
            data.id = id
            data.rowNumber = rowNumber
            data.status = FollowKolPostGqlUseCase.PARAM_FOLLOW
            followKolPostGqlUseCase.clearRequest()
            val params = FollowKolPostGqlUseCase.getParam(id, FollowKolPostGqlUseCase.PARAM_FOLLOW)
            val response = followKolPostGqlUseCase.createObservable(params).toBlocking().single()

            val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
            if (query.data != null) {
                val followKolDomain = FollowKolDomain(query.data.data.status)
                if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) data.isSuccess =
                    true
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun unfollowKol(id: Int, rowNumber: Int): FollowKolViewModel {
        try {
            val data = FollowKolViewModel()
            data.id = id
            data.rowNumber = rowNumber
            data.status = FollowKolPostGqlUseCase.PARAM_UNFOLLOW
            followKolPostGqlUseCase.clearRequest()
            val params =
                FollowKolPostGqlUseCase.getParam(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
            val response = followKolPostGqlUseCase.createObservable(params).toBlocking().single()

            val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
            if (query.data != null) {
                val followKolDomain = FollowKolDomain(query.data.data.status)
                if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) data.isSuccess =
                    true
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun likeKol(id: Int, rowNumber: Int): LikeKolViewModel {
        try {
            val data = LikeKolViewModel()
            data.id = id
            data.rowNumber = rowNumber
            val params = LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Like)
            val isSuccess = likeKolPostUseCase.createObservable(params).toBlocking().first()
            data.isSuccess = isSuccess
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun unlikeKol(id: Int, rowNumber: Int): LikeKolViewModel {
        try {
            val data = LikeKolViewModel()
            data.id = id
            data.rowNumber = rowNumber
            val params =
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Unlike)
            val isSuccess = likeKolPostUseCase.createObservable(params).toBlocking().first()
            data.isSuccess = isSuccess
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun followKolFromRecom(id: Int, rowNumber: Int, position: Int): FollowKolViewModel {
        try {
            val data = FollowKolViewModel()
            data.status = FollowKolPostGqlUseCase.PARAM_FOLLOW
            data.position = position
            data.rowNumber = rowNumber
            val params = FollowKolPostGqlUseCase.getParam(id, data.status)
            followKolPostGqlUseCase.clearRequest()
            followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, data.status))
            val response = followKolPostGqlUseCase.createObservable(params).toBlocking().single()

            val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
            if (query.data != null) {
                val followKolDomain = FollowKolDomain(query.data.data.status)
                if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) {
                    data.isSuccess = true
                    data.isFollow = true
                }
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun unfollowKolFromRecom(id: Int, rowNumber: Int, position: Int): FollowKolViewModel {
        try {
            val data = FollowKolViewModel()
            data.status = FollowKolPostGqlUseCase.PARAM_UNFOLLOW
            data.position = position
            data.rowNumber = rowNumber
            val params = FollowKolPostGqlUseCase.getParam(id, data.status)
            followKolPostGqlUseCase.clearRequest()
            followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, data.status))
            val response = followKolPostGqlUseCase.createObservable(params).toBlocking().single()

            val query = response.getData<FollowKolQuery>(FollowKolQuery::class.java)
            if (query.data != null) {
                val followKolDomain = FollowKolDomain(query.data.data.status)
                if (followKolDomain.status == FollowKolPostGqlUseCase.SUCCESS_STATUS) {
                    data.isSuccess = true
                    data.isFollow = false
                }
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun deletePost(id: Int, rowNumber: Int): DeletePostViewModel {
        try {
            val data = DeletePostViewModel()
            data.id = id
            data.rowNumber = rowNumber
            val params = DeletePostUseCase.createRequestParams(id.toString())
            val isSuccess = deletePostUseCase.createObservable(params).toBlocking().first()
            data.isSuccess = isSuccess
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun atc(
        postTagItem: FeedXProduct,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        activityId: String
    ): AtcViewModel {
        try {
            val data = AtcViewModel()
            data.applink = postTagItem.appLink
            data.activityId = activityId
            data.postType = type
            data.isFollowed = isFollowed
            data.shopId = shopId

            val params = AddToCartUseCase.getMinimumParams(
                postTagItem.id,
                shopId,
                productName = postTagItem.name,
                price = postTagItem.price.toString(),
                userId = userId
            )
            val result = atcUseCase.createObservable(params).toBlocking().single()
            data.isSuccess = result.data.success == 1
            if (result.isStatusError()) {
                data.errorMsg = result.errorMessage.firstOrNull() ?: ""
            }
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun toggleFavoriteShop(
        rowNumber: Int,
        adapterPosition: Int,
        shopId: String,
        isUnfollowClickedFromBottomSheetMenu: Boolean = false
    ): FavoriteShopViewModel {
        try {
            val data = FavoriteShopViewModel()
            data.rowNumber = rowNumber
            data.adapterPosition = adapterPosition
            data.shopId = shopId
            data.isUnfollowFromShopsMenu = isUnfollowClickedFromBottomSheetMenu
            val params = ToggleFavouriteShopUseCase.createRequestParam(shopId)
            val isSuccess = doFavoriteShopUseCase.createObservable(params).toBlocking().first()
            data.isSuccess = isSuccess
            return data
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun trackAffiliate(url: String): TrackAffiliateViewModel {
        try {
            val data = TrackAffiliateViewModel()
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
        return model.postList.any { it is CarouselPlayCardViewModel }
    }

    private suspend fun processPlayWidget(isAutoRefresh: Boolean = false): CarouselPlayCardViewModel {
        val response = playWidgetTools.getWidgetFromNetwork(
            widgetType = PlayWidgetUseCase.WidgetType.Feeds,
            coroutineContext = baseDispatcher.io
        )
        val uiModel = playWidgetTools.mapWidgetToModel(response)
        return CarouselPlayCardViewModel(uiModel, isAutoRefresh)
    }
}