package com.tokopedia.common.topupbills.usecase

import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

/**
 * @author by jessica on 08/04/21
 */
class RechargeCatalogPrefixSelectUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<TelcoCatalogPrefixSelect>,
        private val remoteConfig: RemoteConfig
) {
    /**
     * To fetch all prefixes and its operator of telco and recharge products
     * CacheStrategy: CACHE_FIRST 1 minutes.
     */
    fun execute(
            params: Map<String, Any>,
            onSuccess: (TelcoCatalogPrefixSelect) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        useCase.apply {
            setTypeClass(TelcoCatalogPrefixSelect::class.java)
            setRequestParams(params)
            setGraphqlQuery(CommonTopupBillsGqlQuery.prefixSelectTelco)
            val isEnableGqlCache = remoteConfig.getBoolean(RemoteConfigKey.ANDROID_ENABLE_DIGITAL_GQL_CACHE, false)
            if (isEnableGqlCache) {
                setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * EXP_TIME).build())
            } else {
                setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            }
            execute(onSuccess, onError)
        }
    }

    companion object {
        private const val KEY_MENU_ID = "menuID"
        fun createParams(menuId: Int): Map<String, Any> = mapOf(KEY_MENU_ID to menuId)
        const val EXP_TIME = 10
    }
}
