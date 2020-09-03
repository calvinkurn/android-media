package com.tokopedia.seller_migration_common.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller_migration_common.presentation.adapter.viewholder.CardSellerFeatureViewHolder
import com.tokopedia.seller_migration_common.presentation.adapter.viewholder.SellerFeatureViewHolder
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel

class SellerFeatureAdapterTypeFactory(private val listener: SellerFeatureCarousel.SellerFeatureClickListener?): BaseAdapterTypeFactory() {
    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            CardSellerFeatureViewHolder.LAYOUT -> CardSellerFeatureViewHolder(parent, listener)
            SellerFeatureViewHolder.LAYOUT -> SellerFeatureViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(sellerFeatureUiModel: SellerFeatureUiModel): Int {
        return if (sellerFeatureUiModel.data == null) {
            SellerFeatureViewHolder.LAYOUT
        } else {
            CardSellerFeatureViewHolder.LAYOUT
        }
    }
}