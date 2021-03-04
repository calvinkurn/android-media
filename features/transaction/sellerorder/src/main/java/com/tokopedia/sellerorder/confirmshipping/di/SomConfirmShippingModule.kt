package com.tokopedia.sellerorder.confirmshipping.di

import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerorder.confirmshipping.data.model.SomChangeCourier
import com.tokopedia.sellerorder.confirmshipping.data.model.SomConfirmShipping
import com.tokopedia.sellerorder.confirmshipping.data.model.SomCourierList
import dagger.Module
import dagger.Provides

/**
 * Created by fwidjaja on 06/05/20.
 */

@Module
class SomConfirmShippingModule {

    @SomConfirmShippingScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @SomConfirmShippingScope
    @Provides
    fun provideSomConfirmShippingResultUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomConfirmShipping.Data> = GraphqlUseCase(graphqlRepository)

    @SomConfirmShippingScope
    @Provides
    fun provideSomGetCourierListUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment> = GraphqlUseCase(graphqlRepository)

    @SomConfirmShippingScope
    @Provides
    fun provideSomChangeCourierUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomChangeCourier.Data> = GraphqlUseCase(graphqlRepository)
}