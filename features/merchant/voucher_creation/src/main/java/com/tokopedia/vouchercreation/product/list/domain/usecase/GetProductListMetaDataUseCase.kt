package com.tokopedia.vouchercreation.product.list.domain.usecase

class GetProductListMetaDataUseCase {

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