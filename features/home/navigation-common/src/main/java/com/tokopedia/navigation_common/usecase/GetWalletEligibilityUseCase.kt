package com.tokopedia.navigation_common.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.navigation_common.model.wallet.WalletStatus
import com.tokopedia.navigation_common.usecase.pojo.eligibility.WalletAppEligibility
import com.tokopedia.navigation_common.usecase.query.QueryHomeWallet
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetWalletEligibilityUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : UseCase<WalletStatus>() {

    private var graphqlUseCase: GraphqlUseCase<WalletAppEligibility>? = null

    init {
        graphqlUseCase = GraphqlUseCase(graphqlRepository)
        graphqlUseCase?.setGraphqlQuery(QueryHomeWallet.eligibilityQuery)
        graphqlUseCase?.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase?.setTypeClass(WalletAppEligibility::class.java)
    }
    override suspend fun executeOnBackground(): WalletStatus = withContext(Dispatchers.IO){
        graphqlUseCase?.clearCache()
        graphqlUseCase?.setRequestParams(getParams().parameters)
        val data = graphqlUseCase?.executeOnBackground()
        data?.let {
            return@withContext WalletStatus(isGoPointsEligible = it.walletappGetWalletEligible.data.getOrNull(0)?.isEligible?:false)
        }
        return@withContext WalletStatus()
    }

    fun getParams(): RequestParams {
        val reqParams= RequestParams.create()
        val params= mapOf(
            PARTNER_CODE to PARTNER_PEMUDA,
            WALLET_CODE to arrayOf(WALLET_PEMUDA_POINTS)
        )
        reqParams.putAll(params)
        return reqParams
    }

    companion object {
        private const val PARTNER_CODE = "partnerCode"
        private const val WALLET_CODE = "walletCode"

        private const val PARTNER_PEMUDA = "PEMUDA"
        private const val WALLET_PEMUDA_POINTS = "PEMUDAPOINTS"
    }
}