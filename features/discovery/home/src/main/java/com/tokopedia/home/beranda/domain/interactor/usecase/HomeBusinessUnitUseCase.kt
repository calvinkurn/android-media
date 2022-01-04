package com.tokopedia.home.beranda.domain.interactor.usecase

import android.content.res.Resources
import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitDataRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitTabRepository
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
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
            throw Resources.NotFoundException()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getBusinessUnitData(tabId: Int, position: Int, tabName: String) : List<BusinessUnitItemDataModel>{
        homeBusinessUnitDataRepository.setParams(tabId, position, tabName)
        return homeBusinessUnitDataRepository.executeOnBackground()
    }
}