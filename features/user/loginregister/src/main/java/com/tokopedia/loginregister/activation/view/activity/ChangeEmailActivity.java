package com.tokopedia.loginregister.activation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.loginregister.activation.view.fragment.ChangeEmailFragment;
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailActivity extends BaseSimpleActivity implements HasComponent{

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        bundle.putAll(getIntent().getExtras());
        return ChangeEmailFragment.createInstance(bundle);
    }

    public static Intent getCallingIntent(Context context, String oldEmail) {
        Intent intent = new Intent(context, ChangeEmailActivity.class);
        intent.putExtra(ChangeEmailFragment.EXTRA_EMAIL, oldEmail);
        return intent;
    }

    @Override
    public LoginRegisterComponent getComponent() {
        return DaggerLoginRegisterComponent.builder().baseAppComponent(((BaseMainApplication)
                getApplication()).getBaseAppComponent()).build();
    }

}
