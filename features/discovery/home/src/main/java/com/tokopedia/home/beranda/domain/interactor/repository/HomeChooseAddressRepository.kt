package com.tokopedia.home.beranda.domain.interactor.repository

import android.content.Context
import android.os.Bundle
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import javax.inject.Inject

/**
 * @author by devara on 2020-02-18
 */

class HomeChooseAddressRepository @Inject constructor(
        @ApplicationContext val context: Context
)
    : HomeRepository<LocalCacheModel?> {

    companion object {
        const val WIDGET_PARAM = "widgetParam"
    }

    override suspend fun getRemoteData(bundle: Bundle): LocalCacheModel? {
        return ChooseAddressUtils.getLocalizingAddressData(context = context)
    }

    override suspend fun getCachedData(bundle: Bundle): LocalCacheModel? {
        return getRemoteData()
    }
}