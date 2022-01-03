package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitRepository
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import javax.inject.Inject

class HomeBusinessUnitUseCase @Inject constructor(
    private val homeBusinessUnitRepository: HomeBusinessUnitRepository
) {
    suspend fun getBuUnitTab(homeDataModel: HomeDynamicChannelModel, position: Int) {
        try {
            val data = homeBusinessUnitRepository.executeOnBackground()
            (homeDataModel.list.getOrNull(position) as? NewBusinessUnitWidgetDataModel)?.let{ buWidget ->
                val buWidgetData = buWidget.copy(
                    tabList = data.tabBusinessList,
                    backColor = data.widgetHeader.backColor,
                    contentsList = data.tabBusinessList.withIndex().map { BusinessUnitDataModel(tabName = it.value.name, tabPosition = it.index) })
                homeDataModel.updateWidgetModel(visitable = buWidgetData, position = position) {}
            }
        }
        catch (e: Exception) {
            (homeDataModel.list.getOrNull(position) as? NewBusinessUnitWidgetDataModel)?.let{ buWidget ->
                homeDataModel.updateWidgetModel(visitable = buWidget.copy(tabList = listOf()), position = position) {}
            }
        }
    }
}