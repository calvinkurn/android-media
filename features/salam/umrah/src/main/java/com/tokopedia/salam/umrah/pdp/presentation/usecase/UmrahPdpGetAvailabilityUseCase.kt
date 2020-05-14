package com.tokopedia.salam.umrah.pdp.presentation.usecase

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

class UmrahPdpGetAvailabilityUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<UmrahProductModel>(graphqlRepository) {

    suspend fun executeUsecase(rawQuery: String, slugName: String): Result<UmrahProductModel.UmrahProduct> {
        return try {
            val params = mapOf(UMRAH_PDP_SLUG_NAME to slugName)
            this.setGraphqlQuery(rawQuery)
            this.setRequestParams(params)
            this.setTypeClass(UmrahProductModel::class.java)
            this.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            val umrahProductData = this.executeOnBackground().umrahProduct
            Success(umrahProductData)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    companion object {
        const val UMRAH_PDP_SLUG_NAME = "slugName"
    }
}