package com.tokopedia.kategori.model.raw

const val GQL_CATEGORY_LIST: String = """query AllCategoryQuery(${'$'}depth :Int,${'$'}isTrending: Boolean!) {
  categoryAllList(depth:${'$'}depth,isTrending:${'$'}isTrending) {
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