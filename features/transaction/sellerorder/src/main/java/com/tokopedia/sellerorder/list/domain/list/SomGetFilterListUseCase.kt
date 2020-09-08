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

    init {
        useCase.setGraphqlQuery(QUERY)
    }

    suspend fun execute(): Result<SomListFilter.Data.OrderFilterSom> {
        useCase.setTypeClass(SomListFilter.Data::class.java)

        return try {
            val resultFilterList = useCase.executeOnBackground().orderFilterSom
            Success(resultFilterList)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    companion object {
        private val QUERY = """
            query OrderFilterSom {
              orderFilterSom{
                status_list{
                    order_status_id
                  key
                  order_status
                  order_status_amount
                  is_checked
                  child_status {
                    id
                    key
                    text
                    amount
                    is_checked
                  }
                }
                shipping_list{
                  shipping_id
                  shipping_code
                  shipping_name
                  shipping_desc
                  status
                  img_logo
                }
              }
            }
        """.trimIndent()
    }
}