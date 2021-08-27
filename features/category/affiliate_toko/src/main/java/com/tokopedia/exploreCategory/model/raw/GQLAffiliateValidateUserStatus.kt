package com.tokopedia.exploreCategory.model.raw

const val GQL_Affiliate_Validate: String = """query validateUserStatus(${"$"}userID : String!, ${"$"}email : String!)
{
  validateUserStatus(userID: ${"$"}userId, email: ${"$"}email) {
    data {
      status
      isRegistered
      isEligible
      error {
        message
        cta_text
        cta_link {
          android
        }
      }
    }
  }
}"""