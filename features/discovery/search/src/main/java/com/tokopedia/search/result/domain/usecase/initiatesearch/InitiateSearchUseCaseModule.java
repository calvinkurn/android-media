package com.tokopedia.search.result.domain.usecase.initiatesearch;

import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.search.result.data.repository.initiatesearch.InitiateSearchRepositoryModule;
import com.tokopedia.usecase.UseCase;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module(includes = InitiateSearchRepositoryModule.class)
public class InitiateSearchUseCaseModule {

    @SearchScope
    @Provides
    UseCase<InitiateSearchModel> provideInitiateSearchUseCase(Repository<InitiateSearchModel> initiateSearchModelRepository) {
        return new InitiateSearchUseCase(initiateSearchModelRepository);
    }
}