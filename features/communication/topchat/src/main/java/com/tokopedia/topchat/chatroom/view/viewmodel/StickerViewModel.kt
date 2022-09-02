package com.tokopedia.topchat.chatroom.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatListStickerUseCase
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StickerViewModel @Inject constructor(
    private val chatListStickerUseCase: ChatListStickerUseCase,
    private val cacheManager: TopchatCacheManager,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _stickers = MutableLiveData<Result<List<Sticker>>>()
    val stickers: LiveData<Result<List<Sticker>>>
        get() = _stickers

    private val cacheKey = ChatListStickerUseCase::class.java.simpleName

    fun loadStickers(stickerGroupUID: String, needUpdate: Boolean) {
        launchCatchError(context = dispatcher.io, block = {
            val isPreviousRequestSuccess = getPreviousRequestState(stickerGroupUID)
            val cache = getCacheStickerGroup(stickerGroupUID)?.also {
                withContext(dispatcher.main) {
                    _stickers.value = Success(it.chatBundleSticker.list)
                }
            }
            if (cache != null && !needUpdate && isPreviousRequestSuccess) return@launchCatchError
            getStickerList(stickerGroupUID)
        }, onError = {
            withContext(dispatcher.main) {
                saveFailRequestState(stickerGroupUID)
                _stickers.value = Fail(it)
            }
        })
    }

    private suspend fun getStickerList(stickerGroupUID: String) {
        val param = ChatListStickerUseCase.Param(stickerGroupUID)
        val response = chatListStickerUseCase(param)
        saveToCache(stickerGroupUID, response)
        saveSuccessRequestState(stickerGroupUID)
        withContext(dispatcher.main) {
            _stickers.value = Success(response.chatBundleSticker.list)
        }
    }

    private fun getCacheStickerGroup(stickerUID: String): StickerResponse? {
        return try {
            val key = generateCacheKey(stickerUID)
             cacheManager.loadCache(key, StickerResponse::class.java)
        } catch (e: Throwable) {
            null
        }

    }

    private fun generateCacheKey(stickerUID: String): String {
        return "$cacheKey - $stickerUID"
    }

    private fun saveToCache(stickerUID: String, result: StickerResponse) {
        val key = generateCacheKey(stickerUID)
        cacheManager.saveCache(key, result)
    }

    private fun saveSuccessRequestState(stickerUID: String) {
        saveRequestState(stickerUID, true)
    }

    private fun saveFailRequestState(stickerUID: String) {
        saveRequestState(stickerUID, false)
    }

    private fun saveRequestState(stickerUID: String, isSuccess: Boolean) {
        val stateCacheKey = generateStateCacheKey(stickerUID)
        cacheManager.saveState(stateCacheKey, isSuccess)
    }

    private fun getPreviousRequestState(stickerUID: String): Boolean {
        return try {
            val stateCacheKey = generateStateCacheKey(stickerUID)
            cacheManager.getPreviousState(stateCacheKey)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            false
        }
    }

    private fun generateStateCacheKey(stickerUID: String): String {
        return "$STATE_KEY - $stickerUID"
    }

    companion object {
        private const val STATE_KEY = "state"
    }

}