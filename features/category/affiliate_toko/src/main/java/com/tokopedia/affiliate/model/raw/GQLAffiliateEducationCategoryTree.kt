package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Education_Category_TREE: String = """query categoryTree(${"$"}source:String) {
  categoryTree(source: ${"$"}source){
    data{
      status
      categories{
        id
        title
        description
        url
        children{
          id
          title
          description
          url
          icon {
            url
          }
        }
        icon{
          url
        }
      }
    }
  }
}""".trimIndent()
