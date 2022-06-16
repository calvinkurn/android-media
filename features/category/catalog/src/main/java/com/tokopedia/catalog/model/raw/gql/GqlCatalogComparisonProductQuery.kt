package com.tokopedia.catalog.model.raw.gql

const val GQL_CATALOG_COMPARISON_PRODUCT_QUERY: String = """query catalogComparisonList(${'$'}catalog_id: String!,${'$'}brand: String!, ${'$'}category_id: String!,${'$'}limit: String!,${'$'}page: String!, ${'$'}name: String!) {
  catalogComparisonList(catalog_id: ${'$'}catalog_id,brand: ${'$'}brand, category_id: ${'$'}category_id,limit: ${'$'}limit,page: ${'$'}page, name: ${'$'}name) {
    header{
      code
      message
    }
    catalogComparisonList {
        id
        name
        brand
        appLink
        catalogImage {
          imageUrl
          isPrimary
        }
        marketPrice {
          min
          max
          minFmt
          maxFmt
          date
          name
        }
    }
  }
}
"""