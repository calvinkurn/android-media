package com.tokopedia.mvcwidget

const val IO = "IO"

const val TOKOPOINTS_CATALOG_MVC_SUMMARY_QUERY = """
query mvcCatalogSummaryQuery(${'$'}shopID: String!,${'$'}limit: Int! , ${'$'}apiVersion : String!){
    tokopointsCatalogMVCSummary(shopID: ${'$'}shopID,limit: ${'$'}limit , apiVersion: ${'$'}apiVersion) {
       resultStatus {
          code
          status
          message
        }
        isShown
        animatedInfos {
          title
          subTitle
          iconURL
        }
    }
}
"""

const val TP_CATALOG_MVC_LIST_QUERY = """
    query tokopointsCatalogMVCList(${'$'}shopID: String!) {
  tokopointsCatalogMVCList(shopID: ${'$'}shopID) {
    resultStatus {
      code
      status
      message
    }
    followWidget {
      isShown
      type
      content
      contentDetails
      iconURL
      membershipHowTo {
        imageURL
        description
      }
      membershipCardID
      membershipMinimumTransaction
      membershipMinimumTransactionLabel
    }
    shopName
    catalogList {
      id
      slug
      baseCode
      promoID
      catalogType
      promoType
      title
      maximumBenefitAmount
      minimumUsageAmount
      minimumUsageLabel
      expiredDate
      expiredLabel
      quotaLeft
      quotaLeftLabel
      tagImageURLs
    }
    toasterSuccessMessage
  }
}

"""

const val MEMBERSHIP_REGISTER ="""
    mutation membershipRegister(${'$'}cardID: Int!) {
  membershipRegister(cardID: ${'$'}cardID) {
    resultStatus {
      code
      message
      reason
    }
    infoMessage {
      imageURL
      title
      subtitle
      cta {
        text
        url
        appLink
      }
    }
  }
}
"""

const val FOLLOW_SHOP ="""
    mutation followShop(${'$'}input: ParamFollowShop!) {
      followShop(input: ${'$'}input) {
        success
        message
        isFirstTime
      }
    }
"""