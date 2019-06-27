package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.fragments.ChallengeDetailsFragment;
import com.tokopedia.challenges.view.fragments.VideoViewFragment;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.ChallengesFragmentCallbacks;
import com.tokopedia.challenges.view.utils.Utils;

/**
 * @author lalit.singh
 */
public class ChallengeDetailsActivity extends ChallengesBaseActivity implements ChallengesFragmentCallbacks {


    public static final int REQUEST_CODE_SUBMISSIONDETAILACTIVITY = 10;

    @DeepLink({ChallengesUrl.AppLink.CHALLENGES_DETAILS})
    public static TaskStackBuilder getInstanceIntentAppLinkBackToHome(Context context, Bundle extras) {
        String deepLink = extras.getString(DeepLink.URI);
        Intent destination;

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

        Intent homeIntent = ((ChallengesModuleRouter) context.getApplicationContext()).getHomeIntent(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(new Intent(context, ChallengesHomeActivity.class));

        Uri.Builder uri = Uri.parse(deepLink).buildUpon();

        extras.putString(Utils.QUERY_PARAM_CHALLENGE_ID, extras.getString(Utils.QUERY_PARAM_CHALLENGE_ID));
        extras.putString(Utils.QUERY_PARAM_IS_PAST_CHALLENGE, extras.getString(Utils.QUERY_PARAM_IS_PAST_CHALLENGE));
        destination = new Intent(context, ChallengeDetailsActivity.class)
                .setData(uri.build())
                .putExtras(extras);

        taskStackBuilder.addNextIntent(destination);
        return taskStackBuilder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected Fragment getNewFragment() {
        return ChallengeDetailsFragment.createInstance(getIntent().getExtras());
    }

    @Override
    public void replaceFragment(String text, String toolBarText) {
    }

    public void openVideoPlayer(SubmissionResult submissionResult) {
        Fragment fragemnt = VideoViewFragment.createInstance(submissionResult);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, fragemnt);
        transaction.addToBackStack("VideoViewer");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("VideoViewer");
        if (fragment != null) {
            DialogFragment df = (DialogFragment) fragment;
            df.dismiss();
            return;
        }
        super.onBackPressed();
    }
}
