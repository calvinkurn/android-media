package com.tokopedia.topads.dashboard.view.adapter.movetogroup

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewholder.MovetoGroupEmptyViewHolder
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewholder.MovetoGroupItemViewHolder
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewholder.MovetoGroupViewHolder
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupItemModel

class MovetoGroupAdapterTypeFactoryImpl(var itemSelected: ((pos: Int) -> Unit)) : MovetoGroupAdapterTypeFactory {

    override fun type(model: MovetoGroupItemModel) = MovetoGroupItemViewHolder.LAYOUT

    override fun type(model: MovetoGroupEmptyModel) = MovetoGroupEmptyViewHolder.LAYOUT

    override fun holder(type: Int, view: View): MovetoGroupViewHolder<*> {
        return when (type) {
            MovetoGroupEmptyViewHolder.LAYOUT -> MovetoGroupEmptyViewHolder(view)
            MovetoGroupItemViewHolder.LAYOUT -> MovetoGroupItemViewHolder(view, itemSelected)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}