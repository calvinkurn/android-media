package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalnavwidget.GlobalNavWidgetListener
import com.tokopedia.globalnavwidget.GlobalNavWidgetModel
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.GlobalNavDataView
import com.tokopedia.search.result.presentation.view.listener.GlobalNavListener
import kotlinx.android.synthetic.main.search_global_nav_view_holder.view.*

class GlobalNavViewHolder(
        itemView: View,
        private val globalNavListener: GlobalNavListener?
) : AbstractViewHolder<GlobalNavDataView>(itemView) {

    override fun bind(element: GlobalNavDataView) {
        val globalNavWidgetModel = createGlobalNavWidgetModel(element)
        val globalNavWidgetListener = createGlobalNavWidgetListener(element)

        itemView.globalNavWidget?.setData(globalNavWidgetModel, globalNavWidgetListener)
    }

    private fun createGlobalNavWidgetModel(element: GlobalNavDataView): GlobalNavWidgetModel {
        return GlobalNavWidgetModel(
                source = element.source,
                keyword = element.keyword,
                title = element.title,
                navTemplate = element.navTemplate,
                background = element.background,
                clickSeeAllApplink = element.seeAllApplink,
                clickSeeAllUrl = element.seeAllUrl,
                itemList = mutableListOf<GlobalNavWidgetModel.Item>().also {
                    for(item in element.itemList) {
                        it.add(convertGlobalNavWidgetItemModel(item))
                    }
                }
        )
    }

    private fun convertGlobalNavWidgetItemModel(item: GlobalNavDataView.Item): GlobalNavWidgetModel.Item {
        return GlobalNavWidgetModel.Item(
                categoryName = item.categoryName,
                name = item.name,
                info = item.info,
                imageUrl = item.imageUrl,
                clickItemApplink = item.applink,
                clickItemUrl = item.url,
                subtitle = item.subtitle,
                strikethrough = item.strikethrough,
                backgroundUrl = item.backgroundUrl,
                logoUrl = item.logoUrl,
                position = item.position
        )
    }

    private fun createGlobalNavWidgetListener(element: GlobalNavDataView): GlobalNavWidgetListener {
        return object : GlobalNavWidgetListener {
            override fun onClickItem(item: GlobalNavWidgetModel.Item) {
                globalNavListener?.onGlobalNavWidgetClicked(convertGlobalNavWidgetItemViewModel(item), element.keyword)
            }

            override fun onClickSeeAll(globalNavWidgetModel: GlobalNavWidgetModel) {
                globalNavListener?.onGlobalNavWidgetClickSeeAll(element)
            }
        }
    }

    private fun convertGlobalNavWidgetItemViewModel(
            globalNavWidgetModelItem: GlobalNavWidgetModel.Item
    ): GlobalNavDataView.Item {
        return GlobalNavDataView.Item(
                globalNavWidgetModelItem.categoryName,
                globalNavWidgetModelItem.name,
                globalNavWidgetModelItem.info,
                globalNavWidgetModelItem.imageUrl,
                globalNavWidgetModelItem.clickItemApplink,
                globalNavWidgetModelItem.clickItemUrl,
                globalNavWidgetModelItem.subtitle,
                globalNavWidgetModelItem.strikethrough,
                globalNavWidgetModelItem.backgroundUrl,
                globalNavWidgetModelItem.logoUrl,
                globalNavWidgetModelItem.position
        )
    }

    companion object {

        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_global_nav_view_holder
    }
}
