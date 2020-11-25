package com.tokopedia.talk_old.common.adapter.viewholder

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.talk_old.R
import com.tokopedia.talk_old.producttalk.view.data.ChatBannerUiModel

class ChatBannerTalkViewHolder(itemView: View?, private val listener: Listener) : AbstractViewHolder<ChatBannerUiModel>(itemView) {

    interface Listener {
        fun onDismissChatTicker(position: Int)
        fun trackOnClickChatBanner(element: ChatBannerUiModel)
    }

    private var tickerDescriptionView: TextView? = null
    private var tickerCloseButtonView: ImageView? = null

    override fun bind(element: ChatBannerUiModel?) {
        if (element == null) return
        bindView()
        bindText(element)
        bindClickDismiss(element)
    }

    private fun bindView() {
        tickerDescriptionView = itemView.findViewById(com.tokopedia.unifycomponents.R.id.ticker_description)
        tickerCloseButtonView = itemView.findViewById(com.tokopedia.unifycomponents.R.id.ticker_close_icon)
    }

    private fun bindText(element: ChatBannerUiModel) {
        val bannerText = createBannerText(element)
        tickerDescriptionView?.movementMethod = LinkMovementMethod.getInstance()
        tickerDescriptionView?.text = bannerText
    }

    private fun bindClickDismiss(element: ChatBannerUiModel) {
        tickerCloseButtonView?.setOnClickListener { listener.onDismissChatTicker(adapterPosition) }
    }

    private fun createBannerText(element: ChatBannerUiModel): CharSequence {
        val prefixText = "Mau tahu info seputar stok, varian, atau pengiriman? "
        val clickText = SpannableString("Chat penjual sekarang").apply {
            assignClickTextImplementation(this, element)
            assignClickTextColor(this)
        }
        return SpannableStringBuilder(prefixText).apply {
            append(clickText)
        }
    }

    private fun assignClickTextColor(spannable: SpannableString) {
        val greenColor = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Green_G500)
        spannable.setSpan(ForegroundColorSpan(greenColor), 0, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun assignClickTextImplementation(spannable: SpannableString, element: ChatBannerUiModel) {
        val clickAction = object : ClickableSpan() {
            override fun onClick(widget: View) {
                listener.trackOnClickChatBanner(element)
                goToChatRoom(element)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannable.setSpan(clickAction, 0, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun goToChatRoom(element: ChatBannerUiModel) {
        val productPreviews = listOf(
                ProductPreview(
                        id = element.productId,
                        imageUrl = element.productImageUrl,
                        name = element.productName,
                        price = element.productPrice
                )
        )
        val stringProductPreviews = CommonUtil.toJson(productPreviews)
        val intent = RouteManager.getIntent(itemView.context,
                ApplinkConst.TOPCHAT_ASKSELLER, element.shopId, "",
                ApplinkConst.Chat.SOURCE_ASK_SELLER, element.shopName, ""
        ).apply {
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
        }
        itemView.context?.startActivity(intent)
    }

    companion object {
        val LAYOUT = R.layout.item_talk_chat_banner
    }
}