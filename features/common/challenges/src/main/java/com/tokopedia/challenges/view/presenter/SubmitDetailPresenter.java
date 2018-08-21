package com.tokopedia.challenges.view.presenter;

import android.text.TextUtils;
import android.view.View;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.domain.usecase.GetDetailsSubmissionsUseCase;
import com.tokopedia.challenges.domain.usecase.PostBuzzPointEventUseCase;
import com.tokopedia.challenges.domain.usecase.PostSubmissionLikeUseCase;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
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


    @Inject
    public SubmitDetailPresenter(PostSubmissionLikeUseCase postSubmissionLikeUseCase, PostBuzzPointEventUseCase postBuzzPointEventUseCase, GetDetailsSubmissionsUseCase getDetailsSubmissionsUseCase) {
        this.postSubmissionLikeUseCase = postSubmissionLikeUseCase;
        this.postBuzzPointEventUseCase = postBuzzPointEventUseCase;
        this.getDetailsSubmissionsUseCase = getDetailsSubmissionsUseCase;
    }

    @Override
    public void setDataInFields(SubmissionResult model) {
        getView().setProfilePic(model.getUser().getThumbnailImage());
        getView().setProfileText(model.getUser().getTitle());
        if (model.getMedia().get(0).getMediaType().equalsIgnoreCase("Image")) {
            getView().setChallengeImage(model.getMedia().get(0).getImageUrl(), "");
            sendBuzzPointEvent(model.getId());
        } else if (model.getMedia() != null && model.getMedia().get(0).getVideo() != null && model.getMedia().get(0).getVideo().getSources() != null) {
            getView().setChallengeImage(model.getThumbnailUrl(), model.getMedia().get(0).getVideo().getSources().get(1).getSource());
        }
        getView().setLikes(model.getMe().isLiked());
        getView().setLikesCountView(String.valueOf(model.getLikes()));
        getView().setPointsView(String.valueOf(model.getPoints()));
        String status = model.getStatus();
        getView().setApprovedView(status);
        getView().setDetailTitle(model.getTitle());
        getView().setDetailContent(model.getDescription());
        getView().setParticipateTitle(model.getCollection().getTitle());
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
}
