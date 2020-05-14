package com.tokopedia.logisticorder.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticorder.view.TrackingPageFragment;

import dagger.Component;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

@TrackingPageScope
@Component(modules = {TrackingPageModule.class, GqlQueryModule.class}, dependencies = BaseAppComponent.class)
public interface TrackingPageComponent {

    void inject(TrackingPageFragment fragment);

}
