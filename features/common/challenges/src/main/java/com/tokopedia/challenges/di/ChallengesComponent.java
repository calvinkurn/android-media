package com.tokopedia.challenges.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.challenges.view.fragments.ChallengesFragment;

import javax.inject.Scope;

import dagger.Component;

@ChallengesScope
@Component(dependencies = BaseAppComponent.class)
public interface ChallengesComponent {
    void inject (ChallengesFragment challengesFragment);
}
