package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.tokopedianow.home.domain.usecase.ValidateReferralUserUseCase

internal object ValidateReferralUser {
    const val OPERATION_NAME = "gamiReferralValidateUser"

    val QUERY = """
        query ValidateReferralUser(${'$'}${ValidateReferralUserUseCase.SLUG}: String){
            gamiReferralValidateUser(slug:${'$'}${ValidateReferralUserUseCase.SLUG}) {
                resultStatus {
                  code
                  message
                  reason
                }
                status
            }
        }
    """.trimIndent()
}