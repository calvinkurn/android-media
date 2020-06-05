package com.tokopedia.topchat.chatroom.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.usecase.ChatListStickerUseCase
import javax.inject.Inject

class StickerViewModel @Inject constructor(
        private val chatListUseCase: ChatListStickerUseCase
) : ViewModel() {

    private val _stickers = MutableLiveData<List<Sticker>>()
    val stickers: LiveData<List<Sticker>> get() = _stickers


    fun loadStickers(stickerGroupUID: String, needUpdate: Boolean) {
        chatListUseCase.loadSticker(
                stickerGroupUID, needUpdate, ::onLoadSticker, ::onSuccessLoadSticker,
                ::onErrorLoadSticker
        )
    }

    fun cancelAllJobs() {
        chatListUseCase.safeCancel()
    }

    private fun onLoadSticker(stickers: List<Sticker>) {
        _stickers.value = stickers
    }

    private fun onSuccessLoadSticker(stickers: List<Sticker>) {
        _stickers.value = stickers
    }

    private fun onErrorLoadSticker(throwable: Throwable) {}

}