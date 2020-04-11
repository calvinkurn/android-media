package com.tokopedia.loginregister.registerinitial.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterEmailFragment;

/**
 * @author by nisie on 10/25/18.
 */
public class RegisterEmailActivity extends BaseSimpleActivity implements HasComponent {

    public static final String EXTRA_PARAM_EMAIL = "email";

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return RegisterEmailFragment.createInstance(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        toolbar.setPadding(0, 0, 30, 0);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RegisterEmailActivity.class);
    }

    public static Intent getCallingIntentWithEmail(@NonNull Context context, @NonNull String email,
                                                   @NonNull String source) {
        Intent intent = new Intent(context, RegisterEmailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_EMAIL, email);
        bundle.putString(ApplinkConstInternalGlobal.PARAM_SOURCE, source);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public LoginRegisterComponent getComponent() {
        return DaggerLoginRegisterComponent.builder().baseAppComponent(((BaseMainApplication)
                getApplication()).getBaseAppComponent()).build();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.parent_view) instanceof
                RegisterEmailFragment) {
            ((RegisterEmailFragment) getSupportFragmentManager().findFragmentById(R.id
                    .parent_view)).onBackPressed();
        }

        super.onBackPressed();
    }
}
