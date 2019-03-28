package com.tokopedia.tokopoints.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tokopoints.view.activity.CatalogListingActivity;
import com.tokopedia.tokopoints.view.activity.MyCouponListingActivity;
import com.tokopedia.tokopoints.view.fragment.CatalogListItemFragment;
import com.tokopedia.tokopoints.view.fragment.CatalogListingFragment;
import com.tokopedia.tokopoints.view.fragment.CouponCatalogFragment;
import com.tokopedia.tokopoints.view.fragment.CouponDetailFragment;
import com.tokopedia.tokopoints.view.fragment.HomepageFragment;
import com.tokopedia.tokopoints.view.fragment.MyCouponListingFragment;
import com.tokopedia.tokopoints.view.fragment.SendGiftFragment;
import com.tokopedia.tokopoints.view.fragment.TokoPointsHomeFragmentNew;
import com.tokopedia.tokopoints.view.fragment.ValidateMerchantPinFragment;

import dagger.Component;

@TokoPointScope
@Component(dependencies = BaseAppComponent.class)
public interface TokoPointComponent {
    void inject(HomepageFragment fragment);

    void inject(TokoPointsHomeFragmentNew fragment);

    void inject(CatalogListingFragment fragment);

    void inject(CatalogListItemFragment fragment);

    void inject(MyCouponListingFragment fragment);

    void inject(CouponCatalogFragment fragment);

    void inject(SendGiftFragment fragment);

    void inject(ValidateMerchantPinFragment fragment);

    void inject(MyCouponListingActivity activity);

    void inject(CouponDetailFragment fragment);
}
