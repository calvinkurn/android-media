package com.tokopedia.search.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.module.SearchModule;
import com.tokopedia.search.presentation.view.activity.SearchActivity;

import dagger.Component;

@SearchScope
@Component(modules = SearchModule.class, dependencies = BaseAppComponent.class)
public interface SearchComponent {
    void inject(SearchActivity searchActivity);

}