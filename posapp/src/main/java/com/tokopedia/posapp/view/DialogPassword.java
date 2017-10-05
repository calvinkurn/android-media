package com.tokopedia.posapp.view;

/**
 * Created by okasurya on 9/27/17.
 */

public interface DialogPassword {
    interface Presenter {
        void checkPassword(String password);
    }

    interface View {
        void onCheckPasswordSuccess();

        void onCheckPasswordError(Throwable e);

        void onCheckPasswordError(String message);
     }
}
