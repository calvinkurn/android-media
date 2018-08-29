package com.tokopedia.updateinactivephone.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.updateinactivephone.activity.ChangeInactiveFormRequestActivity;
import com.tokopedia.updateinactivephone.di.module.UpdateInactivePhoneModule;
import com.tokopedia.updateinactivephone.fragment.ChangeInactivePhoneFragment;
import com.tokopedia.updateinactivephone.fragment.SelectImageNewPhoneFragment;

import dagger.Component;

@UpdateInactivePhoneScope
@Component(modules = UpdateInactivePhoneModule.class, dependencies = AppComponent.class)
public interface UpdateInactivePhoneComponent {

    void inject(ChangeInactivePhoneFragment changeInactivePhoneFragment);

    void inject(SelectImageNewPhoneFragment selectImageNewPhoneFragment);

    void inject(ChangeInactiveFormRequestActivity changeInactiveFormRequestActivity);
}
