package com.tokopedia.catalog_library.model.raw.gql

const val GQL_CATALOG_SPECIAL: String = """query catalogCategorySpecial(${'$'}user_id: String!) {
    catalogCategorySpecial(user_id: ${'$'}user_id) {
        header{
          code
          message
        }
        data{
          id
          name
          iconUrl
          categoryUrl
          categoryIdentifier
        }
    }
}
"""
