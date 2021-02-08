package com.tokopedia.catalog.usecase

import com.tokopedia.catalog.repository.catalogdetail.CatalogDetailRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import javax.inject.Inject

class GetProductCatalogOneUseCase @Inject constructor(private val catalogDetailRepository: CatalogDetailRepository) {

    suspend fun getCatalogDetail(catalogID : String) : GraphqlResponse? {
        return catalogDetailRepository.getCatalogDetail(catalogID)
    }

}