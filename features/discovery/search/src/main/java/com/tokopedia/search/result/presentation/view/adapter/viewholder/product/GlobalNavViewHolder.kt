package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel
import com.tokopedia.search.result.presentation.view.listener.GlobalNavWidgetListener
import com.tokopedia.search.result.presentation.view.widget.GlobalNavWidget

class GlobalNavViewHolder(itemView: View,
                          private val globalNavWidgetListener: GlobalNavWidgetListener?)
    : AbstractViewHolder<GlobalNavViewModel>(itemView) {

    private val globalNavWidget: GlobalNavWidget

    init {
        globalNavWidget = itemView.findViewById(R.id.globalNavWidget)
    }

    override fun bind(element: GlobalNavViewModel) {
        globalNavWidget.setData(element, object : GlobalNavWidget.ClickListener {
            override fun onClickItem(item: GlobalNavViewModel.Item) {
                globalNavWidgetListener?.onGlobalNavWidgetClicked(item, element.keyword)
            }

            override fun onclickSeeAllButton(globalNavViewModel: GlobalNavViewModel) {
                globalNavWidgetListener?.onGlobalNavWidgetClickSeeAll(globalNavViewModel)
            }
        })
    }

    companion object {

        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_global_nav_view_holder
    }
}
