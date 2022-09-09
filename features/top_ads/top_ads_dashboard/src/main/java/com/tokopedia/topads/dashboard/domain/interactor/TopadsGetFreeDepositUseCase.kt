package com.tokopedia.topads.dashboard.domain.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.ExpiryDateResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopadsGetFreeDepositUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
) : GraphqlUseCase<ExpiryDateResponse>() {

    fun execute(response: (ExpiryDateResponse.TopAdsGetFreeDeposit) -> Unit) {
        setGraphqlQuery(GQL_QUERY)
        setRequestParams(mapOf(TopAdsDashboardConstant.PARAM_SHOP_ID to userSession.shopId))
        setTypeClass(ExpiryDateResponse::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
            .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.`val`())
            .setSessionIncluded(true)
            .build())
        execute({
            response.invoke(it.topAdsGetFreeDeposit)
        }, {
            it.printStackTrace()
        })
    }

    companion object {
        private val GQL_QUERY = """
            query topAdsGetFreeDeposit(${'$'}shopID:String!) {
              topAdsGetFreeDeposit(shopID:${'$'}shopID) {
                nominal
                depositID
                status
                expiryDate
                pendingRebateCredit
              }
            }

        """.trimIndent()
    }
}