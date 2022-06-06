package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.tokopedianow.home.domain.usecase.GetReferralSenderHomeUseCase

object GetReferralSenderHome {
    val QUERY = """
        query GetReferralSenderHome(${'$'}${GetReferralSenderHomeUseCase.SLUG}: String!){
          gamiReferralSenderHome(slug:${'$'}${GetReferralSenderHomeUseCase.SLUG}) {
            resultStatus {
                code
                message
                reason
            }
            reward {
                maxReward
            }
            sharingMetadata {
                ogImage
                ogTitle
                ogDescription
                textDescription
                sharingURL
            }
          }
        }
    """.trimIndent()
}