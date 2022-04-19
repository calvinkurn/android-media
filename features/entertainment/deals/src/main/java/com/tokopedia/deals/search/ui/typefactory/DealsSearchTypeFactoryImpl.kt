package com.tokopedia.deals.search.ui.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.deals.search.model.visitor.*
import com.tokopedia.deals.search.listener.DealsSearchListener
import com.tokopedia.deals.search.ui.adapter.viewholder.*

class DealsSearchTypeFactoryImpl(
        private val dealsSearchListener: DealsSearchListener
) : BaseAdapterTypeFactory(), DealsSearchTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            SectionTitleViewHolder.LAYOUT -> SectionTitleViewHolder(parent)
            VoucherViewHolder.LAYOUT -> VoucherViewHolder(parent, dealsSearchListener)
            MerchantViewHolder.LAYOUT -> MerchantViewHolder(parent, dealsSearchListener)
            InterestingCollectionViewHolder.LAYOUT -> InterestingCollectionViewHolder(parent, dealsSearchListener)
            RecentViewHolder.LAYOUT -> RecentViewHolder(parent, dealsSearchListener)
            MoreBrandViewHolder.LAYOUT -> MoreBrandViewHolder(parent, dealsSearchListener)
            NotFoundViewHolder.LAYOUT -> NotFoundViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(viewItem: SectionTitleModel): Int {
        return SectionTitleViewHolder.LAYOUT
    }

    override fun type(viewItem: VoucherModel): Int {
        return VoucherViewHolder.LAYOUT
    }

    override fun type(item: MerchantModelModel): Int {
        return MerchantViewHolder.LAYOUT
    }

    override fun type(viewItem: CuratedModel): Int {
        return InterestingCollectionViewHolder.LAYOUT
    }

    override fun type(viewItem: RecentModel): Int {
        return RecentViewHolder.LAYOUT
    }

    override fun type(viewItem: MoreBrandModel): Int {
        return MoreBrandViewHolder.LAYOUT
    }

    override fun type(viewItem: NotFoundModel): Int {
        return NotFoundViewHolder.LAYOUT
    }

}