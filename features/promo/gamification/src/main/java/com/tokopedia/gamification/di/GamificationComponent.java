package com.tokopedia.gamification.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.gamification.cracktoken.fragment.CrackTokenFragment;
import com.tokopedia.gamification.floating.view.fragment.FloatingEggButtonFragment;

import dagger.Component;

/**
 * Created by nabillasabbaha on 3/28/18.
 */
@GamificationScope
@Component(modules = GamificationModule.class, dependencies = BaseAppComponent.class)
public interface GamificationComponent {

    void inject(FloatingEggButtonFragment floatingEggButtonFragment);

    void inject(CrackTokenFragment crackTokenFragment);
}
