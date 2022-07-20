package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class DriverTippingInfoViewHolder(
    itemView: View?,
    private val navigator: BuyerOrderDetailNavigator
) : AbstractViewHolder<ShipmentInfoUiModel.DriverTippingInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_tipping
    }

    private val ivBuyerOrderDetailTippingIcon = itemView?.findViewById<ImageUnify>(R.id.ivBuyerOrderDetailTippingIcon)
    private val tvBuyerOrderDetailTippingTitle = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailTippingTitle)
    private val tvBuyerOrderDetailTippingDescription = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailTippingDescription)

    init {
        setupTouchLinkHandler()
    }

    override fun bind(element: ShipmentInfoUiModel.DriverTippingInfoUiModel?) {
        element?.run {
            setupIcon(imageUrl)
            setupTitle(title)
            setupDescription(description)
        }
    }

    private fun setupTouchLinkHandler() {
        tvBuyerOrderDetailTippingDescription?.movementMethod = object : LinkMovementMethod() {
            override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
                val action = event.action

                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                    var x = event.x
                    var y = event.y.toInt()

                    x -= widget.totalPaddingLeft
                    y -= widget.totalPaddingTop

                    x += widget.scrollX
                    y += widget.scrollY

                    val layout = widget.layout
                    val line = layout.getLineForVertical(y)
                    val off = layout.getOffsetForHorizontal(line, x)

                    val link = buffer.getSpans(off, off, URLSpan::class.java)
                    if (link.isNotEmpty() && action == MotionEvent.ACTION_UP) {
                        return navigator.openAppLink(
                            link.firstOrNull()?.url.orEmpty(),
                            shouldRefreshWhenBack = true
                        )
                    }
                }
                return super.onTouchEvent(widget, buffer, event);
            }
        }
    }

    private fun setupIcon(imageUrl: String) {
        ivBuyerOrderDetailTippingIcon?.setImageUrl(imageUrl)
    }

    private fun setupTitle(title: String) {
        tvBuyerOrderDetailTippingTitle?.text = MethodChecker.fromHtml(title)
    }

    private fun setupDescription(description: String) {
        tvBuyerOrderDetailTippingDescription?.run {
            text = HtmlLinkHelper(context, description).spannedString
        }
    }
}