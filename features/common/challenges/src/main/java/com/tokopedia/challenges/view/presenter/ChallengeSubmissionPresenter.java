package com.tokopedia.challenges.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.challenges.domain.usecase.GetChallengeDetailsUseCase;
import com.tokopedia.challenges.domain.usecase.GetSubmissionChallengesUseCase;
import com.tokopedia.challenges.domain.usecase.GetTermsNConditionUseCase;
import com.tokopedia.challenges.domain.usecase.PostBuzzPointEventUseCase;
import com.tokopedia.challenges.domain.usecase.PostSubmissionLikeUseCase;
import com.tokopedia.challenges.view.contractor.ChallengeSubmissonContractor;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.TermsNCondition;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ChallengeSubmissionPresenter extends BaseDaggerPresenter<ChallengeSubmissonContractor.View> implements ChallengeSubmissonContractor.Presenter {

    GetSubmissionChallengesUseCase getSubmissionChallengesUseCase;
    GetTermsNConditionUseCase getTermsNConditionUseCase;
    GetChallengeDetailsUseCase getChallengeDetailsUseCase;

    @Inject
    public ChallengeSubmissionPresenter(GetChallengeDetailsUseCase getChallengeDetailsUseCase, GetSubmissionChallengesUseCase getSubmissionChallengesUseCase, GetTermsNConditionUseCase getTermsNConditionUseCase) {
        this.getSubmissionChallengesUseCase = getSubmissionChallengesUseCase;
        this.getTermsNConditionUseCase = getTermsNConditionUseCase;
        this.getChallengeDetailsUseCase = getChallengeDetailsUseCase;
    }

    @Override
    public void initialize(boolean loadFromApi, Result challengeResult) {
        getSubmissionChallenges(loadFromApi, challengeResult);
        getTermsNCondition(challengeResult.getId());
    }

    @Override
    public void onDestroy() {
        getSubmissionChallengesUseCase.unsubscribe();
        getTermsNConditionUseCase.unsubscribe();
        getChallengeDetailsUseCase.unsubscribe();
    }

    public void getSubmissionChallenges(boolean loadFromApi, Result challengeResult) {
        if (loadFromApi) {
            getView().showProgressBar();
            getView().hideCollapsingHeader();
            getChallengeDetailsUseCase.setRequestParams(getView().getChallengeDetailsParams());
            getChallengeDetailsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    CommonUtils.dumper("enter error");
                    e.printStackTrace();
                    getView().hideProgressBar();
                    getView().hideCollapsingHeader();
                    NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getSubmissionChallenges(true, null);
                        }
                    });
                }

                @Override
                public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                    RestResponse res1 = typeRestResponseMap.get(Result.class);
                    Result challengeResult = res1.getData();
                    getView().hideProgressBar();
                    getView().showCollapsingHeader();
                    getView().renderChallengeDetail(challengeResult);
                    loadCountdownView(challengeResult);
                    loadSubmissions();
                }
            });
        } else {
            getView().renderChallengeDetail(challengeResult);
            loadCountdownView(challengeResult);
            loadSubmissions();
        }

    }

    private void loadCountdownView(Result challengeResult) {
        if (challengeResult.getMe() != null && challengeResult.getMe().getSubmissionCounts() != null) {
            if (challengeResult.getMe().getSubmissionCounts().getApproved() > 0) {
                getView().setCountDownView("Approved");
            } else if (challengeResult.getMe().getSubmissionCounts().getDeclined() > 0) {
                getView().setCountDownView("Declined");
            } else if (challengeResult.getMe().getSubmissionCounts().getWaiting() > 0) {
                getView().setCountDownView("Waiting");
            } else if (System.currentTimeMillis() > Utils.convertUTCToMillis(challengeResult.getEndDate())) {
                getView().setCountDownView("Completed");
            } else {
                getView().setCountDownView("");
            }
        } else {
            getView().setCountDownView("");
        }
    }


    private void loadSubmissions() {
        getSubmissionChallengesUseCase.setRequestParams(getView().getSubmissionsParams());
        getSubmissionChallengesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                RestResponse res1 = typeRestResponseMap.get(SubmissionResponse.class);
                SubmissionResponse submissionResponse = res1.getData();
                getView().renderSubmissionItems(submissionResponse);
            }
        });
    }

    public void getTermsNCondition(String id) {
        getTermsNConditionUseCase.setCollectionID(id);
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
