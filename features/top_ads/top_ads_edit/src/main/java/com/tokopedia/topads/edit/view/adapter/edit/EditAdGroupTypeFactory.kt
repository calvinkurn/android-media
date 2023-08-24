package com.tokopedia.topads.edit.view.adapter.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.edit.databinding.TopadsEditItemEditAdGroupBinding
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemAdsPotentialUiModel
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemUiModel
import com.tokopedia.topads.edit.view.viewholder.EditAdGroupItemViewHolder

class EditAdGroupTypeFactory : BaseAdapterTypeFactory() {

    fun type(uiModel: EditAdGroupItemUiModel): Int {
        return EditAdGroupItemViewHolder.LAYOUT
    }

    fun type(editAdGroupItemAdsPotentialUiModel: EditAdGroupItemAdsPotentialUiModel): Int {

    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            EditAdGroupItemViewHolder.LAYOUT -> {
                val viewBinding = TopadsEditItemEditAdGroupBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                EditAdGroupItemViewHolder(viewBinding)
            }

            else -> super.createViewHolder(parent, type)
        }
    }
}