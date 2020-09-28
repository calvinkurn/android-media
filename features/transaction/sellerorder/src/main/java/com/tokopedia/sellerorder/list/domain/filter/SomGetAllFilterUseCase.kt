package com.tokopedia.sellerorder.list.domain.filter

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.list.data.model.SomListAllFilter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/05/20.
 */
class SomGetAllFilterUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomListAllFilter.Data>) {
    private var resultFilterList = SomListAllFilter.Data()

    suspend fun execute(query: String) {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomListAllFilter.Data::class.java)

        resultFilterList = useCase.executeOnBackground()
    }

    fun getShippingListResult(): Result<MutableList<SomListAllFilter.Data.ShippingList>> {
        return try {
            Success(resultFilterList.orderShippingList.toMutableList())
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    fun getStatusOrderListResult(): Result<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>> {
        return try {
            Success(resultFilterList.orderFilterSomSingle.statusList.toMutableList())
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    fun getOrderTypeListResult(): Result<MutableList<SomListAllFilter.Data.OrderType>> {
        return try {
            Success(resultFilterList.orderTypeList.toMutableList())
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

}