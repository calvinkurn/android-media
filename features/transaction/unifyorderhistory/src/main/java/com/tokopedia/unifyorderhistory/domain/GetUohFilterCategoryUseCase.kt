package com.tokopedia.unifyorderhistory.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.unifyorderhistory.data.model.UohFilterCategory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class GetUohFilterCategoryUseCase  @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) {

    suspend fun executeSuspend(): Result<UohFilterCategory.Data> {
        return try {
            val request = GraphqlRequest(QUERY, UohFilterCategory.Data::class.java)
            val response = gqlRepository.response(listOf(request)).getSuccessData<UohFilterCategory.Data>()
            Success(response)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    companion object {
        val QUERY = """
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
            """.trimIndent()
    }
}
