package com.tokopedia.challenges.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.fragments.AllSubmissionFragment;
import com.tokopedia.challenges.view.fragments.ChallegeneSubmissionFragment;
import com.tokopedia.challenges.view.fragments.TncBottomSheetFragment;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.ChallengesFragmentCallbacks;

import java.util.List;

public class ChallengeDetailActivity extends BaseActivity implements ChallengesFragmentCallbacks{


    public static final int REQUEST_CODE_LOGIN = 1011;
    public static final int REQUEST_CODE_SUBMISSIONDETAILACTIVITY = 10;
    private List<SubmissionResult> submissions;


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
    public void replaceFragment(List<SubmissionResult> submissions) {
        this.submissions = submissions;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, AllSubmissionFragment.createInstance());
        transaction.addToBackStack(null);
        transaction.commit();
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
    public List<SubmissionResult> getSubmissions() {
        return submissions;
    }
}
