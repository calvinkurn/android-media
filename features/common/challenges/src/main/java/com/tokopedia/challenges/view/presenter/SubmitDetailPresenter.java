package com.tokopedia.challenges.view.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.domain.usecase.GetChallengeDetailsAndSttingsUseCase;
import com.tokopedia.challenges.domain.usecase.GetDetailsSubmissionsUseCase;
import com.tokopedia.challenges.domain.usecase.PostBuzzPointEventUseCase;
import com.tokopedia.challenges.domain.usecase.PostDeleteSubmissionUseCase;
import com.tokopedia.challenges.domain.usecase.PostSubmissionLikeUseCase;
import com.tokopedia.challenges.view.activity.ChallengesSubmitActivity;
import com.tokopedia.challenges.view.analytics.ChallengesMoengageAnalyticsTracker;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;
import com.tokopedia.challenges.view.utils.ChallengesCacheHandler;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class SubmitDetailPresenter extends BaseDaggerPresenter<SubmitDetailContract.View> implements SubmitDetailContract.Presenter {
    private PostSubmissionLikeUseCase postSubmissionLikeUseCase;
    private PostBuzzPointEventUseCase postBuzzPointEventUseCase;
    private GetDetailsSubmissionsUseCase getDetailsSubmissionsUseCase;
    private GetChallengeDetailsAndSttingsUseCase getChallengeDetailsAndSttingsUseCase;
    private PostDeleteSubmissionUseCase postDeleteSubmissionUseCase;


    @Inject
    public SubmitDetailPresenter(PostSubmissionLikeUseCase postSubmissionLikeUseCase, PostBuzzPointEventUseCase postBuzzPointEventUseCase, GetDetailsSubmissionsUseCase getDetailsSubmissionsUseCase,
                                 GetChallengeDetailsAndSttingsUseCase getChallengeDetailsAndSttingsUseCase, PostDeleteSubmissionUseCase postDeleteSubmissionUseCase) {
        this.postSubmissionLikeUseCase = postSubmissionLikeUseCase;
        this.postBuzzPointEventUseCase = postBuzzPointEventUseCase;
        this.getDetailsSubmissionsUseCase = getDetailsSubmissionsUseCase;
        this.getChallengeDetailsAndSttingsUseCase = getChallengeDetailsAndSttingsUseCase;
        this.postDeleteSubmissionUseCase = postDeleteSubmissionUseCase;
    }

    @Override
    public void setDataInFields(SubmissionResult model) {
        getView().setProfilePic(model.getUser().getThumbnailImage());
        getView().setProfileText(model.getUser().getTitle());
        if (model.getMedia().get(0).getMediaType().equalsIgnoreCase("Image")) {
            getView().setChallengeImage(model.getMedia().get(0).getImageUrl(), "");
        } else if (model.getMedia() != null && model.getMedia().get(0).getVideo() != null && model.getMedia().get(0).getVideo().getSources() != null) {
            getView().setChallengeImage(model.getThumbnailUrl(), model.getMedia().get(0).getVideo().getSources().get(1).getSource());
        } else {
            getView().setChallengeImage("", "");
        }
        if (getParticipatedStatus(model)) {
            getView().isParticipated(true);
            getView().setLikesCountView(String.valueOf(model.getLikes()));
        } else {
            getView().isParticipated(false);
            sendBuzzPointEvent(model.getId());
        }
        getView().setLikes(model.getMe().isLiked());
        getView().setApprovedView(model.getStatus(), model.getStatusMessage());
        getView().setPointsView(String.valueOf(model.getPoints()));
        getView().setDetailTitle(model.getTitle());
        getView().setDetailContent(model.getDescription());
        getView().setParticipateTitle(model.getCollection().getTitle());
        if (model.getAwards() != null) {
            int position = Utils.getWinnerPosition(model.getAwards());
            if (position != -1)
                getView().setWinnerPosition(String.valueOf(position));
        }
        if (model.getCollection() != null) {
            ChallengesMoengageAnalyticsTracker.challengePostOpen(getView().getActivity(), model.getCollection().getTitle(), model.getCollection().getId(), model.getId(), getParticipatedStatus(model));
        }
    }

    @Override
    public void likeBtnClick(SubmissionResult result) {
        RequestParams requestParams = RequestParams.create();
        if (result.getMe() != null)
            requestParams.putBoolean(PostSubmissionLikeUseCase.IS_LIKED, !result.getMe().isLiked());
        if (!TextUtils.isEmpty(result.getId()))
            requestParams.putString(Utils.QUERY_PARAM_SUBMISSION_ID, result.getId());
        postSubmissionLikeUseCase.setRequestParams(requestParams);
        postSubmissionLikeUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getView().setLikes(result.getMe().isLiked());
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {

            }
        });

    }

    @Override
    public void sendBuzzPointEvent(String submissionId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(Utils.QUERY_PARAM_SUBMISSION_ID, submissionId);
        postBuzzPointEventUseCase.setRequestParams(requestParams);
        postBuzzPointEventUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
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

    @Override
    public void getSubmissionDetails(String submissionId) {
        getDetailsSubmissionsUseCase.setRequestParams(submissionId);
        getDetailsSubmissionsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hidProgressBar();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {

                RestResponse res1 = typeRestResponseMap.get(SubmissionResult.class);
                SubmissionResult submissionResult = res1.getData();
                if (submissionResult != null) {
                    getView().hidProgressBar();
                    getView().setSubmittResult(submissionResult);
                    setDataInFields(submissionResult);
                } else {
                    getView().showDialogInfo("Under Review", "All submissions from participants of the challenge must approved by Tokopedia");
                }
            }
        });

    }


    @Override
    public void onSubmitButtonClick(String challegeId) {
        getView().showProgressBar();
        getChallengeDetailsAndSttingsUseCase.setRequestParams(challegeId);
        getChallengeDetailsAndSttingsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hidProgressBar();
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                getView().hidProgressBar();
                RestResponse res1 = restResponse.get(ChallengeSettings.class);
                RestResponse res2 = restResponse.get(Result.class);
                ChallengeSettings settings = res1.getData();
                Result result = res2.getData();
                if (!settings.isUploadAllowed() || result == null) {
                    getView().setSnackBarErrorMessage("Upload Not allowed for this Challenge"); // update challenge as per UX
                } else {
                    getView().navigateToActivity(ChallengesSubmitActivity.getStartingIntent(getView().getActivity(), settings, result.getId(), result.getTitle(), result.getDescription()));
                    getView().getActivity().finish();
                }
            }
        });
    }

    @Override
    public void deleteSubmittedPost(String submissionId, String challengeId, String challengeName) {
        getView().showProgressBar();
        postDeleteSubmissionUseCase.setRequestParams(submissionId);
        postDeleteSubmissionUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hidProgressBar();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                getView().hidProgressBar();
                Toast.makeText(getView().getActivity(), R.string.post_deleted_msg, Toast.LENGTH_SHORT).show();
                ChallengesCacheHandler.addManipulatedMap(submissionId, ChallengesCacheHandler.Manupulated.DELETE.ordinal());
                ChallengesCacheHandler.resetCache();
                ChallengesMoengageAnalyticsTracker.challenge_DeleteSubmission(getView().getActivity(), challengeName,
                        submissionId, submissionId);
                getView().getActivity().finish();
            }
        });
    }

    @Override
    public boolean getParticipatedStatus(SubmissionResult submissionResult) {
        if (submissionResult != null)
            return (submissionResult.getMe() != null && submissionResult.getUser() != null && submissionResult.getMe().getId() != null && submissionResult.getUser().getId() != null && submissionResult.getMe().getId().equalsIgnoreCase(submissionResult.getUser().getId()));
        else
            return false;
    }
}
