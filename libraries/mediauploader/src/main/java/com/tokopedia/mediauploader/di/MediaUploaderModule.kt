package com.tokopedia.mediauploader.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.data.consts.MediaUploaderQuery
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import com.tokopedia.mediauploader.domain.DataPolicyUseCase
import dagger.Module
import dagger.Provides

@Module class MediaUploaderModule {

//    @MediaUploaderQualifier
//    @Provides
//    fun provideGraphqlDataPolicyUseCase(
//            repository: GraphqlRepository
//    ): GraphqlUseCase<DataUploaderPolicy> {
//        return GraphqlUseCase(repository)
//    }

    @MediaUploaderQualifier
    @Provides
    fun provideDataPolicyUseCase() : DataPolicyUseCase {
        return DataPolicyUseCase(
                MediaUploaderQuery.dataPolicyQuery,
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }

}