package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.model.FollowResponseModel
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides

@SearchScope
@Module
class FollowKolPostUseCaseModule {

    @SearchScope
    @Provides
    fun provideFollowKolPostUseCase(@ApplicationContext context: Context) : UseCase<FollowResponseModel> {
        return FollowKolPostGqlUseCase(context, GraphqlUseCase())
    }
}