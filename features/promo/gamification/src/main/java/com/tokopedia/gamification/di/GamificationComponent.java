package com.tokopedia.gamification.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;

import dagger.Component;

/**
 * Created by nabillasabbaha on 3/28/18.
 */
@GamificationScope
@Component(modules = GamificationModule.class, dependencies = BaseAppComponent.class)
public interface GamificationComponent {

}
