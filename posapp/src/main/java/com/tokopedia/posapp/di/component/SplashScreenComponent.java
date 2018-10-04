package com.tokopedia.posapp.di.component;

import com.tokopedia.posapp.PosAppSplashScreen;
import com.tokopedia.posapp.di.scope.SplashScreenScope;

import dagger.Component;

/**
 * @author okasurya on 4/24/18.
 */
@SplashScreenScope
@Component(dependencies = PosAppComponent.class)
public interface SplashScreenComponent {
    void inject(PosAppSplashScreen posAppSplashScreen);
}
