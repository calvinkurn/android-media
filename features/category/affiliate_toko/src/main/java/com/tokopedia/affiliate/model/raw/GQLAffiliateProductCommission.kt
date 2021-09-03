package com.tokopedia.affiliate.model.raw

const val GQLAffiliateProductCommission: String = """query getAffiliateProductCommission(${"$"}userID: String!){
  getAffiliateProductCommission(userID: ${"$"}userID) {
    data {
      status
      commission {
        {
          productID
          shopID
          categoryID
          priceFormatted
          price
          amountFormatted
          amount
          percentageFormatted
          percentage
        }
      }
      error {
        message
        cta_text
        cta_link {
          desktop
          mobile
          android
          ios
        }
      }
    }
  }
}"""