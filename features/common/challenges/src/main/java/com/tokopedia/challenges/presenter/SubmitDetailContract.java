package com.tokopedia.challenges.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.data.model.IndiUserModel;

public interface SubmitDetailContract {
    public interface Presenter extends CustomerPresenter<View> {
        void setDataInFields(IndiUserModel model);
    }

    public interface View extends CustomerView {
        void setProfilePic(String text);

        void setProfileText(String text);

        public void setChallengeImage(String text);

        public void setLikesCountView(String text);

        public void setPointsView(String text);

        public void setShareView(String text);

        public void setDateView(String text);

        public void setApprovedView(String text);

        public void setDetailTitle(String text);

        public void setDetailContent(String text);

        public void setChallengeIcon(String text);

        public void setChallengeTitle(String text);

        public void setChallengeAt(String text);

        public void setChallengeHash(String text);

        public void setSubmitBtnClick(String text);
    }
}
