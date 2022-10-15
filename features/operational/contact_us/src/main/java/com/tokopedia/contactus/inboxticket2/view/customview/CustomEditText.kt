package com.tokopedia.contactus.inboxticket2.view.customview

import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by pranaymohapatra on 27/06/18.
 */
class CustomEditText : AppCompatEditText {
    private val delayTextChanged = DEFAULT_DELAY_TEXT_CHANGED
    private var listener: Listener? = null

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnEditorActionListener { textView: TextView, actionId: Int, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && listener != null) {
                listener?.onSearchSubmitted(textView.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
        addTextChangedListener(object : TextWatcher {
            private var timer: Timer? = Timer()
            override fun afterTextChanged(s: Editable) {
                runTimer(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                timer?.cancel()
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
                if (listener == null) {
                    return
                }
                val mainHandler = Handler(context.mainLooper)
                val myRunnable = Runnable { listener?.onSearchTextChanged(text) }
                mainHandler.post(myRunnable)
            }
        })
        invalidate()
        requestLayout()
    }

    interface Listener {
        fun onSearchSubmitted(text: String)
        fun onSearchTextChanged(text: String)
    }

    companion object {
        private val DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.SECONDS.toMillis(1)
    }
}
