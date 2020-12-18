package com.tokopedia.loginregister.registerinitial.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment;

/**
 * @author by nisie on 10/2/18.
 */
public class RegisterInitialActivity extends BaseSimpleActivity implements HasComponent{

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if(getIntent().getExtras()!= null){
            bundle.putAll(getIntent().getExtras());
        }

        return RegisterInitialFragment.Companion.createInstance(bundle);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RegisterInitialActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitleTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_96));
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        toolbar.setPadding(0, 0, 30, 0);
    }

    @Override
    public LoginRegisterComponent getComponent() {
        return DaggerLoginRegisterComponent.builder().baseAppComponent(((BaseMainApplication)
                getApplication()).getBaseAppComponent()).build();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.parent_view) instanceof
                RegisterInitialFragment) {
            ((RegisterInitialFragment) getSupportFragmentManager().findFragmentById(R.id
                    .parent_view)).onBackPressed();
        }

        super.onBackPressed();
    }
}
