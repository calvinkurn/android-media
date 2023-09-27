package com.tokopedia.home.beranda.data.newatf.mission

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.AtfRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeMissionWidgetRepository
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import com.tokopedia.home_component.usecase.missionwidget.GetMissionWidget
import javax.inject.Inject

class MissionWidgetRepository @Inject constructor(
    private val missionWidgetRepository: HomeMissionWidgetRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
    atfDao: AtfDao,
): AtfRepository(atfDao) {

    @SuppressLint("PII Data Exposure")
    override suspend fun getData(atfMetadata: AtfMetadata) {
        val iconParam = Bundle().apply {
            putString(
                GetMissionWidget.BANNER_LOCATION_PARAM,
                homeChooseAddressRepository.getRemoteData()
                    ?.convertToLocationParams()
            )
        }
        val data = missionWidgetRepository.getRemoteData(iconParam)
        val atfData = AtfData(atfMetadata, data.getHomeMissionWidget, isCache = false)
        emitAndSaveData(atfData)
    }
}
