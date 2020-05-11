package com.tokopedia.sellerorder.list.domain.list

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.list.data.model.SomListFilter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 08/05/20.
 */
class SomGetFilterListUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomListFilter.Data>) {

    suspend fun execute(query: String): Result<MutableList<SomListFilter.Data.OrderFilterSom.StatusList>> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomListFilter.Data::class.java)

        return try {
            val resultFilterList = useCase.executeOnBackground().orderFilterSom.statusList.toMutableList()
            Success(resultFilterList)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }
}