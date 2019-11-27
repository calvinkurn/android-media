package com.tokopedia.globalnavwidget

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.global_nav_widget_pill_item_layout.view.*

internal class GlobalNavWidgetPillViewHolder(
        itemView: View,
        private val listener: GlobalNavWidgetListener
): RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.global_nav_widget_pill_item_layout
    }

    private val context: Context? = itemView.context

    internal fun bind(item: GlobalNavWidgetModel.Item) {
        itemView.globalNavPillItemContainer?.setOnClickListener { _ ->
            listener.onClickItem(item)
        }

        itemView.globalNavPillItemImage?.let {
            ImageHandler.loadImageFitCenter(context, it, item.imageUrl)
        }

        itemView.globalNavPillItemName?.shouldShowWithAction(item.name.isNotEmpty()) {
            itemView.globalNavPillItemName?.text = item.name
        }

        itemView.globalNavPillItemInfo?.shouldShowWithAction(item.info.isNotEmpty()) {
            itemView.globalNavPillItemInfo?.text = item.info
        }
    }
}