package com.tokopedia.loginregister.login.stub

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser

class FakeGraphqlRepository : GraphqlRepository {

    var registerCheckConfig: Config = Config.Default

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return when (GqlQueryParser.parse(requests).first()) {
            "registerCheck" -> {
                val obj = when (registerCheckConfig) {
                    Config.WithResponse -> RegisterCheckPojo(
                        RegisterCheckData(
                            isExist = true,
                            useHash = true,
                            userID = "123456",
                            registerType = "email",
                            view = "yoris.prayogo@tokopedia.com"
                        )
                    )
                    else -> RegisterCheckPojo()
                }
                GqlMockUtil.createSuccessResponse(obj)
            }
            else -> throw IllegalArgumentException()
        }
    }

}

sealed class Config {
    object Default : Config()
    object WithResponse : Config()
    object Error : Config()
    object Delay : Config()
}