package com.tokopedia.tkpd.session.presenter;

import com.tokopedia.tkpd.presenter.BaseView;
import com.tokopedia.tkpd.session.model.LoginGoogleModel;
import com.tokopedia.tkpd.session.model.LoginProviderModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by m.normansyah on 1/22/16.
 */
public interface RegisterNewView extends BaseView {
    String TAG = "MNORMANSYAH";
    String messageTAG = "RegisterNewView ";

    List<String> getEmailListOfAccountsUserHasLoggedInto();

    boolean isEmailAddressFromDevice();

    void setupEmailAddressToEmailTextView();

    void initView();

    void alertbox(String mymessage);

    void attemptRegisterStep1();

    void enableDisableAllFieldsForEmailValidationForm(boolean isEnable);

    void validateEmailAddressTask();

    void showProgress(final boolean show);

    void showErrorValidateEmail();

    void moveToRegisterNext(String email, String password);

    void alertBox();

    void setData(HashMap<String, Object> data);

    void startLoginWithGoogle(String type, LoginGoogleModel loginGoogleModel);

    void showProvider(List<LoginProviderModel.ProvidersBean> data);

    void finishActivity();

    void addProgressBar();

    void removeProgressBar();

    void showErrorValidateEmail(String s);

    boolean checkHasNoProvider();

    void showError(String string);
}
