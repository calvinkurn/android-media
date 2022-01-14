package com.tokopedia.topchat.stub.chatroom.usecase

import android.content.res.Resources
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

class ToggleFavouriteShopUseCaseStub @Inject constructor(
    graphqlUseCase: GraphqlUseCase,
    resources: Resources
): ToggleFavouriteShopUseCase(graphqlUseCase, resources) {

    var isError = false
    var response = true

    //Mock the observable, need to migrate the use case to coroutine
    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        if (isError) throw MessageErrorException("Oops!")
        return Observable.just(response)
    }

}