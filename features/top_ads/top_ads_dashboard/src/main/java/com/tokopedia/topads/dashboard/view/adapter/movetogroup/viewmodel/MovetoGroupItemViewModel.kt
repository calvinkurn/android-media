package com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel

import com.tokopedia.topads.dashboard.data.model.GroupListDataItem
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.MovetoGroupAdapterTypeFactory

class MovetoGroupItemViewModel(val result: GroupListDataItem): MovetoGroupViewModel() {

    override fun type(typesFactory: MovetoGroupAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}