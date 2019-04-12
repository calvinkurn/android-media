package com.tokopedia.search.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.search.SearchActivity;
import com.tokopedia.search.di.module.SearchModule;

import dagger.Component;

@Component(modules = SearchModule.class, dependencies = BaseAppComponent.class)
public interface SearchComponent {
    void inject(SearchActivity searchActivity);

}