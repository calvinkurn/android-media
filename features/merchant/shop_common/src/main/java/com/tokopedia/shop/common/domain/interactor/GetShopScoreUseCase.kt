package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.common.graphql.data.shopscore.ShopScore
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 15/06/19.
 */
class GetShopScoreUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase)
    : UseCase<ShopScore>() {
    override fun createObservable(requestParams: RequestParams?): Observable<ShopScore> {
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            ShopScore()
        }
    }
}