package com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel

import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.MovetoGroupAdapterTypeFactory

class MovetoGroupItemModel(val result: GroupListDataItem) : MovetoGroupModel() {

    override fun type(typesFactory: MovetoGroupAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}