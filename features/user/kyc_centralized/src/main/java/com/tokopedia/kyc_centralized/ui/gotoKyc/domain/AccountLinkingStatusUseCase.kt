package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.ProjectInfoResponse
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class AccountLinkingStatusUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    @ApplicationContext private val context: Context,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Int, AccountLinkingStatusResult>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query kycProjectInfo (${'$'}projectID: Int!){
              kycProjectInfo(projectID: ${'$'}projectID) {
                IsGotoKyc
                AccountLinked
                ErrorMessages
                ErrorCode
                IsBlocked
                NonEligibleGoToKYCReason
              }
            }
        """.trimIndent()

    override suspend fun execute(params: Int): AccountLinkingStatusResult {
        val parameter = mapOf(PROJECT_ID to params)
        val response: ProjectInfoResponse = repository.request(graphqlQuery(), parameter)

        return when {
            response.kycProjectInfo.errorCode.isNotEmpty() -> {
                AccountLinkingStatusResult.Failed(
                    mappingErrorMessage(
                        response.kycProjectInfo.errorMessages.joinToString(),
                        response.kycProjectInfo.errorCode
                    )
                )
            }
            response.kycProjectInfo.isBlocked -> {
                val isMultipleAccount = LIST_REASON_BLOCK_MULTIPLE_ACCOUNT.contains(
                    response.kycProjectInfo.nonEligibleGoToKYCReason
                )
                AccountLinkingStatusResult.Blocked(isMultipleAccount)
            }
            !response.kycProjectInfo.isGotoKyc -> {
                AccountLinkingStatusResult.TokoKyc
            }
            response.kycProjectInfo.accountLinked == KEY_ACCOUNT_LINKED -> {
                AccountLinkingStatusResult.Linked
            }
            else -> {
                AccountLinkingStatusResult.NotLinked
            }
        }
    }

    private fun mappingErrorMessage(message: String, errorCode: String): MessageErrorException {
        var keyKnowError = ""
        val messageError: String

        when {
            LIST_COMMON_ERROR_CODE.contains(errorCode)-> {
                messageError = context.getString(R.string.goto_kyc_error_know_code)
                keyKnowError = KYCConstant.KEY_KNOWN_ERROR_CODE
            }
            else -> {
                messageError = message
            }
        }

        val generateErrorCode = context.getString(R.string.error_code, errorCode)

        return MessageErrorException("$messageError $generateErrorCode", keyKnowError)
    }


    companion object {
        private const val KEY_ACCOUNT_LINKED = 1
        private const val PROJECT_ID = "projectID"
        private val LIST_COMMON_ERROR_CODE = listOf("30004", "30009", "1539", "30003", "900", "1546")
        private val LIST_REASON_BLOCK_MULTIPLE_ACCOUNT =
            listOf("ALREADY_REGISTERED_OTHER_PROFILE", "NOT_ELIGIBLE_ACC_LINK")
    }
}

sealed class AccountLinkingStatusResult {
    object Loading : AccountLinkingStatusResult()
    object TokoKyc : AccountLinkingStatusResult()
    data class Blocked(val isMultipleAccount: Boolean) : AccountLinkingStatusResult()
    object Linked : AccountLinkingStatusResult()
    object NotLinked : AccountLinkingStatusResult()
    data class Failed(val throwable: Throwable) : AccountLinkingStatusResult()
}

