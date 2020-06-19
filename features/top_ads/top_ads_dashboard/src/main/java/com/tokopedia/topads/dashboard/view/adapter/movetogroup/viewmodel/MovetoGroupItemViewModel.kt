package com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel

import com.tokopedia.topads.dashboard.data.model.DashGroupListResponse
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.MovetoGroupAdapterTypeFactory

class MovetoGroupItemViewModel(val result: DashGroupListResponse.GetTopadsDashboardGroups.GroupDataItem): MovetoGroupViewModel() {

    override fun type(typesFactory: MovetoGroupAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}