package com.tokopedia.play.ui.pinned

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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
    private val llPinnedProduct: LinearLayout = view.findViewById(R.id.ll_pinned_product)
    private val tvPinnedProductMessage: TextView = view.findViewById(R.id.tv_pinned_product_message)
    private val animationProduct: LottieAnimationView = view.findViewById(R.id.animation_product)
    private val tvPinnedAction: TextView = view.findViewById(R.id.tv_pinned_action)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    fun setPinnedMessage(pinnedMessage: PinnedMessageUiModel) {
        llPinnedProduct.hide()

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
    
    fun setPinnedProduct(pinnedProduct: PinnedProductUiModel) {
        llPinnedProduct.show()
        tvPinnedAction.show()

        tvPinnedAction.setOnClickListener {
            listener.onPinnedProductActionClicked(this)
        }

        tvPinnedMessage.text = spanPartnerName(pinnedProduct.partnerName, SpannableString(pinnedProduct.partnerName))
        tvPinnedProductMessage.text = pinnedProduct.title

        animationProduct.setAnimation(
                if (pinnedProduct.isPromo) R.raw.anim_play_product_promo
                else R.raw.anim_play_product
        )
        animationProduct.playAnimation()
    }

    private fun spanPartnerName(partnerName: String, fullMessage: Spannable): Spannable {
        fullMessage.setSpan(
                ForegroundColorSpan(
                        MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Green_G300)
                ),
                fullMessage.indexOf(partnerName),
                partnerName.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return fullMessage
    }

    interface Listener {

        fun onPinnedMessageActionClicked(view: PinnedView, applink: String, message: String)
        fun onPinnedProductActionClicked(view: PinnedView)
    }
}