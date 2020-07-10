package com.tokopedia.find_native.data.raw

const val GQL_NAV_RELATED_LINK: String = """query findRelatedLink(${'$'}keyword: String){
   categoryTkpdFindRelated(q: ${'$'}keyword){
    relatedHotlist {
      id
      url: URL
      text: name
    }
    relatedCategory {
      id
      url: URL
      text: name
    }
  }
}"""