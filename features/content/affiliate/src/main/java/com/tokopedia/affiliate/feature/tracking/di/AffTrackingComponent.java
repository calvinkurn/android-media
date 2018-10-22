package com.tokopedia.affiliate.feature.tracking.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.affiliate.feature.tracking.view.presenter.AffTrackingPresenter;
import com.tokopedia.affiliate.feature.tracking.view.activity.AffiliateTrackingActivity;

import dagger.Component;

@AffTrackingScope
@Component(modules = AffTrackingModule.class, dependencies = BaseAppComponent.class)
public interface AffTrackingComponent {

    void inject(AffiliateTrackingActivity affiliateTrackingActivity);

    void inject(AffTrackingPresenter affTrackingPresenter);

}
