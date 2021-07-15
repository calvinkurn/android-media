package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.di.module.query.QueryHomeWallet
import com.tokopedia.home.beranda.domain.model.walletapp.WalletAppData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetWalletAppBalanceUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : UseCase<WalletAppData>() {

    private var graphqlUseCase: GraphqlUseCase<WalletAppData>? = null

    init {
        graphqlUseCase = GraphqlUseCase(graphqlRepository)
        graphqlUseCase?.setGraphqlQuery(QueryHomeWallet.walletAppQuery)
        graphqlUseCase?.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase?.setTypeClass(WalletAppData::class.java)
    }
    override suspend fun executeOnBackground(): WalletAppData = withContext(Dispatchers.IO){
        graphqlUseCase?.clearCache()
        graphqlUseCase?.setRequestParams(getParams().parameters)
        graphqlUseCase?.executeOnBackground()?: WalletAppData()
    }

    fun getParams(): RequestParams {
        val reqParams= RequestParams.create()
        val params= mapOf(
            PARTNER_CODE to arrayOf(PARTNER_PEMUDA)
        )
        reqParams.putAll(params)
        return reqParams
    }

    companion object {
        private const val PARTNER_CODE = "partnerCode"

        private const val PARTNER_PEMUDA = "PEMUDA"
    }
}