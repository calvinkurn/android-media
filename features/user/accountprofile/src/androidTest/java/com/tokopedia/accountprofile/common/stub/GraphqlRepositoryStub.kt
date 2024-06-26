package com.tokopedia.accountprofile.common.stub

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_account.common.AndroidFileUtil
import com.tokopedia.accountprofile.addphone.INVALID_UPDATE_PHONE_NUMBER
import com.tokopedia.accountprofile.addphone.INVALID_VALIDATE_PHONE_NUMBER
import com.tokopedia.accountprofile.settingprofile.addphone.data.AddPhonePojo
import com.tokopedia.accountprofile.settingprofile.addphone.data.UserValidatePojo
import com.tokopedia.accountprofile.biousername.BioUsernameInstrumentTest
import com.tokopedia.accountprofile.biousername.BioUsernameInstrumentTest.Companion.ERROR_MESSAGE_USERNAME
import com.tokopedia.accountprofile.settingprofile.changebiousername.data.SubmitBioUsername
import com.tokopedia.accountprofile.settingprofile.changebiousername.data.SubmitBioUsernameResponse
import com.tokopedia.accountprofile.settingprofile.changebiousername.data.UsernameValidation
import com.tokopedia.accountprofile.settingprofile.changebiousername.data.UsernameValidationResponse
import com.tokopedia.accountprofile.common.stub.FileUtils.createResponseFromJson
import com.tokopedia.accountprofile.settingprofile.profileinfo.data.ProfileFeedResponse
import com.tokopedia.accountprofile.settingprofile.profileinfo.data.ProfileInfoResponse
import com.tokopedia.accountprofile.settingprofile.profileinfo.data.ProfileRoleResponse
import com.tokopedia.accountprofile.settingprofile.profileinfo.data.UserFinancialAssetsData
import com.tokopedia.accountprofile.test.R
import java.lang.reflect.Type

class GraphqlRepositoryStub : GraphqlRepository {

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        val param = requests.firstOrNull()?.variables
        requests.firstOrNull()?.query?.let {
            return when {
                it.contains("feedXProfileForm") -> {
                    val response = getResponse<ProfileFeedResponse>(R.raw.success_feed_profile_form)
                    GraphqlResponse(response, mapOf(), false)
                }
                it.contains("userProfileInfo") -> GraphqlResponse(
                    getResponse<ProfileInfoResponse>(R.raw.success_profile_info),
                    mapOf(),
                    false
                )
                it.contains("userProfileRole") -> GraphqlResponse(
                    getResponse<ProfileRoleResponse>(R.raw.success_case_profile_role),
                    mapOf(),
                    false
                )
                it.contains("checkUserFinancialAssets") -> createResponseFromJson<UserFinancialAssetsData>(
                    R.raw.success_case_financial_assets
                )
                it.contains("feedXProfileValidateUsername") -> {
                    if (param?.get("username")?.toString()
                            .equals(BioUsernameInstrumentTest.USERNAME_EXISTS)
                    )
                        GraphqlResponse(
                            mapOf(
                                UsernameValidationResponse::class.java to
                                    UsernameValidationResponse(
                                        UsernameValidation(
                                            isValid = false,
                                            errorMessage = ERROR_MESSAGE_USERNAME
                                        )
                                    )
                            ), mapOf(), false
                        )
                    else
                        GraphqlResponse(
                            mapOf(
                                UsernameValidationResponse::class.java to
                                    UsernameValidationResponse(
                                        UsernameValidation(
                                            isValid = true,
                                            errorMessage = ""
                                        )
                                    )
                            ), mapOf(), false
                        )
                }
                it.contains("feedXProfileSubmit") -> {
                    if ((param?.get("param") as Map<String, Any>).get("username").toString()
                            .equals(BioUsernameInstrumentTest.USERNAME_FAILED)
                        || (param.get("param") as Map<String, Any>).get("biography").toString()
                            .equals(BioUsernameInstrumentTest.BIO_FAILED)
                    ) {
                        GraphqlResponse(
                            mapOf(
                                SubmitBioUsernameResponse::class.java to SubmitBioUsernameResponse(
                                    SubmitBioUsername(status = false)
                                )
                            ),
                            mapOf(
                                SubmitBioUsernameResponse::class.java to listOf(
                                    AndroidFileUtil.parse(
                                        R.raw.error_profile_submit,
                                        GraphqlError::class.java
                                    )
                                )
                            ),
                            false
                        )
                    } else
                        GraphqlResponse(
                            mapOf(
                                SubmitBioUsernameResponse::class.java to SubmitBioUsernameResponse(
                                    SubmitBioUsername(status = true)
                                )
                            ), mapOf(), false
                        )
                }
                it.contains("userProfileValidate") -> {
                    if (param?.get("phone").toString() == INVALID_VALIDATE_PHONE_NUMBER) {
                        GraphqlResponse(
                            getResponse<UserValidatePojo>(R.raw.user_profile_validate_failed),
                            mapOf(),
                            false
                        )
                    } else {
                        GraphqlResponse(
                            getResponse<UserValidatePojo>(R.raw.user_profile_validate_success),
                            mapOf(),
                            false
                        )
                    }
                }
                it.contains("userProfileUpdate") -> {
                    if (param?.get("phone").toString() == INVALID_UPDATE_PHONE_NUMBER) {
                        GraphqlResponse(
                            getResponse<AddPhonePojo>(R.raw.user_profile_update_failed),
                            mapOf(),
                            false
                        )
                    } else {
                        GraphqlResponse(
                            getResponse<AddPhonePojo>(R.raw.user_profile_update_success),
                            mapOf(),
                            false
                        )
                    }
                }
                else -> GraphqlResponse(mapOf(), mapOf(), false)
            }
        }
        throw Exception("request empty")
    }

    private inline fun <reified T> getResponse(resId: Int): Map<Type, T> =
        mapOf(T::class.java to AndroidFileUtil.parse(resId, T::class.java))
}
