package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.AffiliateValidateUserData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Validate
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateValidateUserStatus @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(userId: String, email: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_USER_ID] = userId
        request[PARAM_EMAIL] = email
        return request
    }

    suspend fun validateUserStatus(userId: String, email: String): AffiliateValidateUserData {
        return repository.getGQLData(
                GQL_Affiliate_Validate,
                AffiliateValidateUserData::class.java,
                createRequestParams(userId, email)
        )
    }

    companion object {
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_EMAIL = "email"
    }


}