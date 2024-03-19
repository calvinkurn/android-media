package com.tokopedia.home.beranda.domain.interactor.usecase

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeMissionWidgetRepository
import com.tokopedia.home.beranda.helper.LazyLoadDataMapper
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import com.tokopedia.home_component.usecase.missionwidget.GetMissionWidget
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component.widget.mission.MissionWidgetMapper.getAsHomeComponentHeader
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
                    putString(
                        GetMissionWidget.PARAM,
                        currentMissionWidgetListDataModel.widgetParam
                    )
                }
            )
            val missionWidgetList = LazyLoadDataMapper
                .mapMissionWidgetData(results.getHomeMissionWidget.missions, false)

            val mission4SquareWidgetList = LazyLoadDataMapper
                .map4SquareMissionWidgetData(results.getHomeMissionWidget.missions, false)

            currentMissionWidgetListDataModel.copy(
                header = results.getHomeMissionWidget.header.getAsHomeComponentHeader(),
                missionWidgetList = missionWidgetList,
                mission4SquareWidgetList = mission4SquareWidgetList,
                status = MissionWidgetListDataModel.STATUS_SUCCESS
            )
        } catch (e: Exception) {
            currentMissionWidgetListDataModel.copy(status = MissionWidgetListDataModel.STATUS_ERROR)
        }
    }
}
