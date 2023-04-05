package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
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
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.feedplus.presentation.model.FollowShopModel
import com.tokopedia.feedplus.presentation.model.LikeFeedDataModel
import com.tokopedia.feedplus.presentation.util.common.FeedLikeAction
import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
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
    private val userSession: UserSessionInterface,
    private val shopFollowUseCase: ShopFollowUseCase,
    private val userFollowUseCase: ProfileFollowUseCase,
    private val setCampaignReminderUseCase: FeedCampaignReminderUseCase,
    private val checkCampaignReminderUseCase: FeedCampaignCheckReminderUseCase,
    private val topAdsHeadlineUseCase: GetTopAdsHeadlineUseCase,
    private val topAdsAddressHelper: TopAdsAddressHelper,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val _feedHome = MutableLiveData<Result<FeedModel>>()
    val feedHome: LiveData<Result<FeedModel>>
        get() = _feedHome

    private val _atcResp = MutableLiveData<FeedResult<Boolean>>()
    val atcRespData: LiveData<FeedResult<Boolean>>
        get() = _atcResp

    private val _followResult = MutableLiveData<Result<String>>()
    val followResult: LiveData<Result<String>>
        get() = _followResult

    private val _likeKolResp = MutableLiveData<FeedResult<LikeFeedDataModel>>()
    val getLikeKolResp: LiveData<FeedResult<LikeFeedDataModel>>
        get() = _likeKolResp

    private val _suspendedFollowData = MutableLiveData<FollowShopModel>()
    private var cursor = ""
    private var currentTopAdsPage = 0

    fun fetchFeedPosts(
        source: String,
        isNewData: Boolean = false,
        postId: String? = null
    ) {
        if (isNewData) _feedHome.value = null

        viewModelScope.launch {
            if (cursor == "" || cursor != _feedHome.value?.cursor.orEmpty()) {
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
                    getFeedPosts(
                        source = source,
                        cursor = _feedHome.value?.cursor.orEmpty()
                    )
                }

                _feedHome.value = try {
                    val feedPosts = feedPostsDeferred.await()
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

                    Success(
                        feedPosts.copy(
                            items = relevantPostsDeferred.await() +
                                _feedHome.value?.items.orEmpty() +
                                items
                        )
                    )
                } catch (e: Throwable) {
                    Fail(e)
                }
            }
        }
    }

    fun addToCart(
        productId: String,
        productName: String,
        price: String,
        shopId: String
    ) {
        _atcResp.value = FeedResult.Loading
        viewModelScope.launchCatchError(block = {
            val isSuccess = addToCartImplementation(productId, productName, price, shopId)
            _atcResp.value = FeedResult.Success(isSuccess)
        }) {
            _atcResp.value = FeedResult.Failure(it)
        }
    }

    fun setUnsetReminder(
        campaignId: Long,
        setReminder: Boolean,
        itemPosition: Int
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
                            val prevDataItems = if (itemPosition > 0) {
                                it.data.items.subList(
                                    0,
                                    itemPosition
                                )
                            } else {
                                listOf()
                            }
                            val nextDataItems =
                                if (itemPosition < it.data.items.size - 1) {
                                    it.data.items.subList(
                                        itemPosition + 1,
                                        it.data.items.size
                                    )
                                } else {
                                    listOf()
                                }

                            val data = it.data.items[itemPosition]
                            when (data) {
                                is FeedCardImageContentModel ->
                                    data.campaign.isReminderActive =
                                        setReminder
                                is FeedCardVideoContentModel ->
                                    data.campaign.isReminderActive =
                                        setReminder
                            }

                            _feedHome.value = it.copy(
                                data = it.data.copy(
                                    items = mutableListOf<Visitable<FeedAdapterTypeFactory>>().also { list ->
                                        list.addAll(prevDataItems)
                                        list.add(data)
                                        list.addAll(nextDataItems)
                                    }
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
                if (it is Success) {
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
                val mappedResponse = mapLikeResponse(action, rowNumber)
                _likeKolResp.value = FeedResult.Success(mappedResponse)
            } catch (it: Throwable) {
                _likeKolResp.value = FeedResult.Failure(it)
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

    private fun mapLikeResponse(likeAction: FeedLikeAction, rowNumber: Int) =
        LikeFeedDataModel(
            rowNumber = rowNumber,
            action = likeAction
        )

    private suspend fun addToCartImplementation(
        productId: String,
        productName: String,
        price: String,
        shopId: String
    ): Boolean {
        val params = AddToCartUseCase.getMinimumParams(
            productId,
            shopId,
            productName = productName,
            price = price,
            userId = userSession.userId
        )
        try {
            addToCartUseCase.setParams(params)
            val response = addToCartUseCase.executeOnBackground()
            if (response.isDataError()) throw MessageErrorException(response.getAtcErrorMessage())
            return !response.isStatusError()
        } catch (e: Throwable) {
            if (e is ResponseErrorException) {
                throw MessageErrorException(e.localizedMessage)
            } else {
                throw e
            }
        }
    }

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

        private const val FOLLOWING_TYPE = "type"
    }
}
