package com.tokopedia.mvcwidget

const val IO = "IO"
const val UI_SETTLING_DELAY_MS = 250

const val TOKOPOINTS_CATALOG_MVC_SUMMARY_QUERY = """
query mvcCatalogSummaryQuery(${'$'}shopID: String!,${'$'}limit: Int!,${'$'}apiVersion: String!){
    tokopointsCatalogMVCSummary(shopID: ${'$'}shopID,limit: ${'$'}limit,apiVersion: ${'$'}apiVersion) {
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
      cta {
        text
        url
        appLink
        type
      }
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

const val TP_CATALOG_MULTISHOP_MVC_LIST_QUERY = """
    query tokopointsCatalogMVCWithProductsList(${'$'}page: Int!,${'$'}pageSize: Int!,${'$'}categoryRootID: String!) {
  productlist:tokopointsCatalogMVCWithProductsList(page: ${'$'}page,pageSize: ${'$'}pageSize,categoryRootID: ${'$'}categoryRootID) {
    resultStatus {
      code
      status
    }
    catalogMVCWithProductsList {
      shopInfo {
        id
        name
        iconUrl
        url
        appLink
        shopStatusIconURL
      }
      title
      maximumBenefitAmountStr
      subtitle
      adInfo {
        AdID
        AdViewUrl
        AdClickUrl
      }
      products {
        id
        name
        imageURL
        redirectURL
        redirectAppLink
        benefitLabel
        category{
          rootID
          rootName
        }
      }
    }
    tokopointsPaging {
      hasNext
    }
    productCategoriesFilter{
      rootID
      rootName
    }
  }
}

"""

const val MVC_REWARD_MULTISHOP_QUERY ="""
   layoutMerchantCouponAttr {
        topAdsJsonParam
        CatalogMVCWithProductsList {
           shopInfo {
            id
            name
            iconUrl
            url
            appLink
            shopStatusIconURL
           }
        title
        maximumBenefitAmountStr
        subtitle
        adInfo {
            AdID
            AdViewUrl
            AdClickUrl
           }
        products {
        id
        imageURL
        name
        redirectURL
        redirectAppLink
        benefitLabel
        category {
             id
             name
             rootID
             rootName
               }
            }
         }
       }
"""