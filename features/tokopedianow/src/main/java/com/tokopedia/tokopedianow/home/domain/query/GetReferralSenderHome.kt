package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetReferralSenderHome: GqlQueryInterface {

    const val PARAM_SLUG = "slug"

    private const val OPERATION_NAME = "gamiReferralSenderHome"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(${'$'}${PARAM_SLUG}: String!){
          $OPERATION_NAME(slug:${'$'}${PARAM_SLUG}) {
            resultStatus {
                code
                message
                reason
            }
            reward {
                maxReward
            }
            sharingMetadata {
                ogImage
                ogTitle
                ogDescription
                textDescription
                sharingURL
            }
            actionButton {
                text
                url
                appLink
                type
            }
            header {
                title
                subtitle
            }
          }
        }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
