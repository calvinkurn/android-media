package com.tokopedia.instantloan.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.instantloan.InstantLoanComponentInstance;
import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.data.model.response.PhoneDataEntity;
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity;
import com.tokopedia.instantloan.di.component.InstantLoanComponent;
import com.tokopedia.instantloan.router.InstantLoanRouter;
import com.tokopedia.instantloan.view.activity.InstantLoanActivity;
import com.tokopedia.instantloan.view.adapter.InstantLoanIntroViewPagerAdapter;
import com.tokopedia.instantloan.view.contractor.InstantLoanContractor;
import com.tokopedia.instantloan.view.presenter.InstantLoanPresenter;

import javax.inject.Inject;

import static com.tokopedia.instantloan.network.InstantLoanUrl.WEB_LINK_OTP;
import static com.tokopedia.instantloan.view.activity.InstantLoanActivity.PINJAMAN_TITLE;


public class DanaInstantFragment extends BaseDaggerFragment implements InstantLoanContractor.View {

    public static final int LOGIN_REQUEST_CODE = 1005;
    private static final String TAB_POSITION = "tab_position";
    private final String SCREEN_NAME = "dana instan";

    private ProgressBar mProgressBar;
    private Dialog mDialogIntro;

    @Inject
    InstantLoanPresenter presenter;

    private int mCurrentTab;
    private int mCurrentPagePosition = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.attachView(this);
        mCurrentTab = getArguments().getInt(TAB_POSITION);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
    }

    @Override
    protected void initInjector() {
        InstantLoanComponent daggerInstantLoanComponent = InstantLoanComponentInstance
                .get(getActivity().getApplication());
        daggerInstantLoanComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_instant_loan_home_page, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {

        TextView mTextAmount = view.findViewById(R.id.text_value_amount);
        TextView mTextDuration = view.findViewById(R.id.text_value_duration);
        TextView mTextProcessingTime = view.findViewById(R.id.text_value_processing_time);
        TextView mTextInterestRate = view.findViewById(R.id.text_value_interest_rate);
        TextView mTextFormDescription = view.findViewById(R.id.text_form_description);
        mProgressBar = view.findViewById(R.id.progress_bar_status);

        mTextAmount.setText(getResources().getStringArray(R.array.values_amount)[mCurrentTab]);
        mTextDuration.setText(getResources().getStringArray(R.array.values_duration)[mCurrentTab]);
        mTextProcessingTime.setText(getResources().getStringArray(R.array.values_processing_time)[mCurrentTab]);
        mTextInterestRate.setText(getResources().getStringArray(R.array.values_interest_rate)[mCurrentTab]);
        mTextFormDescription.setText(getResources().getStringArray(R.array.values_description)[mCurrentTab]);

        view.findViewById(R.id.button_search_pinjaman).setOnClickListener(view1 -> searchLoanOnline());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Context getAppContext() {
        return getContext().getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return getContext();
    }

    @Override
    public void onSuccessLoanProfileStatus(UserProfileLoanEntity data) {

        if (!data.getWhitelist()) {
            if (!TextUtils.isEmpty(data.getWhiteListUrl())) {
                com.tkpd.library.utils.CommonUtils.dumper(data.getWhiteListUrl());
                openWebView(data.getWhiteListUrl());
            } else {
                com.tokopedia.core.network.NetworkErrorHelper.showSnackbar(getActivity(),
                        getResources().getString(R.string.instant_loan_coming_soon));
            }
        } else if (!data.getDataCollection() ||
                (data.getDataCollection() && data.getDataCollected())) {

            if (!TextUtils.isEmpty(data.getRedirectUrl())) {

                com.tkpd.library.utils.CommonUtils.dumper(data.getWhiteListUrl());
                openWebView(data.getRedirectUrl());

            } else {
                NetworkErrorHelper.showSnackbar(getActivity(),
                        getResources().getString(R.string.default_request_error_unknown));
            }

        } else {
            startIntroSlider();
        }

    }

    @Override
    public void onErrorLoanProfileStatus(String onErrorLoanProfileStatus) {
        hideLoader();
        NetworkErrorHelper.showSnackbar(getActivity(), onErrorLoanProfileStatus);
    }

    @Override
    public void onSuccessPhoneDataUploaded(PhoneDataEntity data) {
        hideLoaderIntroDialog();

        if (data.getMobileDeviceId() > 0) {
            openWebView(WEB_LINK_OTP);
            if (mDialogIntro != null && !getActivity().isFinishing()) {
                mDialogIntro.dismiss();
            }
        }
    }

    @Override
    public void onErrorPhoneDataUploaded(String errorMessage) {
        hideIntroDialog();
        hideLoaderIntroDialog();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void navigateToLoginPage() {
        Intent intent = ((InstantLoanRouter) MainApplication.getAppContext())
                .getLoginIntent(getContext());
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }


    @Override
    public void startIntroSlider() {
        View view = getLayoutInflater().inflate(R.layout.dialog_intro_instnat_loan, null);
        final ViewPager pager = view.findViewById(R.id.view_pager_il_intro);
        final CirclePageIndicator pageIndicator = view.findViewById(R.id.page_indicator_il_intro);
        final FloatingActionButton btnNext = view.findViewById(R.id.button_next);
        final int[] layouts = new int[]{
                R.layout.intro_instant_loan_slide_1,
                R.layout.intro_instant_loan_slide_2,
                R.layout.intro_instant_loan_slide_3,
        };

        pager.setAdapter(new InstantLoanIntroViewPagerAdapter(((InstantLoanActivity) getActivity()),
                layouts, presenter));
        pageIndicator.setFillColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
        pageIndicator.setPageColor(ContextCompat.getColor(getContext(), R.color.black_38));
        pageIndicator.setViewPager(pager, 0);
        btnNext.show();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == layouts.length - 1) {
                    pageIndicator.setVisibility(View.INVISIBLE);
                    btnNext.hide();
                } else {
                    pageIndicator.setVisibility(View.VISIBLE);
                    btnNext.show();
                }

                boolean right = mCurrentPagePosition < position;

                if (mCurrentPagePosition == 0 && right) {
                    sendIntroSliderScrollEvent(AppEventTracking.EventLabel.PL_INTRO_SLIDER_FIRST_NEXT);
                } else if (mCurrentPagePosition == 1 && !right) {
                    sendIntroSliderScrollEvent(AppEventTracking.EventLabel.PL_INTRO_SLIDER_SECOND_PREVIOUS);
                } else if (mCurrentPagePosition == 1) {
                    sendIntroSliderScrollEvent(AppEventTracking.EventLabel.PL_INTRO_SLIDER_SECOND_NEXT);
                } else if (mCurrentPagePosition == 2 && !right) {
                    sendIntroSliderScrollEvent(AppEventTracking.EventLabel.PL_INTRO_SLIDER_THIRD_PREVIOUS);
                }

                mCurrentPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(v -> {
            if (pager.getCurrentItem() != layouts.length) {

                int position = pager.getCurrentItem();

                if (position == 0) {
                    sendIntroSliderScrollEvent(AppEventTracking.EventLabel.PL_INTRO_SLIDER_FIRST_NEXT);
                } else if (position == 1) {
                    sendIntroSliderScrollEvent(AppEventTracking.EventLabel.PL_INTRO_SLIDER_SECOND_NEXT);
                }

                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            }
        });

        mDialogIntro = new Dialog(getContext());


        mDialogIntro.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogIntro.setContentView(view, new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialogIntro.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mDialogIntro.setCanceledOnTouchOutside(false);
        mDialogIntro.show();
        mDialogIntro.getWindow().setAttributes(lp);
    }

    private void sendIntroSliderScrollEvent(String label) {
        UnifyTracking.eventIntroSliderScrollEvent(label);
    }

    @Override
    public void showToastMessage(String message, int duration) {
        Toast.makeText(getContext(), message, duration).show();
    }

    @Override
    public void openWebView(String url) {
        Intent intent = SimpleWebViewWithFilePickerActivity.getIntentWithTitle(getContext(),
                url, PINJAMAN_TITLE);
        startActivity(intent);
    }

    @Override
    public void searchLoanOnline() {
        if (presenter.isUserLoggedIn()) {
            sendCariPinjamanClickEvent();
            presenter.getLoanProfileStatus();
        } else {
            navigateToLoginPage();
        }
    }

    @Override
    public void showLoader() {
        mProgressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideLoader() {
        mProgressBar.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void showLoaderIntroDialog() {
        if (mDialogIntro == null || getActivity().isFinishing()) {
            return;
        }

        mDialogIntro.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mDialogIntro.findViewById(R.id.view_pager_il_intro).findViewWithTag(2)
                .findViewById(R.id.progress_bar_status).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoaderIntroDialog() {
        if (mDialogIntro == null || getActivity().isFinishing()) {
            return;
        }

        mDialogIntro.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mDialogIntro.findViewById(R.id.view_pager_il_intro).findViewWithTag(2)
                .findViewById(R.id.progress_bar_status).setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideIntroDialog() {
        if (mDialogIntro == null || getActivity().isFinishing()) {
            return;
        }
        mDialogIntro.dismiss();
    }

    public String getScreenNameId() {
        return SCREEN_NAME;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (!SessionHandler.isV4Login(getContext())) {
                NetworkErrorHelper.showSnackbar(getActivity(),
                        getResources().getString(R.string.login_to_proceed));
            } else {
                presenter.getLoanProfileStatus();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return getScreenNameId();
    }

    private void sendCariPinjamanClickEvent() {
        String eventLabel = getScreenName();
        UnifyTracking.eventCariPinjamanClick(eventLabel);
    }

    public static DanaInstantFragment createInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, position);
        DanaInstantFragment danaInstantFragment = new DanaInstantFragment();
        danaInstantFragment.setArguments(args);
        return danaInstantFragment;
    }

}
