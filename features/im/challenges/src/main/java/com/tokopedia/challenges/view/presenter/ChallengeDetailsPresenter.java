package com.tokopedia.challenges.view.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.domain.usecase.GetChallengeDetailsUseCase;
import com.tokopedia.challenges.domain.usecase.GetChallengeSettingUseCase;
import com.tokopedia.challenges.domain.usecase.GetSubmissionChallengesUseCase;
import com.tokopedia.challenges.domain.usecase.GetSubmissionInChallengeUseCase;
import com.tokopedia.challenges.domain.usecase.GetTermsNConditionUseCase;
import com.tokopedia.challenges.domain.usecase.GetWinnersUseCase;
import com.tokopedia.challenges.domain.usecase.PostSubmissionLikeUseCase;
import com.tokopedia.challenges.view.activity.ChallengesSubmitActivity;
import com.tokopedia.challenges.view.activity.SubmitDetailActivity;
import com.tokopedia.challenges.view.adapter.ChallengeMainDetailsAdapter;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.TermsNCondition;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.challenges.view.contractor.ChallengeDetailsContract.Presenter;
import static com.tokopedia.challenges.view.contractor.ChallengeDetailsContract.View;

/**
 * @author lalit.singh
 */
public class ChallengeDetailsPresenter extends BaseDaggerPresenter<View>
        implements Presenter, ChallengeMainDetailsAdapter.LoadMoreListener {


    private final GetChallengeSettingUseCase getChallengeSettingUseCase;
    private GetSubmissionChallengesUseCase getSubmissionChallengesUseCase;
    private GetTermsNConditionUseCase getTermsNConditionUseCase;
    private GetChallengeDetailsUseCase getChallengeDetailsUseCase;
    private GetWinnersUseCase getWinnersUseCase;
    private GetSubmissionInChallengeUseCase getSubmissionInChallengeUseCase;
    private PostSubmissionLikeUseCase postSubmissionLikeUseCase;


    /*---All Submission Properties---*/
    private RequestParams searchParams = RequestParams.create();
    private String sortType = Utils.QUERY_PARAM_KEY_SORT_RECENT;
    private int pageStart = 0;
    private int pageSize = 20;
    private int totalItems = 20;
    private boolean isLoading;
    private boolean isLastPage;


    @Inject
    public ChallengeDetailsPresenter(GetChallengeSettingUseCase mGetChallengeSettingUseCase, GetChallengeDetailsUseCase getChallengeDetailsUseCase,
                                     GetSubmissionChallengesUseCase getSubmissionChallengesUseCase, GetTermsNConditionUseCase getTermsNConditionUseCase,
                                     GetWinnersUseCase getWinnersUseCase, GetSubmissionInChallengeUseCase getSubmissionInChallengeUseCase,
                                     PostSubmissionLikeUseCase postSubmissionLikeUseCase) {
        this.getChallengeSettingUseCase = mGetChallengeSettingUseCase;
        this.getSubmissionChallengesUseCase = getSubmissionChallengesUseCase;
        this.getTermsNConditionUseCase = getTermsNConditionUseCase;
        this.getChallengeDetailsUseCase = getChallengeDetailsUseCase;
        this.getWinnersUseCase = getWinnersUseCase;
        this.getSubmissionInChallengeUseCase = getSubmissionInChallengeUseCase;
        this.postSubmissionLikeUseCase = postSubmissionLikeUseCase;
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

    private void checkSettings() {
        getView().showProgressBar();
        getChallengeSettingUseCase.setCHALLENGE_ID(getView().getChallengeId());
        getChallengeSettingUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {

            @Override
            public void onCompleted() {
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

    @Override
    public void onDestroy() {
        detachView();
    }

    @Override
    public void getSubmissionChallenges(boolean loadFromApi, Result challengeResult) {
        if (loadFromApi) {
            getView().showProgressBar();
            getChallengeDetailsUseCase.execute(getView().getChallengeDetailsParams(), new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (!isViewAttached()) return;
                    e.printStackTrace();
                    getView().hideProgressBar();
                    NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(),
                            () -> getSubmissionChallenges(true, null));
                }

                @Override
                public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                    if (!isViewAttached()) return;
                    RestResponse res1 = typeRestResponseMap.get(Result.class);
                    Result challengeResult = res1.getData();
                    boolean isPastChallenge = checkIsPastChallenge(challengeResult);
                    getView().setIsPastChallenge(isPastChallenge);
                    getView().hideProgressBar();
                    getView().setChallengeResult(challengeResult);
                    getView().renderChallengeDetail(challengeResult);
                    getTermsNCondition(challengeResult.getId());
                    loadCountdownView(challengeResult, isPastChallenge);
                    if (isPastChallenge) {
                        getWinnerList();
                    }
                    onLoadMoreStarts();

                }
            });
        } else {
            getView().renderChallengeDetail(challengeResult);
            boolean isPastChallenge = checkIsPastChallenge(challengeResult);
            getView().setIsPastChallenge(isPastChallenge);
            loadCountdownView(challengeResult, isPastChallenge);
            if (isPastChallenge) {
                getWinnerList();
            }
            onLoadMoreStarts();
            getTermsNCondition(challengeResult.getId());
        }
    }

    public void setSubmissionLike(final SubmissionResult result) {
        RequestParams requestParams = RequestParams.create();
        if (result.getMe() != null)
            requestParams.putBoolean(PostSubmissionLikeUseCase.IS_LIKED, !result.getMe().isLiked());
        if (!TextUtils.isEmpty(result.getId()))
            requestParams.putString(Utils.QUERY_PARAM_SUBMISSION_ID, result.getId());
        postSubmissionLikeUseCase.execute(requestParams, new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
            }
        });

    }

    public void loadMoreItems() {
        setNextPageParams();
        getSubmissionChallengesUseCase.execute(searchParams, new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() == null) return;
                //TODO remove loader
                getView().hideSubmissionListLoader();
                Log.d("ChallengeMain", "LOAD_MORE: Hide Loader onError");
                if (pageStart > 0)
                    NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(),
                            () -> {
                                getView().showSubmissionListLoader();
                                loadMoreItems();
                            });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                if (getView() == null) return;
                RestResponse res1 = typeRestResponseMap.get(SubmissionResponse.class);
                SubmissionResponse submissionResponse = res1.getData();
                if (submissionResponse != null) {
                    totalItems = submissionResponse.getFound();
                }
                isLoading = false;
                if (submissionResponse != null && submissionResponse.getSubmissionResults() != null
                        && submissionResponse.getSubmissionResults().size() > 0) {
                    pageStart += submissionResponse.getSubmissionResults().size();
                    getView().renderSubmissions(submissionResponse.getSubmissionResults());
                } else {
                    isLastPage = true;
                    Log.d("ChallengeMain", "LOAD_MORE: LAST PAGE in onNext");
                    getView().hideSubmissionListLoader();
                }
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

    private void getWinnerList() {
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

    private void loadCountdownView(Result challengeResult, boolean isPastChallenge) {
        if (challengeResult == null) {
            return;
        }
        if (isPastChallenge) {
            getView().renderCountDownView(Utils.STATUS_COMPLETED);
            getWinnerList();
        } else {
            if (isParticipated(challengeResult)) {
                getView().renderCountDownView(Utils.STATUS_PARTICIPATED);
            } else {
                getView().renderCountDownView("");
            }
        }
    }

    private void getSubmissionInChallenge() {
        getView().showProgressBar();
        getSubmissionInChallengeUseCase.setRequestParams(getView().getChallengeId());
        getSubmissionInChallengeUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached())
                    return;
                getView().hideProgressBar();
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

    private boolean isParticipated(Result challengeResult) {
        return challengeResult != null && challengeResult.getMe() != null && challengeResult.getMe().getSubmissionCounts() != null
                && (challengeResult.getMe().getSubmissionCounts().getApproved() > 0
                || challengeResult.getMe().getSubmissionCounts().getWaiting() > 0);
    }

    private boolean checkIsPastChallenge(Result challengeResult) {
        return Utils.checkIsPastChallenge(challengeResult.getEndDate());
    }

    private void setNextPageParams() {
        searchParams.putString(Utils.QUERY_PARAM_CHALLENGE_ID, getView().getChallengeId());
        searchParams.putInt(Utils.QUERY_PARAM_KEY_START, pageStart);
        searchParams.putInt(Utils.QUERY_PARAM_KEY_SIZE, pageSize);
        searchParams.putString(Utils.QUERY_PARAM_KEY_SORT, sortType);
    }

    @Override
    public void onLoadMoreStarts() {
        if (!isLoading && !isLastPage) {
            if (pageStart + pageSize <= totalItems) {
                isLoading = true;
                loadMoreItems();
            } else {
                getView().hideSubmissionListLoader();
            }
        }
    }

    public void setSortType(String sortType) {
        if (this.sortType.equals(sortType))
            return;
        this.sortType = sortType;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
        isLastPage = false;
    }

    public void unsubscribe() {
        getSubmissionChallengesUseCase.unsubscribe();
    }
}
