package com.tokopedia.mediauploader.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import com.tokopedia.mediauploader.domain.DataPolicyUseCase
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides

@Module class MediaUploaderModule {

    @MediaUploaderQualifier
    @Provides
    fun provideDataPolicyUseCase(): UseCase<DataUploaderPolicy> {
        return DataPolicyUseCase(
                "",
                GraphqlCacheStrategy.Builder(CacheType.NONE).build(),
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }

}