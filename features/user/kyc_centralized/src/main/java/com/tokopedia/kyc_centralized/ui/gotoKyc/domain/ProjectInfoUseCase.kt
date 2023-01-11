package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.ProjectInfoResponse
import javax.inject.Inject

class ProjectInfoUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, ProjectInfoResult>(dispatchers.io) {
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
                GotoLinked
                AccountLinkingStatus
              }
            }
        """.trimIndent()

    override suspend fun execute(params: String): ProjectInfoResult {
        val response: ProjectInfoResponse = repository.request(graphqlQuery(), params)
        response.kycProjectInfo.apply {
            return if (!isGoToKyc) {
                ProjectInfoResult.NotGoToKyc()
            } else {
                if (status == NOT_VERIFIED) {
                    if (dataSource.toString() == KYCConstant.GotoDataSource.GOTO_PROGRESSIVE) {
                        ProjectInfoResult.Progressive()
                    } else {
                        ProjectInfoResult.NonProgressive(
                            isAccountLinked = accountLinkingStatus == ACCOUNT_LINKED,
                            //TODO("please change this value if BE already provide")
                            isKtpAlreadyTaken = false
                        )
                    }
                } else {
                    ProjectInfoResult.StatusSubmission()
                }
            }
        }
    }

    companion object {
        const val NOT_VERIFIED = 3
        const val ACCOUNT_NOT_LINKED = -1
        const val ACCOUNT_LINKED = 1
    }
}
