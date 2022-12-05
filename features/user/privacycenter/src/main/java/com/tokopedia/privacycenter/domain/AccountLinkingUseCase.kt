package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.AccountLinkingResponse
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
import javax.inject.Inject

class AccountLinkingUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val userSessionInterface: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, PrivacyCenterStateResult<AccountLinkingStatus>>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query link_status(${'$'}linking_type: String!){
                accountsLinkerStatus(linking_type:${'$'}linking_type){
                    link_status {
                        linking_type
                        status
                        partner_user_id
                        linked_time
                    }
                    error
                }
            }
        """.trimIndent()

    override suspend fun execute(params: String): PrivacyCenterStateResult<AccountLinkingStatus> {
        val parameter = mapOf(PARAM_LINKING to params)
        val response: AccountLinkingResponse = repository.request(graphqlQuery(), parameter)

        val linkedStatus = response.accountsLinkerStatus.linkStatus
        val phoneNumber = userSessionInterface.phoneNumber

        return if (linkedStatus.isNotEmpty() && linkedStatus.first().status == KEY_ACCOUNT_LINKED) {
            val linkedTime = DateUtil.formatDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_SSS_Z, DateUtil.DEFAULT_VIEW_FORMAT, response.accountsLinkerStatus.linkStatus.first().linkedTime)
            PrivacyCenterStateResult.Success(AccountLinkingStatus(true, phoneNumber, linkedTime))
        } else {
            PrivacyCenterStateResult.Success(AccountLinkingStatus(false))
        }
    }

    companion object {
        private const val KEY_ACCOUNT_LINKED = "linked"
        private const val PARAM_LINKING = "linking_type"
        const val ACCOUNT_LINKING_TYPE = "account_linking"
    }
}

data class AccountLinkingStatus(
    val isLinked: Boolean = false,
    val phoneNumber: String = "",
    val linkedTime: String = ""
)
