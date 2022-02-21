package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Validate
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateValidateUserStatusUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(email: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_EMAIL] = email
        return request
    }

    suspend fun validateUserStatus(email: String): AffiliateValidateUserData {
        return repository.getGQLData(
                GQL_Affiliate_Validate,
                AffiliateValidateUserData::class.java,
                createRequestParams(email)
        )
    }

    companion object {
        private const val PARAM_EMAIL = "email"
    }
}