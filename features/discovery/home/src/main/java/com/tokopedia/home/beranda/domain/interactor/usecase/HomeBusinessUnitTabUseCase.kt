package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitDataRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitTabRepository
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import javax.inject.Inject

class HomeBusinessUnitTabUseCase @Inject constructor(
    private val homeBusinessUnitTabRepository: HomeBusinessUnitTabRepository,
    private val homeBusinessUnitDataRepository: HomeBusinessUnitDataRepository
) {
    suspend fun getBuUnitTab(homeDataModel: HomeDynamicChannelModel, position: Int, updateWidget:() -> Unit) {
        try {
            val data = homeBusinessUnitTabRepository.executeOnBackground()
            (homeDataModel.list.getOrNull(position) as? NewBusinessUnitWidgetDataModel)?.let{ buWidget ->
                val buWidgetData = buWidget.copy(
                    tabList = data.tabBusinessList,
                    backColor = data.widgetHeader.backColor,
                    contentsList = data.tabBusinessList.withIndex().map { BusinessUnitDataModel(tabName = it.value.name, tabPosition = it.index) })
                homeDataModel.updateWidgetModel(visitable = buWidgetData, position = position) {
                    updateWidget.invoke()
                }
            }
        }
        catch (e: Exception) {
            (homeDataModel.list.getOrNull(position) as? NewBusinessUnitWidgetDataModel)?.let{ buWidget ->
                homeDataModel.updateWidgetModel(visitable = buWidget.copy(tabList = listOf()), position = position) {
                    updateWidget.invoke()
                }
            }
        }
    }

    suspend fun getBusinessUnitData(tabId: Int, position: Int, tabName: String, homeDataModel: HomeDynamicChannelModel, updateWidget:() -> Unit){
        try {
            homeBusinessUnitDataRepository.setParams(tabId, position, tabName)
            val data = homeBusinessUnitDataRepository.executeOnBackground()
            homeDataModel.list.withIndex().find { it.value is NewBusinessUnitWidgetDataModel }?.let { buModel ->
                val oldBuData = buModel.value as NewBusinessUnitWidgetDataModel
                val newBuList = oldBuData.contentsList.copy().toMutableList()
                newBuList[position] = newBuList[position].copy(list = data)
                homeDataModel.updateWidgetModel(visitable = oldBuData.copy(contentsList = newBuList), position = buModel.index) {
                    updateWidget.invoke()
                }
            }
        }
        catch (e: Exception){
            // show error
            homeDataModel.list.withIndex().find { it.value is NewBusinessUnitWidgetDataModel }?.let { buModel ->
                val oldBuData = buModel.value as NewBusinessUnitWidgetDataModel
                val newBuList = oldBuData.contentsList.copy().toMutableList()
                newBuList[position] = newBuList[position].copy(list = listOf())
                val newList = homeDataModel.list.copy().toMutableList()
                newList[buModel.index] = oldBuData.copy(contentsList = newBuList)
                homeDataModel.updateWidgetModel(visitable = oldBuData.copy(contentsList = newBuList), position = buModel.index) {
                    updateWidget.invoke()
                }
            }
        }
    }
}