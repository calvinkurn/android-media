package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.challenges.view.fragments.submit.ChallengesSubmitFragment;
import com.tokopedia.challenges.view.model.Result;

public class ChallengesSubmitActivity extends BaseActivity {

    public static Intent getStartingIntent(Context context, Result challengesResult) {
        Intent intent = new Intent(context, ChallengesSubmitActivity.class);
        intent.putExtra("challengesResult", challengesResult);
        return intent;
    }
    @Override
    protected Fragment getNewFragment() {
        return ChallengesSubmitFragment.newInstance(getIntent().getExtras());
    }
}
