package com.tokopedia.challenges.view.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.fragments.ChallengeDetailsFragment;
import com.tokopedia.challenges.view.fragments.ImageViewerFragment;
import com.tokopedia.challenges.view.fragments.VideoViewFragment;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.ChallengesFragmentCallbacks;

/**
 * @author lalit.singh
 */
public class ChallengeDetailsActivity extends ChallengesBaseActivity implements ChallengesFragmentCallbacks {

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
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(TncBottomSheetFragment.TEXT, text);
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up);
        transaction.add(R.id.parent_view, TncBottomSheetFragment.createInstance(bundle));
        transaction.addToBackStack(null);
        transaction.commit();*/
    }

    public void openVideoPlayer(SubmissionResult submissionResult){
        Fragment fragemnt = VideoViewFragment.createInstance(submissionResult);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, fragemnt);
        transaction.addToBackStack("VideoViewer");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("VideoViewer");
        if(fragment!= null) {
            if (fragment != null) {
                DialogFragment df = (DialogFragment) fragment;
                df.dismiss();
                return;
            }
        }
        super.onBackPressed();
    }
}
