package com.tokopedia.globalnavwidget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.widget_global_nav_item.view.*

internal class GlobalNavWidgetViewHolder(
        itemView: View,
        private val listener: GlobalNavWidgetListener
): RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.widget_global_nav_item
    }

    private val context: Context? = itemView.context

    internal fun bind(item: GlobalNavWidgetModel.Item) {
        itemView.globalNavItemContainer?.let {
            listener.onClickItem(item)
        }

        itemView.globalNavItemImage?.let {
            ImageHandler.loadImageFitCenter(context, it, item.imageUrl)
        }

        itemView.globalNavItemName?.shouldShowWithAction(item.name.isNotEmpty()) {
            itemView.globalNavItemName?.text = item.name
        }

        itemView.globalNavItemInfo?.shouldShowWithAction(item.info.isNotEmpty()) {
            itemView.globalNavItemName?.text = item.info
        }
    }
}