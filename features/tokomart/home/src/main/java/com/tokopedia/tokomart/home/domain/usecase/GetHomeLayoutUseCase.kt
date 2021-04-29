package com.tokopedia.tokomart.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomart.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import javax.inject.Inject

class GetHomeLayoutUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetHomeLayoutResponse>(graphqlRepository) {

    fun execute(): List<HomeLayoutResponse> {
        // Dummy data
        return listOf(
            HomeLayoutResponse("1", "Kategori"),
            HomeLayoutResponse("1", "Promo spesial")
        )
    }
}