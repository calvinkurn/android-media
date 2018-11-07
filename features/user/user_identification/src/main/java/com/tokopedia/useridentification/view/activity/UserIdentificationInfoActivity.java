package com.tokopedia.useridentification.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.useridentification.view.fragment.UserIdentificationInfoFragment;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationInfoActivity extends BaseSimpleActivity {

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, UserIdentificationInfoActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return UserIdentificationInfoFragment.createInstance();
    }
}
