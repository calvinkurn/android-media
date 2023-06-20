package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.ProjectInfoResponse
import javax.inject.Inject

class ProjectInfoUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
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
              }
            }
        """.trimIndent()

    override suspend fun execute(params: Int): ProjectInfoResult {
        val parameter = mapOf(PROJECT_ID to params)
        val response: ProjectInfoResponse = repository.request(graphqlQuery(), parameter)
        response.kycProjectInfo.apply {
            return if (!isGotoKyc) {
                ProjectInfoResult.TokoKyc
            } else {
                if (status == NOT_VERIFIED) {
                    ProjectInfoResult.NotVerified(
                        isAccountLinked = accountLinked == ACCOUNT_LINKED
                    )
                } else {
                    ProjectInfoResult.StatusSubmission(
                        status = status,
                        rejectionReason = reason.joinToString(),
                        waitMessage = waitMessage
                    )
                }
            }
        }
    }

    companion object {
        const val NOT_VERIFIED = "3"
        const val ACCOUNT_LINKED = 1
        private const val PROJECT_ID = "projectID"
    }
}

sealed class ProjectInfoResult {
    object TokoKyc : ProjectInfoResult()
    data class StatusSubmission(
        val status: String,
        val rejectionReason: String,
        val waitMessage: String
    ): ProjectInfoResult()
    data class NotVerified(val isAccountLinked: Boolean) : ProjectInfoResult()
    data class Failed(val throwable: Throwable) : ProjectInfoResult()
}
