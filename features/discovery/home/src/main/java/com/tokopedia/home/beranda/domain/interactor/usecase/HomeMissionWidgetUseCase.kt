package com.tokopedia.home.beranda.domain.interactor.usecase

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeMissionWidgetRepository
import com.tokopedia.home.beranda.helper.LazyLoadDynamicChannelHelper
import com.tokopedia.home_component.usecase.missionwidget.GetMissionWidget
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.convertToLocationParams
import javax.inject.Inject

/**
 * Created by dhaba
 */
class HomeMissionWidgetUseCase @Inject constructor(
    private val missionWidgetRepository: HomeMissionWidgetRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository
) {

    suspend fun onMissionWidgetRefresh(currentMissionWidgetListDataModel: MissionWidgetListDataModel): MissionWidgetListDataModel {
        return try {
            val results = missionWidgetRepository.getRemoteData(
                Bundle().apply {
                    putString(
                        GetMissionWidget.BANNER_LOCATION_PARAM,
                        homeChooseAddressRepository.getRemoteData()?.convertToLocationParams()
                    )
                }
            )
            val resultList =
                LazyLoadDynamicChannelHelper.convertMissionWidgetDataList(results.getHomeMissionWidget.missions)
            currentMissionWidgetListDataModel.copy(
                missionWidgetList = resultList,
                status = MissionWidgetListDataModel.STATUS_SUCCESS
            )
        } catch (e: Exception) {
            currentMissionWidgetListDataModel.copy(status = MissionWidgetListDataModel.STATUS_ERROR)
        }
    }
}
