package com.tokopedia.sellerappwidget.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 17/11/20
 */

abstract class BaseUseCase<T: Any> : UseCase<T>() {

    var params: RequestParams = RequestParams.EMPTY

    inline fun <reified T> GraphqlResponse.getData(): T {
        return this.getData(T::class.java)
    }
}