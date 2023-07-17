package com.tokopedia.topads.dashboard.recommendation.data.model.local

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory

interface GroupDetailDataModel : Visitable<GroupDetailAdapterFactory> {
    fun type(): String
    fun isAvailable(): Boolean = true
    fun equalsWith(newItem: GroupDetailDataModel): Boolean
}
