package com.tokopedia.loginregister.login.behaviour.data

import android.content.res.Resources
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.data.profile.ShopBasicData
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import rx.Subscriber
import java.lang.reflect.Type

class GetProfileUseCaseStub(resources: Resources,
                            graphqlUseCase: GraphqlUseCase):
        GetProfileUseCase(resources, graphqlUseCase) {

    var fakeProfile = ProfilePojo(
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

    override fun execute(subscriber: Subscriber<GraphqlResponse>) {
        val gqlResponse = GraphqlResponse(
                mapOf<Type, Any>(
                        ProfilePojo::class.java to fakeProfile
                ),
                mapOf(),
                false)
        subscriber.onNext(gqlResponse)
    }
}