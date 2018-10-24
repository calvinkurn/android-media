package com.tokopedia.loginregister.registerinitial.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment;

/**
 * @author by nisie on 10/2/18.
 */
public class RegisterInitialActivity extends BaseSimpleActivity{

    @Override
    protected Fragment getNewFragment() {
        return RegisterInitialFragment.createInstance();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RegisterInitialActivity.class);
    }

}
