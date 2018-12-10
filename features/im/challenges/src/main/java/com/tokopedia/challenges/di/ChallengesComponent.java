package com.tokopedia.challenges.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.challenges.domain.usecase.PostMapBranchUrlUseCase;
import com.tokopedia.challenges.view.adapter.SubmissionItemAdapter;
import com.tokopedia.challenges.view.fragments.AllSubmissionFragment;
import com.tokopedia.challenges.view.fragments.ChallegeneSubmissionFragment;
import com.tokopedia.challenges.view.fragments.ChallengesFragment;
import com.tokopedia.challenges.view.fragments.submit.ChallengesSubmitFragment;
import com.tokopedia.challenges.view.fragments.MySubmissionsFragment;
import com.tokopedia.challenges.view.fragments.SubmitDetailFragment;
import com.tokopedia.challenges.view.service.UploadChallengeService;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.share.ShareInstagramBottomSheet;

import dagger.Component;

@ChallengesScope
@Component(dependencies = BaseAppComponent.class)
public interface ChallengesComponent {
    void inject(ChallengesFragment challengesFragment);

    void inject(ChallegeneSubmissionFragment challegeneSubmissionFragment);

    void inject(ChallengesSubmitFragment challengesSubmitFragment);

    void inject(MySubmissionsFragment mySubmissionsFragment);

    void inject(SubmitDetailFragment submitDetailFragment);

    void inject(AllSubmissionFragment allSubmissionFragment);

    void inject(SubmissionItemAdapter submissionItemAdapter);

    void inject(UploadChallengeService uploadChallengeService);

    void inject(ShareBottomSheet shareBottomSheet);

}
