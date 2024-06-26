package com.tokopedia.catalog_library.model.raw.gql

const val GQL_CATALOG_LIST: String =
    """query catalogGetList(${'$'}category_id: String!, ${'$'}sortType: String!, ${'$'}rows: String!, ${'$'}page: String!,${'$'}brand_id: String!) {
    catalogGetList(category_id: ${'$'}category_id, sortType: ${'$'}sortType, rows: ${'$'}rows, page: ${'$'}page,brand_id: ${'$'}brand_id) {
        header{
          code
          message
        }
        category_name
        catalogs{
          id
          name
          categoryID
          brand
          brand_id
          imageUrl
          url
          mobileUrl
          applink
          marketPrice {
            min
            max
            minFmt
            maxFmt
          }
        }
    }
}
"""
