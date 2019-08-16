package com.tokopedia.tradein.usecase

import android.content.Context
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tradein.model.MoneyInCheckoutMutationResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class MoneyInCheckoutMutationUseCase @Inject constructor(private val context: Context,
                                                         private val graphqlUseCase: GraphqlUseCase) : UseCase<MoneyInCheckoutMutationResponse.Data.Checkout.Data.Data>(){
    override fun createObservable(p0: RequestParams?): Observable<MoneyInCheckoutMutationResponse.Data.Checkout.Data.Data> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}