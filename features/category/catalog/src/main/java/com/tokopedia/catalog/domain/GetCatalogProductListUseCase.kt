package com.tokopedia.catalog.domain

import com.tokopedia.catalog.domain.model.CatalogProductListResponse
import com.tokopedia.catalog.domain.model.CatalogSearchProductForReimaganeResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.oldcatalog.model.util.CatalogConstant
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetCatalogProductQuery", GetCatalogProductListUseCase.QUERY)
class GetCatalogProductListUseCase @Inject constructor(repository: GraphqlRepository) :
    GraphqlUseCase<CatalogProductListResponse>(repository) {

    companion object {

        const val QUERY = """
            query CatalogProductList(${'$'}params: String!) {
              catalogGetProductList(params: ${'$'}params) {
                header {
                  totalData
                }
                products {
                  productID
                  warehouseID
                  isVariant
                  mediaUrl {
                    image
                    image300
                    image500
                    image700
                  }
                  shop {
                    id
                    name
                    city
                    badge
                  }
                  price {
                    text
                    original
                  }
                  stock {
                    soldPercentage
                    wording
                    isHidden
                  }
                  credibility {
                    rating
                    ratingCount
                  }
                  labelGroups {
                    position
                    title
                    url
                  }
                  additionalService {
                    name
                  }
                  delivery {
                    eta
                    type
                  }
                  paymentOption {
                    desc
                    iconUrl
                  }
                }
              }
            }
        """

    }

    init {
        setGraphqlQuery(GetCatalogProductQuery())
        setTypeClass(CatalogProductListResponse::class.java)
    }

    suspend fun execute(param: RequestParams): CatalogProductListResponse {
        val paramProductListing = RequestParams.create()
        paramProductListing.putString("params", param.getString(CatalogConstant.PRODUCT_PARAMS, ""))
        setRequestParams(paramProductListing.parameters)
        return executeOnBackground()
    }
}
