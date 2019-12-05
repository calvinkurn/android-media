package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.ui.chatlist.model.PlayChat
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 29/11/19
 */
class PlayInteractionViewModel @Inject constructor() : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val _observableChatList = MutableLiveData<PlayChat>()
    val observableChatList: LiveData<PlayChat> = _observableChatList

    private val listOfUser = listOf(
            "Rifqi",
            "Meyta",
            "IJ",
            "Yehez"
    )

    private val listOfMessage = listOf(
            "Great product!",
            "I watched all of that till te end and i decided i will buy this dress.",
            "Great, wellspoken review. Such a wonderful information. Thanks a lot!"
    )

    fun startObservingChatList() {
        launch {
            var id = 0
            while (job.isActive) {
                _observableChatList.value =
                        PlayChat(
                                ++id,
                                listOfUser.random(),
                                listOfMessage.random()
                        )
                delay(5000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}