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
       // getView().setProfilePic(model.getThumbnailUrl());
        //getView().setProfileText(model.getTitle());
        getView().setChallengeImage(model.getThumbnailUrl());
        getView().setLikesCountView(String.valueOf(model.getLikes()));
        getView().setPointsView(String.valueOf(model.getPoints()));
     //   getView().setShareView(model.getUserId());
        getView().setDateView(model.getCreateDate());
        getView().setApprovedView(model.getStatus());
        getView().setDetailTitle(model.getTitle());
        getView().setDetailContent(model.getDescription());
    }
}
