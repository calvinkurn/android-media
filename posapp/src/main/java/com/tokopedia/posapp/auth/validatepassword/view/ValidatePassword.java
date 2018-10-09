package com.tokopedia.posapp.auth.validatepassword.view;

/**
 * Created by okasurya on 9/27/17.
 */

public interface ValidatePassword {
    interface Presenter {
        void checkPassword(String password);
    }

    interface View {
        void onCheckPasswordSuccess();

        void onCheckPasswordError(Throwable e);

        void onCheckPasswordError(String message);
     }
}
