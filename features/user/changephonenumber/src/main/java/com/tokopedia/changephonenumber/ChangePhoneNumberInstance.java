package com.tokopedia.changephonenumber;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.changephonenumber.di.ChangePhoneNumberComponent;
import com.tokopedia.changephonenumber.di.DaggerChangePhoneNumberComponent;

/**
 * @author by alvinatin on 01/10/18.
 */

public class ChangePhoneNumberInstance {
    private static ChangePhoneNumberComponent changePhoneNumberComponent;

    public static ChangePhoneNumberComponent getChangePhoneNumberComponent(Application application) {
        if (changePhoneNumberComponent == null) {
            changePhoneNumberComponent = DaggerChangePhoneNumberComponent.builder()
                    .baseAppComponent(((BaseMainApplication) application).getBaseAppComponent())
                    .build();
        }
        return changePhoneNumberComponent;
    }
}
