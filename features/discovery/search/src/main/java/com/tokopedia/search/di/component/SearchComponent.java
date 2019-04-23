package com.tokopedia.search.di.component;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.presentation.view.activity.SearchActivity;

import dagger.Component;

@SearchScope
@Component
public interface SearchComponent extends com.tokopedia.discovery.newdiscovery.di.component.SearchComponent {
    void inject(SearchActivity searchActivity);

}