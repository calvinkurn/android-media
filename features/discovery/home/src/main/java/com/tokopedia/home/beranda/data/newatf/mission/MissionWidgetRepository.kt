package com.tokopedia.home.beranda.data.newatf.mission

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.AtfRepository
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeMissionWidgetRepository
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import com.tokopedia.home_component.usecase.missionwidget.GetMissionWidget
import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import javax.inject.Inject

/**
 * Created by Frenzel
 */
@HomeScope
class MissionWidgetRepository @Inject constructor(
    private val missionWidgetRepository: HomeMissionWidgetRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
): AtfRepository() {

    @SuppressLint("PII Data Exposure")
    override suspend fun getData(atfMetadata: AtfMetadata) {
        val missionParam = Bundle().apply {
            putString(
                GetMissionWidget.BANNER_LOCATION_PARAM,
                homeChooseAddressRepository.getRemoteData()
                    ?.convertToLocationParams()
            )
        }
        val (data, status) = try {
            missionWidgetRepository.getRemoteData(missionParam).getHomeMissionWidget to AtfKey.STATUS_SUCCESS
        } catch (_: Exception) {
            HomeMissionWidgetData.GetHomeMissionWidget() to AtfKey.STATUS_ERROR
        }
        val atfData = AtfData(
            atfMetadata = atfMetadata,
            atfContent = data,
            atfStatus = status,
            isCache = false,
        )
        emitData(atfData)
    }
}
