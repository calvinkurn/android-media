package com.tokopedia.catalog_library.model.raw.gql

const val GQL_CATALOG_BRAND_POPULAR: String =
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
            }
        }
    }
"""
