package com.tokopedia.hotel.orderdetail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.domain.GraphqlRepository
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.usecase.GetHotelOrderDetailUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by jessica on 09/05/19
 */

@Module
class HotelOrderDetailModule {

    @HotelOrderDetailScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)
}