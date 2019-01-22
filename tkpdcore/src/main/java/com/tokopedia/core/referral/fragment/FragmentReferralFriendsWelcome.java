package com.tokopedia.core.referral.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.manage.general.ManageWebViewActivity;
import com.tokopedia.core.referral.listener.FriendsWelcomeView;
import com.tokopedia.core.referral.presenter.IReferralFriendsWelcomePresenter;
import com.tokopedia.core.referral.presenter.ReferralFriendsWelcomePresenter;
import com.tokopedia.core.var.TkpdUrl;
import com.tokopedia.design.bottomsheet.BottomSheetView;

import butterknife.BindView;

/**
 * Created by ashwanityagi on 06/12/17.
 */

public class FragmentReferralFriendsWelcome extends BasePresenterFragment<IReferralFriendsWelcomePresenter> implements FriendsWelcomeView {


    @BindView(R2.id.btn_referral_explore)
    TextView btnReferralExplore;
    @BindView(R2.id.tv_referral_header)
    TextView welcomeMessageHearer;
    @BindView(R2.id.tv_referral_subheader)
    TextView welcomeMessageSubHearer;
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

        btnReferralExplore.setOnClickListener(v -> {
            UnifyTracking.eventReferralAndShare(getActivity(), AppEventTracking.Action.CLICK_EXPLORE_TOKOPEDIA, AppEventTracking.EventLabel.HOME);
            closeView();
        });
        TextViewHelpLink.setVisibility(presenter.isShowReferralHelpLink() ? View.VISIBLE : View.GONE);
        TextViewHelpLink.setText(Html.fromHtml(presenter.getHowItWorks()));
        TextViewHelpLink.setOnClickListener(view1 -> {
            UnifyTracking.eventReferralAndShare(getActivity(), AppEventTracking.Action.CLICK_KNOW_MORE,"");
            showOnBoardingTooltip(presenter.getHelpButtonContentTitle(), presenter.getHelpButtonContentSubtitle());
        });
        welcomeMessageHearer.setText(Html.fromHtml(getString(R.string.referral_welcome_header)));
        welcomeMessageSubHearer.setText(Html.fromHtml(presenter.getSubHeaderFromFirebase()));
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
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void showOnBoardingTooltip(String title, String content) {
        BottomSheetView mToolTip = new BottomSheetView(context);
        mToolTip.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(title)
                .setBody(content)
                .build());

        mToolTip.show();
    }

}