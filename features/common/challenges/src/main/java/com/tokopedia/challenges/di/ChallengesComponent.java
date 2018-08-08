package com.tokopedia.challenges.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.challenges.view.fragments.ChallegeneSubmissionFragment;
import com.tokopedia.challenges.view.fragments.ChallengesFragment;
import com.tokopedia.challenges.view.fragments.MySubmissionsFragment;
import com.tokopedia.challenges.view.fragments.SubmitDetailFragment;

import javax.inject.Scope;

import dagger.Component;

@ChallengesScope
@Component(dependencies = BaseAppComponent.class)
public interface ChallengesComponent {
    void inject(ChallengesFragment challengesFragment);

    void inject(ChallegeneSubmissionFragment challegeneSubmissionFragment);

    void inject(MySubmissionsFragment mySubmissionsFragment);

    void inject(SubmitDetailFragment submitDetailFragment);
}
