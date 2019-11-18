package com.tokopedia.home.beranda.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.home.beranda.di.module.HomeModule;
import com.tokopedia.home.beranda.di.module.HomeRawModule;
import com.tokopedia.home.beranda.di.module.ShopModule;
import com.tokopedia.home.beranda.di.module.ViewModelModule;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemFragment;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFeedFragment;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment;
import com.tokopedia.home.beranda.presentation.view.fragment.TabBusinessFragment;
import com.tokopedia.home.common.ApiModule;
import dagger.Component;

/**
 * @author by errysuprayogi on 11/27/17.
 */

@HomeScope
@Component(modules = {
        ApiModule.class,
        HomeModule.class,
        HomeRawModule.class,
        ShopModule.class,
        com.tokopedia.v2.home.di.HomeModule.class,
        ViewModelModule.class
}, dependencies = BaseAppComponent.class)
public interface BerandaComponent {

    void inject(HomeFragment homeFragment);

    void inject(HomeFeedFragment homeFeedFragment);

    void inject(com.tokopedia.v2.home.ui.fragment.HomeFragment homeFeedFragment);

    void inject(HomePresenter homePresenter);

    void inject(HomeFeedPresenter homeFeedPresenter);

    void inject(TabBusinessFragment fragment);

    void inject(BusinessUnitItemFragment fragment);
}