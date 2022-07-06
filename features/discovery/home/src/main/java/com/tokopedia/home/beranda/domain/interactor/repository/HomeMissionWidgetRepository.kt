package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home_component.usecase.missionwidget.GetMissionWidget
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by dhaba
 */
class HomeMissionWidgetRepository @Inject constructor(
         private val getMissionWidget: Lazy<GetMissionWidget>
)
    : HomeRepository<HomeMissionWidgetData.HomeMissionWidget> {

    override suspend fun getRemoteData(bundle: Bundle): HomeMissionWidgetData.HomeMissionWidget {
        getMissionWidget.get().generateParam(bundle)
        return getMissionWidget.get().executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): HomeMissionWidgetData.HomeMissionWidget {
        return HomeMissionWidgetData.HomeMissionWidget()
    }
}