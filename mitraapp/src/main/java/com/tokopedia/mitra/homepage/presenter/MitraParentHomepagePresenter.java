package com.tokopedia.mitra.homepage.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.homepage.contract.MitraParentHomepageContract;

import javax.inject.Inject;

public class MitraParentHomepagePresenter extends BaseDaggerPresenter<MitraParentHomepageContract.View> implements MitraParentHomepageContract.Presenter {
    private UserSession userSession;

    @Inject
    public MitraParentHomepagePresenter(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public void onHomepageMenuClicked() {
        getView().inflateHomepageFragment();
        getView().setHomepageMenuSelected();
    }

    @Override
    public void onAccountMenuClicked() {
        if (userSession.isLoggedIn()) {
            getView().inflateAccountFragment();
            getView().setAccountMenuSelected();
        } else {
            getView().navigateToLoggedInThenAccountPage();
        }
    }

    @Override
    public void onLoginFromAccountResultReceived() {
        if (userSession.isLoggedIn()) {
            getView().inflateAccountFragment();
            getView().setAccountMenuSelected();
        } else {
            getView().showErrorMessageInSnackbar(R.string.mitra_homepage_login_failed_error_message);
        }
    }

    @Override
    public void onHelpMenuClicked() {
        getView().inflateHelpFragment();
        getView().setHelpMenuSelected();
    }
}
