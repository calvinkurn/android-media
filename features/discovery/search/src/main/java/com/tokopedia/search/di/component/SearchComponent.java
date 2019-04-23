package com.tokopedia.search.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.discovery.newdiscovery.di.module.SearchModule;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.presentation.view.activity.SearchActivity;

import dagger.Component;

@SearchScope
@Component(modules = SearchModule.class, dependencies = AppComponent.class)
public interface SearchComponent {
    void inject(SearchActivity searchActivity);

}