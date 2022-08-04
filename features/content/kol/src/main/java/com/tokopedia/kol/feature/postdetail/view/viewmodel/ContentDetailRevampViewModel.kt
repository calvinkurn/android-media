package com.tokopedia.kol.feature.postdetail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.domain.usecase.FeedXTrackViewerUseCase
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.AtcViewModel
import com.tokopedia.kol.common.util.ContentDetailResult
import com.tokopedia.kol.feature.postdetail.data.FeedXPostRecommendation
import com.tokopedia.kol.feature.postdetail.data.FeedXPostRecommendationData
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailUseCase
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetRecommendationPostUseCase
import com.tokopedia.kol.feature.postdetail.domain.mapper.ContentDetailMapper
import com.tokopedia.kol.feature.postdetail.view.datamodel.*
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.NON_LOGIN_USER_ID
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction
import com.tokopedia.kolcommon.view.viewmodel.ViewsKolModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ContentDetailRevampViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val getRecommendationPostUseCase: GetRecommendationPostUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val feedXTrackViewerUseCase: FeedXTrackViewerUseCase,
    private val repository: ContentDetailRepository,
    private val mapper: ContentDetailMapper,
): BaseViewModel(dispatcher.main) {

    var currentCursor = ""

    private val _getCDPPostRecomData = MutableLiveData<Result<FeedXPostRecommendation>>()
    private val _getCDPPostFirstPostData = MutableLiveData<Result<ContentDetailRevampDataUiModel>>()
    private val _likeKolResp = MutableLiveData<ContentDetailResult<LikeContentModel>>()
    private val _followShopObservable = MutableLiveData<ContentDetailResult<ShopFollowModel>>()
    private val _longVideoViewTrackResponse = MutableLiveData<Result<ViewsKolModel>>()
    private val _trackVodVisitChannelData = MutableLiveData<ContentDetailResult<VisitChannelModel>>()
    private val _atcResp = MutableLiveData<Result<AtcViewModel>>()
    private val _reportResponse = MutableLiveData<ContentDetailResult<ReportContentModel>>()
    private val _deletePostResp = MutableLiveData<ContentDetailResult<DeleteContentModel>>()

    val cDPPostRecomData: LiveData<Result<FeedXPostRecommendation>>
        get() = _getCDPPostRecomData

    val getCDPPostFirstPostData: LiveData<Result<ContentDetailRevampDataUiModel>>
        get() = _getCDPPostFirstPostData

    val getLikeKolResp: LiveData<ContentDetailResult<LikeContentModel>>
        get() = _likeKolResp

    val followShopObservable: LiveData<ContentDetailResult<ShopFollowModel>>
        get() = _followShopObservable

    val longVideoViewTrackResponse: LiveData<Result<ViewsKolModel>>
       get() = _longVideoViewTrackResponse

    val vodViewData: LiveData<ContentDetailResult<VisitChannelModel>>
        get() = _trackVodVisitChannelData

    val atcRespData: LiveData<Result<AtcViewModel>>
        get() = _atcResp

    val reportResponse: LiveData<ContentDetailResult<ReportContentModel>>
         get() = _reportResponse

    val deletePostResp: LiveData<ContentDetailResult<DeleteContentModel>>
         get() = _deletePostResp

    val observableWishlist: LiveData<Result<AddToWishlistV2Response.Data.WishlistAddV2>>
        get() = _observableWishlist
    private val _observableWishlist = MutableLiveData<Result<AddToWishlistV2Response.Data.WishlistAddV2>>()

    private val userId: String
            get() = if (userSession.isLoggedIn) userSession.userId else NON_LOGIN_USER_ID

    fun getCDPPostDetailFirstData(id: String){
        launchCatchError( block = {
            val results = withContext(dispatcher.io) {
                getFeedDataResult(id)
            }
            _getCDPPostFirstPostData.value = Success(results)

        }) {
            _getCDPPostFirstPostData.value = Fail(it)

        }
    }

    fun getCDPRecomData(id: String){
        launchCatchError(block = {
            val results = withContext(dispatcher.io) {
                getCDPRecomDataResult(id)
            }
            currentCursor = results.feedXPostRecommendation.nextCursor

            _getCDPPostRecomData.value = Success(results.feedXPostRecommendation)

        }) {
            _getCDPPostRecomData.value = Fail(it)
        }

    }
    private suspend fun getFeedDataResult(detailId: String): ContentDetailRevampDataUiModel {
        try {
            return getPostDetailUseCase.executeForCDPRevamp(cursor = "", detailId = detailId)
        } catch (e: Throwable) {
            Timber.e(e)
            throw e
        }
    }

    private suspend fun getCDPRecomDataResult(activityId: String): FeedXPostRecommendationData {
        try {
            return getRecommendationPostUseCase.execute(cursor = currentCursor, activityId = activityId )
        } catch (e: Throwable) {
            Timber.e(e)
            throw e
        }
    }

    fun likeContent(contentId: String, action: ContentLikeAction, rowNumber: Int) {
        _likeKolResp.value = ContentDetailResult.Loading
        launchCatchError(block = {
            repository.likeContent(contentId, action)
            _likeKolResp.value = ContentDetailResult.Success(
                mapper.mapLikeContent(rowNumber, action)
            )
        }) {
            _likeKolResp.value = ContentDetailResult.Failure(it)
        }
    }

    fun followShop(shopId: String, action: ShopFollowAction, rowNumber: Int) {
        launchCatchError(block = {
            repository.followShop(shopId, action)
            _followShopObservable.value = ContentDetailResult.Success(
                mapper.mapShopFollow(rowNumber, action)
            )
        }) {
            _followShopObservable.value = ContentDetailResult.Failure(it) {
                followShop(shopId, action, rowNumber)
            }
        }
    }

    fun trackVisitChannel(channelId: String, rowNumber: Int) {
        _trackVodVisitChannelData.value = ContentDetailResult.Loading
        launchCatchError(block = {
            repository.trackVisitChannel(channelId)
            _trackVodVisitChannelData.value = ContentDetailResult.Success(
                mapper.mapVisitChannel(rowNumber)
            )
        }) {
            _trackVodVisitChannelData.value = ContentDetailResult.Failure(it)
        }
    }

    fun trackLongVideoView(activityId: String, rowNumber: Int) {

        launchCatchError(dispatcher.io, block = {
            feedXTrackViewerUseCase.setRequestParams(FeedXTrackViewerUseCase.createParams(activityId))
            val trackResponse = feedXTrackViewerUseCase.executeOnBackground()
            val data = ViewsKolModel()
            data.rowNumber = rowNumber
            data.isSuccess = trackResponse.feedXTrackViewerResponse.success
            _longVideoViewTrackResponse.postValue(Success(data))
        }) {
            _longVideoViewTrackResponse.postValue(Fail(it))
        }
    }

    fun addToCart(
        productId: String,
        productName: String,
        price: String,
        shopId: String,
        appLink: String,
    ) {
        launchCatchError(block = {
            val isSuccess = repository.addToCart(productId, productName, price, shopId)
            val atcModel = AtcViewModel(
                isSuccess = isSuccess,
                applink = appLink
            )
            _atcResp.value = Success(atcModel)
        }) {
            _atcResp.value = Fail(it)
        }
    }

    fun addToWishlist(productId: String) {
        launch {
            val response = repository.addToWishlist(productId)
            _observableWishlist.value = response
        }
    }

    fun sendReport(
        positionInFeed: Int,
        contentId: String,
        reasonType: String,
        reasonMessage: String,
    ) {
        launchCatchError(block = {
            repository.reportContent(contentId, reasonType, reasonMessage)
            _reportResponse.value = ContentDetailResult.Success(
                mapper.mapReportContent(positionInFeed)
            )
        }) {
            _reportResponse.value = ContentDetailResult.Failure(it) {
                sendReport(positionInFeed, contentId, reasonType, reasonMessage)
            }
        }
    }

    fun deleteContent(contentId: String, rowNumber: Int) {
        _deletePostResp.value = ContentDetailResult.Loading
        launchCatchError(block = {
            repository.deleteContent(contentId)
            _deletePostResp.value = ContentDetailResult.Success(
                mapper.mapDeleteContent(rowNumber)
            )
        }) {
            _deletePostResp.value = ContentDetailResult.Failure(it) {
                deleteContent(contentId, rowNumber)
            }
        }
    }
}