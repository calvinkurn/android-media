package com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel

import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.MovetoGroupAdapterTypeFactory

class MovetoGroupItemViewModel(val result: DataItem): MovetoGroupViewModel() {

    override fun type(typesFactory: MovetoGroupAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}