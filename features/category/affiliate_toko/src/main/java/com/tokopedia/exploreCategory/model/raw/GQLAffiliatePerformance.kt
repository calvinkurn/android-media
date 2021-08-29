package com.tokopedia.exploreCategory.model.raw

const val GQL_Affiliate_Performance: String = """query getAffiliatePerformance(${"$"}userID: String!){
  getAffiliatePerformance(userID: ${"$"}userID) {
    data {
      status
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
      performanceSummary {
        formattedCommission
        commission
        conversionPercentage
        compareCommissionPercentage
        click
        compareClick
        sold
        compareSold
      }
      links {
        sectionID
        has_more
        sectionTitle
        totalCount
        items{
          id
          title
          image {
            desktop
            mobile
            android
            ios
          }
          status
          footer [
            footerIcon
            footerText
          ]
          performanceSummary {
            formattedCommission
            commission
            conversionPercentage
            compareCommissionPercentage
            click
            compareClick
            sold
            compareSold
          }
        }
      }
      filters {
        title
        from
        to
      }
    }
  }
}"""