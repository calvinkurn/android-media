package com.tokopedia.tokochat_common.view.customview

import android.text.Editable
import android.text.TextWatcher
import com.tokopedia.tokochat_common.view.listener.TokoChatTypingListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TokoChatMessageTextWatcher(
    private val typingListener: TokoChatTypingListener
) : TextWatcher, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var message = ""
    private var typing = false
    private var job: Job? = null

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
        if (query == null) return
        val typedMessage = query.toString()
        if (!typing && query.isNotEmpty()) {
            typingListener.onStartTyping()
            typing = true
        }
        message = typedMessage
        job = launch {
            delay(TYPING_DELAY)
            if (typedMessage != message) {
                return@launch
            }
            typingListener.onStopTyping()
            typing = false
        }
    }

    fun cancelJob() {
        typing = false
        job?.cancel()
    }

    companion object {
        private const val TYPING_DELAY = 2000L
    }
}
