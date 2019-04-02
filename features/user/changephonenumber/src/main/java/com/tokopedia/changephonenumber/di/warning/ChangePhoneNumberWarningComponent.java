package com.tokopedia.changephonenumber.di.warning;

import com.tokopedia.changephonenumber.di.ChangePhoneNumberComponent;
import com.tokopedia.changephonenumber.view.fragment.ChangePhoneNumberWarningFragment;

import dagger.Component;

/**
 * @author by alvinatin on 01/10/18.
 */

@ChangePhoneNumberWarningScope
@Component(modules = ChangePhoneNumberWarningModule.class, dependencies = ChangePhoneNumberComponent.class)
public interface ChangePhoneNumberWarningComponent {
    void inject(ChangePhoneNumberWarningFragment fragment);
}
