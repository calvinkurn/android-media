package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitDataRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitTabRepository
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import javax.inject.Inject

class HomeBusinessUnitUseCase @Inject constructor(
    private val homeBusinessUnitTabRepository: HomeBusinessUnitTabRepository,
    private val homeBusinessUnitDataRepository: HomeBusinessUnitDataRepository
) {
    suspend fun getBusinessUnitTab(buModel: NewBusinessUnitWidgetDataModel) : NewBusinessUnitWidgetDataModel {
        return try {
            val data =  homeBusinessUnitTabRepository.executeOnBackground()
            buModel.copy(
                tabList = data.tabBusinessList,
                backColor = data.widgetHeader.backColor,
                contentsList = data.tabBusinessList.withIndex()
                    .map {
                        BusinessUnitDataModel(
                            tabId = it.value.id.toString(),
                            tabName = it.value.name,
                            tabPosition = it.index,
                            channelId = buModel.channelId,
                            campaignCode = buModel.campaignCode,
                        )
                    }
            )
        } catch (e: Exception) {
            buModel.copy(tabList = listOf())
        }
    }

    suspend fun getBusinessUnitData(
        tabId: Int,
        position: Int,
        tabName: String,
        homeDataModel: HomeDynamicChannelModel,
        buModel: NewBusinessUnitWidgetDataModel,
        buModelIndex: Int
    ): NewBusinessUnitWidgetDataModel {
        return try {
            homeBusinessUnitDataRepository.setParams(tabId, position, tabName)
            val data = homeBusinessUnitDataRepository.executeOnBackground()
            val newBuList = buModel.contentsList.copy().toMutableList()
            if (newBuList.size > position) {
                newBuList[position] = newBuList[position].copy(list = data)
            }
            else {
                newBuList.add(newBuList[position].copy(list = data))
            }
            buModel.copy(contentsList = newBuList)
        } catch (e: Exception) {
            val newBuList = buModel.contentsList.copy().toMutableList()
            if (newBuList.size > position) {
                newBuList[position] = newBuList[position].copy(list = listOf())
            }
            else {
                newBuList.add(BusinessUnitDataModel(tabPosition = position))
            }
            val newList = homeDataModel.list.copy().toMutableList()
            if(newList.size > buModelIndex) {
                newList[buModelIndex] = buModel.copy(contentsList = newBuList)
            }
            else {
                newList.add(buModel.copy(contentsList = newBuList))
            }
            buModel.copy(contentsList = newBuList)
        }
    }
}
