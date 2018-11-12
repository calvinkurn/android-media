package com.tokopedia.useridentification.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity;
import com.tokopedia.useridentification.view.fragment.UserIdentificationFormFaceFragment;
import com.tokopedia.useridentification.view.fragment.UserIdentificationFormKtpFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationFormActivity extends BaseStepperActivity {



    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, UserIdentificationFormActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(UserIdentificationFormKtpFragment.createInstance());
        fragmentList.add(UserIdentificationFormFaceFragment.createInstance());
        return fragmentList;
    }
}
