package com.tokopedia.deals.common.domain

import com.tokopedia.deals.search.model.response.CuratedData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by firman on 16/06/20
 */

class GetDealsBrandCategoryActivityUseCase @Inject constructor(
        private val useCase: MultiRequestGraphqlUseCase
){
    suspend fun getChildCategoryResult(): Result<CuratedData>{
        return try {
            val childCategoryRequest = GraphqlRequest(DealsGqlQueries.getChildCategory(), CuratedData::class.java)
            useCase.addRequest(childCategoryRequest)

            val childCategory = useCase.executeOnBackground().getSuccessData<CuratedData>()

            Success(childCategory)
        } catch (throwable: Throwable){
            Fail(throwable)
        }
    }
}