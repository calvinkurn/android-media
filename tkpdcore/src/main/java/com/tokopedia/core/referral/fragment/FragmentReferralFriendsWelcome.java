package com.tokopedia.core.referral.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.manage.general.ManageWebViewActivity;
import com.tokopedia.core.referral.listener.FriendsWelcomeView;
import com.tokopedia.core.referral.presenter.IReferralFriendsWelcomePresenter;
import com.tokopedia.core.referral.presenter.ReferralFriendsWelcomePresenter;
import com.tokopedia.core.var.TkpdUrl;

import butterknife.BindView;

/**
 * Created by ashwanityagi on 06/12/17.
 */

public class FragmentReferralFriendsWelcome extends BasePresenterFragment<IReferralFriendsWelcomePresenter> implements FriendsWelcomeView {


    @BindView(R2.id.btn_referral_explore)
    TextView btnReferralExplore;
    @BindView(R2.id.tv_referral_code)
    TextView referralCodeTextView;
    @BindView(R2.id.btn_copy_referral_code)
    TextView copyReferralCodeButton;
    @BindView(R2.id.tv_app_share_desc)
    TextView welcomeMessageTextView;
    @BindView(R2.id.tv_referral_help_link)
    TextView TextViewHelpLink;

    private ReferralFriendsWelcomePresenter presenter;


    public static FragmentReferralFriendsWelcome newInstance() {
        FragmentReferralFriendsWelcome fragmentFriendsWelcome = new FragmentReferralFriendsWelcome();
        return fragmentFriendsWelcome;
    }


    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ReferralFriendsWelcomePresenter(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_friends_welcome;
    }


    @Override
    protected void initView(View view) {
        presenter.initialize();
        copyReferralCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.copyVoucherCode(referralCodeTextView.getText().toString());
            }
        });

        btnReferralExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_EXPLORE_TOKOPEDIA, AppEventTracking.EventLabel.HOME);
                closeView();
            }
        });
        String msg = presenter.getReferralWelcomeMsg();
        if (!TextUtils.isEmpty(msg))
            welcomeMessageTextView.setText(msg);

        TextViewHelpLink.setText(presenter.getHowItWorks());
        TextViewHelpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_KNOW_MORE,"");

                startActivity(ManageWebViewActivity.getCallingIntent(getActivity(), TkpdUrl.REFERRAL_URL, getString(R.string.app_name)));

            }
        });
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

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    public void closeView() {
        getActivity().finish();
    }

    @Override
    public void renderReferralCode(String code) {
        referralCodeTextView.setText(code);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}