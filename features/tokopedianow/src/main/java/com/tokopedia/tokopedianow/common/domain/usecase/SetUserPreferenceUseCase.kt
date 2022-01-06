package com.tokopedia.tokopedianow.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference.SetUserPreferenceData
import com.tokopedia.tokopedianow.common.domain.query.SetUserPreferenceQuery
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SetUserPreferenceUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    companion object {
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_WAREHOUSE_ID = "warehouse_id"
        private const val PARAM_SERVICE_TYPE = "service_type"
        private const val PARAM_WAREHOUSES = "warehouses"
    }

    private val graphql by lazy { GraphqlUseCase<SetUserPreference>(graphqlRepository) }

    suspend fun execute(localCacheModel: LocalCacheModel): SetUserPreferenceData {
        graphql.apply {
            val requestParams = RequestParams().apply {
                putInt(PARAM_SHOP_ID, localCacheModel.shop_id.toInt())
                putInt(PARAM_WAREHOUSE_ID, localCacheModel.warehouse_id.toInt())
                putString(PARAM_SERVICE_TYPE, localCacheModel.service_type)
                putObject(PARAM_WAREHOUSES, localCacheModel.warehouses)
            }.parameters

            setGraphqlQuery(SetUserPreferenceQuery)
            setTypeClass(SetUserPreference::class.java)
            setRequestParams(requestParams)
        }

        return graphql.executeOnBackground().response.data
    }
}