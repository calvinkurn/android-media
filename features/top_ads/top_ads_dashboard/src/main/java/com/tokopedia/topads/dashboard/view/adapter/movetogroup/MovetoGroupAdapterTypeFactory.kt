package com.tokopedia.topads.dashboard.view.adapter.movetogroup

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupItemViewModel
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewholder.MovetoGroupViewHolder


interface MovetoGroupAdapterTypeFactory {

    fun type(model: MovetoGroupItemViewModel): Int

    fun type(model: MovetoGroupEmptyViewModel): Int

    fun holder(type: Int, view: View): MovetoGroupViewHolder<*>

}