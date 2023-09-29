package com.tokopedia.home.beranda.data.newatf.icon

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.AtfRepository
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeIconRepository
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import javax.inject.Inject

/**
 * Created by Frenzel
 */
@HomeScope
class DynamicIconRepository @Inject constructor(
    private val homeIconRepository: HomeIconRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
): AtfRepository() {

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
        val (data, status) = try {
            homeIconRepository.getRemoteData(iconParam).dynamicHomeIcon to AtfKey.STATUS_SUCCESS
        } catch (_: Exception) {
            DynamicHomeIcon() to AtfKey.STATUS_ERROR
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
