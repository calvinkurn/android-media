package com.tokopedia.challenges.view.utils;

import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

import java.util.List;

public interface ChallengesFragmentCallbacks {

    void replaceFragment(List<SubmissionResult> outlets);

    List<SubmissionResult> getSubmissions();
}
