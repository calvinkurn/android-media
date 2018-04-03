package com.tokopedia.gamification.cracktoken.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.gamification.cracktoken.fragment.CrackTokenFragment;
import com.tokopedia.gamification.floating.view.model.TokenData;

public class CrackTokenActivity extends BaseSimpleActivity {

    public static final String EXTRA_TOKEN_DATA = "extra_token_data";

    private TokenData tokenData;

    public static Intent newInstance(Context context) {
        return new Intent(context, CrackTokenActivity.class);
    }

    public static Intent getIntent(Context context, TokenData tokenData) {
        Intent intent = new Intent(context, CrackTokenActivity.class);
        intent.putExtra(EXTRA_TOKEN_DATA, tokenData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TOKEN_DATA)) {
            tokenData = intent.getParcelableExtra(EXTRA_TOKEN_DATA);
        }
        super.onCreate(savedInstanceState);
        updateTitle("TokoPoints");
    }

    @Override
    protected Fragment getNewFragment() {
        return CrackTokenFragment.newInstance(tokenData);
    }

}
