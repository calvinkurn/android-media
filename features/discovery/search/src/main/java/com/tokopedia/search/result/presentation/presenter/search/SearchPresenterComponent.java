package com.tokopedia.search.result.presentation.presenter.search;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.domain.usecase.initiatesearch.InitiateSearchUseCaseModule;

import dagger.Component;

@SearchScope
@Component(modules = {
        InitiateSearchUseCaseModule.class
}, dependencies = BaseAppComponent.class)
public interface SearchPresenterComponent {

    void inject(SearchPresenter searchPresenter);
}
