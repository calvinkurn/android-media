package com.tokopedia.tokomart.categorylist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomart.categorylist.domain.model.CategoryListResponse
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<CategoryListResponse>(graphqlRepository) {

    fun execute(): List<CategoryListResponse> {
        //Dummy data
        val iconUrl = "https://img.icons8.com/cotton/2x/circled-down--v2.png"
        val childCategoryList = listOf(
            CategoryListResponse("21", "All category"),
            CategoryListResponse("22", "L2 Category with image", iconUrl),
            CategoryListResponse("23", "L2 Category with image", iconUrl),
            CategoryListResponse("24", "L2 Category with image", iconUrl),
            CategoryListResponse("25", "L2 Category with image", iconUrl),
            CategoryListResponse("26", "L2 Category with image", iconUrl)
        )

        return listOf(
            CategoryListResponse("1", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("2", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("3", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("4", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("5", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("6", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("7", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("8", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("9", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("10", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("11", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("12", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("13", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("14", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("15", "L1 Category", iconUrl, childCategoryList),
            CategoryListResponse("16", "L1 Category", iconUrl, childCategoryList)
        )
    }
}