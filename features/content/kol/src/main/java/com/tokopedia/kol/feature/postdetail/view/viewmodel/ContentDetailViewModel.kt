package com.tokopedia.kol.feature.postdetail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kol.common.util.ContentDetailResult
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.domain.mapper.ContentDetailMapper
import com.tokopedia.kol.feature.postdetail.view.datamodel.*
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContentDetailViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val repository: ContentDetailRepository,
    private val mapper: ContentDetailMapper,
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

    val observableWishlist: LiveData<Result<AddToWishlistV2Response.Data.WishlistAddV2>>
        get() = _observableWishlist
    private val _observableWishlist = MutableLiveData<Result<AddToWishlistV2Response.Data.WishlistAddV2>>()

    fun getContentDetail(contentId: String){
        launchCatchError( block = {
            val results = repository.getContentDetail(contentId)
            _getCDPPostFirstPostData.value = Success(results)
        }) {
            _getCDPPostFirstPostData.value = Fail(it)
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