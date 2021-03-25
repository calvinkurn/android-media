package com.tokopedia.play.view.viewcomponent

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play_common.view.PlayNoImageEditText
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 03/08/20
 */
class SendChatViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes), PlayNoImageEditText.OnErrorListener {

    private val etChat: PlayNoImageEditText = findViewById(R.id.et_chat)
    private val ivSend: ImageView = findViewById(R.id.iv_send)

    private var prevText = ""

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (s.isNotEmpty()) ivSend.show() else ivSend.hide()

            if (s.length > MAX_CHARS) {
                if (prevText.length < MAX_CHARS) prevText = s.substring(0, MAX_CHARS)
                s.replace(0, s.length, prevText)
            }
            else prevText = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    init {
        etChat.addTextChangedListener(textWatcher)

        etChat.setOnClickListener {
            listener.onChatFormClicked(this)
        }

        etChat.setOnErrorListener(this)

        etChat.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                send()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        ivSend.setOnClickListener {
            send()
        }
    }

    override fun onForbiddenImage(view: PlayNoImageEditText) {
        Toast.makeText(rootView.context, getString(R.string.play_error_image_edit_text), Toast.LENGTH_SHORT).show()
    }

    fun focusChatForm(shouldFocus: Boolean, forceChangeKeyboardState: Boolean = false) {
        if (shouldFocus && !etChat.isFocusable) {
            etChat.apply {
                isFocusable = true
                isFocusableInTouchMode = true
                isLongClickable = true
            }
        } else if (!shouldFocus && etChat.isFocusable) {
            etChat.apply {
                isFocusable = false
                isFocusableInTouchMode = false
                isLongClickable = false
            }
        }

        if (!etChat.hasFocus() && shouldFocus) {
            etChat.requestFocus()
            if (forceChangeKeyboardState) showKeyboard(true)
        }
        else if (!shouldFocus) {
            etChat.clearFocus()
            showKeyboard(false)
        }
    }

    private fun showKeyboard(shouldShow: Boolean) {
        val imm = etChat.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (shouldShow) imm.showSoftInput(etChat, InputMethodManager.SHOW_IMPLICIT)
        else imm.hideSoftInputFromWindow(etChat.windowToken, 0)
    }

    private fun send() {
        val message: String = etChat.text.toString()
        if (message.isNotEmpty() && message.isNotBlank()) {
            listener.onSendChatClicked(this, message)
            etChat.setText("")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        etChat.removeTextChangedListener(textWatcher)
    }

    companion object {
        private const val MAX_CHARS = 140
    }

    interface Listener {
        fun onChatFormClicked(view: SendChatViewComponent)
        fun onSendChatClicked(view: SendChatViewComponent, message: String)
    }
}
