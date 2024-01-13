package com.tokopedia.notifications.domain.query

const val GQL_QUERY_GAMI_ANIMATION_POPUP = """
   query(${"$"}slug: String, ${"$"}source: String) {
  gamiScratchCardPreEvaluate(input: {slug: ${"$"}slug, source: ${"$"}source}) {
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
    popUpContent {
      isShown
      assets {
        key
        value
      }
      hint
    }
}
}"""



