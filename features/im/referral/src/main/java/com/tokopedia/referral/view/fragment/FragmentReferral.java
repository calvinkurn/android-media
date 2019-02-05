package com.tokopedia.referral.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.referral.Constants;
import com.tokopedia.referral.view.HeightWrappingViewPager;
import com.tokopedia.referral.R;
import com.tokopedia.referral.view.adapter.ReferralGuidePagerAdapter;
import com.tokopedia.referral.data.ReferralCodeEntity;
import com.tokopedia.referral.di.ReferralComponent;
import com.tokopedia.referral.ReferralRouter;
import com.tokopedia.referral.view.listener.ReferralView;
import com.tokopedia.referral.domain.model.ShareApps;
import com.tokopedia.referral.view.presenter.ReferralPresenter;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public class FragmentReferral extends BaseDaggerFragment implements ReferralView, ReferralGuidePagerAdapter.ReferralGuidePagerListener, View.OnClickListener {

    @Inject
    ReferralPresenter presenter;
    private TextView referralCodeTextView;
    private TextView referralContentTextView;
    private TextView TextViewHelpLink;
    private RelativeLayout referralCodeLayout;
    private HeightWrappingViewPager pagerGuide;
    private TabLayout tabGuide;
    private LinearLayout llShareIcons;
    private NestedScrollView nestedScrollView;
    private View viewLine;
    private TextView referralCount;
    private ImageView imgTick;
    private TextView btnCopyReferralCode;
    private ProgressBar progressBarReferral;
    private TextView tvPercent;
    private TextView referralTitleTextview;

    private ReferralGuidePagerAdapter referralGuidePagerAdapter;

    private ProgressDialog progressBar;
    public static final int REFERRAL_PHONE_VERIFY_REQUEST_CODE = 1011;
    public static final int LOGIN_REQUEST_CODE = 1012;

    private void assignViews(View view){
        referralCodeTextView = view.findViewById(R.id.tv_referral_code);
        referralContentTextView = view.findViewById(R.id.tv_referral_desc);
        TextViewHelpLink = view.findViewById(R.id.tv_referral_help_link);
        referralCodeLayout = view.findViewById(R.id.rl_referral_code);
        pagerGuide = view.findViewById(R.id.view_pager_referral_guide);
        tabGuide = view.findViewById(R.id.tab_referral_guide);
        llShareIcons = view.findViewById(R.id.ll_share_icons);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        viewLine = view.findViewById(R.id.view_line);
        referralCount = view.findViewById(R.id.referral_count);
        imgTick = view.findViewById(R.id.img_tick);
        btnCopyReferralCode = view.findViewById(R.id.btn_copy_referral_code);
        btnCopyReferralCode.setOnClickListener(this);
        progressBarReferral = view.findViewById(R.id.progress_bar_referral);
        tvPercent = view.findViewById(R.id.tv_referral_percent);
        referralTitleTextview = view.findViewById(R.id.tv_referral_title);
    }

    public static FragmentReferral newInstance() {
        FragmentReferral fragmentReferral = new FragmentReferral();
        return fragmentReferral;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(getFragmentLayout(), container, false);
        return view;
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialPresenter();
        initView(view);
    }

    protected boolean getOptionsMenuEnable() {
        return false;
    }

    protected void initialPresenter() {
        presenter.attachView(this);
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_referral;
    }

    protected void initView(View view) {
        assignViews(view);
        presenter.initialize();
        referralContentTextView.setText(presenter.getReferralSubHeader());
        referralTitleTextview.setText(presenter.getReferralTitleDesc());
        if (presenter.isAppShowReferralButtonActivated()) {
            referralCodeLayout.setVisibility(View.VISIBLE);
            TextViewHelpLink.setVisibility(View.VISIBLE);
            TextViewHelpLink.setOnClickListener(view1 -> {
                focusOnView();
                ((ReferralRouter)getActivity().getApplicationContext()).eventReferralAndShare(getActivity(),
                        Constants.Action.Companion.CLICK_HOW_IT_WORKS, "");
            });
        } else {
            referralCodeLayout.setVisibility(View.INVISIBLE);
            TextViewHelpLink.setVisibility(View.INVISIBLE);
        }

        referralGuidePagerAdapter = new ReferralGuidePagerAdapter(getActivity(), this);
        pagerGuide.setAdapter(referralGuidePagerAdapter);

        pagerGuide.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabGuide));
        tabGuide.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pagerGuide));

        llShareIcons.removeAllViews();
        presenter.getSharableApps();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInjector() {
        getComponent(ReferralComponent.class).inject(this);
    }

    public void clickOnCopyButton() {
        presenter.copyVoucherCode(referralCodeTextView.getText().toString());
        if (!TextUtils.isEmpty(referralCodeTextView.getText().toString())) {
            btnCopyReferralCode.setText(R.string.copied);
            btnCopyReferralCode.setTextColor(getResources().getColor(R.color.green_250));
            imgTick.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void renderVoucherCodeData(ReferralCodeEntity referralData) {
        referralCodeTextView.setText(referralData.getPromoContent().getCode());
        referralCount.setText(referralData.getPromoContent().getFriendCount() + " " + getString(R.string.fiends_invited_lable));
        if (referralData.getPromoBenefit() != null) {
            progressBarReferral.setProgress((referralData.getPromoBenefit().getCurrentBenefit() == 0 ?
                    referralData.getPromoBenefit().getCurrentBenefit() : referralData.getPromoBenefit().getCurrentBenefit() * 100
                    / referralData.getPromoBenefit().getMaxBenefit()));
            tvPercent.setText("Rp " + CurrencyFormatUtil.convertPriceValue((double) referralData.getPromoBenefit().getCurrentBenefit(), false) +
                    " / Rp " + CurrencyFormatUtil.convertPriceValue((double) referralData.getPromoBenefit().getMaxBenefit(), false));
        }
    }

    @Override
    public void showToastMessage(String message) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeView() {
        if (getActivity() != null)
            getActivity().finish();
    }

    @Override
    public void navigateToLoginPage() {
        Intent intent = ((ReferralRouter) getContext()).getLoginIntent(getActivity());
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    @Override
    public void showVerificationPhoneNumberPage() {
        if (getActivity().getApplicationContext() instanceof ReferralRouter) {
            ReferralRouter referralOtpRouter = (ReferralRouter) getActivity().getApplicationContext();
            startActivityForResult(referralOtpRouter.getReferralPhoneNumberActivityIntent(getActivity()),
                    REFERRAL_PHONE_VERIFY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REFERRAL_PHONE_VERIFY_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getActivity(), getActivity().getString(R.string.phone_not_verified_error_message), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    break;
                default:
                    presenter.initialize();
                    break;
            }
        } else if (requestCode == LOGIN_REQUEST_CODE) {
            if (!(new UserSession(getActivity())).isLoggedIn()) {
                closeView();
            } else {
                presenter.initialize();
            }
        }
    }

    @Override
    public String getReferralCodeFromTextView() {
        return referralCodeTextView.getText().toString();
    }

    @Override
    public void showProcessDialog() {
        if (progressBar == null) {
            progressBar = new ProgressDialog(getActivity());
        }
        progressBar.show();

    }

    @Override
    public void hideProcessDialog() {
        if (progressBar != null && progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void renderErrorGetVoucherCode(String message) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, () -> presenter.checkLoginAndFetchReferralCode()).showRetrySnackbar();
    }

    @Override
    public void renderSharableApps(ShareApps shareApps, int index) {
        if (index == 0) {
            llShareIcons.removeAllViews();
        }
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(shareApps.getIcon());
        imageView.setTag(shareApps);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        imageView.setLayoutParams(layoutParams);
        imageView.setOnClickListener(v -> {
            presenter.appShare(((ShareApps) v.getTag()), getChildFragmentManager());
        });
        llShareIcons.addView(imageView);

    }

    private final void focusOnView() {
        nestedScrollView.post(() -> {
            nestedScrollView.fling(0);
            nestedScrollView.smoothScrollTo(0, viewLine.getBottom());
        });
    }

    @Override
    public void onShareClick() {
        presenter.shareApp(getChildFragmentManager());
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_copy_referral_code) {
            clickOnCopyButton();
        }
    }
}
