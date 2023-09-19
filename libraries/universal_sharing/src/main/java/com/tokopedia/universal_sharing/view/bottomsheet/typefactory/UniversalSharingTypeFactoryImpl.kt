package com.tokopedia.universal_sharing.view.bottomsheet.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.universal_sharing.view.bottomsheet.viewholder.UniversalSharingGlobalErrorViewHolder
import com.tokopedia.universal_sharing.view.bottomsheet.viewholder.UniversalSharingPostPurchaseProductViewHolder
import com.tokopedia.universal_sharing.view.bottomsheet.viewholder.UniversalSharingPostPurchaseShopTitleViewHolder
import com.tokopedia.universal_sharing.view.model.UniversalSharingGlobalErrorUiModel
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseProductUiModel
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseShopTitleUiModel

/**
 * Reuse this interface for other recycler view
 * Add override new type here, add it to createViewHolder too
 */
class UniversalSharingTypeFactoryImpl() : BaseAdapterTypeFactory(), UniversalSharingTypeFactory {

    override fun type(uiModel: UniversalSharingPostPurchaseShopTitleUiModel): Int {
        return UniversalSharingPostPurchaseShopTitleViewHolder.LAYOUT
    }

    override fun type(uiModel: UniversalSharingPostPurchaseProductUiModel): Int {
        return UniversalSharingPostPurchaseProductViewHolder.LAYOUT
    }

    override fun type(uiModel: UniversalSharingGlobalErrorUiModel): Int {
        return UniversalSharingGlobalErrorViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            UniversalSharingPostPurchaseShopTitleViewHolder.LAYOUT -> {
                UniversalSharingPostPurchaseShopTitleViewHolder(parent)
            }
            UniversalSharingPostPurchaseProductViewHolder.LAYOUT -> {
                UniversalSharingPostPurchaseProductViewHolder(parent)
            }
            UniversalSharingGlobalErrorViewHolder.LAYOUT -> {
                UniversalSharingGlobalErrorViewHolder(parent)
            }
            else -> super.createViewHolder(parent, type)
        }
    }
}
