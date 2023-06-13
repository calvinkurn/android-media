package com.tokopedia.autocompletecomponent.universal.domain.getuniversalsearch

import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchScope
import com.tokopedia.autocompletecomponent.universal.UniversalConstant
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class UniversalSearchUseCaseModule {

    @UniversalSearchScope
    @Provides
    @Named(UniversalConstant.UNIVERSAL_SEARCH_USE_CASE)
    fun provideUniversalSearchUseCase(): UseCase<UniversalSearchModel> {
        return UniversalSearchUseCase(
            GraphqlCacheStrategy.Builder(CacheType.NONE).build(),
            GraphqlInteractor.getInstance().graphqlRepository
        )
    }
}