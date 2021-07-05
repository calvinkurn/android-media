package com.tokopedia.review.feature.ovoincentive.presentation.adapter.viewholder

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.item_incentive_ovo.view.*

class IncentiveOvoViewHolder(view: View, private val incentiveOvoListener: IncentiveOvoListener) : RecyclerView.ViewHolder(view) {
    fun bindHero(explanation: String) {
        itemView.apply {
            tgIncentiveOvoNumber.text = "${adapterPosition+1}."
            tgIncentiveOvoExplanation.apply {
                text = HtmlLinkHelper(context, explanation).spannedString
                movementMethod = object : LinkMovementMethod() {
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
                                return incentiveOvoListener.onUrlClicked(link.first().url.toString())
                            }
                        }
                        return super.onTouchEvent(widget, buffer, event);
                    }
                }
            }
        }
    }
}