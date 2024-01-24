package com.tokopedia.shareexperience.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ShareExGetSharePropertiesQuery : GqlQueryInterface {

    private val operationName = "getShareProperties"
    override fun getOperationNameList(): List<String> {
        return listOf(operationName)
    }

    override fun getQuery(): String = """
        query getShareProperties(${'$'}params: ParamGetShareProperties!) {
          getShareProperties(params: ${'$'}params) {
            bottomsheet {
              title
              subtitle
              properties {
                shareId
                chipTitle
                shareBody {
                  title
                  thumbnailUrls
                }
                affiliateRegistrationWidget {
                  icon
                  title
                  label
                  description
                  link
                }
                affiliateEligibility {
                  eligible
                  commission
                }
                imageGeneratorPayload {
                  args {
                    key
                    value
                  }
                  sourceId
                }
                generateLinkProperties {
                  message
                  ogTitle
                  ogDescription
                  ogType
                  ogImageUrl
                  ogVideo
                  desktopUrl
                  androidUrl
                  iosUrl
                  iosDeeplinkPath
                  canonicalUrl
                  canonicalIdentifier
                  customMetaTags
                  anMinVersion
                }
              }
            }
          }
        }
    """.trimIndent()

    override fun getTopOperationName(): String = operationName
}
