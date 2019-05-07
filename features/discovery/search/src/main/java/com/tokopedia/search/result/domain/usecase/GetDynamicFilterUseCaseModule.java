package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.usecase.UseCase;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class GetDynamicFilterUseCaseModule {

    @SearchScope
    @Provides
    UseCase<DynamicFilterModel> provideGetDynamicFilterUseCase() {
        return null;
    }
}
