package com.tokopedia.affiliate.usecase

import com.google.gson.Gson
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
        return Gson().fromJson("{\n" +
                "    \"validateAffiliateUserStatus\": {\n" +
                "      \"Data\": {\n" +
                "        \"Status\": 1,\n" +
                "        \"IsEligible\": false,\n" +
                "        \"IsReviewed\": true,\n" +
                "        \"IsRegistered\": true,\n" +
                "        \"IsSystemDown\": true,\n" +
                "        \"Error\": {\n" +
                "          \"ErrorType\": 0,\n" +
                "          \"Message\": \"\",\n" +
                "          \"CtaText\": \"\",\n" +
                "          \"CtaLink\": {\n" +
                "            \"DesktopURL\": \"\",\n" +
                "            \"MobileURL\": \"\",\n" +
                "            \"AndroidURL\": \"\",\n" +
                "            \"IosURL\": \"\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }",AffiliateValidateUserData::class.java)
    }

    companion object {
        private const val PARAM_EMAIL = "email"
    }
}