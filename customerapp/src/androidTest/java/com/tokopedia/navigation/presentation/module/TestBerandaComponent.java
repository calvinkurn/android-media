package com.tokopedia.navigation.presentation.module;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.home.beranda.di.BerandaComponent;
import com.tokopedia.home.beranda.di.HomeScope;
import com.tokopedia.home.beranda.di.module.ShopModule;
import com.tokopedia.home.common.ApiModule;
import com.tokopedia.home.beranda.di.module.HomeModule;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment;
import com.tokopedia.navigation.presentation.activity.MainParentActivityTest;

import dagger.Component;

/**
 * @author by errysuprayogi on 11/27/17.
 */

@HomeScope
@Component(modules = {ApiModule.class, TestHomeModule.class, ShopModule.class}, dependencies = BaseAppComponent.class)
public interface TestBerandaComponent extends BerandaComponent {

    void inject(MainParentActivityTest mainParentActivityTest);
}
