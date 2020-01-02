package com.tokopedia.v2.home.data.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.v2.home.base.HomeRepository
import com.tokopedia.v2.home.base.NetworkBoundResource
import com.tokopedia.v2.home.data.datasource.local.dao.HomeDao
import com.tokopedia.v2.home.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.v2.home.data.datasource.remote.WalletRemoteDataSource
import com.tokopedia.v2.home.model.pojo.home.HomeData
import com.tokopedia.v2.home.model.pojo.home.PlayCard
import com.tokopedia.v2.home.model.vo.Resource
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl(
        private val homeDao: HomeDao,
        private val homeRemoteDataSource: HomeRemoteDataSource,
        private val walletRemoteDataSource: WalletRemoteDataSource
) : HomeRepository{
    override suspend fun getHomeDataWithCache(): LiveData<Resource<HomeData>> {
        return object : NetworkBoundResource<HomeData, GraphqlResponse>(){
            override fun processResponse(response: GraphqlResponse): HomeData {
                response.getError(HomeData::class.java)?.let {
                    if (it.isNotEmpty()) {
                        if (!TextUtils.isEmpty(it[0].message)){
                            throw Throwable(it[0].message)
                        }
                    }
                }
                return response.getSuccessData()
            }

            override suspend fun saveCallResults(items: HomeData) {
                homeDao.save(items)
            }

            override fun shouldFetch(data: HomeData?): Boolean {
                return true
            }

            override suspend fun loadFromDb(): HomeData {
                return homeDao.getHomeData()
            }

            override suspend fun createCallAsync(): GraphqlResponse {
                return homeRemoteDataSource.getHomeData()
            }

        }.build().asLiveData()
    }

    override suspend fun getOldHomeDataWithCache(): GraphqlResponse {
        return homeRemoteDataSource.getOldHomeData()
    }

    override suspend fun getWalletData() = walletRemoteDataSource.getWallet()

    override suspend fun getTokopointData() = walletRemoteDataSource.getTokopoint()

    override suspend fun getPendingCashbackData() = walletRemoteDataSource.getPendingCashback()

    override fun getPlayCard(): Flow<PlayCard> {
        return null
    }
}