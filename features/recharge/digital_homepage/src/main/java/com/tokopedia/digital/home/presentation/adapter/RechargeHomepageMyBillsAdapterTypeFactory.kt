package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.model.RechargeHomepageMyBillsWidgetModel
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageMyBillsWidgetViewHolder

/**
 * created by @bayazidnasir on 20/10/2022
 */

class RechargeHomepageMyBillsAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(model: RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsItemModel) : Int {
        return RechargeHomepageMyBillsWidgetViewHolder.RechargeHomepageMyBillsItemViewHolder.LAYOUT
    }

    fun type(model: RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsLastItemModel) : Int {
        return RechargeHomepageMyBillsWidgetViewHolder.RechargeHomepageMyBillsLastItemViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            RechargeHomepageMyBillsWidgetViewHolder.RechargeHomepageMyBillsItemViewHolder.LAYOUT ->
                RechargeHomepageMyBillsWidgetViewHolder.RechargeHomepageMyBillsItemViewHolder(parent)
            RechargeHomepageMyBillsWidgetViewHolder.RechargeHomepageMyBillsLastItemViewHolder.LAYOUT ->
                RechargeHomepageMyBillsWidgetViewHolder.RechargeHomepageMyBillsLastItemViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
