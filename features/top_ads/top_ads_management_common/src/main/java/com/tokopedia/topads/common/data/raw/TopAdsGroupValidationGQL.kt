package com.tokopedia.topads.common.data.raw

const val GROUP_VALIDATION_QUERY = """
    query topAdsGroupValidateName(${'$'}shopID: Int!,${'$'}groupName: String!){
  topAdsGroupValidateName(shopID: ${'$'}shopID, groupName:${'$'}groupName){
      data {
        shopID
        groupName
      }
      errors {
         code
         detail
         title
      }
  }
}

"""