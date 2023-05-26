package com.tokopedia.catalog_library.model.raw.gql

const val GQL_CATALOG_BRAND_POPULAR_WITH_CATALOGS: String =
    """query catalogGetBrandPopular() {
         catalogGetBrandPopular {
            header {
              code
              message
            }
            brands {
              id
              name
              identifier
              imageUrl
              catalogs {
                id
                name
                imageUrl
                url
                mobileUrl
                applink
              }
            }
        }
    }
"""
