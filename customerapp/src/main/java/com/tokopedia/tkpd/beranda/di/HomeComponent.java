package com.tokopedia.tkpd.beranda.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.beranda.di.module.ApiModule;
import com.tokopedia.tkpd.beranda.di.module.HomeModule;
import com.tokopedia.tkpd.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.tkpd.beranda.presentation.view.fragment.HomeFragment;

import dagger.Component;

/**
 * @author by errysuprayogi on 11/27/17.
 */

@HomeScope
@Component(modules = {ApiModule.class, HomeModule.class}, dependencies = AppComponent.class)
public interface HomeComponent {

    void inject(HomeFragment homeFragment);

    void inject(HomePresenter homePresenter);
}
