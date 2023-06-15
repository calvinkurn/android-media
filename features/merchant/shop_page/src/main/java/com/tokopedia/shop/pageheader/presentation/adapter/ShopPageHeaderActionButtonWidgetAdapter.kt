package com.tokopedia.shop.pageheader.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderButtonComponentUiModel

class ShopPageHeaderActionButtonWidgetAdapter(
    typeFactoryComponent: ShopPageHeaderActionButtonWidgetAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopPageHeaderActionButtonWidgetAdapterTypeFactory>(typeFactoryComponent) {

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun addComponents(listComponentPage: List<BaseShopPageHeaderComponentUiModel>) {
        addElement(listComponentPage)
        notifyDataSetChanged()
    }

    fun notifyButtonWidgetAdapter() {
        notifyDataSetChanged()
    }

    fun getFollowButtonViewHolder(uiModel: ShopPageHeaderButtonComponentUiModel): View? {
        return recyclerView?.findViewHolderForLayoutPosition(visitables.indexOf(uiModel))?.itemView
    }
}
