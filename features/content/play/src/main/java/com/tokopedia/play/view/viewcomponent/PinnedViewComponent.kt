package com.tokopedia.play.view.viewcomponent

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.recom.PinnedMessageUiModel
import com.tokopedia.play_common.util.extension.append
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 03/08/20
 */
class PinnedViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    private val tvPinnedMessage: TextView = findViewById(R.id.tv_pinned_message)

    fun setPinnedMessage(pinnedMessage: PinnedMessageUiModel) {
        tvPinnedMessage.text = constructPinnedMessage(
            pinnedMessage.title,
            pinnedMessage.appLink.isNotBlank()
        )

        rootView.setOnClickListener {
            if (pinnedMessage.appLink.isBlank()) return@setOnClickListener
            listener.onPinnedMessageActionClicked(
                this,
                pinnedMessage.appLink,
                pinnedMessage.title
            )
        }
    }

    private fun constructPinnedMessage(message: String, hasAppLink: Boolean): CharSequence {
        val spanBuilder = SpannableStringBuilder()
        spanBuilder.append(message)
        if (hasAppLink) {
            spanBuilder.append(' ')
            spanBuilder.append(
                text = getString(R.string.play_pinned_msg_applink_text),
                flags = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
                StyleSpan(Typeface.BOLD),
                ForegroundColorSpan(
                    MethodChecker.getColor(rootView.context, R.color.play_dms_pinned_link)
                ),
            )
        }
        return spanBuilder
    }

    interface Listener {

        fun onPinnedMessageActionClicked(view: PinnedViewComponent, appLink: String, message: String)
    }
}