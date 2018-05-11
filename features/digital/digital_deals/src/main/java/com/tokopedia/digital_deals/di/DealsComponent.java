package com.tokopedia.digital_deals.di;


import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.digital_deals.di.scope.DealsScope;
import com.tokopedia.digital_deals.view.activity.BrandDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsSearchActivity;
import com.tokopedia.digital_deals.view.fragment.DealDetailsFragment;

import dagger.Component;

@DealsScope
@Component(modules = DealsModule.class, dependencies = BaseAppComponent.class)

public interface DealsComponent {

    void inject(DealsHomeActivity activity);

    void inject(DealsSearchActivity activity);

    void inject(BrandDetailsActivity activity);

    void inject(DealDetailsActivity activity);

    void inject(DealDetailsFragment fragment);
}
