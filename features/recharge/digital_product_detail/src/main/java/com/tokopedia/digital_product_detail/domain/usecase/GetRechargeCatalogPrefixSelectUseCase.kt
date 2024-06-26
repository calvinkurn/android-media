package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetRechargeCatalogPrefixSelectUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val remoteConfig: FirebaseRemoteConfigImpl
): GraphqlUseCase<TelcoCatalogPrefixSelect>(graphqlRepository) {

    private var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): TelcoCatalogPrefixSelect {
        val gqlRequest = GraphqlRequest(
            CommonTopupBillsGqlQuery.prefixSelectTelco,
            TelcoCatalogPrefixSelect::class.java,
            params.parameters
        )
        val isEnableGqlCache = remoteConfig.getBoolean(RemoteConfigKey.ANDROID_ENABLE_DIGITAL_GQL_CACHE, false)
        val gqlCacheStrategy: GraphqlCacheStrategy.Builder = if (isEnableGqlCache) {
            GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * EXP_TIME)
        } else {
            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD)
        }
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), gqlCacheStrategy.build())

        val error = gqlResponse.getError(TelcoCatalogPrefixSelect::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(TelcoCatalogPrefixSelect::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun setRequestParam(menuId: Int) {
        params = RequestParams.create().apply {
            putInt(KEY_MENU_ID, menuId)
        }
    }

    companion object {
        private const val KEY_MENU_ID = "menuID"
        private const val EXP_TIME = 10
    }
}
