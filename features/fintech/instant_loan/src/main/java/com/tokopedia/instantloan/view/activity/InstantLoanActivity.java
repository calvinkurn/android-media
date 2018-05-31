package com.tokopedia.instantloan.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.instantloan.InstantLoanComponentInstance;
import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.ddcollector.DDCollectorManager;
import com.tokopedia.instantloan.di.component.InstantLoanComponent;
import com.tokopedia.instantloan.router.InstantLoanRouter;
import com.tokopedia.instantloan.util.CommonUtils;
import com.tokopedia.instantloan.view.adapter.BannerPagerAdapter;
import com.tokopedia.instantloan.view.adapter.InstantLoanIntroViewPagerAdapter;
import com.tokopedia.instantloan.view.contractor.BannerContractor;
import com.tokopedia.instantloan.view.contractor.InstantLoanContractor;
import com.tokopedia.instantloan.view.model.BannerViewModel;
import com.tokopedia.instantloan.view.model.LoanProfileStatusViewModel;
import com.tokopedia.instantloan.view.model.PhoneDataViewModel;
import com.tokopedia.instantloan.view.presenter.BannerListPresenter;
import com.tokopedia.instantloan.view.presenter.InstantLoanPresenter;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.instantloan.network.InstantLoanUrl.WEB_LINK_COLLATERAL_FUND;
import static com.tokopedia.instantloan.network.InstantLoanUrl.WEB_LINK_DASHBOARD;
import static com.tokopedia.instantloan.network.InstantLoanUrl.WEB_LINK_NO_COLLATERAL;
import static com.tokopedia.instantloan.network.InstantLoanUrl.WEB_LINK_OTP;

public class InstantLoanActivity extends BaseSimpleActivity implements HasComponent<AppComponent>, BannerContractor.View, InstantLoanContractor.View, View.OnClickListener {

    public static final int LOGIN_REQUEST_CODE = 1005;
    public static final int TAB_INSTANT_FUND = 0;
    public static final int TAB_NO_COLLATERAL = 1;
    public static final int TAB_WITH_COLLATERAL = 2;

    @Inject
    BannerListPresenter mBannerPresenter;
    @Inject
    InstantLoanPresenter mPresenter;
    private Spinner mSpinnerLoanAmount;
    private ViewPager mBannerPager;
    private FloatingActionButton mBtnNextBanner, mBtnPreviousBanner;
    private Dialog mDialogIntro;
    private Button mBtnInstantFund, mBtnNoCollateral, mBtnWithCollateral;
    private TextView mTextAmount, mTextDuration, mTextProcessingTime, mTextInterestRate;
    private TextView mTextFormDescription;
    private int mCurrentTab = -1;

    public static Intent createIntent(Context context) {
        return new Intent(context, InstantLoanActivity.class);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        initInjector();
        mBannerPresenter.attachView(this);
        mPresenter.attachView(this);
        initializeView();
        attachViewListener();
        setupToolbar();
        updateTabs(TAB_INSTANT_FUND);
        updateFormValues(TAB_INSTANT_FUND);
        setupLoanAmountSpinner();
        mBannerPresenter.loadBanners();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mBannerPresenter.detachView();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_instant_loan;
    }

    @Override
    public void renderUserList(List<BannerViewModel> banners) {
        if (!banners.isEmpty()) {
            if (banners.size() > 1) {
                ((FloatingActionButton) findViewById(R.id.button_next)).show();
            }
            findViewById(R.id.container_banner).setVisibility(View.VISIBLE);
            mBannerPager = findViewById(R.id.view_pager_banner);
            mBannerPager.setOffscreenPageLimit(2);
            mBannerPager.setAdapter(new BannerPagerAdapter(this, banners));
            mBannerPager.setPadding(getResources().getDimensionPixelOffset(R.dimen.il_margin_large), 0, getResources().getDimensionPixelOffset(R.dimen.il_margin_large), 0);
            mBannerPager.setClipToPadding(false);
            mBannerPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.il_margin_medium));
            mBannerPager.addOnPageChangeListener(mBannerPageChangeListener);
        }
    }

    @Override
    public void nextBanner() {
        if (mBannerPager == null
                || mBannerPager.getAdapter() == null
                || mBannerPager.getCurrentItem() == mBannerPager.getAdapter().getCount()) {
            return;
        }

        mBannerPager.setCurrentItem(mBannerPager.getCurrentItem() + 1, true); //+1 for move the page to next
    }

    @Override
    public void previousBanner() {
        if (mBannerPager == null
                || mBannerPager.getAdapter() == null
                || mBannerPager.getCurrentItem() == 0) {
            return;
        }

        mBannerPager.setCurrentItem(mBannerPager.getCurrentItem() - 1, true); //-1 for move the page to prev
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

        pager.setAdapter(new InstantLoanIntroViewPagerAdapter(this, layouts, mPresenter));
        //adding bottom dots(Page Indicator)
        pageIndicator.setFillColor(ContextCompat.getColor(getApplicationContext(), R.color.tkpd_main_green));
        pageIndicator.setPageColor(ContextCompat.getColor(getApplicationContext(), R.color.black_38));
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

        mDialogIntro = new Dialog(this);
        mDialogIntro.setCanceledOnTouchOutside(false);
        mDialogIntro.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogIntro.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mDialogIntro.show();
    }

    @Override
    public void showToastMessage(String message, int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    @Override
    public void openWebView(String url) {
        Intent intent = SimpleWebViewWithFilePickerActivity.getIntent(this, url);
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

        if (mPresenter.isUserLoggedIn()) {
            switch (mCurrentTab) {
                case TAB_INSTANT_FUND:
                    mPresenter.getLoanProfileStatus();
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
    }

    @Override
    public void showLoader() {
        findViewById(R.id.progress_bar_status).setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideLoader() {
        findViewById(R.id.progress_bar_status).setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void showLoaderIntroDialog() {
        if (mDialogIntro == null || isFinishing()) {
            return;
        }

        mDialogIntro.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mDialogIntro.findViewById(R.id.view_pager_il_intro).findViewWithTag(2).findViewById(R.id.progress_bar_status).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoaderIntroDialog() {
        if (mDialogIntro == null || isFinishing()) {
            return;
        }

        mDialogIntro.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mDialogIntro.findViewById(R.id.view_pager_il_intro).findViewWithTag(2).findViewById(R.id.progress_bar_status).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View source) {
        if (source.getId() == R.id.button_search_pinjaman) {
            searchLoanOnline();
        } else if (source.getId() == R.id.button_next) {
            nextBanner();
        } else if (source.getId() == R.id.button_previous) {
            previousBanner();
        } else if (source.getId() == R.id.card_instant_fund) {
            updateTabs(TAB_INSTANT_FUND);
            updateFormValues(mCurrentTab);
        } else if (source.getId() == R.id.card_no_collateral) {
            updateTabs(TAB_NO_COLLATERAL);
            updateFormValues(mCurrentTab);
        } else if (source.getId() == R.id.card_with_collateral) {
            updateTabs(TAB_WITH_COLLATERAL);
            updateFormValues(mCurrentTab);
        }
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void onSuccessLoanProfileStatus(LoanProfileStatusViewModel data) {
        //TODO @lavekush check possible error cases from @OKA
        if (!data.isSubmitted()) {
            openWebView(WEB_LINK_DASHBOARD);
        } else {
            startIntroSlider();
        }
    }

    @Override
    public void onErrorLoanProfileStatus(String errorMessage) {
        hideLoader();
        NetworkErrorHelper.showSnackbar(this, errorMessage);
        //TODO @lavekush add retry method if require ask from Vishal
    }

    @Override
    public void onSuccessPhoneDataUploaded(PhoneDataViewModel data) {
        //TODO @lavekush check possible error cases from @OKA
        hideLoaderIntroDialog();

        if (data.getMobileDeviceId() > 0) {
            openWebView(WEB_LINK_OTP);

            if (mDialogIntro != null && !isFinishing()) {
                mDialogIntro.dismiss();
            }
        }
    }

    @Override
    public void onErrorPhoneDataUploaded(String errorMessage) {
        hideLoaderIntroDialog();
        NetworkErrorHelper.showSnackbar(this, errorMessage);
        //TODO @lavekush add retry method if require ask from Vishal
    }

    @Override
    public void navigateToLoginPage() {
        Intent intent = ((InstantLoanRouter) MainApplication.getAppContext()).getLoginIntent(this);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    private void initInjector() {
        InstantLoanComponent daggerInstantLoanComponent = InstantLoanComponentInstance.get(getApplication());
        daggerInstantLoanComponent.inject(this);
    }

    private void initializeView() {
        mSpinnerLoanAmount = findViewById(R.id.spinner_value_nominal);
        mBtnNextBanner = findViewById(R.id.button_next);
        mBtnPreviousBanner = findViewById(R.id.button_previous);
        mBtnInstantFund = findViewById(R.id.button_instant_fund);
        mBtnNoCollateral = findViewById(R.id.button_no_collateral);
        mBtnWithCollateral = findViewById(R.id.button_with_collateral);
        mTextAmount = findViewById(R.id.text_value_amount);
        mTextDuration = findViewById(R.id.text_value_duration);
        mTextProcessingTime = findViewById(R.id.text_value_processing_time);
        mTextInterestRate = findViewById(R.id.text_value_interest_rate);
        mTextFormDescription = findViewById(R.id.text_form_description);
    }

    private void attachViewListener() {
        findViewById(R.id.button_search_pinjaman).setOnClickListener(this);
        findViewById(R.id.card_instant_fund).setOnClickListener(this);
        findViewById(R.id.card_no_collateral).setOnClickListener(this);
        findViewById(R.id.card_with_collateral).setOnClickListener(this);
        mBtnNextBanner.setOnClickListener(this);
        mBtnPreviousBanner.setOnClickListener(this);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(this.getTitle());
            updateTitle("");
        }
    }

    private void setupLoanAmountSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.values_amount_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerLoanAmount.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        DDCollectorManager.getsInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_REQUEST_CODE) {
            if (!SessionHandler.isV4Login(this)) {
                showToastMessage("Please login to access instant loan features", Toast.LENGTH_SHORT);
                finish();
            } else {
                mPresenter.getLoanProfileStatus();
            }
        }
    }

    private ViewPager.OnPageChangeListener mBannerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == mBannerPager.getAdapter().getCount() - 1) {
                mBtnNextBanner.hide();
                mBtnPreviousBanner.show();
            } else if (position == 0) {
                mBtnPreviousBanner.hide();
                mBtnNextBanner.show();
            } else {
                mBtnNextBanner.show();
                mBtnPreviousBanner.show();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void updateTabs(int whichTab) {
        if (mCurrentTab == whichTab) {
            return;
        }

        mCurrentTab = whichTab;

        if (whichTab == TAB_INSTANT_FUND) {
            findViewById(R.id.view_instant_fund).setVisibility(View.VISIBLE);
            findViewById(R.id.view_no_collateral).setVisibility(View.INVISIBLE);
            findViewById(R.id.view_with_collateral).setVisibility(View.INVISIBLE);
            mBtnInstantFund.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_green));
            mBtnNoCollateral.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black_38));
            mBtnWithCollateral.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black_38));
            CommonUtils.setTextViewDrawableColor(mBtnInstantFund, R.color.medium_green);
            CommonUtils.setTextViewDrawableColor(mBtnNoCollateral, R.color.black_38);
            CommonUtils.setTextViewDrawableColor(mBtnWithCollateral, R.color.black_38);
            mSpinnerLoanAmount.setVisibility(View.INVISIBLE);
        } else if (whichTab == TAB_NO_COLLATERAL) {
            findViewById(R.id.view_instant_fund).setVisibility(View.INVISIBLE);
            findViewById(R.id.view_no_collateral).setVisibility(View.VISIBLE);
            findViewById(R.id.view_with_collateral).setVisibility(View.INVISIBLE);
            mBtnInstantFund.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black_38));
            mBtnNoCollateral.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_green));
            mBtnWithCollateral.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black_38));
            CommonUtils.setTextViewDrawableColor(mBtnInstantFund, R.color.black_38);
            CommonUtils.setTextViewDrawableColor(mBtnNoCollateral, R.color.medium_green);
            CommonUtils.setTextViewDrawableColor(mBtnWithCollateral, R.color.black_38);
            mSpinnerLoanAmount.setVisibility(View.VISIBLE);
        } else if (whichTab == TAB_WITH_COLLATERAL) {
            findViewById(R.id.view_instant_fund).setVisibility(View.INVISIBLE);
            findViewById(R.id.view_no_collateral).setVisibility(View.INVISIBLE);
            findViewById(R.id.view_with_collateral).setVisibility(View.VISIBLE);
            mBtnInstantFund.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black_38));
            mBtnNoCollateral.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black_38));
            mBtnWithCollateral.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_green));
            CommonUtils.setTextViewDrawableColor(mBtnInstantFund, R.color.black_38);
            CommonUtils.setTextViewDrawableColor(mBtnNoCollateral, R.color.black_38);
            CommonUtils.setTextViewDrawableColor(mBtnWithCollateral, R.color.medium_green);
            mSpinnerLoanAmount.setVisibility(View.VISIBLE);
        }
    }

    private void updateFormValues(int whichTab) {
        if (whichTab == TAB_INSTANT_FUND) {
            findViewById(R.id.text_label_nominal).setVisibility(View.GONE);
            mSpinnerLoanAmount.setVisibility(View.GONE);
        } else if (whichTab == TAB_NO_COLLATERAL) {
            findViewById(R.id.text_label_nominal).setVisibility(View.VISIBLE);
            mSpinnerLoanAmount.setVisibility(View.VISIBLE);
        } else if (whichTab == TAB_WITH_COLLATERAL) {
            findViewById(R.id.text_label_nominal).setVisibility(View.VISIBLE);
            mSpinnerLoanAmount.setVisibility(View.VISIBLE);
        }

        mTextAmount.setText(getResources().getStringArray(R.array.values_amount)[mCurrentTab]);
        mTextDuration.setText(getResources().getStringArray(R.array.values_duration)[mCurrentTab]);
        mTextProcessingTime.setText(getResources().getStringArray(R.array.values_processing_time)[mCurrentTab]);
        mTextInterestRate.setText(getResources().getStringArray(R.array.values_interest_rate)[mCurrentTab]);
        mTextFormDescription.setText(getResources().getStringArray(R.array.values_description)[mCurrentTab]);
    }
}
