package com.tokopedia.tokomart.categorylist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomart.categorylist.domain.model.CategoryListResponse
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<CategoryListResponse>(graphqlRepository) {

    fun execute(): List<CategoryListResponse> {
        //Dummy data
        val iconUrl = "https://img.icons8.com/cotton/2x/circled-down--v2.png"
        return listOf(
            CategoryListResponse("1", "Collapsed Category", iconUrl),
            CategoryListResponse("2", "Expanded Category", iconUrl, listOf(
                CategoryListResponse("21", "L2 Category without image"),
                CategoryListResponse("22", "L2 Category with image", iconUrl),
                CategoryListResponse("23", "L2 Category with image", iconUrl),
                CategoryListResponse("24", "L2 Category with image", iconUrl)
            )),
            CategoryListResponse("3", "Collapsed Category", iconUrl),
            CategoryListResponse("4", "Collapsed Category", iconUrl),
            CategoryListResponse("5", "Collapsed Category", iconUrl),
            CategoryListResponse("6", "Collapsed Category", iconUrl),
            CategoryListResponse("7", "Collapsed Category", iconUrl),
            CategoryListResponse("8", "Collapsed Category", iconUrl),
            CategoryListResponse("9", "Collapsed Category", iconUrl)
        )
    }
}