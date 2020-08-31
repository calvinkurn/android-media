package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.InitialStateData
import com.tokopedia.autocomplete.initialstate.data.InitialStateDataMapper
import com.tokopedia.graphql.data.model.GraphqlResponse
import dagger.Module
import dagger.Provides
import rx.functions.Func1

@InitialStateScope
@Module
class InitialStateDataMapperModule {

    @InitialStateScope
    @Provides
    internal fun provideInitialStateDataMapper(): Func1<GraphqlResponse, List<InitialStateData>> {
        return InitialStateDataMapper()
    }
}