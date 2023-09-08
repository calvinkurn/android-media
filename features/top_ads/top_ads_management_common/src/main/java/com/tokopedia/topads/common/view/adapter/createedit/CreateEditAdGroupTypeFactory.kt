package com.tokopedia.topads.common.view.adapter.createedit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.common.databinding.TopadsCreateApplyItemAdGroupBinding
import com.tokopedia.topads.common.databinding.TopadsCreateDailyBudgetItemAdGroupBinding
import com.tokopedia.topads.common.databinding.TopadsCreateEditItemAdsPotentialAdGroupBinding
import com.tokopedia.topads.common.databinding.TopadsCreateEditItemEditAdGroupBinding
import com.tokopedia.topads.common.databinding.TopadsCreateItemAdGroupBinding
import com.tokopedia.topads.common.domain.model.createedit.CreateAdGroupDailyBudgetItemUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateAdGroupItemUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateApplyAdGroupItemUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemUiModel
import com.tokopedia.topads.common.view.adapter.createedit.viewholder.CreateAdGroupDailyBudgetItemViewHolder
import com.tokopedia.topads.common.view.adapter.createedit.viewholder.CreateAdGroupItemViewHolder
import com.tokopedia.topads.common.view.adapter.createedit.viewholder.CreateApplyAdGroupItemViewHolder
import com.tokopedia.topads.common.view.adapter.createedit.viewholder.CreateEditAdGroupAdsPotentialViewHolder
import com.tokopedia.topads.common.view.adapter.createedit.viewholder.CreateEditAdGroupItemViewHolder

class CreateEditAdGroupTypeFactory : BaseAdapterTypeFactory() {

    fun type(uiModel: CreateEditAdGroupItemUiModel): Int {
        return CreateEditAdGroupItemViewHolder.LAYOUT
    }

    fun type(editAdGroupItemAdsPotentialUiModel: CreateEditAdGroupItemAdsPotentialUiModel): Int {
        return CreateEditAdGroupAdsPotentialViewHolder.LAYOUT
    }

    fun type(createAdGroupItemUiModel: CreateAdGroupItemUiModel): Int {
        return CreateAdGroupItemViewHolder.LAYOUT
    }

    fun type(createAdGroupDailyBudgetItemUiModel: CreateAdGroupDailyBudgetItemUiModel): Int {
        return CreateAdGroupDailyBudgetItemViewHolder.LAYOUT
    }

    fun type(createApplyAdGroupItemUiModel: CreateApplyAdGroupItemUiModel): Int {
        return CreateApplyAdGroupItemViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {

            CreateEditAdGroupAdsPotentialViewHolder.LAYOUT -> {
                val viewBinding = TopadsCreateEditItemAdsPotentialAdGroupBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                CreateEditAdGroupAdsPotentialViewHolder(viewBinding)
            }

            CreateAdGroupItemViewHolder.LAYOUT -> {
                val viewBinding = TopadsCreateItemAdGroupBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                CreateAdGroupItemViewHolder(viewBinding)
            }

            CreateEditAdGroupItemViewHolder.LAYOUT -> {
                val viewBinding = TopadsCreateEditItemEditAdGroupBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                CreateEditAdGroupItemViewHolder(viewBinding)
            }

            CreateAdGroupDailyBudgetItemViewHolder.LAYOUT -> {
                val viewBinding = TopadsCreateDailyBudgetItemAdGroupBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                CreateAdGroupDailyBudgetItemViewHolder(viewBinding)
            }

            CreateApplyAdGroupItemViewHolder.LAYOUT -> {
                val viewBinding = TopadsCreateApplyItemAdGroupBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                CreateApplyAdGroupItemViewHolder(viewBinding)
            }

            else -> super.createViewHolder(parent, type)
        }
    }
}
