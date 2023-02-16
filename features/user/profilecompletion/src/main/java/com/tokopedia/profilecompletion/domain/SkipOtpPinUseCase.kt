package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addpin.data.AddPinPojo
import com.tokopedia.profilecompletion.addpin.data.SkipOtpPinPojo
import javax.inject.Inject

class SkipOtpPinUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, SkipOtpPinPojo>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          query otp_skip_validation(${'$'}otpType: Int!, ${'$'}validateToken: String){
            OTPSkipValidation (otp_type: ${'$'}otpType, ValidateToken: ${'$'}validateToken){
              skip_otp
              validate_token
              message
              error_message
            }
          }
        """.trimIndent()

    override suspend fun execute(params: Map<String, Any>): SkipOtpPinPojo {
        return repository.request(graphqlQuery(), params)
    }

}
