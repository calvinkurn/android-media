package com.tokopedia.core.manage.people.password.presenter;

import android.app.Activity;
import android.os.Bundle;

import com.tokopedia.core.manage.people.password.activity.ManagePasswordActivity;
import com.tokopedia.core.manage.people.password.activity.ManagePasswordActivityView;
import com.tokopedia.core.manage.people.password.fragment.ManagePasswordFragment;
import com.tokopedia.core.manage.people.password.fragment.ManagePasswordFragmentView;
import com.tokopedia.core.manage.people.password.intentservice.ManagePasswordIntentService;
import com.tokopedia.core.manage.people.password.interactor.ManagePasswordInteractor;
import com.tokopedia.core.manage.people.password.interactor.ManagePasswordInteractorImpl;
import com.tokopedia.core.manage.people.password.model.ChangePasswordParam;

/**
 * Created by stevenfredian on 9/28/16.
 */
public class ManagePasswordFragmentPresenterImpl implements ManagePasswordFragmentPresenter{

    ManagePasswordFragmentView view;
    ManagePasswordInteractor interactor;
    ManagePasswordActivityView listener;

    public ManagePasswordFragmentPresenterImpl(ManagePasswordFragment fragment) {
        view = fragment;
        interactor = new ManagePasswordInteractorImpl();
    }

    @Override
    public void changePassword(Activity activity, ChangePasswordParam param) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(ManagePasswordIntentService.PARAM_CHANGE_PASSWORD, param);
        view.dismissKeyboard();
        view.showProgress();
        listener = (ManagePasswordActivity)activity;
        listener.changePassword(bundle);
    }
}
