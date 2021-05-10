package com.tokopedia.logisticorder.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticorder.view.TrackingPageFragment;
import com.tokopedia.logisticorder.view.TrackingPageFragmentKotlin;

import dagger.Component;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

@TrackingPageScope
@Component(modules = {TrackingPageModule.class, GqlQueryModule.class}, dependencies = BaseAppComponent.class)
public interface TrackingPageComponent {

    void inject(TrackingPageFragment fragment);
    void inject(TrackingPageFragmentKotlin fragment);

}
