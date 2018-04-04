package com.tokopedia.gamification.cracktoken.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.fragment.CrackEmptyTokenFragment;
import com.tokopedia.gamification.cracktoken.fragment.CrackTokenFragment;

public class CrackTokenActivity extends BaseSimpleActivity implements CrackTokenFragment.ActionListener {

    private static final String SUM_TOKEN = "sum_token";

    public static Intent newInstance(Context context) {
        return new Intent(context, CrackTokenActivity.class);
    }

    public static Intent getIntent(int sumToken, Context context) {
        Intent intent = new Intent(context, CrackTokenActivity.class);
        intent.putExtra(SUM_TOKEN, sumToken);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle("TokoPoints");
    }

    @Override
    protected Fragment getNewFragment() {
        if (getIntent().getIntExtra(SUM_TOKEN, 0) > 0) {
            return CrackTokenFragment.newInstance();
        } else {
            return CrackEmptyTokenFragment.newInstance();
        }
    }

    @Override
    public void directPageToCrackEmpty() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.parent_view);
        if (fragment == null || !(fragment instanceof CrackEmptyTokenFragment))
            getSupportFragmentManager().beginTransaction().replace(R.id.parent_view,
                    CrackEmptyTokenFragment.newInstance()).commit();
    }

}
