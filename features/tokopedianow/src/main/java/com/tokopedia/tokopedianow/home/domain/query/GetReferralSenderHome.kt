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
            sharingMetadata {
                sharingURL
            }
          }
        }
    """.trimIndent()
}