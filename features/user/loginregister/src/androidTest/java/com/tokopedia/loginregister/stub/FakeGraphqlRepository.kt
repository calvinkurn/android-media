package com.tokopedia.loginregister.stub

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.loginregister.common.domain.pojo.DiscoverPojo
import com.tokopedia.loginregister.common.domain.pojo.DynamicBannerDataModel
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.common.domain.pojo.TickerInfoData
import com.tokopedia.loginregister.common.domain.pojo.TickersInfoPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestV2
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.data.admin.AdminTypeResponse
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import timber.log.Timber
import com.tokopedia.loginregister.test.R as loginregistertestR

class FakeGraphqlRepository : GraphqlRepository {

    var registerCheckConfig: Config = Config.Default
    var discoverConfig: Config = Config.Default
    var registerRequestConfig: Config = Config.Default
    var loginConfig: Config = Config.Default
    var profileConfig: Config = Config.Default

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        Timber.d("Passed through FakeGraphql $this: ${requests.first().query.slice(0..20)}")
        return when (GqlQueryParser.parse(requests).first()) {
            "registerCheck" -> {
                val obj: RegisterCheckPojo = when (registerCheckConfig) {
                    is Config.WithResponse -> (registerCheckConfig as Config.WithResponse).response as RegisterCheckPojo
                    is Config.Default -> RegisterCheckPojo(
                        RegisterCheckData(
                            isExist = true,
                            useHash = true,
                            userID = "123456",
                            registerType = "email",
                            view = "yoris.prayogo@tokopedia.com"
                        )
                    )

                    else -> throw IllegalStateException()
                }
                GqlMockUtil.createSuccessResponse(obj)
            }

            "discover" -> {
                when (discoverConfig) {
                    is Config.Default -> GqlMockUtil.createSuccessResponse<DiscoverPojo>(
                        loginregistertestR.raw.discover_success
                    )

                    is Config.Error -> {
                        throw Throwable("Error")
                    }
                    else -> GqlMockUtil.createSuccessResponse(DiscoverPojo())
                }
            }

            "generate_key" -> {
                GqlMockUtil.createSuccessResponse<GenerateKeyPojo>(loginregistertestR.raw.generate_key_success)
            }

            "GetBanner" -> {
                GqlMockUtil.createSuccessResponse(DynamicBannerDataModel())
            }

            "ticker" -> {
                GqlMockUtil.createSuccessResponse(TickerInfoData(TickersInfoPojo(listOf())))
            }

            "register_v2" -> {
                when (registerRequestConfig) {
                    is Config.Success -> GqlMockUtil.createSuccessResponse<RegisterRequestV2>(
                        loginregistertestR.raw.register_v2_success
                    )

                    is Config.Error -> GqlMockUtil.createSuccessResponse<RegisterRequestV2>(
                        loginregistertestR.raw.register_v2_failed_forbidden_name
                    )

                    else -> GqlMockUtil.createSuccessResponse(RegisterRequestV2())
                }
            }

            "login_token_v2" -> {
                return when (loginConfig) {
                    is Config.Default -> {
                        GqlMockUtil.createSuccessResponse(LoginTokenPojoV2())
                    }
                    is Config.WithResponse -> {
                        GqlMockUtil.createSuccessResponse((loginConfig as Config.WithResponse).response as LoginTokenPojoV2)
                    } else -> {
                        GqlMockUtil.createSuccessResponse(LoginTokenPojoV2())
                    }
                }
            }

            "getAdminType" -> {
                GqlMockUtil.createSuccessResponse(AdminTypeResponse())
            }

            "profile" -> {
                val profilePojo = ProfilePojo(
                    profileInfo = ProfileInfo(
                        userId = "123456",
                        fullName = "Testing User",
                        firstName = "Testing",
                        email = "testinguser@email.com",
                        birthday = "2020-11-11",
                        gender = "male",
                        isPhoneVerified = true,
                        profilePicture = "",
                        isCreatedPassword = true,
                        isLoggedIn = true
                    )
                )
                val mockResponse = when (profileConfig) {
                    is Config.Default -> GqlMockUtil.createSuccessResponse(profilePojo)

                    is Config.WithResponse -> GqlMockUtil.createSuccessResponse(((profileConfig as Config.WithResponse).response as ProfilePojo))
                    else -> GqlMockUtil.createSuccessResponse(profilePojo)
                }
                mockResponse
            }

            else -> {
                Timber.w("unhandled request ${GqlQueryParser.parse(requests).joinToString()}")
                throw IllegalArgumentException()
            }
        }
    }
}

sealed class Config {
    object Default : Config()
    data class WithResponse(val response: Any) : Config()
    object Error : Config()
    object Success : Config()
}
