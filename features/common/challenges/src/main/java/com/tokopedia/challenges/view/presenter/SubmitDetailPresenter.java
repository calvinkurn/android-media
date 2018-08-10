package com.tokopedia.challenges.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.data.model.IndiUserModel;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;


import javax.inject.Inject;

public class SubmitDetailPresenter extends BaseDaggerPresenter<SubmitDetailContract.View> implements SubmitDetailContract.Presenter {
    @Inject
    public SubmitDetailPresenter() {
    }

    @Override
    public void setDataInFields(SubmissionResult model) {
        getView().setProfilePic(model.getUser().getThumbnailImage());
        getView().setProfileText(model.getUser().getTitle());
        getView().setChallengeImage(model.getThumbnailUrl());
        getView().setLikesCountView(String.valueOf(model.getLikes()));
        getView().setPointsView(String.valueOf(model.getPoints()));
        String status = model.getStatus();
        if(status.contains("approved") || status.contains("participated")){
            getView().setApprovedView(status);
        } else if(status.contains("declined")){
            getView().setDeclinedView(status);
        } else if(status.contains("pending")){
            getView().setPendingView(status);
        }
        getView().setDetailTitle(model.getTitle());
        getView().setDetailContent(model.getDescription());
    }
}
