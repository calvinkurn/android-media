package com.tokopedia.sellerorder.list.domain.list

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.list.data.model.SomListAllFilter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 08/05/20.
 */
class SomGetOrderStatusListUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomListAllFilter.Data>) {

    suspend fun execute(query: String): Result<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomListAllFilter.Data::class.java)

        return try {
            val resultAllFilter = useCase.executeOnBackground().orderFilterSomSingle.statusList.toMutableList()
            Success(resultAllFilter)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }
}