package com.tokopedia.review.feature.createreputation.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.feature.createreputation.model.BadRatingCategoriesResponse
import javax.inject.Inject

@GqlQuery(
    GetBadRatingCategoryUseCase.REPUTATION_FORM_QUERY_CLASS_NAME,
    GetBadRatingCategoryUseCase.BAD_RATING_CATEGORY_QUERY
)
class GetBadRatingCategoryUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<BadRatingCategoriesResponse>(graphqlRepository) {

    companion object {
        const val REPUTATION_FORM_QUERY_CLASS_NAME = "BadRatingCategoryQuery"
        const val BAD_RATING_CATEGORY_QUERY = """
            {
              productrevGetBadRatingCategory {
                list {
                  badRatingCategoryID
                  description
                  isTextFocused
                }
              }
            }
        """
    }

    init {
        setGraphqlQuery(BadRatingCategoryQuery.GQL_QUERY)
        setTypeClass(BadRatingCategoriesResponse::class.java)
    }
}