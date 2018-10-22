package com.tokopedia.loginregister.activation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.loginregister.activation.view.fragment.ChangeEmailFragment;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailActivity extends BaseSimpleActivity {

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

}
