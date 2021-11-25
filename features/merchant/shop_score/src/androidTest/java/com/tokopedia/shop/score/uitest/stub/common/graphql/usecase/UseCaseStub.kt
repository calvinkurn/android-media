package com.tokopedia.shop.score.uitest.stub.common.graphql.usecase

import com.tokopedia.usecase.coroutines.UseCase

open class UseCaseStub<T : Any> : UseCase<T>() {

    open lateinit var response: T

    override suspend fun executeOnBackground(): T {
        return response
    }

}