package com.tokopedia.updateinactivephone.di.module

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.data.repository.UploadImageRepositoryImpl
import com.tokopedia.updateinactivephone.di.UpdateInActiveContext
import com.tokopedia.updateinactivephone.di.UpdateInactivePhoneScope
import com.tokopedia.updateinactivephone.usecase.*
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module class UpdateInactivePhoneModule(val context: Context) {

    @Provides
    @UpdateInActiveContext
    fun provideInActivePhoneContext() = context

    @Provides
    @UpdateInactivePhoneScope
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @UpdateInactivePhoneScope
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @UpdateInactivePhoneScope
    fun provideUploadImageUseCase(uploadImageRepository: UploadImageRepositoryImpl): UploadImageUseCase {
        return UploadImageUseCase(uploadImageRepository)
    }

    @Provides
    @UpdateInactivePhoneScope
    fun provideGetUploadHostUseCase(uploadImageRepository: UploadImageRepositoryImpl): GetUploadHostUseCase {
        return GetUploadHostUseCase(uploadImageRepository)
    }

}
