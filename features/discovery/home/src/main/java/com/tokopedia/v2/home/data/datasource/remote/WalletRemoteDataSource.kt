package com.tokopedia.v2.home.data.datasource.remote

import com.tokopedia.common_wallet.balance.domain.GetWalletBalanceUseCase
import com.tokopedia.common_wallet.pendingcashback.domain.GetPendingCasbackUseCase
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import com.tokopedia.v2.home.data.query.TokopointQuery
import com.tokopedia.v2.home.data.query.WalletQuery
import com.tokopedia.v2.home.model.pojo.wallet.TokopointData
import com.tokopedia.v2.home.model.pojo.wallet.WalletData
import com.tokopedia.v2.home.model.vo.Resource
import com.tokopedia.v2.home.model.vo.WalletDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WalletRemoteDataSource @Inject constructor(
        private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
        private val getPendingCasbackUseCase: GetPendingCasbackUseCase,
        private val graphqlRepository: GraphqlRepository

){
    suspend fun getWallet() : WalletDataModel.WalletAction{
        return withContext(Dispatchers.IO){
            val data = getWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().first()
            WalletDataModel.WalletAction(
                    linked = data.link,
                    balance = data.balance,
                    labelTitle = data.titleText,
                    appLinkBalance = data.applinks,
                    labelActionButton = data.actionBalanceModel?.labelAction ?: "",
                    visibleActionButton = data.actionBalanceModel?.visibility != null && data.actionBalanceModel?.visibility.equals("1"),
                    appLinkActionButton = data.actionBalanceModel?.applinks ?: "",
                    abTags = data.abTags ?: listOf(),
                    pointBalance = data.pointBalance,
                    rawPointBalance = data.rawPointBalance,
                    cashBalance = data.cashBalance,
                    rawCashBalance = data.rawCashBalance,
                    walletType = data.walletType,
                    showAnnouncement = data.isShowAnnouncement,
                    status = Resource.Status.SUCCESS
            )
        }
    }

    suspend fun getTokopoint(): WalletDataModel.TokopointAction{
        return withContext(Dispatchers.IO){
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

            val gqlRecommendationRequest = GraphqlRequest(
                    TokopointQuery.getQuery(),
                    TokopointData::class.java
            )
            val response = graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
            val tokopointData = response.getSuccessData<TokopointData>()
            WalletDataModel.TokopointAction(tokopointData.tokopoint, status = Resource.Status.SUCCESS)
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