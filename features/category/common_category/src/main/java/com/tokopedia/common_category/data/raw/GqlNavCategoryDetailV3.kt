package com.tokopedia.common_category.data.raw

const val GQL_NAV_CATEGORY_DETAIL_V3: String = """query CategoryDetailQueryV3(${'$'}identifier: String!, ${'$'}intermediary: Boolean!, ${'$'}safeSearch: Boolean!) {
  CategoryDetailQueryV3(identifier: ${'$'}identifier, intermediary: ${'$'}intermediary, safeSearch: ${'$'}safeSearch) {
    header{
      code
      serverProcessTime
      message
    }
        data{
        id
        rootId
        parent
      name
      url
      appRedirectionURL
      isAdult
 	  isBanned
      bannedMsg
      bannedMsgHeader
      child{
        id
        name
        url
        thumbnailImage
        isAdult
        applinks
      }
    }
  }
}"""