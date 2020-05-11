package com.tokopedia.home.account.presentation.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.label.LabelView
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.listener.AccountItemListener
import com.tokopedia.home.account.presentation.util.AccountByMeHelper
import com.tokopedia.home.account.presentation.viewmodel.RekeningPremiumViewModel
import com.tokopedia.home.account.presentation.widget.TagRoundedSpan

class RekeningPremiumViewHolder(itemView: View, val listener: AccountItemListener)
    : AbstractViewHolder<RekeningPremiumViewModel>(itemView) {

    private var lastClickTime = System.currentTimeMillis()
    private val CLICK_TIME_INTERVAL: Long = 1000
    private val cornerRadius = 4
    private val relativeSpanSize = 0.57F

    val layout: View = itemView.findViewById<View>(R.id.container)
    val labelView = itemView.findViewById<LabelView>(R.id.labelview)
    val separator = itemView.findViewById<View>(R.id.separator)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.account_item_rekening_premium
    }

    override fun bind(element: RekeningPremiumViewModel?) {
        element?.let {
            layout.setOnClickListener { v ->
                val now = System.currentTimeMillis()
                if (now - lastClickTime >= CLICK_TIME_INTERVAL) {
                    listener.openRekeningPremiumWebLink(element)
                    setWidgetClicked()
                    lastClickTime = now
                }
            }
            labelView.setTitle(generateSpannableTitle(element))
            labelView.setSubTitle(element.menuDescription)
            labelView.showRightArrow(false)
            if (element.isUseSeparator) {
                separator.visibility = View.VISIBLE
            } else {
                separator.visibility = View.GONE
            }
        }
    }

    private fun generateSpannableTitle(item: RekeningPremiumViewModel): SpannableString {
        val indicatorNew = " ${itemView.context.resources.getString(R.string.label_new).toUpperCase()}"
        var title = item.menu

        if (title != null && !hasBeenClickedOnce()) {
            val startPosition = title.length + 1
            val endPosition = startPosition + indicatorNew.length - 1
            title += indicatorNew
            val spannable = SpannableString(title)
            val newTag = TagRoundedSpan(
                    itemView.context,
                    cornerRadius,
                    R.color.Red_R400,
                    R.color.white
            )

            spannable.setSpan(
                    RelativeSizeSpan(relativeSpanSize),
                    startPosition,
                    endPosition,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    startPosition,
                    endPosition,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                    newTag,
                    startPosition,
                    endPosition,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            return spannable
        } else {
            return SpannableString(title)
        }
    }

    private fun hasBeenClickedOnce(): Boolean {
        return AccountByMeHelper.isRekeningPremiumWidgetClicked(itemView.context)
    }

    private fun setWidgetClicked() {
        return AccountByMeHelper.setRekeningPremiumWidgetClicked(itemView.context)
    }

}