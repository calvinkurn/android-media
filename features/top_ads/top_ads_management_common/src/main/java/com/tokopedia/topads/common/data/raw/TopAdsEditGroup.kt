package com.tokopedia.topads.common.data.raw

const val EDIT_GROUP_QUERY = """mutation topadsManageGroupAds(${'$'}input: TopadsManageGroupAdsInput!){
    topadsManageGroupAds(input:${'$'}input){
        groupResponse {
            errors {
                code
                detail
                title
            }
        }
        keywordResponse {
            errors {
                code
                detail
                title
            }
        }
    }
}"""
