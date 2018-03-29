package com.tokopedia.gamification;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.gamification.cracktoken.presentation.fragment.CrackTokenFragment;

public class EggGamificationActivity extends BaseSimpleActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, EggGamificationActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return CrackTokenFragment.newInstance();
    }
}
