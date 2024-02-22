package com.tokopedia.notifications.domain.query

const val GQL_QUERY_GAMI_ANIMATION_CRACK_COUPON = """
   query(${"$"}slug: String!, ${"$"}source: String!) {
  gamiScratchCardCrack(input: {slug: ${"$"}slug, source: ${"$"}source}) {
  resultStatus {
      code
      reason
      message
    }
    scratchCard {
      id
      name
      description
      slug
      startTime
      endTime
    }
    benefits {
      catalogID
      promoID
      title
      baseCode
      slug
      assets {
        key
        value
      }
    }
    cta {
      text
      url
      appLink
      type
      backgroundColor
      color
      iconURL
      imageURL
    }
    assets {
      key
      value
    }
  }
}"""



