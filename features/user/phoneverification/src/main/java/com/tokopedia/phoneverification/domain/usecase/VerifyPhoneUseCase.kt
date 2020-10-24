package com.tokopedia.phoneverification.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.phoneverification.PhoneVerificationConst
import com.tokopedia.phoneverification.data.VerifyPhoneNumberDomain
import com.tokopedia.phoneverification.data.model.response.PhoneVerificationResponseData
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class VerifyPhoneUseCase @Inject constructor(
        private val rawQueries: Map<String, String>,
        private val gqlUseCase: GraphqlUseCase<PhoneVerificationResponseData>
) {

    private val params = RequestParams.EMPTY

    val paramMsisdn = "msisdn"

    fun verifyPhone(
            phone: String,
            onSuccessVerifyPhone: (PhoneVerificationResponseData) -> Unit,
            onErrorVerifyPhone: (Throwable) -> Unit
    ) {
        val params = generateParam(phone)
        rawQueries[PhoneVerificationConst.MUTATION_USER_MSISDN_ADD]?.let {
            query ->
            gqlUseCase.apply {
                setTypeClass(PhoneVerificationResponseData::class.java)
                setRequestParams(params)
                setGraphqlQuery(query)
                execute({ result ->
                    onSuccessVerifyPhone(result)
                }, { error ->
                    onErrorVerifyPhone(error)
                })
            }
        }

    }

    private fun generateParam(phone: String): Map<String, Any> {
        return mapOf(paramMsisdn to phone)
    }
}