package com.tokopedia.review.feature.inbox.history.presentation.util

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.*


class SearchTextWatcher(private val searchTextView: EditText, private val delayTextChanged: Long = 500L, private val searchListener: SearchListener) : TextWatcher{

    private var timer: Timer? = Timer()

    override fun afterTextChanged(s: Editable?) {
        runTimer(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //No op
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (timer != null) {
            timer!!.cancel()
        }
    }

    private fun runTimer(text: String) {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                updateListener(text)
            }
        }, delayTextChanged)
    }

    private fun updateListener(text: String) {
        val mainHandler = Handler(this.searchTextView.context.mainLooper)
        val myRunnable = Runnable { searchListener.onSearchTextChanged(text) }
        mainHandler.post(myRunnable)
    }
}


interface SearchListener {
    fun onSearchTextChanged(text: String)
}