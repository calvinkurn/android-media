package com.tokopedia.gamification.cracktoken.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.gamification.cracktoken.fragment.CrackTokenFragment;
import com.tokopedia.gamification.floating.view.model.TokenData;

public class CrackTokenActivity extends BaseSimpleActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, CrackTokenActivity.class);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, CrackTokenActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle("TokoPoints");
    }

    @Override
    protected Fragment getNewFragment() {
        return CrackTokenFragment.newInstance();
    }

}
