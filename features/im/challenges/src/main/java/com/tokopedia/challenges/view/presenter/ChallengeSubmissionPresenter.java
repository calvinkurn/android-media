package com.tokopedia.challenges.view.presenter;

import android.content.Intent;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.domain.usecase.GetChallengeDetailsUseCase;
import com.tokopedia.challenges.domain.usecase.GetChallengeSettingUseCase;
import com.tokopedia.challenges.domain.usecase.GetSubmissionChallengesUseCase;
import com.tokopedia.challenges.domain.usecase.GetSubmissionInChallengeUseCase;
import com.tokopedia.challenges.domain.usecase.GetTermsNConditionUseCase;
import com.tokopedia.challenges.domain.usecase.GetWinnersUseCase;
import com.tokopedia.challenges.view.activity.ChallengesSubmitActivity;
import com.tokopedia.challenges.view.activity.SubmitDetailActivity;
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

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */
public class ChallengeSubmissionPresenter extends BaseDaggerPresenter<ChallengeSubmissonContractor.View> implements ChallengeSubmissonContractor.Presenter {

    private final GetChallengeSettingUseCase getChallengeSettingUseCase;
    private GetSubmissionChallengesUseCase getSubmissionChallengesUseCase;
    private GetTermsNConditionUseCase getTermsNConditionUseCase;
    private GetChallengeDetailsUseCase getChallengeDetailsUseCase;
    private GetWinnersUseCase getWinnersUseCase;
    private GetSubmissionInChallengeUseCase getSubmissionInChallengeUseCase;

    @Inject
    public ChallengeSubmissionPresenter(GetChallengeSettingUseCase mGetChallengeSettingUseCase, GetChallengeDetailsUseCase getChallengeDetailsUseCase,
                                        GetSubmissionChallengesUseCase getSubmissionChallengesUseCase, GetTermsNConditionUseCase getTermsNConditionUseCase,
                                        GetWinnersUseCase getWinnersUseCase, GetSubmissionInChallengeUseCase getSubmissionInChallengeUseCase) {
        this.getChallengeSettingUseCase = mGetChallengeSettingUseCase;
        this.getSubmissionChallengesUseCase = getSubmissionChallengesUseCase;
        this.getTermsNConditionUseCase = getTermsNConditionUseCase;
        this.getChallengeDetailsUseCase = getChallengeDetailsUseCase;
        this.getWinnersUseCase = getWinnersUseCase;
        this.getSubmissionInChallengeUseCase = getSubmissionInChallengeUseCase;
    }

    @Override
    public void initialize(boolean loadFromApi, Result challengeResult) {
        getSubmissionChallenges(loadFromApi, challengeResult);
    }

    @Override
    public void onSubmitButtonClick() {
        if (isParticipated(getView().getChallengeResult())) {
            getSubmissionInChallenge();
        } else {
            checkSettings();
        }

    }

    @Override
    public void onDestroy() {
        detachView();
    }


    @Override
    public void getSubmissionChallenges(boolean loadFromApi, Result challengeResult) {
        if (loadFromApi) {
            getView().showProgressBar();
            getView().hideCollapsingHeader();
            getChallengeDetailsUseCase.execute(getView().getChallengeDetailsParams(), new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (!isViewAttached()) return;
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
                    if (!isViewAttached()) return;
                    RestResponse res1 = typeRestResponseMap.get(Result.class);
                    Result challengeResult = res1.getData();
                    boolean isPastChallenge = checkIsPastChallenge(challengeResult);
                    getView().setIsPastChallenge(isPastChallenge);
                    getView().hideProgressBar();
                    getView().showCollapsingHeader();
                    getView().setChallengeResult(challengeResult);
                    getView().renderChallengeDetail(challengeResult);
                    getTermsNCondition(challengeResult.getId());
                    loadCountdownView(challengeResult, isPastChallenge);
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
        if (challengeResult == null || getView() == null) {
            return;
        }
        if (isPastChallenge) {
            getView().setCountDownView(Utils.STATUS_COMPLETED);
            getWinnerList();
        } else {
            if (isParticipated(challengeResult)) {
                getView().setCountDownView(Utils.STATUS_PARTICIPATED);
            } else {
                getView().setCountDownView("");
            }
        }
    }

    public boolean isParticipated(Result challengeResult) {
        return challengeResult != null && challengeResult.getMe() != null && challengeResult.getMe().getSubmissionCounts() != null
                && (challengeResult.getMe().getSubmissionCounts().getApproved() > 0
                || challengeResult.getMe().getSubmissionCounts().getWaiting() > 0);
    }

    private void getWinnerList() {
        if (getView() == null) return;
        getWinnersUseCase.execute(getView().getSubmissionsParams(), new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                if (!isViewAttached()) return;
                RestResponse res1 = typeRestResponseMap.get(SubmissionResponse.class);
                SubmissionResponse submissionResponse = res1.getData();
                getView().renderWinnerItems(submissionResponse);
            }
        });
    }


    public void loadSubmissions() {
        if (getView() == null) return;
        getSubmissionChallengesUseCase.execute(getView().getSubmissionsParams(), new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                if (!isViewAttached()) return;
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
                if (!isViewAttached()) return;
                RestResponse res1 = typeRestResponseMap.get(TermsNCondition.class);
                TermsNCondition termsNCondition = res1.getData();
                getView().renderTnC(termsNCondition);
            }
        });
    }

    private void checkSettings() {
        if (getView() == null)
            return;
        getView().showProgressBar();
        getChallengeSettingUseCase.setCHALLENGE_ID(getView().getChallengeId());
        getChallengeSettingUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {

            @Override
            public void onCompleted() {
                if (getView() != null)
                    getView().hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) return;
                getView().hideProgressBar();
                getView().setSnackBarErrorMessage(getView().getActivity().getString(R.string.ch_network_error_msg));

            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                if (!isViewAttached()) return;
                getView().hideProgressBar();
                RestResponse res1 = restResponse.get(ChallengeSettings.class);
                ChallengeSettings settings = res1.getData();
                if (!settings.isUploadAllowed()) {
                    getView().setSnackBarErrorMessage(getView().getActivity().getString(R.string.ch_upload_not_allowed_text)); // update challenge as per UX
                } else {
                    getView().navigateToActivity(ChallengesSubmitActivity.getStartingIntent(getView().getActivity(), settings, getView().getChallengeResult().getId(), getView().getChallengeResult().getTitle(), getView().getChallengeResult().getDescription()));
                }
            }
        });
    }

    private boolean checkIsPastChallenge(Result challengeResult) {
        return Utils.checkIsPastChallenge(challengeResult.getEndDate());
    }

    private void getSubmissionInChallenge() {
        if (getView() == null)
            return;
        getView().showProgressBar();
        getSubmissionInChallengeUseCase.setRequestParams(getView().getChallengeId());
        getSubmissionInChallengeUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) return;
                getView().hideProgressBar();
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                if (!isViewAttached()) return;
                getView().hideProgressBar();
                RestResponse res1 = restResponse.get(SubmissionResponse.class);
                SubmissionResponse mainDataObject = res1.getData();

                if (mainDataObject != null && mainDataObject.getSubmissionResults() != null && mainDataObject.getSubmissionResults().size() > 0) {

                    Intent detailsIntent = new Intent(getView().getActivity(), SubmitDetailActivity.class);
                    detailsIntent.putExtra(Utils.QUERY_PARAM_SUBMISSION_RESULT, mainDataObject.getSubmissionResults().get(0));
                    getView().navigateToActivity(detailsIntent);
                } else {
                    getView().setSnackBarErrorMessage(getView().getActivity().getString(R.string.ch_wait_msg));
                }
            }
        });
    }
}
