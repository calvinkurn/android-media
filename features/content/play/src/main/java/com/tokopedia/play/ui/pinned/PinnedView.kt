package com.tokopedia.play.ui.pinned

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.pinned.model.PinnedMessage

/**
 * Created by jegul on 03/12/19
 */
class PinnedView(
        container: ViewGroup,
        private val listener: Listener
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_pinned, container, true)
                    .findViewById(R.id.cl_pinned)

    private val tvPinnedMessage: TextView = view.findViewById(R.id.tv_pinned_message)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    fun setPinnedMessage(pinnedMessage: PinnedMessage) {
        val spannableString = SpannableString("${pinnedMessage.shopName} ${pinnedMessage.description}")
        spannableString.setSpan(
                ForegroundColorSpan(
                        MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Green_G300)
                ),
                spannableString.indexOf(pinnedMessage.shopName),
                pinnedMessage.shopName.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvPinnedMessage.text = spannableString

        view.findViewById<TextView>(R.id.tv_pinned_action)
                .setOnClickListener {
                    listener.onPinnedActionClicked(this, pinnedMessage.appLink)
                }
    }

    interface Listener {
        fun onPinnedActionClicked(pinnedView: PinnedView, applink: String)
    }
}