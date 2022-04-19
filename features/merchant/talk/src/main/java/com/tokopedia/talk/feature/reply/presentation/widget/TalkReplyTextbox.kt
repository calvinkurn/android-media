package com.tokopedia.talk.feature.reply.presentation.widget

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.talk.databinding.WidgetTalkReplyTextboxBinding
import com.tokopedia.talk.feature.reply.presentation.util.TalkDebouncer
import com.tokopedia.talk.feature.reply.presentation.util.textwatcher.TalkReplyTextWatcher
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyTextboxListener
import com.tokopedia.unifycomponents.BaseCustomView

class TalkReplyTextbox : BaseCustomView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = WidgetTalkReplyTextboxBinding.inflate(LayoutInflater.from(context), this, true)

    fun setText(text: String) {
        binding.replyEditText.apply {
            setText(text)
            setSelection(text.length)
        }
    }

    fun bind(
        profilePicture: String,
        listener: TalkReplyTextboxListener,
        textLimit: Int
    ) {
        with(binding) {
            replyUserProfilePicture.loadImageRounded(profilePicture)
            replyAttachProduct.setOnClickListener {
                listener.onAttachProductButtonClicked()
            }
            replySendButton.setOnClickListener(object : TalkDebouncer() {
                override fun onDebouncedClick(v: View?) {
                    if (replyEditText.text.toString().length <= textLimit) {
                        listener.onSendButtonClicked(replyEditText.text.toString())
                    }
                }
            })
            val textWatcher = TalkReplyTextWatcher(textLimit, listener, replyEditText)
            replyEditText.apply {
                addTextChangedListener(textWatcher)
                filters = arrayOf(InputFilter.LengthFilter(textLimit))
            }
        }
    }

    fun reset() {
        binding.replyEditText.apply {
            setText("")
            clearFocus()
        }
    }

    override fun hasFocus(): Boolean {
        return binding.replyEditText.hasFocus()
    }

    fun setOnFocusChangeListener(isYours: Boolean, onHasFocus: () -> Unit, onLoseFocus: () -> Unit) {
        binding.replyEditText.apply {
            setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && !isYours) {
                    onHasFocus.invoke()
                } else {
                    onLoseFocus.invoke()
                }
            }
            requestFocus()
        }
    }
}