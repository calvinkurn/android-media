package com.tokopedia.challenges.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.data.IndiUserModel;

import javax.inject.Inject;

public class SubmitDetailPresenter extends BaseDaggerPresenter<SubmitDetailContract.View> implements SubmitDetailContract.Presenter {
    @Inject
    public SubmitDetailPresenter() {
    }

    @Override
    public void setDataInFields(IndiUserModel model) {
        getView().setProfilePic(model.getUserId());
        getView().setProfileText(model.getUserId());
        getView().setChallengeImage(model.getUserId());
        getView().setLikesCountView(model.getUserId());
        getView().setPointsView(model.getUserId());
        getView().setShareView(model.getUserId());
        getView().setDateView(model.getUserId());
        getView().setApprovedView(model.getUserId());
        getView().setDetailTitle(model.getUserId());
        getView().setDetailContent(model.getUserId());
        getView().setChallengeIcon(model.getUserId());
        getView().setChallengeTitle(model.getUserId());
        getView().setChallengeAt(model.getUserId());
        getView().setChallengeHash(model.getUserId());
        getView().setSubmitBtnClick(model.getUserId());
    }
}
