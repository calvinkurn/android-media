package com.tokopedia.discovery2.analytics

import com.tokopedia.discovery2.data.ComponentSourceData
import com.tokopedia.discovery2.data.ComponentTracker
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.TopLevelTab
import com.tokopedia.discovery2.data.UnknownTab
import com.tokopedia.discovery2.datamapper.getComponent

object TrackingMapper {

    fun List<DataItem>?.setAppLog(
        tracker: ComponentTracker?,
        source: ComponentSourceData,
    ) {
        tracker?.let { componentTracker ->
            this?.forEachIndexed { index, dataItem ->
                dataItem.itemPosition = index
                dataItem.setAppLog(componentTracker)
                dataItem.source = source
            }
        }
    }

    fun getTopLevelParentComponent(component: ComponentsItem?): TopLevelTab {
        try {
            if (component == null) return UnknownTab

            val parentComponentId = component.parentComponentId
            if (parentComponentId.isEmpty()) return component.getSelectedTab()

            val parentComponent = getComponent(parentComponentId, component.pageEndPoint)
            return getTopLevelParentComponent(parentComponent)

        } catch (e: StackOverflowError) {
            return UnknownTab
        }
    }

    private fun ComponentsItem.getSelectedTab(): TopLevelTab {
        return data?.let { dataItem ->
            val index = dataItem.indexOfFirst { it.isSelected }

            if (index < 0) return UnknownTab

            return TopLevelTab(dataItem[index].name.orEmpty(), index)
        } ?: UnknownTab
    }
}
