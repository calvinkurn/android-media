package com.tokopedia.kol.feature.postdetail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.feedcomponent.domain.usecase.FeedBroadcastTrackerUseCase
import com.tokopedia.feedcomponent.domain.usecase.FeedXTrackViewerUseCase
import com.tokopedia.feedcomponent.domain.usecase.SendReportUseCase
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.AtcViewModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.DeletePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FavoriteShopViewModel
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.postdetail.data.FeedXPostRecommendation
import com.tokopedia.kol.feature.postdetail.data.FeedXPostRecommendationData
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetPostDetailUseCase
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetRecommendationPostUseCase
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampArgumentModel.Companion.NON_LOGIN_USER_ID
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailRevampDataUiModel
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.view.viewmodel.LikeKolViewModel
import com.tokopedia.kolcommon.view.viewmodel.ViewsKolModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
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
    private val likeKolPostUseCase: LikeKolPostUseCase,
    private val doFavoriteShopUseCase: ToggleFavouriteShopUseCase,
    private val feedXTrackViewerUseCase: FeedXTrackViewerUseCase,
    private val trackVisitChannelBroadcasterUseCase: FeedBroadcastTrackerUseCase,
    private val sendReportUseCase: SendReportUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val repository: ContentDetailRepository,
): BaseViewModel(dispatcher.main) {

    var currentCursor = ""

    private val _getCDPPostRecomData = MutableLiveData<Result<FeedXPostRecommendation>>()
    private val _getCDPPostFirstPostData = MutableLiveData<Result<ContentDetailRevampDataUiModel>>()
    private val _likeKolResp = MutableLiveData<Result<LikeKolViewModel>>()
    private val _togglefollowUnfollowResp = MutableLiveData<Result<FavoriteShopViewModel>>()
    private val _longVideoViewTrackResponse = MutableLiveData<Result<ViewsKolModel>>()
    private val _trackVODViewsData = MutableLiveData<Result<ViewsKolModel>>()
    private val _atcResp = MutableLiveData<Result<AtcViewModel>>()
    private val _reportResponse = MutableLiveData<Result<DeletePostViewModel>>()
    private val _deletePostResp = MutableLiveData<Result<DeletePostViewModel>>()

    val cDPPostRecomData: LiveData<Result<FeedXPostRecommendation>>
        get() = _getCDPPostRecomData

    val getCDPPostFirstPostData: LiveData<Result<ContentDetailRevampDataUiModel>>
        get() = _getCDPPostFirstPostData

    val getLikeKolResp: LiveData<Result<LikeKolViewModel>>
        get() = _likeKolResp

    val toggleFollowUnfollowResp: LiveData<Result<FavoriteShopViewModel>>
        get() = _togglefollowUnfollowResp

    val longVideoViewTrackResponse: LiveData<Result<ViewsKolModel>>
       get() = _longVideoViewTrackResponse

    val vodViewData: LiveData<Result<ViewsKolModel>>
        get() = _trackVODViewsData

    val atcRespData: LiveData<Result<AtcViewModel>>
        get() = _atcResp

    val reportResponse: LiveData<Result<DeletePostViewModel>>
         get() = _reportResponse

    val deletePostResp: LiveData<Result<DeletePostViewModel>>
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
    fun doLikeKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(dispatcher.io) {
                likeKol(id, rowNumber)
            }
            _likeKolResp.value = Success(results)
        }) {
            _likeKolResp.value = Fail(it)
        }
    }

    fun doUnlikeKol(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(dispatcher.io) {
                unlikeKol(id, rowNumber)
            }
            _likeKolResp.value = Success(results)
        }) {
            _likeKolResp.value = Fail(it)
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
    fun doToggleFavoriteShop(
        rowNumber: Int,
        shopId: String,
        follow: Boolean = true,
        isUnfollowFromBottomSheetMenu: Boolean = false
    ) {
        launchCatchError(block = {
            val results = withContext(dispatcher.io) {
                toggleFavoriteShop(rowNumber, shopId, isUnfollowFromBottomSheetMenu)
            }
            _togglefollowUnfollowResp.value = Success(results)
        }) {
            if (follow)
                _togglefollowUnfollowResp.value = Fail(CustomUiMessageThrowable(R.string.feed_unfollow_error_message))
            else
                _togglefollowUnfollowResp.value = Fail(CustomUiMessageThrowable(R.string.feed_follow_error_message))
        }
        }

    private fun toggleFavoriteShop(
        rowNumber: Int,
        shopId: String,
        isUnfollowClickedFromBottomSheetMenu: Boolean = false
    ): FavoriteShopViewModel {
        try {
            val data = FavoriteShopViewModel()
            data.rowNumber = rowNumber
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

    fun trackVisitChannel(channelId: String,rowNumber: Int) {

        launchCatchError(dispatcher.io, block = {
            trackVisitChannelBroadcasterUseCase.setRequestParams(FeedBroadcastTrackerUseCase.createParams(channelId))
            val trackResponse = trackVisitChannelBroadcasterUseCase.executeOnBackground()
            val data = ViewsKolModel()
            data.rowNumber = rowNumber
            data.isSuccess = trackResponse.reportVisitChannelTracking.success
            _trackVODViewsData.postValue(Success(data))
        }) {
            _trackVODViewsData.postValue(Fail(it))
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

    fun doAddToCart(
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
                    _reportResponse.value = Success(deleteModel)
                } else {
                    _reportResponse.value = Fail(Exception(it.feedReportSubmit.errorMessage))
                }
            },
            {
                _reportResponse.value = Fail(it)
            }
        )
    }
    fun doDeletePost(id: Int, rowNumber: Int) {
        launchCatchError(block = {
            val results = withContext(dispatcher.io) {
                deletePost(id, rowNumber)
            }
            _deletePostResp.value = Success(results)
        }) {
            _deletePostResp.value = Fail(it)
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
}