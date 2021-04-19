package com.tokopedia.play.view.viewcomponent

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.recom.PlayPinnedUiModel
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
    private val tvPinnedAction: TextView = findViewById(R.id.tv_pinned_action)

    fun setPinnedMessage(pinnedMessage: PlayPinnedUiModel.PinnedMessage) {
        val partnerName = pinnedMessage.partnerName
        val spannableString = SpannableString(
                buildString {
                    if (partnerName.isNotEmpty()) {
                        append(pinnedMessage.partnerName)
                        append(' ')
                    }

                    append(pinnedMessage.title)
                }
        )
        if (partnerName.isNotEmpty()) {
            spanPartnerName(pinnedMessage.partnerName, spannableString)
        }
        tvPinnedMessage.text = spannableString

        if (!pinnedMessage.applink.isNullOrEmpty()) {
            tvPinnedAction.show()

            tvPinnedAction.setOnClickListener {
                listener.onPinnedMessageActionClicked(this, pinnedMessage.applink, pinnedMessage.title)
            }

        } else tvPinnedAction.hide()
    }

    private fun spanPartnerName(partnerName: String, fullMessage: Spannable): Spannable {
        fullMessage.setSpan(
                ForegroundColorSpan(
                        MethodChecker.getColor(rootView.context, R.color.play_dms_G300)
                ),
                fullMessage.indexOf(partnerName),
                partnerName.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return fullMessage
    }

    interface Listener {

        fun onPinnedMessageActionClicked(view: PinnedViewComponent, applink: String, message: String)
    }
}