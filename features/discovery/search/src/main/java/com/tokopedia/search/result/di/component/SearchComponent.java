package com.tokopedia.search.result.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.di.module.SearchModule;
import com.tokopedia.search.result.presentation.SearchContract;
import com.tokopedia.search.result.presentation.view.activity.SearchActivity;

import dagger.Component;

@SearchScope
@Component(modules = SearchModule.class, dependencies = BaseAppComponent.class)
public interface SearchComponent {

    @SearchScope
    SearchContract.Presenter getSearchPresenter();

    void inject(SearchActivity searchActivity);
}