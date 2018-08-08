package com.tokopedia.challenges.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.challenges.view.adapter.SubmissionItemAdapter;
import com.tokopedia.challenges.view.fragments.AllSubmissionFragment;
import com.tokopedia.challenges.view.fragments.ChallegeneSubmissionFragment;
import com.tokopedia.challenges.view.fragments.ChallengesFragment;

import dagger.Component;

@ChallengesScope
@Component(dependencies = BaseAppComponent.class)
public interface ChallengesComponent {

    void inject(ChallengesFragment challengesFragment);

    void inject(ChallegeneSubmissionFragment challegeneSubmissionFragment);

    void inject(AllSubmissionFragment allSubmissionFragment);

    void inject(SubmissionItemAdapter submissionItemAdapter);
}
