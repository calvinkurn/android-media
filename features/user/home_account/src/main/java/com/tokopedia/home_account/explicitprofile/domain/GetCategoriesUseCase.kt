package com.tokopedia.home_account.explicitprofile.domain

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.explicitprofile.data.CategoriesDataModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : CoroutineUseCase<Unit, CategoriesDataModel>(
    Dispatchers.IO
) {

    override suspend fun execute(params: Unit): CategoriesDataModel {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
            query {
              explicitprofileGetAllCategories() {
                data {
                  id
                  name
                  imageDisabled
                  imageEnabled
                  templateName
                }
              }
            }
        """.trimIndent()
    }
}