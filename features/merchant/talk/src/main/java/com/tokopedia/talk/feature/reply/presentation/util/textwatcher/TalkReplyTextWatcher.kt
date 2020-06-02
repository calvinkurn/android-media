package com.tokopedia.talk.feature.reply.presentation.util.textwatcher

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyTextboxListener
import java.util.*

class TalkReplyTextWatcher(private val textLimit: Int, private val talkReplyTextboxListener: TalkReplyTextboxListener, private val editText: EditText) : TextWatcher {

    private var timer: Timer? = Timer()
    private var delayTextChanged = 1000L

    override fun afterTextChanged(s: Editable?) {
        runTimer(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //No Op
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (timer != null) {
            timer!!.cancel()
        }
    }

    private fun updateListener(text: String) {
        val mainHandler = Handler(this.editText.context.mainLooper)
        val myRunnable = Runnable {
            if(text.length >= textLimit ) {
                talkReplyTextboxListener.onMaximumTextLimitReached()
            }
        }
        mainHandler.post(myRunnable)
    }

    private fun runTimer(text: String) {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                updateListener(text)
            }
        }, delayTextChanged)
    }

}