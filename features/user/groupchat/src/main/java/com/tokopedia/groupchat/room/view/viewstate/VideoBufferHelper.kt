package com.tokopedia.groupchat.room.view.viewstate

import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.groupchat.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author : Steven 28/05/19
 */
class VideoBufferHelper(var bufferContainer: View, var bufferDimContainer: View) {

    private var bufferLoading = bufferContainer.findViewById<ProgressBar>(R.id.buffer_progress_bar)
    private var bufferText = bufferContainer.findViewById<TextView>(R.id.buffer_text)

    init {
        bufferDimContainer.setOnClickListener{}
        bufferText.text = getSpannable(R.string.buffer_text_long, R.string.buffer_text_retry)
        bufferText.setOnClickListener {
            hideContainer()
        }
    }

    fun showLoadingOnly() {
        showContainer()
        bufferLoading.show()
        bufferText.hide()
    }

    fun showRetryOnly() {
        showContainer()
        bufferLoading.hide()
        bufferText.show()
    }

    fun showContainer() {
        bufferContainer.show()
        bufferDimContainer.show()
    }

    fun hideContainer() {
        bufferContainer.hide()
        bufferDimContainer.hide()
    }

    private fun getSpannable(sourceStringRes: Int, hyperlinkStringRes: Int): Spannable {
        val context = bufferContainer.context
        val sourceString = context.resources.getString(sourceStringRes)
        val hyperlinkString = context.resources.getString(hyperlinkStringRes)
        val spannable = SpannableString(sourceString)

        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = MethodChecker.getColor(context, R.color.Green_G300)
            }
        }, sourceString.indexOf(hyperlinkString), sourceString.length, 0)

        return spannable
    }
}