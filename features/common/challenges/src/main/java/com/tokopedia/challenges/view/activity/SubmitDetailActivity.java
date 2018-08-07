package com.tokopedia.challenges.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.IndiUserModel;
import com.tokopedia.challenges.presenter.SubmitDetailContract;
import com.tokopedia.challenges.presenter.SubmitDetailPresenter;
import com.tokopedia.challenges.view.fragments.SubmitDetailFragment;

public class SubmitDetailActivity extends BaseActivity {
    public static Intent newInstance(Context context){
        return new Intent(context, SubmitDetailActivity.class);
    }
    @Override
    protected Fragment getNewFragment() {
        IndiUserModel model = getIntent().getParcelableExtra("DETAILS");
        Fragment fragment = SubmitDetailFragment.newInstance();
        Bundle arg = new Bundle();
        arg.putParcelable("DETAILS",model);
        fragment.setArguments(arg);
        return fragment;
    }
}