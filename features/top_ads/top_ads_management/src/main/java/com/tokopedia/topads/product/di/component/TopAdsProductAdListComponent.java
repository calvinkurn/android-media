package com.tokopedia.topads.product.di.component;

import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.product.di.module.TopAdsProductAdListModule;
import com.tokopedia.topads.product.di.scope.TopAdsProductAdScope;
import com.tokopedia.topads.product.view.fragment.TopAdsProductListFragment;

import dagger.Component;

/**
 * Created by hadi.putra on 04/05/18.
 */

@TopAdsProductAdScope
@Component(modules = {TopAdsProductAdListModule.class}, dependencies = {TopAdsComponent.class})
public interface TopAdsProductAdListComponent {

    void inject(TopAdsProductListFragment fragment);
}
