package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.discovery.common.domain.Repository;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.usecase.UseCase;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class InitiateSearchUseCaseModule {

    @Provides
    UseCase<InitiateSearchModel> provideInitiateSearchUseCase(Repository<InitiateSearchModel> initiateSearchModelRepository) {
        return new InitiateSearchUseCase(initiateSearchModelRepository);
    }
}