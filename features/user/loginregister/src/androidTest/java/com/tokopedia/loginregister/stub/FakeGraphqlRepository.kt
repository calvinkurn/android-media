package com.tokopedia.loginregister.stub

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.loginregister.discover.pojo.DiscoverData
import com.tokopedia.loginregister.discover.pojo.DiscoverPojo
import com.tokopedia.loginregister.discover.pojo.ProviderData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import timber.log.Timber

class FakeGraphqlRepository : GraphqlRepository {

    var registerCheckConfig: Config = Config.Default
    var discoverConfig: Config = Config.Default

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        Timber.d("Pass through FakeGraphql $requests")
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
                val obj: DiscoverPojo = when (discoverConfig) {
                    is Config.Default -> DiscoverPojo(
                        DiscoverData(
                            arrayListOf(
                                ProviderData(
                                    "gplus",
                                    "Google",
                                    "https://accounts.tokopedia.com/gplus-login",
                                    "",
                                    "#FFFFFF"
                                ),
                            ), ""
                        )
                    )
                    else -> DiscoverPojo()
                }
                GqlMockUtil.createSuccessResponse(obj)
            }
            else -> throw IllegalArgumentException()
        }
    }

}

sealed class Config {
    object Default : Config()
    data class WithResponse(val response: Any) : Config()
    object Error : Config()
    object Delay : Config()
}