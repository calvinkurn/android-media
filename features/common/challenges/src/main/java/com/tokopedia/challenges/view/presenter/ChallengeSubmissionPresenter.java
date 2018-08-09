package com.tokopedia.challenges.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.challenges.domain.usecase.GetSubmissionChallengesUseCase;
import com.tokopedia.challenges.domain.usecase.GetTermsNConditionUseCase;
import com.tokopedia.challenges.view.contractor.ChallengeSubmissonContractor;
import com.tokopedia.challenges.view.model.TermsNCondition;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.common.network.data.model.RestResponse;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ChallengeSubmissionPresenter extends BaseDaggerPresenter<ChallengeSubmissonContractor.View> implements ChallengeSubmissonContractor.Presenter {

    GetSubmissionChallengesUseCase getSubmissionChallengesUseCase;
    GetTermsNConditionUseCase getTermsNConditionUseCase;

    @Inject
    public ChallengeSubmissionPresenter(GetSubmissionChallengesUseCase getSubmissionChallengesUseCase, GetTermsNConditionUseCase getTermsNConditionUseCase) {
        this.getSubmissionChallengesUseCase = getSubmissionChallengesUseCase;
        this.getTermsNConditionUseCase = getTermsNConditionUseCase;
    }

    @Override
    public void initialize() {
        getSubmissionChallenges();
        getTermsNCondition();
    }

    @Override
    public void onDestroy() {
        getSubmissionChallengesUseCase.unsubscribe();
        getTermsNConditionUseCase.unsubscribe();
    }

    public void getSubmissionChallenges() {
        getView().renderChallengeDetail();
        getSubmissionChallengesUseCase.setRequestParams(getView().getParams());
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
                getView().renderSubmissionItems(submissionResponse);
            }
        });
    }

    public void getTermsNCondition() {
        getTermsNConditionUseCase.setRequestParams(getView().getParams());
        getTermsNConditionUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                RestResponse res1 = typeRestResponseMap.get(TermsNCondition.class);
                TermsNCondition termsNCondition = res1.getData();
                getView().renderTnC(termsNCondition);
            }
        });
    }
}
