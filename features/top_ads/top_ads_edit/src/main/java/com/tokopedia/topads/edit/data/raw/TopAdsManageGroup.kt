package com.tokopedia.topads.edit.data.raw

const val MANAGE_GROUP = """
mutation topadsManageGroupAds(${'$'}input: TopadsManageGroupAdsInput!){
    topadsManageGroupAds(input: ${'$'}input){
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