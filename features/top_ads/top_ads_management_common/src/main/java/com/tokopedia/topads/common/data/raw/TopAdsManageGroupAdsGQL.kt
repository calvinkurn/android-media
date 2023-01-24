package com.tokopedia.topads.common.data.raw

const val TOP_ADS_MANAGE_GROUP_ADS_GQL = """
mutation topadsManageGroupAds(${'$'}input:TopadsManageGroupAdsInput!){
  topadsManageGroupAds(input:${'$'}input){
    groupResponse{
      data {
        id
        resourceUrl
      }
      errors {
        code
        detail
        title
      }
    }
  }
}
"""
