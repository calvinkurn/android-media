package com.tokopedia.search.result.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.module.GraphqlModule;
import com.tokopedia.search.di.module.SearchTrackingModule;
import com.tokopedia.search.di.module.UserSessionModule;
import com.tokopedia.search.gql.initiatesearch.GqlInitiateSearchSpecModule;
import com.tokopedia.search.result.presentation.presenter.SearchPresenterModule;
import com.tokopedia.search.result.presentation.view.activity.SearchActivity;

import dagger.Component;

@SearchScope
@Component(modules = {
        UserSessionModule.class,
        SearchTrackingModule.class,
        GraphqlModule.class,
        GqlInitiateSearchSpecModule.class,
        SearchPresenterModule.class
}, dependencies = BaseAppComponent.class)
public interface SearchComponent {

    void inject(SearchActivity searchActivity);
}