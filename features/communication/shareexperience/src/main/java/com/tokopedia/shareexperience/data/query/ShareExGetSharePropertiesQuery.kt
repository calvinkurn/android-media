package com.tokopedia.shareexperience.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ShareExGetSharePropertiesQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
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
                  message
                  badge
                  expiredDataFormatted
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
                channel {
                  title
                  list {
                    id
                    title
                    imageResolution
                    platform
                  }
              }
            }
          }
        }
    """.trimIndent()

    override fun getTopOperationName(): String = OPERATION_NAME

    companion object {
        const val OPERATION_NAME = "getShareProperties"
    }
}
