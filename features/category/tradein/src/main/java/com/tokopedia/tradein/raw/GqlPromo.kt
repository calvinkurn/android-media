package com.tokopedia.tradein.raw

const val GQL_PROMO: String = """query tradeInPromoDetail(${'$'}code: String!){
  tradeInPromoDetail(params: {
    Code:${'$'}code
  }) {
    ImageURL
    Title
    PeriodFmt
    BenefitFmt
    ConditionFmt
    Code
    TermsConditions
  }
}
"""