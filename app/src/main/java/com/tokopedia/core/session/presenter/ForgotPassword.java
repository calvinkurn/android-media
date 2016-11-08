package com.tokopedia.core.session.presenter;

import android.content.Context;
import android.os.Bundle;

/**
 * @author m.normansyah
 * @since 18/11/2015.
 * @version 2
 */
public interface ForgotPassword {
    String FORGOT_PASSWORD = "FORGOT_PASSWORD";
    String FORGOT_PASSWORD_VIEW = "FORGOT_PASSWORD_VIEW";
    String FORGOT_PASSWORD_SHOW_DIALOG = "FORGOT_PASSWORD_SHOW_DIALOG";
    void initDataInstances(Context context);
    void sendRequest(Object... data);
    boolean isAfterRotate();
    void saveDataBeforeRotate(Bundle outstate);
    void fetchDataAfterRotate(Bundle instate);
    void initData(Context context);
    void subscribe();
    void unSubscribe();

    void resetPassword(String s);

    void setData(int type, Bundle data);

    void setError(int type, String text);
}
