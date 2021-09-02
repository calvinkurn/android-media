package com.tokopedia.sellerhome.settings.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.settings.view.adapter.viewholder.*
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.*

class ShopSecondaryInfoAdapterTypeFactory : BaseAdapterTypeFactory(),
    ShopSecondaryInfoAdapterFactory {

    override fun type(uiModel: ShopOperationalWidgetUiModel): Int =
        ShopOperationalViewHolder.LAYOUT_RES

    override fun type(uiModel: ShopStatusWidgetUiModel): Int = ShopStatusViewHolder.LAYOUT_RES

    override fun type(uiModel: RMTransactionWidgetUiModel): Int = RMTransactionViewHolder.LAYOUT_RES

    override fun type(uiModel: ReputationBadgeWidgetUiModel): Int =
        ReputationBadgeViewHolder.LAYOUT_RES

    override fun type(uiModel: ShopFollowersWidgetUiModel): Int = ShopFollowersViewHolder.LAYOUT_RES

    override fun type(uiModel: FreeShippingWidgetUiModel): Int = FreeShippingViewHolder.LAYOUT_RES

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopOperationalViewHolder.LAYOUT_RES -> ShopOperationalViewHolder(parent)
            ShopStatusViewHolder.LAYOUT_RES -> ShopStatusViewHolder(parent)
            RMTransactionViewHolder.LAYOUT_RES -> RMTransactionViewHolder(parent)
            ReputationBadgeViewHolder.LAYOUT_RES -> ReputationBadgeViewHolder(parent)
            ShopFollowersViewHolder.LAYOUT_RES -> ShopFollowersViewHolder(parent)
            FreeShippingViewHolder.LAYOUT_RES -> FreeShippingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}