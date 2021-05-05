package com.tokopedia.tokomart.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomart.home.constant.HomeLayoutType
import com.tokopedia.tokomart.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import javax.inject.Inject

class GetHomeLayoutUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetHomeLayoutResponse>(graphqlRepository) {

    fun execute(): List<HomeLayoutResponse> {
        // Dummy data
        return listOf(
            HomeLayoutResponse("1", "Promo spesial", HomeLayoutType.SECTION),
            HomeLayoutResponse("2", "Kategori Barang di TokoMart", HomeLayoutType.ALL_CATEGORY)
        )
    }
}