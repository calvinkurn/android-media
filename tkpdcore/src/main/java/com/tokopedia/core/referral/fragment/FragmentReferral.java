package com.tokopedia.core.referral.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.BasePresenterFragmentV4;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.referral.HeightWrappingViewPager;
import com.tokopedia.core.referral.adapter.ReferralGuidePagerAdapter;
import com.tokopedia.core.referral.data.ReferralCodeEntity;
import com.tokopedia.core.referral.di.DaggerReferralComponent;
import com.tokopedia.core.referral.di.ReferralComponent;
import com.tokopedia.core.referral.di.ReferralModule;
import com.tokopedia.core.referral.listener.ReferralView;
import com.tokopedia.core.referral.model.ShareApps;
import com.tokopedia.core.referral.presenter.IReferralPresenter;
import com.tokopedia.core.referral.presenter.ReferralPresenter;
import com.tokopedia.core.router.OtpRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.design.utils.CurrencyFormatUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public class FragmentReferral extends BasePresenterFragmentV4<IReferralPresenter> implements ReferralView, ReferralGuidePagerAdapter.ReferralGuidePagerListener {

    @Inject
    ReferralPresenter presenter;
    @BindView(R2.id.tv_referral_code)
    TextView referralCodeTextView;
    @BindView(R2.id.tv_referral_desc)
    TextView referralContentTextView;
    @BindView(R2.id.tv_referral_help_link)
    TextView TextViewHelpLink;
    @BindView(R2.id.rl_referral_code)
    RelativeLayout referralCodeLayout;
    @BindView(R2.id.view_pager_referral_guide)
    HeightWrappingViewPager pagerGuide;
    @BindView(R2.id.tab_referral_guide)
    TabLayout tabGuide;
    @BindView(R2.id.ll_share_icons)
    LinearLayout llShareIcons;
    @BindView(R2.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R2.id.view_line)
    View viewLine;
    @BindView(R2.id.referral_count)
    TextView referralCount;
    @BindView(R2.id.img_tick)
    ImageView imgTick;
    @BindView(R2.id.btn_copy_referral_code)
    TextView btnCopyReferralCode;
    @BindView(R2.id.progress_bar_referral)
    ProgressBar progressBarReferral;
    @BindView(R2.id.tv_referral_percent)
    TextView tvPercent;
    @BindView((R2.id.tv_referral_title))
    TextView referralTitleTextview;

    private ReferralGuidePagerAdapter referralGuidePagerAdapter;

    private ProgressDialog progressBar;
    public static final int REFERRAL_PHONE_VERIFY_REQUEST_CODE = 1011;
    public static final int LOGIN_REQUEST_CODE = 1012;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter.attachView(this);
    }


    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_referral;
    }

    @Override
    protected void initView(View view) {
        presenter.initialize();
        referralContentTextView.setText(presenter.getReferralSubHeader());
        referralTitleTextview.setText(presenter.getReferralTitleDesc());
        if (presenter.isAppShowReferralButtonActivated()) {
            referralCodeLayout.setVisibility(View.VISIBLE);
            TextViewHelpLink.setVisibility(View.VISIBLE);
            TextViewHelpLink.setOnClickListener(view1 -> {
                focusOnView();
                UnifyTracking.eventReferralAndShare(getActivity(), AppEventTracking.Action.CLICK_HOW_IT_WORKS, "");

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
        initInjector();
        super.onCreate(savedInstanceState);
    }

    protected void initInjector() {
        ReferralComponent referralComponent = DaggerReferralComponent.builder()
                .referralModule(new ReferralModule())
                .appComponent(((BasePresenterActivity) getActivity()).getApplicationComponent())
                .build();
        referralComponent.inject(this);

    }

    @OnClick(R2.id.btn_copy_referral_code)
    public void clickOnCopyButton() {
        presenter.copyVoucherCode(referralCodeTextView.getText().toString());
        if (!TextUtils.isEmpty(referralCodeTextView.getText().toString())) {
            btnCopyReferralCode.setText(R.string.copied);
            btnCopyReferralCode.setTextColor(getResources().getColor(R.color.green_250));
            imgTick.setVisibility(View.VISIBLE);
        }
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
        Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext()).getLoginIntent(getActivity());
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    @Override
    public void showVerificationPhoneNumberPage() {
        if (getApplicationContext() instanceof OtpRouter) {
            OtpRouter otpRouter = (OtpRouter) getApplicationContext();
            startActivityForResult(otpRouter.getReferralPhoneNumberActivityIntent(getActivity()),
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
            if (!SessionHandler.isV4Login(getActivity())) {
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

}
