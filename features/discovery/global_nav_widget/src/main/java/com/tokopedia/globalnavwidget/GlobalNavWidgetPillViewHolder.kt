package com.tokopedia.globalnavwidget

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.globalnavwidget.databinding.GlobalNavWidgetPillItemLayoutBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding

internal class GlobalNavWidgetPillViewHolder(
        itemView: View,
        private val listener: GlobalNavWidgetListener
): RecyclerView.ViewHolder(itemView) {

    private var binding: GlobalNavWidgetPillItemLayoutBinding? by viewBinding()
    companion object {
        val LAYOUT = R.layout.global_nav_widget_pill_item_layout
    }

    private val context: Context? = itemView.context

    internal fun bind(item: GlobalNavWidgetModel.Item) {
        binding?.globalNavPillItemContainer?.setOnClickListener { _ ->
            listener.onClickItem(item)
        }

        binding?.globalNavPillItemImage?.let {
            ImageHandler.loadImageFitCenter(context, it, item.imageUrl)
        }

        binding?.globalNavPillItemName?.shouldShowWithAction(item.name.isNotEmpty()) {
            binding?.globalNavPillItemName?.text = item.name
        }

        binding?.globalNavPillItemInfo?.shouldShowWithAction(item.info.isNotEmpty()) {
            binding?.globalNavPillItemInfo?.text = item.info
        }
    }
}