package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.fragments.ChallegeneSubmissionFragment;
import com.tokopedia.challenges.view.fragments.TncBottomSheetFragment;
import com.tokopedia.challenges.view.utils.ChallengesFragmentCallbacks;
import com.tokopedia.challenges.view.utils.Utils;

public class ChallengeDetailActivity extends ChallengesBaseActivity implements ChallengesFragmentCallbacks {


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
        destination = new Intent(context, ChallengeDetailActivity.class)
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
        return ChallegeneSubmissionFragment.createInstance(getIntent().getExtras());
    }

    @Override
    public void replaceFragment(String text, String toolBarText) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(TncBottomSheetFragment.TOOLBAR_TITLE, toolBarText);
        bundle.putString(TncBottomSheetFragment.TEXT, text);
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
        transaction.add(R.id.parent_view, TncBottomSheetFragment.createInstance(bundle));
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            inflateFragment();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
