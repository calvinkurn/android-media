package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import java.util.*

abstract class BaseGraphqlUseCase<T : Any>(graphqlRepository: GraphqlRepository) : GraphqlUseCase<T>(graphqlRepository) {

    companion object {
        const val ERROR_MESSAGE_PARAM_NOT_FOUND = "Terjadi kesalahan, silahkan coba lagi."
    }

    protected var params: Queue<RequestParams> = LinkedList<RequestParams>()

    inline fun <reified T> GraphqlResponse.getData(): T {
        return this.getData(T::class.java)
    }
}