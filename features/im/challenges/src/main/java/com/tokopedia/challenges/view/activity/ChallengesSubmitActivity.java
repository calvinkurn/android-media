package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.fragments.submit.ChallengesSubmitFragment;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;

public class ChallengesSubmitActivity extends BaseActivity {

    public static Intent getStartingIntent(Context context, ChallengeSettings settings, String channelId, String channelTitle, String channelDesc) {
        Intent intent = new Intent(context, ChallengesSubmitActivity.class);
        intent.putExtra("challengeSettings",settings);
        intent.putExtra("channelId", channelId);
        intent.putExtra("channelTitle", channelTitle);
        intent.putExtra("channelDesc", channelDesc);

        return intent;
    }
    @Override
    protected Fragment getNewFragment() {
        updateTitle(getResources().getString(R.string.ch_join_the_challenge));
        return ChallengesSubmitFragment.newInstance(getIntent().getExtras());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
