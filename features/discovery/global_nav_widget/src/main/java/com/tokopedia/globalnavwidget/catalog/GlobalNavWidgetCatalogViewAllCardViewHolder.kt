package com.tokopedia.globalnavwidget.catalog

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.globalnavwidget.GlobalNavWidgetListener
import com.tokopedia.globalnavwidget.GlobalNavWidgetModel
import com.tokopedia.globalnavwidget.R
import com.tokopedia.globalnavwidget.databinding.GlobalNavWidgetCatalogViewAllCardLayoutBinding
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class GlobalNavWidgetCatalogViewAllCardViewHolder(
    itemView: View,
    private val globalNavWidgetListener: GlobalNavWidgetListener,
    private val globalNavWidgetModel: GlobalNavWidgetModel
): RecyclerView.ViewHolder(itemView)  {

    private var context: Context = itemView.context
    private var binding: GlobalNavWidgetCatalogViewAllCardLayoutBinding? by viewBinding()

    companion object {
        val LAYOUT = R.layout.global_nav_widget_catalog_view_all_card_layout
    }

    internal fun bind() {
        setTitleSize()
        bindViewAllCardClick()
    }

    private fun setTitleSize() {
        binding?.globalNavCatalogViewAllCard?.titleView?.apply {
            setType(Typography.DISPLAY_3)
            setWeight(Typography.BOLD)
        }
    }

    private fun bindViewAllCardClick() {
        val text = context.getString(R.string.catalog_global_nav_view_all_cta)
        binding?.globalNavCatalogViewAllCard?.setCta(text) {
            globalNavWidgetListener.onClickSeeAll(globalNavWidgetModel)
        }
    }
}