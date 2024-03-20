package com.tokopedia.shareexperience.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ShareExGetAffiliateLinkQuery : GqlQueryInterface {

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String = """
        mutation generateAffiliateLink(${'$'}input: GenerateLinkRequest!) {
            generateAffiliateLink(input: ${'$'}input) {
                Data {
                    Status 
                    Type
                    Error
                    Identifier
                    IdentifierType
                    LinkID
                    URL {
                      ShortURL
                      RegularURL
                    }
                }
            }
        }
    """.trimIndent()

    override fun getTopOperationName(): String = OPERATION_NAME

    companion object {
        const val OPERATION_NAME = "generateAffiliateLink"
    }
}
