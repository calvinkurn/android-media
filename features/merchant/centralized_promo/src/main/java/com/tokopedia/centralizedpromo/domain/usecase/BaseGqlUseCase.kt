package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 2020-02-17
 */

abstract class BaseGqlUseCase<T : Any> : UseCase<T>() {

    var params: RequestParams = RequestParams.EMPTY

    inline fun <reified T> GraphqlResponse.getData(): T {
        return this.getData<T>(T::class.java)
    }
}
