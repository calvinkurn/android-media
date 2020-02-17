package com.tokopedia.profilecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.profilecommon.domain.pojo.UserProfileCompletionData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody

/**
 * Created by Ade Fulki on 2019-12-13.
 * ade.hadian@tokopedia.com
 */

fun FeatureBody.createGetUserProfileCompletionUseCaseTestInstance(){
    val graphqlRepository by memoized {
        mockk<GraphqlRepository>(relaxed = true)
    }
}

fun TestBody.createGetUserProfileCompletionUseCase(): GetUserProfileCompletionUseCase {
    val graphqlRepository by memoized<GraphqlRepository>()
    return GetUserProfileCompletionUseCase(String(), graphqlRepository)
}

fun GetUserProfileCompletionUseCase.givenResultSuccessUserProfileCompletionData(
        userProfileCompletionData: UserProfileCompletionData
){
    coEvery { getData() } returns Success(userProfileCompletionData)
}

fun GetUserProfileCompletionUseCase.givenResultFailedsUserProfileCompletionData(
        throwable: Throwable
){
    coEvery { getData() } returns Fail(throwable)
}
