package com.tokopedia.loginregister.register;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

/**
 * @author by nisie on 10/2/18.
 */
public class RegisterInitialActivity extends BaseSimpleActivity{

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RegisterInitialActivity.class);
    }

}
