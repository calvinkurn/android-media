package com.tokopedia.topads.common.data.raw

const val MANAGE_GROUP = """
mutation topadsManagePromoGroupProduct(${'$'}input: TopadsManagePromoGroupProductInput!){
    topadsManagePromoGroupProduct(input: ${'$'}input){
        groupResponse {
            data{
              id
            }
            errors {
                code
                detail
                title
            }
        }
        keywordResponse {
            data {
                id
            }
            errors {
                code
                detail
                title
            }
        }
    }
}"""