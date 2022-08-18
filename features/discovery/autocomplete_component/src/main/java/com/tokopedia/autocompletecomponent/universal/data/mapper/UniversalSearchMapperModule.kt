package com.tokopedia.autocompletecomponent.universal.data.mapper

import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchScope
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import dagger.Module
import dagger.Provides
import rx.functions.Func1

@Module
internal class UniversalSearchMapperModule {

    @UniversalSearchScope
    @Provides
    fun provideUniversalSearchMapper(): Func1<GraphqlResponse, UniversalSearchModel> {
        return UniversalSearchMapper()
    }
}