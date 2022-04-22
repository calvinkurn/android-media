package com.tokopedia.imagepicker_insta.di.module

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on April 21, 2022
 */
@Module
class ImagePickerModule {
    @Provides
    fun provideGraphqlRepositoryCase(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}