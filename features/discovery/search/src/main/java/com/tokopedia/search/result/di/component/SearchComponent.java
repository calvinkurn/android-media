package com.tokopedia.search.result.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.di.module.SearchModule;
import com.tokopedia.search.result.presentation.view.activity.SearchActivity;

import dagger.Component;

@SearchScope
@Component(modules = SearchModule.class, dependencies = AppComponent.class)
public interface SearchComponent {
    void inject(SearchActivity searchActivity);

}