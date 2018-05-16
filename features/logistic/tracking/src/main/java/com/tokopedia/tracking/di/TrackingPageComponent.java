package com.tokopedia.tracking.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tracking.view.TrackingPageFragment;

import dagger.Component;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

@TrackingPageScope
@Component(modules = TrackingPageModule.class, dependencies = BaseAppComponent.class)
public interface TrackingPageComponent {

    void inject(TrackingPageFragment fragment);

}
