package com.tokopedia.exploreCategory.model.raw

const val GQL_EC_DYNAMIC_HOME_ICON_QUERY: String = """query dynamicHomeIcon(${'$'}type: Int!) {
    dynamicHomeIcon {
        categoryGroup(types:${'$'}type){
          id
          title
          desc
          categoryRows{
            id
            name
            url
            imageUrl
            applinks
            categoryLabel
            bu_identifier
          }
        }
      }
}"""