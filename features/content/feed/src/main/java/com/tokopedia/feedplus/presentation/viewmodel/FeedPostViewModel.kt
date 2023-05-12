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
import com.tokopedia.createpost.common.domain.entity.SubmitPostData
import com.tokopedia.feed.component.product.FeedTaggedProductUiModel
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TOPADS_HEADLINE_VALUE_SRC
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.domain.mapper.MapperTopAdsXFeed.transformCpmToFeedTopAds
import com.tokopedia.feedplus.domain.usecase.FeedCampaignCheckReminderUseCase
import com.tokopedia.feedplus.domain.usecase.FeedCampaignReminderUseCase
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.model.*
import com.tokopedia.feedplus.presentation.util.common.FeedLikeAction
import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase
import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 23/02/23
 */
class FeedPostViewModel @Inject constructor(
    private val feedXHomeUseCase: FeedXHomeUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val likeContentUseCase: SubmitLikeContentUseCase,
    private val deletePostUseCase: SubmitActionContentUseCase,
    private val userSession: UserSessionInterface,
    private val shopFollowUseCase: ShopFollowUseCase,
    private val userFollowUseCase: ProfileFollowUseCase,
    private val setCampaignReminderUseCase: FeedCampaignReminderUseCase,
    private val checkCampaignReminderUseCase: FeedCampaignCheckReminderUseCase,
    private val topAdsHeadlineUseCase: GetTopAdsHeadlineUseCase,
    private val mvcSummaryUseCase: MVCSummaryUseCase,
    private val topAdsAddressHelper: TopAdsAddressHelper,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val _feedHome = MutableLiveData<Result<FeedModel>>()
    val feedHome: LiveData<Result<FeedModel>>
        get() = _feedHome

    private val _followResult = MutableLiveData<Result<String>>()
    val followResult: LiveData<Result<String>>
        get() = _followResult

    private val _likeKolResp = MutableLiveData<FeedResult<LikeFeedDataModel>>()
    val getLikeKolResp: LiveData<FeedResult<LikeFeedDataModel>>
        get() = _likeKolResp

    private val _deletePostResult = MutableLiveData<Result<Int>>()
    val deletePostResult: LiveData<Result<Int>>
        get() = _deletePostResult

    private val _suspendedFollowData = MutableLiveData<FollowShopModel>()
    private val _suspendedLikeData = MutableLiveData<LikeFeedDataModel>()

    private var cursor = ""
    private var currentTopAdsPage = 0
    private var shouldFetchTopAds = true

    private var _shouldShowNoMoreContent = false
    val shouldShowNoMoreContent: Boolean
        get() = _shouldShowNoMoreContent

    fun fetchFeedPosts(
        source: String,
        isNewData: Boolean = false,
        postId: String? = null
    ) {
        _shouldShowNoMoreContent = false
        if (isNewData) _feedHome.value = null

        viewModelScope.launch {
            if (cursor == "" || cursor != _feedHome.value?.cursor.orEmpty()) {
                shouldFetchTopAds = true
                cursor = _feedHome.value?.cursor.orEmpty()

                val relevantPostsDeferred = async {
                    try {
                        requireNotNull(postId)
                        require(isNewData)

                        getRelevantPosts(postId)
                    } catch (e: Throwable) {
                        FeedModel.Empty
                    }.items
                }

                val feedPostsDeferred = async {
                    try {
                        Success(
                            getFeedPosts(
                                source = source,
                                cursor = _feedHome.value?.cursor.orEmpty()
                            )
                        )
                    } catch (e: Throwable) {
                        Fail(e)
                    }
                }

                val feedPosts = feedPostsDeferred.await()
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

                        _shouldShowNoMoreContent =
                            _feedHome.value?.items.orEmpty().isNotEmpty() &&
                                items.isEmpty()

                        Success(
                            data = feedPosts.data.copy(
                                items = relevantPostsDeferred.await() +
                                    _feedHome.value?.items.orEmpty() +
                                    items
                            )
                        )
                    }
                    else -> feedPosts
                }
            }
        }
    }

    fun setUnsetReminder(
        campaignId: Long,
        setReminder: Boolean
    ) {
        viewModelScope.launch {
            try {
                val response = setCampaignReminderUseCase(
                    setCampaignReminderUseCase.createParams(
                        campaignId,
                        setReminder
                    )
                )

                if (response.success && response.errorMessage.isEmpty()) {
                    _feedHome.value?.let {
                        if (it is Success) {
                            val newData = it.data.items.map { item ->
                                when {
                                    item is FeedCardImageContentModel && item.campaign.id == campaignId.toString() ->
                                        item.copy(
                                            campaign = item.campaign.copy(
                                                isReminderActive = setReminder
                                            )
                                        )
                                    item is FeedCardVideoContentModel && item.campaign.id == campaignId.toString() ->
                                        item.copy(
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
                }
            } catch (e: Throwable) {
            }
        }
    }

    fun fetchTopAdsData() {
        viewModelScope.launch {
            feedHome.value?.let {
                if (it is Success && shouldFetchTopAds) {
                    shouldFetchTopAds = false

                    val defaultTopAdsUrlParams: MutableMap<String, Any> = getTopAdsParams()
                    val topAdsAddressData = topAdsAddressHelper.getAddressData()
                    val indexToRemove = mutableListOf<Int>()

                    val newItems = it.data.items.mapIndexed { index, item ->
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
                                        indexToRemove.add(index)
                                        item.copy(isFetched = true)
                                    }
                                }
                                topAdsDeferred.await()
                            }
                            else -> item
                        }
                    }.toMutableList()

                    indexToRemove.forEach { indexNumber ->
                        newItems.removeAt(indexNumber)
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

    fun suspendFollow(id: String, encryptedId: String, isShop: Boolean) {
        _suspendedFollowData.value =
            FollowShopModel(
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
                val response = withContext(dispatchers.io) {
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

                updateFollowStatus(response.id, response.isFollowing)
                _followResult.value = Success(if (isShop) SHOP else USER)
            } catch (it: Throwable) {
                _followResult.value = Fail(it)
            }
        }
    }

    fun suspendLikeContent(contentId: String, rowNumber: Int) {
        _suspendedLikeData.value = LikeFeedDataModel(
            contentId,
            rowNumber,
            FeedLikeAction.Like,
        )
    }

    fun processSuspendedLike() {
        _suspendedLikeData.value?.let {
            likeContent(it.contentId, it.action, it.rowNumber)
        }
    }

    fun likeContent(contentId: String, action: FeedLikeAction, rowNumber: Int) {
        _likeKolResp.value = FeedResult.Loading
        viewModelScope.launch {
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

            } catch (t: Throwable) {
                _deletePostResult.value = Fail(t)
            }
        }
    }

    private suspend fun getRelevantPosts(postId: String): FeedModel {
        return feedXHomeUseCase(
            feedXHomeUseCase.createPostDetailParams(postId)
        )
    }

    private suspend fun getFeedPosts(
        source: String,
        cursor: String = ""
    ): FeedModel {
        return feedXHomeUseCase(
            feedXHomeUseCase.createParams(
                source,
                cursor
            )
        )
    }

    private suspend fun getCampaignReminderStatus(campaignId: Long): Boolean =
        try {
            checkCampaignReminderUseCase(
                checkCampaignReminderUseCase.createParams(campaignId)
            ).isAvailable
        } catch (e: Throwable) {
            false
        }

    private fun mapLikeResponse(contentId: String, likeAction: FeedLikeAction, rowNumber: Int) =
        LikeFeedDataModel(
            contentId = contentId,
            rowNumber = rowNumber,
            action = likeAction
        )

    private fun updateFollowStatus(id: String, isFollowing: Boolean) {
        val currentValue = feedHome.value

        currentValue?.let {
            when (it) {
                is Success -> {
                    _feedHome.value = Success(
                        it.data.copy(
                            items = it.data.items.map { item ->
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

    private fun getTopAdsParams(): MutableMap<String, Any> =
        mutableMapOf(
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
                _observeAddProductToCart.value = Fail(ResponseErrorException(response.getAtcErrorMessage()))
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
                _observeBuyProduct.value = Fail(ResponseErrorException(response.getAtcErrorMessage()))
            } else {
                _observeBuyProduct.value = Success(response)
            }
        }) {
            _observeBuyProduct.value = Fail(it)
        }
    }

    private suspend fun addToCart(product: FeedTaggedProductUiModel) = withContext(dispatchers.io) {
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
    private val _merchantVoucherLiveData =
        MutableLiveData<Result<TokopointsCatalogMVCSummary>>()
    val merchantVoucherLiveData: LiveData<Result<TokopointsCatalogMVCSummary>>
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
                if (result.resultStatus?.code == "200") {
                    _merchantVoucherLiveData.value = Success(result)
                } else {
                    _merchantVoucherLiveData.value = Fail(
                        ResponseErrorException(response.data?.resultStatus?.message?.firstOrNull().orEmpty())
                    )
                }
            }
        }) {
            _merchantVoucherLiveData.value = Fail(it)
        }
    }

    private val Result<FeedModel>.cursor: String
        get() = when (this) {
            is Success -> data.pagination.cursor
            else -> ""
        }

    private val Result<FeedModel>.items: List<Visitable<FeedAdapterTypeFactory>>
        get() = when (this) {
            is Success -> data.items
            else -> emptyList()
        }

    companion object {
        private const val SHOP = "toko"
        private const val USER = "akun"

        private const val FOLLOWING = "following"

        private const val FOLLOWING_TYPE = "type"
    }
}
