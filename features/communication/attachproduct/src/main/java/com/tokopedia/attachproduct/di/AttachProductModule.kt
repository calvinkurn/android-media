package com.tokopedia.attachproduct.di

import android.content.Context
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.attachproduct.R
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Hendri on 19/02/18.
 */
@Module
class AttachProductModule(private val context: Context) {

    @Provides
    @AttachProductContext
    fun provideAttachProductContext(): Context {
        return context;
    }

    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSession {
        return UserSession(context)
    }

    @Provides
    @AttachProductScope
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO


    @Provides
    @AttachProductScope
    fun provideQuery(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_query_attach_product)
    }
}
