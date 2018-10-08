package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.fragments.ImageViewerFragment;
import com.tokopedia.challenges.view.fragments.SubmitDetailFragment;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.Utils;

import java.util.ArrayList;

public class SubmitDetailActivity extends ChallengesBaseActivity implements SubmitDetailFragment.ImageListener {

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
        extras.putBoolean(Utils.QUERY_PARAM_IS_PAST_CHALLENGE, extras.getBoolean(Utils.QUERY_PARAM_IS_PAST_CHALLENGE));
        extras.putBoolean(Utils.QUERY_PARAM_IS_FROM_NOTIF, uri.build().getBooleanQueryParameter(Utils.QUERY_PARAM_IS_FROM_NOTIF,false));
        destination = new Intent(context, SubmitDetailActivity.class)
                .setData(uri.build())
                .putExtras(extras);

        taskStackBuilder.addNextIntent(destination);
        return taskStackBuilder;
    }

    @Override
    protected Fragment getNewFragment() {
        SubmissionResult model = getIntent().getParcelableExtra(Utils.QUERY_PARAM_SUBMISSION_RESULT);
        String submissionId = getIntent().getStringExtra(Utils.QUERY_PARAM_SUBMISSION_ID);
        boolean isPastChallenge = getIntent().getBooleanExtra(Utils.QUERY_PARAM_IS_PAST_CHALLENGE, false);
        boolean fromSubmission = getIntent().getBooleanExtra(Utils.QUERY_PARAM_FROM_SUBMISSION, false);
        Fragment fragment = SubmitDetailFragment.newInstance();
        Bundle arg = new Bundle();
        arg.putParcelable(Utils.QUERY_PARAM_SUBMISSION_RESULT, model);
        arg.putBoolean(Utils.QUERY_PARAM_FROM_SUBMISSION, fromSubmission);
        arg.putString(Utils.QUERY_PARAM_SUBMISSION_ID, submissionId);
        arg.putBoolean(Utils.QUERY_PARAM_IS_PAST_CHALLENGE, isPastChallenge);
        arg.putBoolean(Utils.QUERY_PARAM_IS_FROM_NOTIF, getIntent().getBooleanExtra(Utils.QUERY_PARAM_IS_FROM_NOTIF,false));

        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }

        updateTitle(getResources().getString(R.string.ch_post_detail));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void openImage(ArrayList<String> imageUrls) {
        ImageViewerFragment fragemnt = ImageViewerFragment.newInstance(0, imageUrls);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, fragemnt);
        transaction.addToBackStack("ImageViewer");
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