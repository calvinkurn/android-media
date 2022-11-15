package com.tokopedia.catalog_library.model.raw.gql

const val GQL_CATALOG_LIST: String = """query catalogGetList(${'$'}category_identifier: String!, ${'$'}sortType: String!, ${'$'}rows: String!) {
    catalogGetList(category_identifier: ${'$'}category_identifier, sortType: ${'$'}sortType, rows: ${'$'}rows) {
        header{
          code
          message
        }
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

//category_id_id: ${'$'}category_id, ${'$'}category_id: String!,
//package com.tokopedia.catalog_library.model.raw.gql.library
//
//const val GQL_CATALOG_LIST: String = """query catalogGetList(${'$'}category_identifier: String!, ${'$'}brand_identifier: String!, ${'$'}keyword: String!, ${'$'}sortType: String!, ${'$'}page: String!, ${'$'}rows: String!) {
//    catalogGetList(category_identifier: ${'$'}category_identifier, brand_identifier: ${'$'}brand_identifier, keyword: ${'$'}keyword, sortType: ${'$'}sortType, page: ${'$'}page, rows: ${'$'}rows) {
//        header{
//          code
//          message
//        }
//        catalogs{
//          id
//          name
//          categoryID
//          brand
//          brand_id
//          imageUrl
//          url
//          mobileUrl
//          applink
//          marketPrice {
//            min
//            max
//            minFmt
//            maxFmt
//          }
//        }
//    }
//}
//"""

//category_id_id: ${'$'}category_id, ${'$'}category_id: String!,

