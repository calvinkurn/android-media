package com.tokopedia.catalog_library.model.raw.gql

const val GQL_CATALOG_LIBRARY_BRAND_CATEGORY: String =
    """query categoryListByBrand(${'$'}brand_id: String!) {
    categoryListByBrand(brand_id: ${'$'}brand_id) {
        header{
          code
          message
        }
        BrandName
        BrandID
        CategoryList{
          RootCategoryId
          RootCategoryName
          ChildCategoryList{
            CategoryID
            CategoryName
            CategoryURL
            CategoryIconURL
          }
       }
    }
}  
"""
