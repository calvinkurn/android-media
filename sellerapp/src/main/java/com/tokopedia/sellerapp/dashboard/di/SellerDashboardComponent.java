package com.tokopedia.sellerapp.dashboard.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.common.logout.di.module.TkpdSellerLogoutModule;
import com.tokopedia.seller.common.logout.di.scope.TkpdSellerLogoutScope;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatClearCacheUseCase;
import com.tokopedia.seller.product.category.domain.interactor.ClearCategoryCacheUseCase;
import com.tokopedia.sellerapp.dashboard.view.activity.DashboardActivity;
import com.tokopedia.sellerapp.dashboard.view.fragment.DashboardFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 5/8/17.
 */
@SellerDashboardScope
@Component(modules = SellerDashboardModule.class, dependencies = AppComponent.class)
public interface SellerDashboardComponent {
    void inject(DashboardFragment dashboardFragment);
}
