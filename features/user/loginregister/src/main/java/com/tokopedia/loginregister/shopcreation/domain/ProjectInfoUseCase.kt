package com.tokopedia.loginregister.shopcreation.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.shopcreation.data.ProjectInfoResponse
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class ProjectInfoUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    @ApplicationContext private val context: Context,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Int, ProjectInfoResult>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query kycProjectInfo (${'$'}projectID: Int!){
              kycProjectInfo(projectID: ${'$'}projectID) {
                Status
                StatusName
                Message
                IsAllowToRegister
                TypeList {
                  TypeID
                  Status
                  StatusName
                  IsAllowToUpload
                }
                Reason
                IsSelfie
                DataSource
                IsGotoKyc
                GoToLinked
                AccountLinked
                WaitMessage
                ErrorMessages
                ErrorCode
                IsBlocked
                NonEligibleGoToKYCReason
              }
            }
        """.trimIndent()

    override suspend fun execute(params: Int): ProjectInfoResult {
        val parameter = mapOf(PROJECT_ID to params)
        val response: ProjectInfoResponse = repository.request(graphqlQuery(), parameter)
        response.kycProjectInfo.apply {
            return if (errorCode.isNotBlank()) {
                ProjectInfoResult.Failed(
                    mappingErrorMessage(errorMessages.joinToString(), errorCode)
                )
            } else {
                when (status) {
                    NOT_VERIFIED -> {
                        ProjectInfoResult.NotVerified
                    }
                    REJECTED -> {
                        ProjectInfoResult.Rejected(
                            rejectionReason = reason.joinToString()
                        )
                    }
                    VERIFIED -> {
                        ProjectInfoResult.Verified
                    }
                    PENDING -> {
                        ProjectInfoResult.Pending(
                            waitMessage = waitMessage
                        )
                    }
                    BLACKLISTED -> {
                        ProjectInfoResult.Blacklisted
                    }
                    else -> {
                        ProjectInfoResult.Failed(Throwable(status))
                    }
                }
            }
        }
    }

    private fun mappingErrorMessage(message: String, errorCode: String): MessageErrorException {
        var keyKnowError = ""
        val messageError: String

        when {
            LIST_COMMON_ERROR_CODE.contains(errorCode) -> {
                messageError = context.getString(R.string.shop_creation_kyc_error_know_code)
                keyKnowError = KEY_KNOWN_ERROR_CODE
            }
            else -> {
                messageError = message
            }
        }

        val generateErrorCode = context.getString(R.string.shop_creation_error_code, errorCode)

        return MessageErrorException("$messageError $generateErrorCode", keyKnowError)
    }

    companion object {
        private const val KEY_KNOWN_ERROR_CODE = "KYC-KNOW-ERROR-CODE"
        const val NOT_VERIFIED = "3"
        const val VERIFIED = "1"
        const val REJECTED = "-1"
        const val PENDING = "0"
        const val BLACKLISTED = "5"
        private const val PROJECT_ID = "projectID"
        private val LIST_COMMON_ERROR_CODE = listOf("30004", "30009", "1539", "30003", "900", "1546")
    }
}

sealed class ProjectInfoResult {
    object Verified : ProjectInfoResult()
    data class Pending(
        val waitMessage: String
    ) : ProjectInfoResult()
    object Blacklisted : ProjectInfoResult()
    data class Rejected(
        val rejectionReason: String
    ) : ProjectInfoResult()
    object NotVerified : ProjectInfoResult()
    data class Failed(val throwable: Throwable) : ProjectInfoResult()
}
