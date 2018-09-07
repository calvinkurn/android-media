package com.tokopedia.mitra.homepage.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.homepage.contract.MitraHomepageContract;

import javax.inject.Inject;

public class MitraHomepagePresenter extends BaseDaggerPresenter<MitraHomepageContract.View> implements MitraHomepageContract.Presenter {

    private UserSession userSession;

    @Inject
    public MitraHomepagePresenter(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public void onViewCreated() {
        if (userSession.isLoggedIn()) {
            getView().hideLoginContainer();
        } else {
            getView().showLoginContainer();
        }
    }

    @Override
    public void onLoginResultReceived() {
        if (userSession.isLoggedIn()) {
            getView().hideLoginContainer();
        } else {
            getView().showMessageInRedSnackBar(R.string.mitra_homepage_login_failed_error_message);
        }
    }

    @Override
    public void onLoginBtnClicked() {
        getView().navigateToLoginPage();
    }
}
