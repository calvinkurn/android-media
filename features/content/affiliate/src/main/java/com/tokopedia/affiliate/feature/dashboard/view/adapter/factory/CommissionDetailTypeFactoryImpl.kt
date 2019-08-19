package com.tokopedia.affiliate.feature.dashboard.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.CommissionDetailHeaderViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.CommissionDetailItemViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder.EmptyCommissionViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailHeaderViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailItemViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyCommissionViewModel

/**
 * @author by yoasfs on 2019-08-12
 */

class CommissionDetailTypeFactoryImpl: BaseAdapterTypeFactory(), CommissionDetailTypeFactory {

    override fun type(commissionDetailHeaderViewModel: CommissionDetailHeaderViewModel): Int {
        return CommissionDetailHeaderViewHolder.LAYOUT
    }

    override fun type(commissionDetailItemViewModel: CommissionDetailItemViewModel): Int {
        return CommissionDetailItemViewHolder.LAYOUT
    }

    override fun type(emptyCommissionViewModel: EmptyCommissionViewModel): Int {
        return EmptyCommissionViewHolder.LAYOUT
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when (type) {
            CommissionDetailHeaderViewHolder.LAYOUT -> CommissionDetailHeaderViewHolder(parent) as AbstractViewHolder<Visitable<*>>
            CommissionDetailItemViewHolder.LAYOUT -> CommissionDetailItemViewHolder(parent) as AbstractViewHolder<Visitable<*>>
            EmptyCommissionViewHolder.LAYOUT -> EmptyCommissionViewHolder(parent) as AbstractViewHolder<Visitable<*>>
            else -> super.createViewHolder(parent, type)
        }
    }
}