package com.tokopedia.sellerorder.requestpickup.di

import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import dagger.Module
import dagger.Provides

/**
 * Created by fwidjaja on 12/05/20.
 */
@Module
class SomConfirmReqPickupModule {
    @SomConfirmReqPickupScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @SomConfirmReqPickupScope
    @Provides
    fun provideSomConfirmReqPickupUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomConfirmReqPickup.Data> = GraphqlUseCase(graphqlRepository)
}