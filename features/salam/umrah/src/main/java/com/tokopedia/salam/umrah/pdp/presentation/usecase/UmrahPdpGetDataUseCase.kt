package com.tokopedia.salam.umrah.pdp.presentation.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by M on 4/11/19
 */

class UmrahPdpGetDataUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<UmrahProductModel>(graphqlRepository) {

    suspend fun executeUsecase(rawQuery: String, slugName: String): Result<UmrahProductModel.UmrahProduct> {
        return try {
            val params = mapOf(UMRAH_PDP_SLUG_NAME to slugName)

            setGraphqlQuery(rawQuery)
            setRequestParams(params)
            setTypeClass(UmrahProductModel::class.java)
            setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            val umrahProductData = executeOnBackground().umrahProduct
            Success(umrahProductData)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    companion object {
        const val UMRAH_PDP_SLUG_NAME = "slugName"
    }
}