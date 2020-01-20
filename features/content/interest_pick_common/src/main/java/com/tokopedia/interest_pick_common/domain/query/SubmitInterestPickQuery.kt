package com.tokopedia.interest_pick_common.domain.query

/**
 * @author by yoasfs on 2019-11-05
 */
object SubmitInterestPickQuery {
    fun getQuery(): String {
        val interestID = "\$interestID"
        val action = "\$action"
        return  """
            mutation saveUserInterests($interestID: [Int!]!, $action: String!) {
              feed_interest_user_update(interestID: $interestID, action: $action) {
                success
                error
              }
            }
        """.trimIndent()
    }

    const val MUTATION_SUBMIT_INTEREST_ID = "MUTATION_SUBMIT_INTEREST_ID"
}