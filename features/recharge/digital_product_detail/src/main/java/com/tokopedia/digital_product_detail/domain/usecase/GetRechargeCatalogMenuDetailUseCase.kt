package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.data.TelcoCatalogMenuDetailData
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetRechargeCatalogMenuDetailUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<TelcoCatalogMenuDetailData>(graphqlRepository) {

    private var params: RequestParams = RequestParams.EMPTY
    private var isLoadFromCloud = false

    override suspend fun executeOnBackground(): TelcoCatalogMenuDetailData {
        val gqlRequest = GraphqlRequest(
            CommonTopupBillsGqlQuery.catalogMenuDetail,
            TelcoCatalogMenuDetailData::class.java,
            params.parameters
        )
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest),
            GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * FIVE_MINS_CACHE_DURATION).build())

        val error = gqlResponse.getError(TelcoCatalogMenuDetailData::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(TelcoCatalogMenuDetailData::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun setMenuDetailParams(menuId: Int) {
        params = RequestParams.create().apply {
            putInt(KEY_MENU_ID, menuId)
        }
    }

    fun setLoadFromCloud(bool: Boolean) {
        isLoadFromCloud = bool
    }

    companion object {
        private const val KEY_MENU_ID = "menuID"
        const val FIVE_MINS_CACHE_DURATION = 5
    }
}