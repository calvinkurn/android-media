package com.tokopedia.loginregister.login.behaviour.data

import android.content.res.Resources
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import java.lang.reflect.Type
import javax.inject.Named

class LoginTokenUseCaseStub(resources: Resources,
                            graphqlUseCase: GraphqlUseCase,
                            @Named(SessionModule.SESSION_MODULE)
                            userSession: UserSessionInterface): LoginTokenUseCase(resources, graphqlUseCase, userSession) {

    var response = LoginTokenPojo()

    override fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>, resId: Int) {
        val gqlResponse = GraphqlResponse(
                mapOf<Type, Any>(
                        LoginTokenPojo::class.java to response
                ),
                mapOf(),
                false)
        subscriber.onNext(gqlResponse)
    }
}