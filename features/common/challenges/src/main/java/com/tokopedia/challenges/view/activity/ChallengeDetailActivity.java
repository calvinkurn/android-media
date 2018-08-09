package com.tokopedia.challenges.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.fragments.AllSubmissionFragment;
import com.tokopedia.challenges.view.fragments.ChallegeneSubmissionFragment;
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
    public List<SubmissionResult> getSubmissions() {
        return submissions;
    }
}
