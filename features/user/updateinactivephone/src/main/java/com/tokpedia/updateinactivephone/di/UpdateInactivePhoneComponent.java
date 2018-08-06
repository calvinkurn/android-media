package com.tokpedia.updateinactivephone.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.updateinactivephone.di.module.UpdateInactivePhoneModule;
import com.tokopedia.updateinactivephone.fragment.ChangeInactiveFormRequestFragment;
import com.tokopedia.updateinactivephone.fragment.ChangeInactivePhoneFragment;

import dagger.Component;

@UpdateInactivePhoneScope
@Component(modules = UpdateInactivePhoneModule.class, dependencies = AppComponent.class)
public interface UpdateInactivePhoneComponent {

    void inject(ChangeInactivePhoneFragment changeInactivePhoneFragment);

    void inject(ChangeInactiveFormRequestFragment changeInactiveFormRequestFragment);
}
