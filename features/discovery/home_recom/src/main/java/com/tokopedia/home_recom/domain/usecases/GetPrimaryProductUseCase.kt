package com.tokopedia.home_recom.domain.usecases

import android.content.Context
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home_recom.*
import com.tokopedia.home_recom.model.entity.PrimaryProductEntity
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.recommendation_widget_common.ext.toQueryParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetPrimaryProductUseCase @Inject constructor(
        private val context: Context,
        private val graphqlUseCase: GraphqlUseCase<PrimaryProductEntity>
) : UseCase<PrimaryProductEntity>() {

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        graphqlUseCase.setTypeClass(PrimaryProductEntity::class.java)
    }

    private val parameter = RequestParams.create()

    override suspend fun executeOnBackground(): PrimaryProductEntity {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(parameter.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    fun setParameter(productId: String, queryParam: String){
        parameter.parameters.clear()
        parameter.putString(PARAM_PRODUCT_ID_STRING, productId)
        parameter.putString(PARAM_QUERY_PARAM, ChooseAddressUtils.getLocalizingAddressData(context)?.toQueryParam(queryParam)
                ?: queryParam)
        parameter.putString(PARAM_X_SOURCE, DEFAULT_X_SOURCE)
    }

}