package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Education_Banner: String = """query GetAffiliateEduBanner(
    ${"$"}source: String, ${"$"}is_home_banner: Int
    ) {
  dynamicBanner(source: ${"$"}source, is_home_banner: ${"$"}is_home_banner) {
    data {
      status
      banners {
        id
        title
        title_color
        description
        description_color
        text {
          primary {
            title
            redirect_url
            color
          }
          secondary {
            title
            redirect_url
            color
          }
        }
        media_type
        media {
          desktop
          mobile
          android
          ios
        }
        attributes {
          background_fill
          start {
            color
            density
          }
          end {
            color
            density
          }
        }
        sequence
      }
    }
  }
}
""".trimIndent()
