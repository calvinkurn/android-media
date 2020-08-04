package com.tokopedia.interestpick.data.raw

const val GQL_MUTATION_UPDATE_INTEREST: String = """mutation FeedInterestUserUpdate(${'$'}interestID: [Int!]!, ${'$'}action: String) {
  feed_interest_user_update(interestID: ${'$'}interestID, action: ${'$'}action) {
    success
    error
  }
}"""