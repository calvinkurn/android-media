package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.fragments.submit.ChallengesSubmitFragment;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;
import com.tokopedia.challenges.view.utils.Utils;

public class ChallengesSubmitActivity extends ChallengesBaseActivity {

    public static Intent getStartingIntent(Context context, ChallengeSettings settings, String channelId, String channelTitle, String channelDesc) {
        Intent intent = new Intent(context, ChallengesSubmitActivity.class);
        intent.putExtra(Utils.QUERY_PARAM_CHALLENGE_SETTINGS,settings);
        intent.putExtra(Utils.QUERY_PARAM_CHANNEL_ID, channelId);
        intent.putExtra(Utils.QUERY_PARAM_CHANNEL_TITLE, channelTitle);
        intent.putExtra(Utils.QUERY_PARAM_CHANNEL_DESC, channelDesc);

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
