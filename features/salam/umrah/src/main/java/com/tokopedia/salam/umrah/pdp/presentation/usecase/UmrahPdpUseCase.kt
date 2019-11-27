package com.tokopedia.salam.umrah.pdp.presentation.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by M on 4/11/19
 */

class UmrahPdpUseCase @Inject constructor(private val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(rawQuery: String, slugName: String): Result<UmrahProductModel.UmrahProduct> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
                .build())

        useCase.clearRequest()

        val params = mapOf(UMRAH_PDP_SLUG_NAME to slugName)
        return try {
            val graphqlRequest = GraphqlRequest(rawQuery, UmrahProductModel::class.java, params)
            useCase.addRequest(graphqlRequest)
            val umrahProduct = useCase.executeOnBackground().getSuccessData<UmrahProductModel>().umrahProduct
            Success(umrahProduct)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    companion object {
        const val UMRAH_PDP_SLUG_NAME = "slugName"
    }
}