package com.tokopedia.v2.home.data.datasource.remote

import com.tokopedia.common_wallet.balance.domain.GetWalletBalanceUseCase
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.common_wallet.pendingcashback.domain.GetPendingCasbackUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.beranda.domain.interactor.GetHomeTokopointsDataUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.v2.home.data.query.TokopointQuery
import com.tokopedia.v2.home.data.query.WalletQuery
import com.tokopedia.v2.home.model.pojo.wallet.Tokopoint
import com.tokopedia.v2.home.model.pojo.wallet.WalletData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WalletRemoteDataSource @Inject constructor(
        private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
        private val getPendingCasbackUseCase: GetPendingCasbackUseCase,
        private val graphqlRepository: GraphqlRepository

){
    suspend fun getWallet() : WalletBalanceModel{
        return withContext(Dispatchers.IO){
            val data = getWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().first()
            data
        }
    }

    suspend fun getTokopoint(): Tokopoint{
        return withContext(Dispatchers.IO){
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

            val gqlRecommendationRequest = GraphqlRequest(
                    TokopointQuery.getQuery(),
                    Tokopoint::class.java
            )
            graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy).getData<Tokopoint>(Tokopoint::class.java)
        }
    }

    suspend fun getPendingCashback(){
        return withContext(Dispatchers.IO){
            val data  = getPendingCasbackUseCase.createObservable(RequestParams.EMPTY)
        }
    }

    suspend fun getWalletData(): WalletData{
        return withContext(Dispatchers.IO){
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

            val gqlRecommendationRequest = GraphqlRequest(
                    WalletQuery.getQuery(),
                    WalletData::class.java
            )
            graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy).getData<WalletData>(WalletData::class.java)
        }
    }
}