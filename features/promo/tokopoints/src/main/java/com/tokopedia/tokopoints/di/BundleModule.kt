package com.tokopedia.tokopoints.di

import android.content.Context
import android.os.Bundle
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class BundleModule constructor(private val bundle : Bundle = Bundle()){

    @Provides
    @TokoPointScope
    fun getBundle() = bundle


    @Provides
    @TokoPointScope
    @Named(tp_send_gift_failed_title)
    fun gettp_send_gift_failed_title(context : Context): String {
        return context.getString(R.string.tp_send_gift_failed_title)
    }

    @Provides
    @TokoPointScope
    @Named(tp_send_gift_failed_message)
    fun gettp_send_gift_failed_message(context : Context): String {
        return context.getString(R.string.tp_send_gift_failed_message)
    }

    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)

    @Provides
    fun getMultiGraphQlRequest(graphqlRepository: GraphqlRepository) = MultiRequestGraphqlUseCase(graphqlRepository)

    @Provides
    fun getGQLUserCase(graphqlRepository: GraphqlRepository) = GraphqlUseCase<GraphqlResponse>(graphqlRepository)



    companion object{
        const val tp_send_gift_failed_title = "tp_send_gift_failed_title"
        const val tp_send_gift_failed_message = "tp_send_gift_failed_message"
    }

}
