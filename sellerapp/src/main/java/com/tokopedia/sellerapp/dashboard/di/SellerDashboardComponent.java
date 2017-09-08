package com.tokopedia.sellerapp.dashboard.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.common.logout.di.module.TkpdSellerLogoutModule;
import com.tokopedia.seller.common.logout.di.scope.TkpdSellerLogoutScope;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatClearCacheUseCase;
import com.tokopedia.seller.product.category.domain.interactor.ClearCategoryCacheUseCase;

import dagger.Component;

/**
 * @author sebastianuskh on 5/8/17.
 */
@TkpdSellerLogoutScope
@Component(modules = TkpdSellerLogoutModule.class, dependencies = AppComponent.class)
public interface SellerDashboardComponent {

}
