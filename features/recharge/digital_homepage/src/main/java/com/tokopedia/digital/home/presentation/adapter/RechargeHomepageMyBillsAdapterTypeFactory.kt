package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.databinding.ContentRechargeHomepageMyBillsLastItemBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeMyBillsItemBinding
import com.tokopedia.digital.home.model.RechargeHomepageMyBillsWidgetModel
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageMyBillsWidgetViewHolder

/**
 * created by @bayazidnasir on 20/10/2022
 */

class RechargeHomepageMyBillsAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(model: RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsItemModel): Int {
        return RechargeHomepageMyBillsWidgetViewHolder.RechargeHomepageMyBillsItemViewHolder.LAYOUT
    }

    fun type(model: RechargeHomepageMyBillsWidgetModel.RechargeHomepageMyBillsLastItemModel): Int {
        return RechargeHomepageMyBillsWidgetViewHolder.RechargeHomepageMyBillsLastItemViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RechargeHomepageMyBillsWidgetViewHolder.RechargeHomepageMyBillsItemViewHolder.LAYOUT -> {
                val binding = ViewRechargeHomeMyBillsItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                RechargeHomepageMyBillsWidgetViewHolder.RechargeHomepageMyBillsItemViewHolder(
                    binding
                )
            }
            RechargeHomepageMyBillsWidgetViewHolder.RechargeHomepageMyBillsLastItemViewHolder.LAYOUT -> {
                val binding = ContentRechargeHomepageMyBillsLastItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                RechargeHomepageMyBillsWidgetViewHolder.RechargeHomepageMyBillsLastItemViewHolder(
                    binding
                )
            }
            else -> super.createViewHolder(parent, type)
        }
    }
}
