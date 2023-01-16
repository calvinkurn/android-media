package com.tokopedia.unifyorderhistory.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.unifyorderhistory.data.model.UohFilterCategory
import javax.inject.Inject

@GqlQuery("GetUOHFilterCategoryQuery", GetUohFilterCategoryUseCase.query)
class GetUohFilterCategoryUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<Unit, UohFilterCategory>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: Unit): UohFilterCategory {
        return repository.request(GetUOHFilterCategoryQuery(), params)
    }

    companion object {
        const val query = """
            query GetUOHFilterCategory {
                uohFilterCategory {
                    filtersV2 {
                      label
                      value
                      isPrimary
                    }
                    categories {
                      value
                      label
                      description
                      category_group
                    }
                }
            }
        """
    }
}
