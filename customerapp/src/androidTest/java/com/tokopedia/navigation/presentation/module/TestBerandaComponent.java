package com.tokopedia.navigation.presentation.module;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.home.beranda.di.BerandaComponent;
import com.tokopedia.home.beranda.di.HomeScope;
import com.tokopedia.home.common.ApiModule;
import com.tokopedia.navigation.presentation.activity.MainParentActivityTest;

import dagger.Component;

/**
 * @author by errysuprayogi on 11/27/17.
 */

@HomeScope
@Component(modules = {ApiModule.class, TestHomeModule.class}, dependencies = BaseAppComponent.class)
public interface TestBerandaComponent extends BerandaComponent {

    void inject(MainParentActivityTest mainParentActivityTest);
}
