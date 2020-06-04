package com.tokopedia.navigation.presentation.module;

import com.tokopedia.home.beranda.di.module.HomeModule;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;

import dagger.Module;

@Module
public class TestHomeModule extends HomeModule {
    private HomePresenter homePresenter;

}
