package com.tokopedia.topchat.chatroom.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.usecase.GetExistingMessageIdUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetShopFollowingUseCaseNew
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

open class TopChatViewModel @Inject constructor(
    private var getExistingMessageIdUseCase: GetExistingMessageIdUseCase,
    private var getShopFollowingUseCase: GetShopFollowingUseCaseNew,
    private val dispatcher: CoroutineDispatchers,
    private val remoteConfig: RemoteConfig
): BaseViewModel(dispatcher.main) {

    private val _messageId = MutableLiveData<Result<String>>()
    val messageId: LiveData<Result<String>>
        get() = _messageId

    private val _shopFollowing = MutableLiveData<Result<ShopFollowingPojo>>()
    val shopFollowing: LiveData<Result<ShopFollowingPojo>>
        get() = _shopFollowing

    fun getMessageId(
        toUserId: String,
        toShopId: String,
        source: String,
    ) {
        launchCatchError(block = {
            val params = getExistingMessageIdUseCase.generateParam(toShopId, toUserId, source)
            val result = getExistingMessageIdUseCase.invoke(params)
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
}