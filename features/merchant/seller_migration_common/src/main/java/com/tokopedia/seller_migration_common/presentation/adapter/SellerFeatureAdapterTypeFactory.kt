package com.tokopedia.seller_migration_common.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller_migration_common.presentation.adapter.viewholder.SellerFeatureViewHolder
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel

class SellerFeatureAdapterTypeFactory: BaseAdapterTypeFactory() {
    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SellerFeatureViewHolder.LAYOUT -> SellerFeatureViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(sellerFeatureUiModel: SellerFeatureUiModel): Int {
        return SellerFeatureViewHolder.LAYOUT
    }
}