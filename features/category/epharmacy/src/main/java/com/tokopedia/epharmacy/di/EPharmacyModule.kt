package com.tokopedia.epharmacy.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.epharmacy.usecase.UploadPrescriptionUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

@Module
class EPharmacyModule {

    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @CoroutineMainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @CoroutineBackgroundDispatcher
    fun provideBackgroundDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideInterceptors(loggingInterceptor: HttpLoggingInterceptor): MutableList<Interceptor> {
        return mutableListOf(loggingInterceptor)
    }

    @Provides
    fun provideRestRepository(interceptors: MutableList<Interceptor>,
                              @ApplicationContext context: Context): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @Provides
    fun provideEPharmacyUploadPrescriptionUseCase(restRepository: RestRepository): UploadPrescriptionUseCase {
        return UploadPrescriptionUseCase(restRepository)
    }
}
