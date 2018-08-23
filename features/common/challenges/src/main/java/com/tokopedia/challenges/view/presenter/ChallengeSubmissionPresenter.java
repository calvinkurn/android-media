package com.tokopedia.challenges.view.presenter;

import android.view.View;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.challenges.domain.usecase.GetChallengeDetailsUseCase;
import com.tokopedia.challenges.domain.usecase.GetChallengeSettingUseCase;
import com.tokopedia.challenges.domain.usecase.GetSubmissionChallengesUseCase;
import com.tokopedia.challenges.domain.usecase.GetTermsNConditionUseCase;
import com.tokopedia.challenges.domain.usecase.GetWinnersUseCase;
import com.tokopedia.challenges.view.activity.ChallengesSubmitActivity;
import com.tokopedia.challenges.view.contractor.ChallengeSubmissonContractor;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.TermsNCondition;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestResponse;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ChallengeSubmissionPresenter extends BaseDaggerPresenter<ChallengeSubmissonContractor.View> implements ChallengeSubmissonContractor.Presenter {

    private final GetChallengeSettingUseCase getChallengeSettingUseCase;
    GetSubmissionChallengesUseCase getSubmissionChallengesUseCase;
    GetTermsNConditionUseCase getTermsNConditionUseCase;
    GetChallengeDetailsUseCase getChallengeDetailsUseCase;
    GetWinnersUseCase getWinnersUseCase;

    @Inject
    public ChallengeSubmissionPresenter(GetChallengeSettingUseCase mGetChallengeSettingUseCase, GetChallengeDetailsUseCase getChallengeDetailsUseCase, GetSubmissionChallengesUseCase getSubmissionChallengesUseCase, GetTermsNConditionUseCase getTermsNConditionUseCase, GetWinnersUseCase getWinnersUseCase) {
        this.getChallengeSettingUseCase = mGetChallengeSettingUseCase;
        this.getSubmissionChallengesUseCase = getSubmissionChallengesUseCase;
        this.getTermsNConditionUseCase = getTermsNConditionUseCase;
        this.getChallengeDetailsUseCase = getChallengeDetailsUseCase;
        this.getWinnersUseCase = getWinnersUseCase;
    }

    @Override
    public void initialize(boolean loadFromApi, Result challengeResult) {
        getSubmissionChallenges(loadFromApi, challengeResult);
    }

    @Override
    public void onSubmitButtonClick() {
        getView().showProgressBar();
        getChallengeSettingUseCase.setCHALLENGE_ID(getView().getChallengeId());
        getChallengeSettingUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {

            @Override
            public void onCompleted() {
                getView().hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressBar();
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                RestResponse res1 = restResponse.get(ChallengeSettings.class);
                ChallengeSettings settings = res1.getData();
                if (!settings.isUploadAllowed()) {
                    getView().setSnackBarErrorMessage("Upload Not allowed for this Challenge"); // update challenge as per UX
                } else {
                    getView().navigateToActivity(ChallengesSubmitActivity.getStartingIntent(getView().getActivity(), settings, getView().getChallengeResult().getId(), getView().getChallengeResult().getTitle(), getView().getChallengeResult().getDescription()));
                }
            }
        });
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
                    getView().setChallengeResult(challengeResult);
                    getView().renderChallengeDetail(challengeResult);
                    getTermsNCondition(challengeResult.getId());
                    loadCountdownView(challengeResult, false);
                    loadSubmissions();
                }
            });
        } else {
            getView().renderChallengeDetail(challengeResult);
            getTermsNCondition(challengeResult.getId());
            loadSubmissions();
        }

    }

    public void loadCountdownView(Result challengeResult, boolean isPastChallenge) {
        if (challengeResult == null) {
            return;
        }
        if (isPastChallenge) {
            getView().setCountDownView("Completed");
            getWinnerList();
        } else {
            if (challengeResult.getMe() != null && challengeResult.getMe().getSubmissionCounts() != null
                    && (challengeResult.getMe().getSubmissionCounts().getApproved() > 0
                    || challengeResult.getMe().getSubmissionCounts().getWaiting() > 0)) {
                    getView().setCountDownView("Participated");
            } else {
                getView().setCountDownView("");
            }
        }
    }

    private void getWinnerList() {
        getWinnersUseCase.setRequestParams(getView().getSubmissionsParams());
        getWinnersUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                RestResponse res1 = typeRestResponseMap.get(SubmissionResponse.class);
                SubmissionResponse submissionResponse = res1.getData();
                getView().renderWinnerItems(submissionResponse);
            }
        });
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
