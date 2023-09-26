package com.tokopedia.home.beranda.data.newatf.icon

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.AtfRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeIconRepository
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import javax.inject.Inject

class DynamicIconRepository @Inject constructor(
    private val homeIconRepository: HomeIconRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
    atfDao: AtfDao,
): AtfRepository(atfDao) {

    @SuppressLint("PII Data Exposure")
    override suspend fun getData(atfMetadata: AtfMetadata) {
        val iconParam = Bundle().apply {
            putString(
                HomeIconRepository.PARAM,
                atfMetadata.param
            )
            putString(
                HomeIconRepository.PARAM_LOCATION,
                homeChooseAddressRepository.getRemoteData()
                    ?.convertToLocationParams()
            )
        }
        val data = homeIconRepository.getRemoteData(iconParam)
        val atfData = AtfData(atfMetadata, data.dynamicHomeIcon, isCache = false)
        emitAndSaveData(atfData)
    }
}
