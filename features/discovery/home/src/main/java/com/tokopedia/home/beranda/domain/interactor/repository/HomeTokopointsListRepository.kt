package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.home.beranda.domain.interactor.GetHomeTokopointsListDataUseCase
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import javax.inject.Inject

class HomeTokopointsListRepository @Inject constructor(
        private val getHomeTokopointsListDataUseCase: GetHomeTokopointsListDataUseCase
): HomeRepository<TokopointsDrawerListHomeData> {
    override suspend fun getRemoteData(bundle: Bundle): TokopointsDrawerListHomeData {
        getHomeTokopointsListDataUseCase.setParams(useNewBalanceWidget = true)
        return getHomeTokopointsListDataUseCase.executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): TokopointsDrawerListHomeData {
        return TokopointsDrawerListHomeData(

        )
    }
}