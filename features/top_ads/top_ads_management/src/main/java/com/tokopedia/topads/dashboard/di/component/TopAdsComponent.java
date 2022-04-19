package com.tokopedia.topads.dashboard.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.topads.dashboard.di.module.TopAdsModule;
import com.tokopedia.topads.dashboard.di.module.TopAdsShopModule;
import com.tokopedia.topads.dashboard.di.scope.TopAdsScope;
import com.tokopedia.topads.dashboard.domain.interactor.GetDepositTopAdsUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Component;

/**
 * Created by zulfikarrahman on 9/18/17.
 */

@TopAdsScope
@Component(modules = {TopAdsModule.class, TopAdsShopModule.class}, dependencies = BaseAppComponent.class)
public interface TopAdsComponent {

    @ApplicationContext
    Context context();

    GetDepositTopAdsUseCase getDepositTopAdsUseCase();

    UserSessionInterface userSession();


}
