package com.tokopedia.category.navbottomsheet.model.raw

const val GQL_CATEGORY_LIST: String = """query AllCategoryQuery(${'$'}categoryId :Int,${'$'}catnav:Boolean) {
  categoryAllList(categoryID:${'$'}categoryId,catnav:${'$'}catnav) {
    categories {
      id
      n:name
      i:iconImageUrl
      ig:iconImageUrlGray
      c:child {
        id
        n:name
        a:applinks
        c:child {
          id
          n:name
          a:applinks
        }
      }
    }
  }
}"""

const val GQL_CATGEOY_DETAIL: String = """query CategoryDetailQueryV3(${'$'}identifier: String!, ${'$'}intermediary: Boolean!, ${'$'}safeSearch: Boolean!) {
    CategoryDetailQueryV3(identifier: ${'$'}identifier, intermediary: ${'$'}intermediary, safeSearch: ${'$'}safeSearch) {
        data {
            id
            rootId
            parent
        }
    }
}"""