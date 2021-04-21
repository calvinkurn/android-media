package com.tokopedia.topchat.chatroom.view.custom

import android.text.Editable
import android.text.TextWatcher
import com.tokopedia.topchat.chatroom.view.listener.ReplyBoxTextListener
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SendButtonTextWatcher (
        private val replyBoxTextListener: ReplyBoxTextListener
): TextWatcher, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var job: Job? = null

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        job = launch {
            delay(DEBOUNCE)
            if(s.isNullOrEmpty()) {
                replyBoxTextListener.onReplyBoxEmpty()
            } else {
                replyBoxTextListener.onReplyBoxNotEmpty()
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
    }

    companion object {
        private const val DEBOUNCE = 300L
    }
}