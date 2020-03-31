package com.tokopedia.shop_showcase.shop_showcase_add.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.model.AddShowcaseHeader
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewholder.AddShowcaseHeaderViewHolder

class ShopShowcaseAddTypeFactory : BaseAdapterTypeFactory(), TypeFactoryViewHolder {

    override fun type(addShowcaseHeader: AddShowcaseHeader): Int {
        return AddShowcaseHeaderViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            AddShowcaseHeaderViewHolder.LAYOUT -> AddShowcaseHeaderViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}