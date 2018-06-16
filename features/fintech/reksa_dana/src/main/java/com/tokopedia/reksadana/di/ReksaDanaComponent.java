package com.tokopedia.reksadana.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.reksadana.view.fragment.ReksaDanaHomeFragment;

import dagger.Component;
import okhttp3.Interceptor;

@ReksaDanaModuleScope
@Component(dependencies = {BaseAppComponent.class})
public interface ReksaDanaComponent {
    void inject(ReksaDanaHomeFragment reksaDanaFragment);
}
