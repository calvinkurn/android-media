package com.tokopedia.tokopoints.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tokopoints.view.fragment.CatalogListItemFragment;
import com.tokopedia.tokopoints.view.fragment.CatalogListingFragment;
import com.tokopedia.tokopoints.view.fragment.MyCouponListingFragment;
import com.tokopedia.tokopoints.view.fragment.HomepageFragment;

import dagger.Component;

@TokoPointScope
@Component(dependencies = BaseAppComponent.class)
public interface TokoPointComponent {
    void inject(HomepageFragment fragment);

    void inject(CatalogListingFragment fragment);

    void inject(CatalogListItemFragment fragment);

    void inject(MyCouponListingFragment fragment);

}
