package com.tokopedia.v2.home.base

import androidx.lifecycle.LiveData
import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.v2.home.model.pojo.home.HomeData
import com.tokopedia.v2.home.model.pojo.home.PlayCard
import com.tokopedia.v2.home.model.vo.Resource
import com.tokopedia.v2.home.model.vo.WalletDataModel
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getHomeDataWithCache(): LiveData<Resource<HomeData>>
    suspend fun getOldHomeDataWithCache(): GraphqlResponse
    suspend fun getWalletData(): WalletDataModel.WalletAction
    suspend fun getTokopointData(): WalletDataModel.TokopointAction
    suspend fun getPendingCashbackData(): PendingCashback
    suspend fun getPlayCard(): Flow<PlayCard>
}