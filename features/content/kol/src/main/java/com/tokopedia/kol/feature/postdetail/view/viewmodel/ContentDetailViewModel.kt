package com.tokopedia.kol.feature.postdetail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.data.feedrevamp.FeedASGCUpcomingReminderStatus
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.domain.usecase.CheckUpcomingCampaignReminderUseCase
import com.tokopedia.feedcomponent.domain.usecase.PostUpcomingCampaignReminderUseCase
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FeedAsgcCampaignResponseModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FeedWidgetData
import com.tokopedia.kol.common.util.ContentDetailResult
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.view.datamodel.*
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContentDetailViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val repository: ContentDetailRepository,
): BaseViewModel(dispatcher.main) {

    var currentCursor = ""

    private val _getCDPPostRecomData = MutableLiveData<Result<ContentDetailUiModel>>()
    private val _getCDPPostFirstPostData = MutableLiveData<Result<ContentDetailUiModel>>()
    private val _likeKolResp = MutableLiveData<ContentDetailResult<LikeContentModel>>()
    private val _followShopObservable = MutableLiveData<ContentDetailResult<ShopFollowModel>>()
    private val _trackVodVisitContentData = MutableLiveData<ContentDetailResult<VisitContentModel>>()
    private val _atcResp = MutableLiveData<ContentDetailResult<Boolean>>()
    private val _reportResponse = MutableLiveData<ContentDetailResult<ReportContentModel>>()
    private val _deletePostResp = MutableLiveData<ContentDetailResult<DeleteContentModel>>()
    private val _asgcReminderButtonInitialStatus = MutableLiveData<ContentDetailResult<FeedAsgcCampaignResponseModel>>()
    private val _asgcReminderButtonStatus = MutableLiveData<ContentDetailResult<FeedAsgcCampaignResponseModel>>()
    private val _feedWidgetLatestData = MutableLiveData<Result<FeedWidgetData>>()


    val cDPPostRecomData: LiveData<Result<ContentDetailUiModel>>
        get() = _getCDPPostRecomData

    val getCDPPostFirstPostData: LiveData<Result<ContentDetailUiModel>>
        get() = _getCDPPostFirstPostData

    val getLikeKolResp: LiveData<ContentDetailResult<LikeContentModel>>
        get() = _likeKolResp

    val followShopObservable: LiveData<ContentDetailResult<ShopFollowModel>>
        get() = _followShopObservable

    val vodViewData: LiveData<ContentDetailResult<VisitContentModel>>
        get() = _trackVodVisitContentData

    val atcRespData: LiveData<ContentDetailResult<Boolean>>
        get() = _atcResp

    val reportResponse: LiveData<ContentDetailResult<ReportContentModel>>
         get() = _reportResponse

    val deletePostResp: LiveData<ContentDetailResult<DeleteContentModel>>
         get() = _deletePostResp

    val observableWishlist: LiveData<ContentDetailResult<WishlistContentModel>>
        get() = _observableWishlist
    private val _observableWishlist = MutableLiveData<ContentDetailResult<WishlistContentModel>>()

    val asgcReminderButtonInitialStatus: LiveData<ContentDetailResult<FeedAsgcCampaignResponseModel>>
        get() = _asgcReminderButtonInitialStatus

    val asgcReminderButtonStatus: LiveData<ContentDetailResult<FeedAsgcCampaignResponseModel>>
        get() = _asgcReminderButtonStatus

    val feedWidgetLatestData: LiveData<Result<FeedWidgetData>>
        get() = _feedWidgetLatestData


    fun fetchLatestFeedPostWidgetData(detailId: String, rowNumber: Int) {
        launchCatchError(block = {
            val response = repository.getContentDetail(detailId)
            if (response.postList.isNotEmpty()) {
                val updatedData = FeedWidgetData(
                    rowNumber = rowNumber,
                    feedXCard = response.postList.first()
                )
                _feedWidgetLatestData.value = Success(updatedData)
            }
        }) {
            _feedWidgetLatestData.value = Fail(it)
        }
    }

    fun getContentDetail(contentId: String){
        launchCatchError( block = {
            val results = repository.getContentDetail(contentId)
            _getCDPPostFirstPostData.value = Success(results)
        }) {
            _getCDPPostFirstPostData.value = Fail(it)
        }
    }

    fun checkUpcomingCampaignInitialReminderStatus(campaign: FeedXCampaign, rowNumber: Int) {
       launchCatchError(block = {
            val data = repository.checkUpcomingCampaign(campaignId = campaign.campaignId)
            val reminderStatusRes = if (data) FeedASGCUpcomingReminderStatus.On(campaign.campaignId) else FeedASGCUpcomingReminderStatus.Off(campaign.campaignId)
            _asgcReminderButtonInitialStatus.value = ContentDetailResult.Success(FeedAsgcCampaignResponseModel(rowNumber = rowNumber, campaignId = campaign.campaignId, reminderStatus = reminderStatusRes))
        }) {
            _asgcReminderButtonInitialStatus.value = ContentDetailResult.Failure(it)
        }
    }
    fun setUnsetReminder(campaign: FeedXCampaign, rowNumber: Int) {
       launchCatchError(block = {
            val data = repository.subscribeUpcomingCampaign(
                campaignId = campaign.campaignId,
                reminderType = campaign.reminder
            )
            val reminderStatusRes = if (data.first) FeedASGCUpcomingReminderStatus.On(campaign.campaignId) else FeedASGCUpcomingReminderStatus.Off(campaign.campaignId)
            _asgcReminderButtonStatus.value = ContentDetailResult.Success(FeedAsgcCampaignResponseModel(rowNumber = rowNumber, campaignId = campaign.campaignId, reminderStatus = reminderStatusRes))
        }) {
            _asgcReminderButtonStatus.value = ContentDetailResult.Failure(it)
        }
    }

    fun getContentDetailRecommendation(activityId: String){
        launchCatchError(block = {
            val result = repository.getContentRecommendation(activityId, currentCursor)
            currentCursor = result.cursor
            _getCDPPostRecomData.value = Success(result)
        }) {
            _getCDPPostRecomData.value = Fail(it)
        }
    }

    fun likeContent(contentId: String, action: ContentLikeAction, rowNumber: Int) {
        _likeKolResp.value = ContentDetailResult.Loading
        launchCatchError(block = {
            val response = repository.likeContent(contentId, action, rowNumber)
            _likeKolResp.value = ContentDetailResult.Success(response)
        }) {
            _likeKolResp.value = ContentDetailResult.Failure(it)
        }
    }

    fun followShop(shopId: String, action: ShopFollowAction, rowNumber: Int) {
        launchCatchError(block = {
            val response = repository.followShop(shopId, action, rowNumber)
            _followShopObservable.value = ContentDetailResult.Success(response)
        }) {
            _followShopObservable.value = ContentDetailResult.Failure(it) {
                followShop(shopId, action, rowNumber)
            }
        }
    }

    fun trackVisitChannel(channelId: String, rowNumber: Int) {
        _trackVodVisitContentData.value = ContentDetailResult.Loading
        launchCatchError(block = {
            val response = repository.trackVisitChannel(channelId, rowNumber)
            _trackVodVisitContentData.value = ContentDetailResult.Success(response)
        }) {
            _trackVodVisitContentData.value = ContentDetailResult.Failure(it)
        }
    }

    fun trackLongVideoView(activityId: String, rowNumber: Int) {
        _trackVodVisitContentData.value = ContentDetailResult.Loading
        launchCatchError(block = {
            val response = repository.trackViewer(activityId, rowNumber)
            _trackVodVisitContentData.value = ContentDetailResult.Success(response)
        }) {
            _trackVodVisitContentData.value = ContentDetailResult.Failure(it)
        }
    }

    fun addToCart(
        productId: String,
        productName: String,
        price: String,
        shopId: String,
    ) {
        _atcResp.value = ContentDetailResult.Loading
        launchCatchError(block = {
            val isSuccess = repository.addToCart(productId, productName, price, shopId)
            _atcResp.value = ContentDetailResult.Success(isSuccess)
        }) {
            _atcResp.value = ContentDetailResult.Failure(it)
        }
    }

    fun addToWishlist(productId: String, rowNumber: Int) {
        launchCatchError(block = {
            val isSuccess = repository.addToWishlist(rowNumber, productId)
            _observableWishlist.value = ContentDetailResult.Success(isSuccess)
        }) {
            _observableWishlist.value = ContentDetailResult.Failure(it)
        }
    }

    fun sendReport(
        positionInFeed: Int,
        contentId: String,
        reasonType: String,
        reasonMessage: String,
    ) {
        launchCatchError(block = {
            val response = repository.reportContent(contentId, reasonType, reasonMessage, positionInFeed)
            _reportResponse.value = ContentDetailResult.Success(response)
        }) {
            _reportResponse.value = ContentDetailResult.Failure(it) {
                sendReport(positionInFeed, contentId, reasonType, reasonMessage)
            }
        }
    }

    fun deleteContent(contentId: String, rowNumber: Int) {
        _deletePostResp.value = ContentDetailResult.Loading
        launchCatchError(block = {
            val response = repository.deleteContent(contentId, rowNumber)
            _deletePostResp.value = ContentDetailResult.Success(response)
        }) {
            _deletePostResp.value = ContentDetailResult.Failure(it) {
                deleteContent(contentId, rowNumber)
            }
        }
    }
}