package com.tokopedia.category.navbottomsheet.model.raw

const val GQL_CATEGORY_LIST: String = """query AllCategoryQuery(${'$'}categoryId :Int) {
  categoryAllList(categoryID:${'$'}categoryId) {
    categories {
      id
      name
      identifier
      url
      iconImageUrl
      iconImageUrlGray
      iconBannerURL
      applinks
      child {
        id
        name
        identifier
        url
        parent
        parentName
        applinks
        child {
          id
          name
          identifier
          url
          parent
          parentName
          applinks
        }
      }
    }
  }
}"""