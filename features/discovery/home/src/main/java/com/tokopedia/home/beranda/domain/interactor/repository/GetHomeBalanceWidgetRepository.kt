package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.home.beranda.data.model.GetHomeBalanceWidgetData
import com.tokopedia.home.beranda.domain.interactor.GetHomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import javax.inject.Inject

/**
 * Created by dhaba
 */
class GetHomeBalanceWidgetRepository @Inject constructor(
        private val getHomeBalanceWidgetUseCase: GetHomeBalanceWidgetUseCase
): HomeRepository<GetHomeBalanceWidgetData> {
    override suspend fun getRemoteData(bundle: Bundle): GetHomeBalanceWidgetData {
        return getHomeBalanceWidgetUseCase.executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): GetHomeBalanceWidgetData {
        return GetHomeBalanceWidgetData()
    }
}
