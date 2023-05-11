package com.tokopedia.catalog_library.model.raw.gql

const val GQL_CATALOG_LIBRARY: String =
    """query categoryListCatalogLibraryPage(${'$'}sortOrder: String!) {
    categoryListCatalogLibraryPage(sortOrder: ${'$'}sortOrder) {
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
