package com.tokopedia.core.referral.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.referral.listner.ReferralView;
import com.tokopedia.core.referral.presenter.IReferralPresenter;
import com.tokopedia.core.referral.presenter.ReferralPresenter;

import butterknife.BindView;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public class FragmentReferral extends BasePresenterFragment<IReferralPresenter> implements ReferralView {

    private ReferralPresenter presenter;

    @BindView(R2.id.btn_app_share)
    TextView appShareButton;

    public static FragmentReferral newInstance() {
        FragmentReferral fragmentReferral = new FragmentReferral();
        return fragmentReferral;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ReferralPresenter(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_app_share;
    }

    @Override
    protected void initView(View view) {
        appShareButton.setOnClickListener(getButtonAppShareClickListner());
    }


    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    private View.OnClickListener getButtonAppShareClickListner() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.shareApp();
                UnifyTracking.eventAppShare();

            }
        };

    }
}
