package com.tokopedia.profilecompletion.common.stub

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_account.common.AndroidFileUtil
import com.tokopedia.profilecompletion.changebiousername.data.SubmitBioUsername
import com.tokopedia.profilecompletion.changebiousername.data.SubmitBioUsernameResponse
import com.tokopedia.profilecompletion.changebiousername.data.UsernameValidation
import com.tokopedia.profilecompletion.changebiousername.data.UsernameValidationResponse
import com.tokopedia.profilecompletion.profileinfo.data.ProfileFeedResponse
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoResponse
import com.tokopedia.profilecompletion.profileinfo.data.ProfileRoleResponse
import com.tokopedia.profilecompletion.profileinfo.data.UserFinancialAssetsData
import com.tokopedia.profilecompletion.test.R
import java.lang.reflect.Type

class GraphqlRepositoryStub : GraphqlRepository {
    override suspend fun response(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponse {
        val param = requests.firstOrNull()?.variables
        requests.firstOrNull()?.query?.let {
            return when {
                it.contains("feedXProfileForm") -> GraphqlResponse(getResponse<ProfileFeedResponse>(R.raw.success_feed_profile_form), mapOf(), false)
                it.contains("userProfileInfo") -> GraphqlResponse(getResponse<ProfileInfoResponse>(R.raw.success_profile_info), mapOf(), false)
                it.contains("userProfileRole") -> GraphqlResponse(getResponse<ProfileRoleResponse>(R.raw.success_case_profile_role), mapOf(), false)
                it.contains("checkUserFinancialAssets") -> GraphqlResponse(getResponse<UserFinancialAssetsData>(R.raw.success_case_financial_assets), mapOf(), false)
                it.contains("feedXProfileValidateUsername") -> {
                    if (param?.get("username")?.toString().equals("rama_exists"))
                        GraphqlResponse(mapOf(UsernameValidationResponse::class.java to
                                UsernameValidationResponse(UsernameValidation(isValid = true, errorMessage = ""))), mapOf(), false)
                    else
                        GraphqlResponse(mapOf(UsernameValidationResponse::class.java to
                                UsernameValidationResponse(UsernameValidation(isValid = false, errorMessage = "Username ini sudah dipakai orang lain."))), mapOf(), false)
                }
                it.contains("feedXProfileSubmit") -> {
                    if (param?.get("username")?.toString().equals("rama_exists"))
                        GraphqlResponse(
                                mapOf(SubmitBioUsernameResponse::class.java to SubmitBioUsernameResponse(
                                        SubmitBioUsername(status = false))), mapOf(), false)
                    else
                        GraphqlResponse(
                                mapOf(SubmitBioUsernameResponse::class.java to SubmitBioUsernameResponse(
                                        SubmitBioUsername(status = true))), mapOf(), false)
                }
                else -> GraphqlResponse(mapOf(), mapOf(), false)
            }
        }
        throw  Exception("request empty")
    }

    private inline fun <reified T> getResponse(resId: Int): Map<Type, T> = mapOf(T::class.java to AndroidFileUtil.parse(resId, T::class.java))
}