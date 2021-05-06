package com.tokopedia.discovery2.repository.emptystate

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.emptystate.EmptyStateModel

interface EmptyStateRepository {
    fun getEmptyStateData(component: ComponentsItem): EmptyStateModel
}