package com.tokopedia.shop.pageheader.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.BUTTON_FOLLOW
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel

class ShopActionButtonWidgetAdapter(
        typeFactoryComponent: ShopHeaderActionButtonWidgetAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopHeaderActionButtonWidgetAdapterTypeFactory>(typeFactoryComponent) {

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun addComponents(listComponent: List<BaseShopHeaderComponentUiModel>) {
        addElement(listComponent)
        notifyDataSetChanged()
    }

    fun notifyButtonWidgetAdapter() {
        notifyDataSetChanged()
    }

    fun getFollowButtonViewHolder(uiModel: ShopHeaderButtonComponentUiModel): View? {
        return recyclerView?.findViewHolderForLayoutPosition(visitables.indexOf(uiModel))?.itemView
    }

}