package com.tokopedia.core.session.presenter;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.session.LoginFragment;
import com.tokopedia.core.session.model.LoginProviderModel;

import java.util.List;

/**
 * Created by m.normansyah on 04/11/2015.
 */
public interface LoginView extends BaseView {
    String TAG = "MNORMANSYAH";
    String TEST_INT_KEY = "O";
    String messageTAG = "LoginView : ";

    void setAutoCompleteAdapter(List<String> LoginIdList);

    void setEmailText(String text);

    void setListener();

    void showProgress(boolean isShow);

    LoginFragment.FocusPair validateSignIn();

    void moveToFragmentSecurityQuestion(int security1, int security2, int userId);

    void notifyAutoCompleteAdapter();

    void showDialog(String dialogText);

    void startLoginWithGoogle(String LoginType, Object model);

    void destroyActivity();

    void showProvider(List<LoginProviderModel.ProvidersBean> data);

    void addProgressbar();

    void removeProgressBar();

    Context getContext();

    Activity getActivity();

    boolean checkHasNoProvider();

    void showError(String string);
}
