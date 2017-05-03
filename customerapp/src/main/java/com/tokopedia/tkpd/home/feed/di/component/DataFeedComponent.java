package com.tokopedia.tkpd.home.feed.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.home.feed.di.module.DataFeedModule;
import com.tokopedia.tkpd.home.feed.di.scope.DataFeedScope;
import com.tokopedia.tkpd.home.feed.view.FragmentProductFeed;

import dagger.Component;

/**
 * @author Kulomady on 1/9/17.
 */

@DataFeedScope
@Component(modules = DataFeedModule.class, dependencies = AppComponent.class)
public interface DataFeedComponent {
    void inject(FragmentProductFeed fragmentProductFeed);
}
