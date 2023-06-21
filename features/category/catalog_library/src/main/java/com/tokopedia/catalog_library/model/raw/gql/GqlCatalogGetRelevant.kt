package com.tokopedia.catalog_library.model.raw.gql

const val GQL_CATALOG_RELEVANT: String = """query catalogGetRelevant() {
    catalogGetRelevant() {
        catalogs{
          id
          name
          imageUrl
          mobileUrl
          applink
          url
        }
    }
}    
"""
