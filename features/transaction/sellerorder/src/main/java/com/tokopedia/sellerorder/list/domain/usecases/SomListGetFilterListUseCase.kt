package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.list.domain.mapper.FilterResultMapper
import com.tokopedia.sellerorder.list.domain.model.SomListFilterResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 08/05/20.
 */
class SomListGetFilterListUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<SomListFilterResponse.Data>) : BaseGraphqlUseCase() {

    init {
        useCase.setGraphqlQuery(QUERY)
    }

    suspend fun execute(): Success<SomListFilterUiModel> {
        useCase.setTypeClass(SomListFilterResponse.Data::class.java)

        val resultFilterList = useCase.executeOnBackground().orderFilterSom
        return Success(FilterResultMapper.mapResponseToUiModel(resultFilterList))
    }

    companion object {
        private val QUERY = """
            query OrderFilterSom {
              orderFilterSom {
                status_list {
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
              }
            }
        """.trimIndent()
    }
}