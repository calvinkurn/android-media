package com.tokopedia.feedcomponent.data.raw

const val GQL_MUTATION_SEND_REPORT: String = """mutation SendReport(${'$'}content: Int!, ${'$'}contentType: String!, ${'$'}reasonType: String!, ${'$'}reasonMessage: String!) {
  feed_report_submit(content: ${'$'}content, contentType: ${'$'}contentType, reasonType: ${'$'}reasonType, reasonMessage: ${'$'}reasonMessage) {
    data {
      success
    }
    error
    error_message
    error_type
  }
}"""