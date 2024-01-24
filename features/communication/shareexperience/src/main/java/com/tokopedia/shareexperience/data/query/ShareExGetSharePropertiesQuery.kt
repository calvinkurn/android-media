package com.tokopedia.shareexperience.data.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ShareExGetSharePropertiesQuery : GqlQueryInterface {

    private val operationName = "getShareProperties"
    override fun getOperationNameList(): List<String> {
        return listOf(operationName)
    }

    override fun getQuery(): String = """
        query getShareProperties($$PARAMS: ParamGetShareProperties!) {
          getShareProperties(params: $$PARAMS) {
            shareId
            bottomsheet {
              title
              subtitle
              properties {
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

    companion object {
        private const val PARAMS = "params"
    }
}
