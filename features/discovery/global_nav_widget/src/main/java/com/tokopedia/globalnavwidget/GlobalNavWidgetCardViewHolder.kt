package com.tokopedia.globalnavwidget

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import kotlinx.android.synthetic.main.global_nav_widget_card_item_layout.view.*

internal class GlobalNavWidgetCardViewHolder(
        itemView: View,
        private val listener: GlobalNavWidgetListener
): RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.global_nav_widget_card_item_layout
    }

    private val context: Context? = itemView.context

    internal fun bind(item: GlobalNavWidgetModel.Item) {
        itemView.globalNavCardItemCardView?.setOnClickListener { _ ->
            listener.onClickItem(item)
        }

        itemView.globalNavCardItemImage?.let {
            ImageHandler.loadImageFitCenter(context, it, item.imageUrl)
        }

        itemView.globalNavCardSource?.shouldShowWithAction(item.categoryName.isNotEmpty()) {
            itemView.globalNavCardSource?.text = item.categoryName
        }

        itemView.globalNavCardTitle.shouldShowWithAction(item.name.isNotEmpty()) {
            it.text = MethodChecker.fromHtml(item.name)
        }

        val shouldShowStrikethrough = item.strikethrough.isNotEmpty()

        itemView.globalNavCardStrikethrough.shouldShowWithAction(shouldShowStrikethrough) {
            it.text = MethodChecker.fromHtml(item.strikethrough)
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        val shouldShowSubtitle = !shouldShowStrikethrough && item.subtitle.isNotEmpty()

        itemView.globalNavCardSubtitle.shouldShowWithAction(shouldShowSubtitle) {
            it.text = MethodChecker.fromHtml(item.subtitle)
        }

        itemView.globalNavCardPrice.shouldShowWithAction(item.info.isNotEmpty()) {
            it.text = MethodChecker.fromHtml(item.info)
        }
    }
}