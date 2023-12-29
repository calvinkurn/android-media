package com.tokopedia.common_digital.common.util

object CommonDigitalGqlMutation {
    val rechargePushEventRecommendation = """
        mutation rechargePushEventRecommendation(${'$'}categoryID: Int!,${'$'}action: EventRecommendationActionType!) {
            rechargePushEventRecommendation(categoryID:${'$'}categoryID, action:${'$'}action) {
                Message
                IsError
            }
        }
    """.trimIndent()
}
