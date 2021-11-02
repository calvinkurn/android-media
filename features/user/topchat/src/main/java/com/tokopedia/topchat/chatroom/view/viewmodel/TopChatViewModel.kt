package com.tokopedia.topchat.chatroom.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.pojo.param.AddToCartParam
import com.tokopedia.topchat.chatroom.domain.pojo.param.ExistingMessageIdParam
import com.tokopedia.topchat.chatroom.domain.usecase.GetExistingMessageIdUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetShopFollowingUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class TopChatViewModel @Inject constructor(
    private var getExistingMessageIdUseCase: GetExistingMessageIdUseCase,
    private var getShopFollowingUseCase: GetShopFollowingUseCase,
    private var toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase,
    private var addToCartUseCase: AddToCartUseCase,
    private val dispatcher: CoroutineDispatchers,
    private val remoteConfig: RemoteConfig
) : BaseViewModel(dispatcher.main) {

    private val _messageId = MutableLiveData<Result<String>>()
    val messageId: LiveData<Result<String>>
        get() = _messageId

    private val _shopFollowing = MutableLiveData<Result<ShopFollowingPojo>>()
    val shopFollowing: LiveData<Result<ShopFollowingPojo>>
        get() = _shopFollowing

    private val _followUnfollowShop =
        MutableLiveData<Pair<BroadcastSpamHandlerUiModel?, Result<Boolean>>>()
    val followUnfollowShop: LiveData<Pair<BroadcastSpamHandlerUiModel?, Result<Boolean>>>
        get() = _followUnfollowShop

    private val _addToCart = MutableLiveData<Result<AddToCartParam>>()
    val addToCart: LiveData<Result<AddToCartParam>>
        get() = _addToCart

    fun getMessageId(
        toUserId: String,
        toShopId: String,
        source: String,
    ) {
        launchCatchError( block = {
            val existingMessageIdParam = ExistingMessageIdParam(
                toUserId = toUserId,
                toShopId = toShopId,
                source = source
            )
            val result = getExistingMessageIdUseCase(existingMessageIdParam)
            _messageId.value = Success(result.chatExistingChat.messageId)
        }, onError = {
            _messageId.value = Fail(it)
        })
    }

    fun getShopFollowingStatus(shopId: Long) {
        launchCatchError(block = {
            val result = getShopFollowingUseCase(shopId)
            _shopFollowing.value = Success(result)
        }, onError = {
            _shopFollowing.value = Fail(it)
        })
    }

    fun followUnfollowShop(
        shopId: String,
        action: ToggleFavouriteShopUseCase.Action? = null,
        element: BroadcastSpamHandlerUiModel? = null
    ) {
        launchCatchError(block = {
            val param = if (action != null) {
                ToggleFavouriteShopUseCase.createRequestParam(shopId, action)
            } else {
                ToggleFavouriteShopUseCase.createRequestParam(shopId)
            }
            withContext(dispatcher.io) {
                val result = toggleFavouriteShopUseCase
                    .createObservable(requestParams = param)
                    .toBlocking()
                    .first()
                _followUnfollowShop.postValue(Pair(element, Success(result)))
            }
        }, onError = {
            _followUnfollowShop.value = Pair(element, Fail(it))
        })
    }

    fun addProductToCart(addToCartParam: AddToCartParam) {
        launchCatchError(block = {
            setupAddToCartParam(addToCartParam)
            val atcResult = addToCartUseCase.executeOnBackground()
            if (atcResult.data.success == 1) {
                addToCartParam.dataModel = atcResult.data
                _addToCart.value = Success(addToCartParam)
            } else {
                _addToCart.value = Fail(MessageErrorException(atcResult.errorMessage.first()))
            }
        }, onError = {
            _addToCart.value = Fail(it)
        })
    }

    private fun setupAddToCartParam(addToCartParam: AddToCartParam) {
        val addToCartRequestParams = AddToCartRequestParams(
            productId = addToCartParam.productId.toLongOrZero(),
            shopId = addToCartParam.shopId.toInt(),
            quantity = addToCartParam.minOrder,
            atcFromExternalSource = AtcFromExternalSource.ATC_FROM_TOPCHAT
        )
        addToCartUseCase.addToCartRequestParams = addToCartRequestParams
    }
}