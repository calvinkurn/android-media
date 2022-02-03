package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.home.beranda.domain.interactor.GetCoroutinePendingCashbackUseCase
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import javax.inject.Inject

class HomePendingCashbackRepository @Inject constructor(
        private val getCoroutinePendingCashbackUseCase: GetCoroutinePendingCashbackUseCase
): HomeRepository<PendingCashback> {
    override suspend fun getRemoteData(bundle: Bundle): PendingCashback {
        return getCoroutinePendingCashbackUseCase.executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): PendingCashback {
        return PendingCashback(

        )
    }
}