package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitDataRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeBusinessUnitTabRepository
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import javax.inject.Inject

class HomeBusinessUnitUseCase @Inject constructor(
    private val homeBusinessUnitTabRepository: HomeBusinessUnitTabRepository,
    private val homeBusinessUnitDataRepository: HomeBusinessUnitDataRepository
) {
    suspend fun getBusinessUnitTab() : HomeWidget {
        return homeBusinessUnitTabRepository.executeOnBackground()
    }

    suspend fun getBusinessUnitData(tabId: Int, position: Int, tabName: String) : List<BusinessUnitItemDataModel>{
        homeBusinessUnitDataRepository.setParams(tabId, position, tabName)
        return homeBusinessUnitDataRepository.executeOnBackground()
    }
}