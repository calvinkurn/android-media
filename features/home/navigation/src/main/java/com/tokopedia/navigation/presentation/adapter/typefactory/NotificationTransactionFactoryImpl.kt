package com.tokopedia.navigation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.domain.model.PurchaseNotification
import com.tokopedia.navigation.domain.model.SaleNotification
import com.tokopedia.navigation.presentation.adapter.viewholder.transaction.PurchaseViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.transaction.SaleViewHolder

class NotificationTransactionFactoryImpl: BaseAdapterTypeFactory(), NotificationTransactionFactory {

    override fun type(purchaseNotification: PurchaseNotification): Int = PurchaseViewHolder.LAYOUT

    override fun type(saleNotification: SaleNotification): Int = SaleViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PurchaseViewHolder.LAYOUT -> PurchaseViewHolder(parent)
            SaleViewHolder.LAYOUT -> SaleViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}