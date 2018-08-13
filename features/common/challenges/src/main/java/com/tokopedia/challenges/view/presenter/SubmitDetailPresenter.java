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
        if (model.getMedia().get(0).getMediaType().equalsIgnoreCase("Image")) {
            getView().setChallengeImage(model.getMedia().get(0).getImageUrl(), "");
        } else if (model.getMedia() != null && model.getMedia().get(0).getVideo() != null && model.getMedia().get(0).getVideo().getSources() != null) {
            getView().setChallengeImage(model.getThumbnailUrl(), model.getMedia().get(0).getVideo().getSources().get(1).getSource());
        }
        getView().setLikesCountView(String.valueOf(model.getLikes()));
        getView().setPointsView(String.valueOf(model.getPoints()));
        String status = model.getStatus();
        if(status.equalsIgnoreCase("approved") || status.equalsIgnoreCase("participated")){
            getView().setApprovedView(status);
        } else if(status.equalsIgnoreCase("declined")){
            getView().setDeclinedView(status);
        } else if(status.equalsIgnoreCase("pending")){
            getView().showStatusInfo();
            getView().setPendingView(status);
        }
        getView().setDetailTitle(model.getTitle());
        getView().setDetailContent(model.getDescription());
        getView().setParticipateTitle(model.getChannel().getTitle());
    }
}
