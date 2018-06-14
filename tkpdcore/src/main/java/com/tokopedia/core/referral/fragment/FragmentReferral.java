package com.tokopedia.core.referral.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.BasePresenterFragmentV4;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.referral.HeightWrappingViewPager;
import com.tokopedia.core.referral.adapter.ReferralGuidePagerAdapter;
import com.tokopedia.core.referral.di.DaggerReferralComponent;
import com.tokopedia.core.referral.di.ReferralComponent;
import com.tokopedia.core.referral.di.ReferralModule;
import com.tokopedia.core.referral.listener.ReferralView;
import com.tokopedia.core.referral.model.ShareApps;
import com.tokopedia.core.referral.presenter.IReferralPresenter;
import com.tokopedia.core.referral.presenter.ReferralPresenter;
import com.tokopedia.core.router.OtpRouter;
import com.tokopedia.core.util.SessionHandler;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public class FragmentReferral extends BasePresenterFragmentV4<IReferralPresenter> implements ReferralView {

    @Inject
    ReferralPresenter presenter;

    //    @BindView(R2.id.btn_app_share)
//    TextView appShareButton;
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
        //  appShareButton.setOnClickListener(getButtonAppShareClickListner());
        referralContentTextView.setText(presenter.getReferralContents());
        if (presenter.isAppShowReferralButtonActivated()) {
            referralCodeLayout.setVisibility(View.VISIBLE);
            TextViewHelpLink.setVisibility(View.VISIBLE);
           // TextViewHelpLink.setText(presenter.getHowItWorks());
            renderVoucherCode(presenter.getVoucherCodeFromCache());
            TextViewHelpLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_HOW_IT_WORKS, "");
//                    startActivity(ManageWebViewActivity.getCallingIntent(getActivity(), TkpdUrl.REFERRAL_URL, ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().toString()));

                    focusOnView();
                }
            });
        } else {
            referralCodeLayout.setVisibility(View.INVISIBLE);
            TextViewHelpLink.setVisibility(View.INVISIBLE);
        }

        referralGuidePagerAdapter = new ReferralGuidePagerAdapter(getActivity());
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
                presenter.shareApp(getChildFragmentManager());
                if (presenter.isAppShowReferralButtonActivated()) {
                    UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_SHARE_CODE, getReferralCodeFromTextView());
                } else {
                    UnifyTracking.eventAppShareWhenReferralOff(AppEventTracking.Action.CLICK, AppEventTracking.EventLabel.APP_SHARE_LABEL);
                }
            }
        };

    }

    @Override
    public void renderVoucherCode(String voucherCode) {
        referralCodeTextView.setText(voucherCode);
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
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getReferralVoucherCode();
            }
        }).showRetrySnackbar();
    }

    @Override
    public void renderSharableApps(ShareApps shareApps, int index) {
        if(index == 0){
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

    private final void focusOnView(){
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.fling(0);
                nestedScrollView.smoothScrollTo(0, viewLine.getBottom());
                //nestedScrollView.smoothScrollBy(0, tabGuide.getTop());
            }
        });
    }
}
