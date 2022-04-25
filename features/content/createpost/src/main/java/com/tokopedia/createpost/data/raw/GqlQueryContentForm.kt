package com.tokopedia.createpost.data.raw

const val GQL_QUERY_CONTENT_FORM: String = """query ContentForm(${'$'}relatedID: [String], ${'$'}type: String, ${'$'}id: String, ${'$'}token: String) {
  feed_content_form(relatedID: ${'$'}relatedID, type: ${'$'}type, ID: ${'$'}id, token: ${'$'}token) {
    token
    type
    defaultPlaceholder
    authors {
      type
      id
      name
      thumbnail
      badge
    }
    media {
      multiple_media
      max_media
      allow_image
      allow_video
      media {
        id
        type
        removable
        media_url
      }
    }
    relatedItems {
      image
      id
      price
      title
    }
    maxTag
    defaultCaptions
    caption
    error
    productTagSources
  }
}"""