package com.tokopedia.loginregister.registeremail.di;

import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.registeremail.view.fragment.RegisterEmailFragment;
import com.tokopedia.loginregister.registeremail.view.activity.RegisterEmailActivity;

import dagger.Component;

/**
 * @author by nisie on 10/26/18.
 */
@RegisterEmailScope
@Component(modules = RegisterEmailModule.class, dependencies = LoginRegisterComponent.class)
public interface RegisterEmailComponent {

    public void inject(RegisterEmailFragment fragment);

    public void inject(RegisterEmailActivity activity);


}