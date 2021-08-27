package com.tokopedia.tokopedianow.recentpurchase.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class RecentPurchaseAdapterTypeFactory : BaseAdapterTypeFactory(), RecentPurchaseTypeFactory {

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return super.createViewHolder(view, type)
    }
}