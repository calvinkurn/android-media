package com.tokopedia.home.beranda.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.home.common.ApiModule;
import com.tokopedia.home.beranda.di.module.HomeModule;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment;

import dagger.Component;

/**
 * @author by errysuprayogi on 11/27/17.
 */

@HomeScope
@Component(modules = {ApiModule.class, HomeModule.class}, dependencies = BaseAppComponent.class)
public interface BerandaComponent {

    void inject(HomeFragment homeFragment);

    void inject(HomePresenter homePresenter);

}
