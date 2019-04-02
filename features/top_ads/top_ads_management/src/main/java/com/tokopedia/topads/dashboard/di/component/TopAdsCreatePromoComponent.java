package com.tokopedia.topads.dashboard.di.component;

import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.di.scope.TopAdsManagementScope;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsCheckProductPromoFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDetailGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditCostExistingGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditCostShopFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditCostWithoutGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditGroupMainPageFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditGroupNameFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditScheduleExistingGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditScheduleShopFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsEditScheduleWithoutGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewCostNewGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewCostWithoutGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewProductListExistingGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewProductListNewGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewProductListWithoutGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleNewGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleShopFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 8/13/17.
 */

@TopAdsManagementScope
@Component(modules = TopAdsCreatePromoModule.class, dependencies = TopAdsComponent.class)
public interface TopAdsCreatePromoComponent {
    void inject(TopAdsNewScheduleNewGroupFragment topAdsNewScheduleFragment);

    void inject(TopAdsNewCostWithoutGroupFragment topAdsNewCostWithoutGroupFragment);

    void inject(TopAdsNewProductListExistingGroupFragment topAdsNewProductListExistingGroupFragment);

    void inject(TopAdsNewProductListNewGroupFragment topAdsNewProductListNewGroupFragment);

    void inject(TopAdsNewProductListWithoutGroupFragment topAdsNewProductListWithoutGroupFragment);

    void inject(TopAdsEditCostWithoutGroupFragment topAdsEditCostWithoutGroupFragment);

    void inject(TopAdsEditCostShopFragment topAdsEditCostShopFragment);

    void inject(TopAdsEditCostExistingGroupFragment topAdsEditCostExistingGroupFragment);

    void inject(TopAdsNewScheduleShopFragment topAdsNewScheduleShopFragment);

    void inject(TopAdsEditScheduleExistingGroupFragment topAdsEditScheduleExistingGroupFragment);

    void inject(TopAdsEditScheduleShopFragment topAdsEditScheduleShopFragment);

    void inject(TopAdsEditScheduleWithoutGroupFragment topAdsEditScheduleWithoutGroupFragment);

    void inject(TopAdsEditGroupNameFragment topAdsEditGroupNameFragment);

    void inject(TopAdsNewCostNewGroupFragment topAdsNewCostNewGroupFragment);

    void inject(TopAdsDetailGroupFragment topAdsDetailGroupFragment);

    void inject(TopAdsEditGroupMainPageFragment topAdsEditGroupMainPageFragment);

    void inject(TopAdsCheckProductPromoFragment topAdsCheckProductPromoFragment);
}
