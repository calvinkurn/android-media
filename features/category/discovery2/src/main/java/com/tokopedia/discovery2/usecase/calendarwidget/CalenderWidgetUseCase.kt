package com.tokopedia.discovery2.usecase.calendarwidget

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.calendarwidget.CalenderWidgetRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import javax.inject.Inject

private const val RPC_PAGE_NUMBER = "rpc_page_number"
private const val RPC_PAGE_SIZE = "rpc_page_size"
private const val RPC_NEXT_PAGE = "rpc_next_page"
private const val CALENDAR_PER_PAGE = 20
private const val PAGE_START = 1

class CalenderWidgetUseCase @Inject constructor(private val calenderWidgetRepository: CalenderWidgetRepository) {
    suspend fun loadFirstPageComponents(
        componentId: String,
        pageEndPoint: String,
        productsLimit: Int = CALENDAR_PER_PAGE
    ): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val (calendarWidgetData, nextPage) = calenderWidgetRepository.getCalendarWidget(
                componentId,
                getQueryParameterMap(PAGE_START, productsLimit, it.nextPageKey),
                pageEndPoint, it.name
            )
            it.showVerticalLoader = calendarWidgetData.isNotEmpty()
            it.setComponentsItem(calendarWidgetData, component.tabName)
            it.noOfPagesLoaded = 1
            it.nextPageKey = nextPage
            if (calendarWidgetData.isEmpty()) return true
            it.pageLoadedCounter = 2
            it.verticalProductFailState = false
            return true
        }
        return false
    }

    suspend fun loadCalendarPageComponents(
        componentId: String,
        pageEndPoint: String,
        productsLimit: Int = CALENDAR_PER_PAGE
    ): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val parentComponent = component?.parentComponentId?.let { getComponent(it, pageEndPoint) }

        if (component?.noOfPagesLoaded == 1) return false
        parentComponent?.let { component1 ->
            val (calendarWidgetData, nextPage) = calenderWidgetRepository.getCalendarWidget(
                component1.id,
                getQueryParameterMap(
                    component1.pageLoadedCounter,
                    productsLimit,
                    component1.nextPageKey
                ),
                pageEndPoint, component1.name
            )
            component1.nextPageKey = nextPage

            if (calendarWidgetData.isEmpty()) {
                component1.showVerticalLoader = false
            } else {
                component1.pageLoadedCounter += 1
                component1.showVerticalLoader = true
                (component1.getComponentsItem() as ArrayList<ComponentsItem>).addAll(
                    calendarWidgetData
                )
            }
            component1.verticalProductFailState = false
            return true
        }
        return false
    }

    suspend fun getCarouselPaginatedData(
        componentId: String,
        pageEndPoint: String,
        productsLimit: Int = CALENDAR_PER_PAGE
    ): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        component?.let {
            it.properties?.let { properties ->
                if (properties.limitProduct && properties.limitNumber.toIntOrZero() == it.getComponentsItem()?.size) return false
            }
            val (calendarWidgetData, nextPage) = calenderWidgetRepository.getCalendarWidget(
                componentId,
                getQueryParameterMap(
                    it.pageLoadedCounter,
                    productsLimit,
                    it.nextPageKey,
                ),
                pageEndPoint,
                it.name
            )
            component.nextPageKey = nextPage
            if (calendarWidgetData.isEmpty()) return false else it.pageLoadedCounter += 1
            (it.getComponentsItem() as ArrayList<ComponentsItem>).addAll(calendarWidgetData)
            return true
        }
        return false
    }

    private fun getQueryParameterMap(
        page: Int,
        productsLimit: Int,
        nextPageKey: String?,
    ): MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[RPC_PAGE_SIZE] = productsLimit.toString()
        queryParameterMap[RPC_PAGE_NUMBER] = page.toString()
        queryParameterMap[RPC_NEXT_PAGE] = nextPageKey ?: ""
        return queryParameterMap
    }

}