package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalnavwidget.GlobalNavWidgetListener
import com.tokopedia.globalnavwidget.GlobalNavWidgetModel
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel
import com.tokopedia.search.result.presentation.view.listener.GlobalNavListener
import kotlinx.android.synthetic.main.search_global_nav_view_holder.view.*

class GlobalNavViewHolder(
        itemView: View,
        private val globalNavListener: GlobalNavListener?
) : AbstractViewHolder<GlobalNavViewModel>(itemView) {

    override fun bind(element: GlobalNavViewModel) {
        val globalNavWidgetModel = createGlobalNavWidgetModel(element)
        val globalNavWidgetListener = createGlobalNavWidgetListener(element)

        itemView.globalNavWidget?.setData(globalNavWidgetModel, globalNavWidgetListener)
    }

    private fun createGlobalNavWidgetModel(element: GlobalNavViewModel): GlobalNavWidgetModel {
        return GlobalNavWidgetModel(
                keyword = element.keyword,
                title = element.title,
                clickSeeAllApplink = element.seeAllApplink,
                clickSeeAllUrl = element.seeAllUrl,
                itemList = mutableListOf<GlobalNavWidgetModel.Item>().also {
                    for(item in element.itemList) {
                        it.add(convertGlobalNavWidgetItemModel(item))
                    }
                }
        )
    }

    private fun convertGlobalNavWidgetItemModel(item: GlobalNavViewModel.Item): GlobalNavWidgetModel.Item {
        return GlobalNavWidgetModel.Item(
                name = item.name,
                info = item.info,
                imageUrl = item.imageUrl,
                clickItemApplink = item.applink,
                clickItemUrl = item.url,
                position = item.position
        )
    }

    private fun createGlobalNavWidgetListener(element: GlobalNavViewModel): GlobalNavWidgetListener {
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
    ): GlobalNavViewModel.Item {
        return GlobalNavViewModel.Item(
                globalNavWidgetModelItem.name,
                globalNavWidgetModelItem.info,
                globalNavWidgetModelItem.imageUrl,
                globalNavWidgetModelItem.clickItemApplink,
                globalNavWidgetModelItem.clickItemUrl,
                globalNavWidgetModelItem.position
        )
    }

    companion object {

        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_global_nav_view_holder
    }
}
