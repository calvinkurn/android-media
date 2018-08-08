package com.tokopedia.challenges.view.presenter;

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

         void setChallengeImage(String text);

         void setLikesCountView(String text);

         void setPointsView(String text);

         void setShareView(String text);

         void setDateView(String text);

         void setApprovedView(String text);

         void setDetailTitle(String text);

         void setDetailContent(String text);
    }
}
