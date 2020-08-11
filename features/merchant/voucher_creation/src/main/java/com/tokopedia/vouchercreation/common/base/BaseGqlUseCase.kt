package com.tokopedia.vouchercreation.common.base

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 11/05/20
 */

abstract class BaseGqlUseCase<T : Any> : UseCase<T>() {

    var params: RequestParams = RequestParams.EMPTY

    inline fun <reified T> GraphqlResponse.getData(): T {
        return this.getData<T>(T::class.java)
    }
}