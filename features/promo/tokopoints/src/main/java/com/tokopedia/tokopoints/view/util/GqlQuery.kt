package com.tokopedia.tokopoints.view.util

const val TP_CATALOG_MVC_LIST_QUERY = """
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