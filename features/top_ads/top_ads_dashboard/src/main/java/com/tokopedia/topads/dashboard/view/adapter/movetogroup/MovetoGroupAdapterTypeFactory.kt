package com.tokopedia.topads.dashboard.view.adapter.movetogroup

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewholder.MovetoGroupViewHolder
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupItemModel


interface MovetoGroupAdapterTypeFactory {

    fun type(model: MovetoGroupItemModel): Int

    fun type(model: MovetoGroupEmptyModel): Int

    fun holder(type: Int, view: View): MovetoGroupViewHolder<*>

}