package com.tokopedia.discovery2.repository.calendarwidget

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import javax.inject.Inject

class CalendarWidgetGQLRepository @Inject constructor() : BaseRepository(),
    CalenderWidgetRepository {
    override suspend fun getCalendarWidget(
        componentId: String,
        queryParamterMap: MutableMap<String, Any>,
        pageEndPoint: String,
        calendarComponentName: String?
    ): Pair<ArrayList<ComponentsItem>, String?> {
        val response = (getGQLData(
            GQL_COMPONENT,
            DataResponse::class.java,
            Utils.getComponentsGQLParams(
                componentId,
                pageEndPoint,
                Utils.getQueryString(queryParamterMap)
            ),
            GQL_COMPONENT_QUERY_NAME
        ) as DataResponse)

        val componentData = response.data.component?.data
        val componentProperties = response.data.component?.properties
        val creativeName = response.data.component?.creativeName ?: ""
        val nextPage = response.data.component?.compAdditionalInfo?.nextPage
        val list = DiscoveryDataMapper().mapListToComponentList(
            componentData,
            ComponentNames.CalendarWidgetItem.componentName,
            componentProperties,
            creativeName
        )

        return Pair(list, nextPage)
    }
}