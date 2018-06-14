package com.tokopedia.instantloan.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity;
import com.tokopedia.instantloan.router.InstantLoanRouter;
import com.tokopedia.instantloan.view.activity.InstantLoanActivity;
import com.tokopedia.instantloan.view.adapter.InstantLoanIntroViewPagerAdapter;
import com.tokopedia.instantloan.view.contractor.InstantLoanContractor;
import com.tokopedia.instantloan.view.model.PhoneDataViewModel;
import com.tokopedia.instantloan.view.presenter.InstantLoanPresenter;

import javax.inject.Inject;

import static com.tokopedia.instantloan.network.InstantLoanUrl.WEB_LINK_COLLATERAL_FUND;
import static com.tokopedia.instantloan.network.InstantLoanUrl.WEB_LINK_NO_COLLATERAL;
import static com.tokopedia.instantloan.network.InstantLoanUrl.WEB_LINK_OTP;

/**
 * Created by sachinbansal on 6/12/18.
 */

public class DanaInstantFragment extends BaseDaggerFragment implements InstantLoanContractor.View {

    public static final int LOGIN_REQUEST_CODE = 1005;

    public static final int TAB_INSTANT_FUND = 0;
    public static final int TAB_NO_COLLATERAL = 1;
    public static final int TAB_WITH_COLLATERAL = 2;

    private ProgressBar mProgressBar;
    private Spinner mSpinnerLoanAmount;
    private Dialog mDialogIntro;

    @Inject
    InstantLoanPresenter presenter;

    private int mCurrentTab = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            loadDataFromSavedState(savedInstanceState);
        } else {
            loadDataFromArguments();
        }
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
    }

    private void loadDataFromSavedState(Bundle savedInstanceState) {
    }

    private void loadDataFromArguments() {
    }


    @Override
    protected void initInjector() {
        /*InstantLoanComponent daggerInstantLoanComponent = InstantLoanComponentInstance.get(getActivity().getApplication());
        daggerInstantLoanComponent.inject(this);*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        presenter.attachView(this, this);
        return inflater.inflate(R.layout.content_instant_loan_home_page, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setupLoanAmountSpinner();
    }

    private void setupLoanAmountSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.values_amount_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerLoanAmount.setAdapter(adapter);
    }

    private void initView(View view) {

        mSpinnerLoanAmount = view.findViewById(R.id.spinner_value_nominal);
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

        view.findViewById(R.id.button_search_pinjaman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchLoanOnline();
            }
        });
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
//TODO @lavekush check possible error cases from @OKA

        if (!data.getWhitelist()) {
            // TODO: 6/5/18 open coming soon web view

            if (!TextUtils.isEmpty(data.getWhiteListUrl())) {
                // TODO: 6/6/18 open webview activity

//                Toast.makeText(this, data.getWhiteListUrl(), Toast.LENGTH_SHORT).show();
                com.tkpd.library.utils.CommonUtils.dumper(data.getWhiteListUrl());
                openWebView(data.getWhiteListUrl());

            } else {
                Toast.makeText(getContext(), "Instant Loan Coming Soon", Toast.LENGTH_SHORT).show();
            }
        } else if (!data.getDataCollection() ||
                (data.getDataCollection() && data.getDataCollected())) {

            if (!TextUtils.isEmpty(data.getRedirectUrl())) {
                // TODO: 6/6/18 open webview activity

                Toast.makeText(getContext(), data.getRedirectUrl(), Toast.LENGTH_SHORT).show();
                com.tkpd.library.utils.CommonUtils.dumper(data.getWhiteListUrl());
                openWebView(data.getRedirectUrl());

            } else {
                Toast.makeText(getContext(), "Check if fintech profile is build", Toast.LENGTH_SHORT).show();
            }

        } else {
            startIntroSlider();
        }
    }

    @Override
    public void onErrorLoanProfileStatus(String onErrorLoanProfileStatus) {
        hideLoader();
        NetworkErrorHelper.showSnackbar(getActivity(), onErrorLoanProfileStatus);
        //TODO @lavekush add retry method if require ask from Vishal
    }

    @Override
    public void onSuccessPhoneDataUploaded(PhoneDataViewModel data) {
        //TODO @lavekush check possible error cases from @OKA
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
        hideLoaderIntroDialog();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        //TODO @lavekush add retry method if require ask from Vishal
    }

    @Override
    public void navigateToLoginPage() {
        Intent intent = ((InstantLoanRouter) MainApplication.getAppContext()).getLoginIntent(getContext());
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }


    @Override
    public void startIntroSlider() {
        // Pass null as the parent view because its going in the dialog layout
        View view = getLayoutInflater().inflate(R.layout.dialog_intro_instnat_loan, null);
        final ViewPager pager = view.findViewById(R.id.view_pager_il_intro);
        final CirclePageIndicator pageIndicator = view.findViewById(R.id.page_indicator_il_intro);
        final FloatingActionButton btnNext = view.findViewById(R.id.button_next);
        // layouts of all intro sliders
        final int[] layouts = new int[]{
                R.layout.intro_instant_loan_slide_1,
                R.layout.intro_instant_loan_slide_2,
                R.layout.intro_instant_loan_slide_3,
        };

        pager.setAdapter(new InstantLoanIntroViewPagerAdapter(((InstantLoanActivity) getActivity()), layouts, presenter));
        //adding bottom dots(Page Indicator)
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() != layouts.length) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                }
            }
        });

        mDialogIntro = new Dialog(getContext());


        mDialogIntro.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogIntro.setContentView(view);//, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialogIntro.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mDialogIntro.setCanceledOnTouchOutside(false);
        mDialogIntro.show();
        mDialogIntro.getWindow().setAttributes(lp);
    }

    @Override
    public void showToastMessage(String message, int duration) {
        Toast.makeText(getContext(), message, duration).show();
    }

    @Override
    public void openWebView(String url) {
        Intent intent = SimpleWebViewWithFilePickerActivity.getIntentWithTitle(getContext(), url, "Pinjaman Online");
        startActivity(intent);
    }

    @Override
    public void searchLoanOnline() {

        if (mCurrentTab != TAB_INSTANT_FUND
                && mSpinnerLoanAmount.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.label_select_nominal))) {
            TextView errorText = (TextView) mSpinnerLoanAmount.getSelectedView();
            errorText.setError("Please select");
            errorText.setTextColor(Color.RED);
            return;
        }

        if (presenter.isUserLoggedIn()) {
            switch (mCurrentTab) {
                case TAB_INSTANT_FUND:
                    presenter.getLoanProfileStatus();
                    startIntroSlider();
                    break;
                case TAB_NO_COLLATERAL:
                    openWebView(WEB_LINK_NO_COLLATERAL + mSpinnerLoanAmount.getSelectedItem().toString().split(" ")[1]);
                    break;
                case TAB_WITH_COLLATERAL:
                    openWebView(WEB_LINK_COLLATERAL_FUND + mSpinnerLoanAmount.getSelectedItem().toString().split(" ")[1]);
                    break;
            }
        } else {
            navigateToLoginPage();
        }

//        startIntroSlider();
    }

    @Override
    public void showLoader() {
        mProgressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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

        mDialogIntro.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mDialogIntro.findViewById(R.id.view_pager_il_intro).findViewWithTag(2).findViewById(R.id.progress_bar_status).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoaderIntroDialog() {
        if (mDialogIntro == null || getActivity().isFinishing()) {
            return;
        }

        mDialogIntro.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mDialogIntro.findViewById(R.id.view_pager_il_intro).findViewWithTag(2).findViewById(R.id.progress_bar_status).setVisibility(View.INVISIBLE);
    }

    public String getScreenNameId() {
        return "Dana Instan";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (!SessionHandler.isV4Login(getContext())) {
                showToastMessage("Please login to access instant loan features", Toast.LENGTH_SHORT);
//                getActivity().finish();
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

    public static DanaInstantFragment createInstance() {
        return new DanaInstantFragment();
    }

}
