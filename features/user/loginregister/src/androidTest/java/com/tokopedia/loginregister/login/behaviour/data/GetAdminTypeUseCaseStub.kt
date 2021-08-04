package com.tokopedia.loginregister.login.behaviour.data

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sessioncommon.data.admin.AdminTypeResponse
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import rx.Subscriber
import java.lang.reflect.Type

class GetAdminTypeUseCaseStub(graphqlUseCase: GraphqlUseCase): GetAdminTypeUseCase(graphqlUseCase) {

    var response = AdminTypeResponse()

    override fun execute(subscriber: Subscriber<GraphqlResponse>, source: String) {
        val gqlResponse = GraphqlResponse(
                mapOf<Type, Any>(
                        AdminTypeResponse::class.java to response
                ),
                mapOf(),
                false)
        subscriber.onNext(gqlResponse)
    }
}