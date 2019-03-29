package com.tokopedia.changephonenumber.di.input;

import com.tokopedia.changephonenumber.di.ChangePhoneNumberComponent;
import com.tokopedia.changephonenumber.view.fragment.ChangePhoneNumberInputFragment;

import dagger.Component;

/**
 * @author by alvinatin on 01/10/18.
 */

@ChangePhoneNumberInputScope
@Component(modules = ChangePhoneNumberInputModule.class, dependencies = ChangePhoneNumberComponent.class)
public interface ChangePhoneNumberInputComponent {
    void inject(ChangePhoneNumberInputFragment fragment);
}
