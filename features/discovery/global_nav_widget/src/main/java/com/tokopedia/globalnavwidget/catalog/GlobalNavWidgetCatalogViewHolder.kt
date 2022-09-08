package com.tokopedia.globalnavwidget.catalog

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.globalnavwidget.GlobalNavWidgetModel
import com.tokopedia.globalnavwidget.R
import com.tokopedia.globalnavwidget.databinding.GlobalNavWidgetCatalogItemLayoutBinding
import com.tokopedia.utils.view.binding.viewBinding

internal class GlobalNavWidgetCatalogViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView)  {

    private var binding: GlobalNavWidgetCatalogItemLayoutBinding? by viewBinding()

    companion object {
        val LAYOUT = R.layout.global_nav_widget_catalog_item_layout
    }

    internal fun bind(item: GlobalNavWidgetModel.Item) {
        bindIcon(item)
        bindTitle(item)
        bindDescription(item)
    }

    private fun bindIcon(item: GlobalNavWidgetModel.Item) {
        val logoUrl = item.logoUrl

        binding?.globalNavCatalogIcon?.setImageUrl(logoUrl)
    }

    private fun bindTitle(item: GlobalNavWidgetModel.Item) {
        val name = item.name

        binding?.globalNavCatalogTitle?.text = name
    }

    private fun bindDescription(item: GlobalNavWidgetModel.Item) {
        val info = item.info

        binding?.globalNavCatalogDescription?.text = info
    }
}