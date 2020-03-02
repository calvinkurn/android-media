package com.tokopedia.play.ui.pinned

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.uimodel.PinnedMessageUiModel
import com.tokopedia.play.view.uimodel.PinnedProductUiModel

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
    private val tvPinnedAction: TextView = view.findViewById(R.id.tv_pinned_action)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    fun setPinnedMessage(pinnedMessage: PinnedMessageUiModel) {
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
            spannableString.setSpan(
                    ForegroundColorSpan(
                            MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Green_G300)
                    ),
                    spannableString.indexOf(pinnedMessage.partnerName),
                    pinnedMessage.partnerName.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        tvPinnedMessage.text = spannableString

        if (!pinnedMessage.applink.isNullOrEmpty()) {
            tvPinnedAction.visible()
            tvPinnedAction.setOnClickListener {
//                listener.onPinnedMessageActionClicked(this, pinnedMessage.applink, pinnedMessage.title)
                listener.onPinnedProductActionClicked(this)
            }
        } else tvPinnedAction.gone()
    }
    
    fun setPinnedProduct(pinnedProduct: PinnedProductUiModel) {
        tvPinnedAction.visible()
        tvPinnedAction.setOnClickListener { 
            listener.onPinnedProductActionClicked(this)
        }
    }

    interface Listener {

        fun onPinnedMessageActionClicked(view: PinnedView, applink: String, message: String)
        fun onPinnedProductActionClicked(view: PinnedView)
    }
}