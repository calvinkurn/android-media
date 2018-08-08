package com.tokopedia.challenges.view.presenter;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.challenges.domain.usecase.GetSubmissionChallengesUseCase;
import com.tokopedia.challenges.view.contractor.ChallengeSubmissonContractor;
import com.tokopedia.challenges.view.model.Challenge;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.common.network.data.model.RestResponse;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ChallengeSubmissionPresenter extends BaseDaggerPresenter<ChallengeSubmissonContractor.View> implements ChallengeSubmissonContractor.Presenter {

    GetSubmissionChallengesUseCase getSubmissionChallengesUseCase;

    @Inject
    public ChallengeSubmissionPresenter(GetSubmissionChallengesUseCase getSubmissionChallengesUseCase) {
        this.getSubmissionChallengesUseCase = getSubmissionChallengesUseCase;
    }

    @Override
    public void initialize() {
        getSubmissionChallenges();
    }

    @Override
    public void onDestroy() {
        getSubmissionChallengesUseCase.unsubscribe();
    }

    public void getSubmissionChallenges() {
        getSubmissionChallengesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getSubmissionChallenges();
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                RestResponse res1 = typeRestResponseMap.get(SubmissionResponse.class);
                SubmissionResponse submissionResponse = res1.getData();
                getView().renderChallengeDetail(submissionResponse);
            }
        });
    }
}
