package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.fragments.SubmitDetailFragment;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.Utils;

public class SubmitDetailActivity extends BaseActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, SubmitDetailActivity.class);
    }

    @DeepLink({ChallengesUrl.AppLink.SUBMISSION_DETAILS})
    public static TaskStackBuilder getInstanceIntentAppLink(Context context, Bundle extras) {
        String deepLink = extras.getString(DeepLink.URI);
        Intent destination;

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

        Intent homeIntent = ((ChallengesModuleRouter) context.getApplicationContext()).getHomeIntent(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(new Intent(context, ChallengesHomeActivity.class));

        Uri.Builder uri = Uri.parse(deepLink).buildUpon();

        extras.putString(Utils.QUERY_PARAM_SUBMISSION_ID, extras.getString(Utils.QUERY_PARAM_SUBMISSION_ID));
        destination = new Intent(context, SubmitDetailActivity.class)
                .setData(uri.build())
                .putExtras(extras);

        taskStackBuilder.addNextIntent(destination);
        return taskStackBuilder;
    }

    @Override
    protected Fragment getNewFragment() {
        SubmissionResult model = getIntent().getParcelableExtra("submissionsResult");
        String submissionId = getIntent().getStringExtra(Utils.QUERY_PARAM_SUBMISSION_ID);
        Fragment fragment = SubmitDetailFragment.newInstance();
        Bundle arg = new Bundle();
        arg.putParcelable("submissionsResult", model);
        arg.putString(Utils.QUERY_PARAM_SUBMISSION_ID, submissionId);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }

        updateTitle("Post");
    }
}