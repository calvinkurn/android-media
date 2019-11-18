package com.tokopedia.v2.home.base

import androidx.lifecycle.LiveData
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.v2.home.model.pojo.home.HomeData
import com.tokopedia.v2.home.model.pojo.wallet.WalletData
import com.tokopedia.v2.home.model.vo.Resource

interface HomeRepository {
    suspend fun getHomeDataWithCache(): LiveData<Resource<HomeData>>
    suspend fun getOldHomeDataWithCache(): GraphqlResponse
    suspend fun getWalletData(): WalletData
}