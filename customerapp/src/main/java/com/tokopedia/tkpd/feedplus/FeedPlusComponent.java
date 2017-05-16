package com.tokopedia.tkpd.feedplus;

import com.tokopedia.core.base.di.component.AppComponent;

import dagger.Component;

/**
 * @author by nisie on 5/15/17.
 */

@FeedPlusScope
@Component(modules = FeedPlusModule.class, dependencies = AppComponent.class)
public interface FeedPlusComponent {

    void inject(FeedPlusFragment feedPlusFragment);
}
