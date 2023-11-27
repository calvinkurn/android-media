package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetHomeBannerV2: GqlQueryInterface {

    const val PARAM_LOCATION = "location"
    private const val PARAM_PAGE = "page"
    private const val OPERATION_NAME = "getHomeBannerV2"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(
                ${'$'}$PARAM_LOCATION: String
            ) {
              $OPERATION_NAME(
                $PARAM_PAGE:"tokonow", 
                $PARAM_LOCATION: ${'$'}$PARAM_LOCATION
              ) {
                banners {
                  id
                  url
                  type
                  title
                  applink
                  message
                  persona
                  brandID
                  imageUrl
                  startTime
                  promoCode
                  backColor
                  expireTime
                  slideIndex
                  categoryID
                  creativeName
                  campaignCode
                  topadsViewUrl
                  pgCampaignType
                  categoryPersona
                  galaxyAttribution
                  redirectionType
                  bottomSheetContent
                }
              }
            }
        """.trimIndent()
    }
}
