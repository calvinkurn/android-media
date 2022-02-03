package com.tokopedia.vouchercreation.product.list.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.vouchercreation.product.list.domain.model.response.ShopShowcasesByShopIdResponse
import javax.inject.Inject

class GetProductListMetaDataUseCas @Inject constructor(@ApplicationContext repository: GraphqlRepository)
    : GraphqlUseCase<ShopShowcasesByShopIdResponse>(repository) {

    private val query = """
        query ProductListMeta() {
            ProductListMeta() {
                header {
                  processTime
                  messages
                  reason
                  errorCode
                }
                data {
                  tab {
                    id
                    name
                    value
                  }
                  filter {
                    id
                    name
                    value
                  }
                  sort {
                    id
                    name
                    value
                  }
                  category {
                    id
                    name
                    value
                  }
                  inbound {
                    filter {
                      id
                      name
                      value
                    }
                    sort {
                      id
                      name
                      value
                    }
                  }
                }
            }
        }
    """.trimIndent()
}