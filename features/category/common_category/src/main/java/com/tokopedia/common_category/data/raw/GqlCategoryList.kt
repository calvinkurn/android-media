package com.tokopedia.common_category.data.raw

const val GQL_CATEGORY_LIST: String = """query AllCategoryQuery(${'$'}depth :Int,${'$'}isTrending: Boolean!, ${'$'}categoryID: Int) {
  categoryAllList(depth:${'$'}depth,isTrending:${'$'}isTrending, categoryID:${'$'}categoryID) {
    categories {
      id
      name
      identifier
      url
      parentName
      iconImageUrl
      iconImageUrlGray
      iconBannerURL
      hexColor
      applinks
      child {
        id
        name
        identifier
        url
        parentName
        iconImageUrl
        iconBannerURL
        hexColor
        applinks
        child {
          id
          name
          identifier
          url
          parentName
          iconImageUrl
          iconBannerURL
          hexColor
          applinks
        }
      }
    }
  }
}"""