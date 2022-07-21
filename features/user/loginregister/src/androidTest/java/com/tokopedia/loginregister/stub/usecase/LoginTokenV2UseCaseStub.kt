package com.tokopedia.loginregister.stub.usecase

import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenV2UseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Named

class LoginTokenV2UseCaseStub(
    graphqlUseCase: GraphqlUseCaseStub<LoginTokenPojoV2>,
    @Named(SessionModule.SESSION_MODULE)
        private val userSession: UserSessionInterface
): LoginTokenV2UseCase(graphqlUseCase, userSession) {
    var response = LoginTokenPojoV2()
    override suspend fun executeOnBackground(): LoginTokenPojoV2 {
        println("executed login token v2")
        return response
    }
}