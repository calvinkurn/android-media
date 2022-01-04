package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitDataRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitTabRepository
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import com.tokopedia.home_component.model.ChannelModel
import javax.inject.Inject

class HomeBusinessUnitUseCase @Inject constructor(
    private val homeBusinessUnitTabRepository: HomeBusinessUnitTabRepository,
    private val homeBusinessUnitDataRepository: HomeBusinessUnitDataRepository
) {
    suspend fun getBusinessUnitTab(homeDataModel: HomeDynamicChannelModel, position: Int) : NewBusinessUnitWidgetDataModel {
        try {
            val data =  homeBusinessUnitTabRepository.executeOnBackground()
            (homeDataModel.list.getOrNull(position) as? NewBusinessUnitWidgetDataModel)?.let{ buWidget ->
                return buWidget.copy(
                    tabList = data.tabBusinessList,
                    backColor = data.widgetHeader.backColor,
                    contentsList = data.tabBusinessList.withIndex().map { BusinessUnitDataModel(tabName = it.value.name, tabPosition = it.index) })
            }
        } catch (e: Exception) {
            (homeDataModel.list.getOrNull(position) as? NewBusinessUnitWidgetDataModel)?.let{ buWidget ->
                return buWidget.copy(tabList = listOf())
            }
        }
        return NewBusinessUnitWidgetDataModel(channelModel = ChannelModel(id = "", groupId = ""))
    }

    suspend fun getBusinessUnitData(tabId: Int, position: Int, tabName: String, homeDataModel: HomeDynamicChannelModel) : Pair<NewBusinessUnitWidgetDataModel, Int> {
        try {
            homeBusinessUnitDataRepository.setParams(tabId, position, tabName)
            val data = homeBusinessUnitDataRepository.executeOnBackground()
            homeDataModel.list.withIndex().find { it.value is NewBusinessUnitWidgetDataModel }?.let { buModel ->
                val oldBuData = buModel.value as NewBusinessUnitWidgetDataModel
                val newBuList = oldBuData.contentsList.copy().toMutableList()
                newBuList[position] = newBuList[position].copy(list = data)
                return Pair(oldBuData.copy(contentsList = newBuList), buModel.index)
            }
        }
        catch (e: Exception) {
            homeDataModel.list.withIndex().find { it.value is NewBusinessUnitWidgetDataModel }?.let { buModel ->
                val oldBuData = buModel.value as NewBusinessUnitWidgetDataModel
                val newBuList = oldBuData.contentsList.copy().toMutableList()
                newBuList[position] = newBuList[position].copy(list = listOf())
                val newList = homeDataModel.list.copy().toMutableList()
                newList[buModel.index] = oldBuData.copy(contentsList = newBuList)
                return Pair(oldBuData.copy(contentsList = newBuList), buModel.index)
            }
        }
        val defaultIndexPosition = 0
        return Pair(NewBusinessUnitWidgetDataModel(channelModel = ChannelModel(id = "", groupId = "")), defaultIndexPosition)
    }
}