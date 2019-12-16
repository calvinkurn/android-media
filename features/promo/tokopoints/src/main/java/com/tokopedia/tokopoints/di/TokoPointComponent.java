package com.tokopedia.tokopoints.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tokopoints.view.activity.CouponListingStackedActivity;
import com.tokopedia.tokopoints.view.fragment.AddPointsFragment;
import com.tokopedia.tokopoints.view.fragment.CatalogListItemFragment;
import com.tokopedia.tokopoints.view.fragment.CatalogListingFragment;
import com.tokopedia.tokopoints.view.fragment.CouponCatalogFragment;
import com.tokopedia.tokopoints.view.fragment.CouponDetailFragment;
import com.tokopedia.tokopoints.view.fragment.CouponListingStackedFragment;
import com.tokopedia.tokopoints.view.pointhistory.PointHistoryFragment;
import com.tokopedia.tokopoints.view.fragment.SendGiftFragment;
import com.tokopedia.tokopoints.view.fragment.TokoPointsHomeFragmentNew;
import com.tokopedia.tokopoints.view.fragment.ValidateMerchantPinFragment;

import dagger.Component;

@TokoPointScope
@Component(dependencies = BaseAppComponent.class , modules = {ViewModelModule.class,TokopointsQueryModule.class})
public interface TokoPointComponent {

    void inject(TokoPointsHomeFragmentNew fragment);

    void inject(CatalogListingFragment fragment);

    void inject(CatalogListItemFragment fragment);


    void inject(CouponListingStackedFragment fragment);

    void inject(CouponCatalogFragment fragment);

    void inject(SendGiftFragment fragment);

    void inject(ValidateMerchantPinFragment fragment);


    void inject(CouponListingStackedActivity activity);

    void inject(CouponDetailFragment fragment);

    void inject(PointHistoryFragment fragment);

    void inject(AddPointsFragment fragment);
}
