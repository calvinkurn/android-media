package com.tokopedia.catalog_library.model.raw.gql

const val GQL_CATALOG_LIBRARY: String = """query categoryListCatalogLibraryPage(${'$'}sortOrder: String!, ${'$'}device: String!) {
    categoryListCatalogLibraryPage(sortOrder: ${'$'}sortOrder, device: ${'$'}device) {
        header{
          code
          message
        }
        CategoryList{
          RootCategoryId
          RootCategoryName
          ChildCategoryList{
            CategoryID
            CategoryName
            CategoryURL
            CategoryIconURL
            CategoryIdentifier
       }
    }
  }
}  
"""
