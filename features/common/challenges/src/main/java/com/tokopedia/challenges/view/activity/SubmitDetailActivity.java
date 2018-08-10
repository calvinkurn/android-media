package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.challenges.view.fragments.SubmitDetailFragment;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;

public class SubmitDetailActivity extends BaseActivity {
    public static Intent newInstance(Context context){
        return new Intent(context, SubmitDetailActivity.class);
    }
    @Override
    protected Fragment getNewFragment() {
        SubmissionResult model = getIntent().getParcelableExtra("submissionsResult");
        Fragment fragment = SubmitDetailFragment.newInstance();
        Bundle arg = new Bundle();
        arg.putParcelable("submissionsResult",model);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }
    }
}