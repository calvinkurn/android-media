package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowUseCase
import com.tokopedia.feedcomponent.people.usecase.ProfileFollowUseCase
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.feedplus.presentation.model.FollowShopModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 23/02/23
 */
class FeedPostViewModel @Inject constructor(
    private val feedXHomeUseCase: FeedXHomeUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val userSession: UserSessionInterface,
    private val shopFollowUseCase: ShopFollowUseCase,
    private val userFollowUseCase: ProfileFollowUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _feedHome = MutableLiveData<Result<FeedModel>>()
    val feedHome: LiveData<Result<FeedModel>>
        get() = _feedHome

    private val _atcResp = MutableLiveData<FeedResult<Boolean>>()
    val atcRespData: LiveData<FeedResult<Boolean>>
        get() = _atcResp

    private val _followResult = MutableLiveData<Result<FollowShopModel>>()
    val followResult: LiveData<Result<FollowShopModel>>
        get() = _followResult

    private var lastCursorFetched = ""

    fun fetchFeedPosts(source: String) {
        launchCatchError(dispatchers.main, block = {
            val cursor = feedHome.value?.let {
                if (it is Success) {
                    it.data.pagination.cursor
                } else {
                    ""
                }
            } ?: ""

            if (lastCursorFetched == "" || cursor != lastCursorFetched) {
                lastCursorFetched = cursor
                val response = feedXHomeUseCase(
                    feedXHomeUseCase.createParams(
                        source,
                        cursor
                    )
                )

                _feedHome.value = Success(response)
            }
        }) {
            _feedHome.value = Fail(it)
        }
    }

    fun addToCart(
        productId: String,
        productName: String,
        price: String,
        shopId: String
    ) {
        _atcResp.value = FeedResult.Loading
        launchCatchError(dispatchers.main, block = {
            val isSuccess = addToCartImplementation(productId, productName, price, shopId)
            _atcResp.value = FeedResult.Success(isSuccess)
        }) {
            _atcResp.value = FeedResult.Failure(it)
        }
    }

    fun doFollow(id: String, isShop: Boolean) {
        launchCatchError(dispatchers.main, block = {
            val response = withContext(dispatchers.io) {
                if (isShop) {
                    val response = shopFollowUseCase(shopFollowUseCase.createParams(id))
                    FollowShopModel(
                        id = id,
                        success = response.followShop.success,
                        isFollowing = response.followShop.isFollowing,
                        isShop = true
                    )
                } else {
                    val response = userFollowUseCase.executeOnBackground(id)
                    if (response.profileFollowers.errorCode.isNotEmpty()) {
                        throw MessageErrorException(
                            response.profileFollowers.messages.firstOrNull() ?: ""
                        )
                    }
                    FollowShopModel(
                        id = response.profileFollowers.data.userIdTarget,
                        success = true,
                        isFollowing = true,
                        isShop = false
                    )
                }
            }
            _followResult.value = Success(response)
        }) {
            _followResult.value = Fail(it)
        }
    }

    fun updateFollowStatus(
        contents: List<Visitable<*>>,
        id: String,
        followStatus: Boolean
    ): List<Visitable<*>> {
        val newList = mutableListOf<Visitable<*>>()

        newList.addAll(
            contents.map { item ->
                when (item) {
                    is FeedCardImageContentModel -> {
                        if (item.author.id == id) {
                            item.copy(
                                followers = item.followers.copy(
                                    isFollowed = followStatus
                                )
                            )
                        } else {
                            item
                        }
                    }
                    else -> item
                }
            }
        )

        return newList.toList()
    }

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
}
