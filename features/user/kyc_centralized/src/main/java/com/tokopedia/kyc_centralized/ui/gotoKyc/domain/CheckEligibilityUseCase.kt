package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import android.content.Context
import com.gojek.kyc.sdk.core.utils.KycSdkPartner
import com.gojek.OneKycSdk
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckEligibilityUseCase @Inject constructor(
    private val oneKycSdk: OneKycSdk,
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers
) {
    suspend fun invoke(): CheckEligibilityResult {
        return withContext(dispatchers.io) {
            val response = oneKycSdk.getOneKycNetworkRepository().getEligibilityStatus(partnerName = KycSdkPartner.TOKOPEDIA_CORE.name)

            if (response.success == true) {
                when (response.data?.flow) {
                    KYCConstant.GotoKycFlow.PROGRESSIVE -> {
                        CheckEligibilityResult.Progressive(response.data?.userIdentityData?.maskedName.orEmpty())
                    }
                    KYCConstant.GotoKycFlow.NON_PROGRESSIVE -> {
                        CheckEligibilityResult.NonProgressive
                    }
                    KYCConstant.GotoKycFlow.ONEKYC_BLOCKED -> {
                        if (response.data?.reasonCode == KYCConstant.GotoKycFlow.AWAITING_APPROVAL_GOPAY) {
                            CheckEligibilityResult.AwaitingApprovalGopay
                        } else {
                            val message = "$FLOW: ${response.data?.flow} - $REASON_CODE: ${response.data?.reasonCode}"
                            CheckEligibilityResult.Failed(Throwable(message = message))
                        }
                    }
                    else -> {
                        CheckEligibilityResult.Failed(Throwable(message = response.data?.flow.toString()))
                    }
                }
            } else {
                CheckEligibilityResult.Failed(
                    mappingErrorMessage(
                        message = response.errors?.first()?.message.orEmpty(),
                        errorCode = response.errors?.first()?.code.orEmpty()
                    )
                )
            }
        }
    }

    private fun mappingErrorMessage(message: String, errorCode: String): MessageErrorException {
        var keyKnowError = ""
        val messageError: String

        when {
            LIST_COMMON_ERROR_CODE.contains(errorCode) -> {
                messageError = context.getString(R.string.goto_kyc_error_know_code)
                keyKnowError = KYCConstant.KEY_KNOW_ERROR_CODE
            }
            else -> {
                messageError = message
            }
        }

        val generateErrorCode = context.getString(R.string.error_code, errorCode)

        return MessageErrorException("$messageError $generateErrorCode", keyKnowError)
    }

    companion object {
        private const val FLOW = "flow"
        private const val REASON_CODE = "reasonCode"
        private val LIST_COMMON_ERROR_CODE = listOf("1539", "30009", "30003", "900", "1546")
    }
}

sealed class CheckEligibilityResult {
    data class Progressive(val encryptedName: String): CheckEligibilityResult()
    object NonProgressive : CheckEligibilityResult()
    object AwaitingApprovalGopay : CheckEligibilityResult()
    data class Failed(val throwable: Throwable): CheckEligibilityResult()
}
