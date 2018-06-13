package com.tokopedia.instantloan.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.instantloan.InstantLoanComponentInstance;
import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.di.component.InstantLoanComponent;
import com.tokopedia.instantloan.view.presenter.InstantLoanPresenter;

import javax.inject.Inject;

/**
 * Created by sachinbansal on 6/12/18.
 */

public class TanpaAgunanFragment extends BaseDaggerFragment {

    public static final int REQUEST_CODE_LOGIN = 561;
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123;
    private static final int REQUEST_ACTIVITY_SORT_PRODUCT = 1233;
    private static final int REQUEST_ACTIVITY_FILTER_PRODUCT = 4320;


    private Spinner mSpinnerLoanAmount;
    private ViewPager mBannerPager;
    private FloatingActionButton mBtnNextBanner, mBtnPreviousBanner;
    private Dialog mDialogIntro;

    private TextView mTextAmount, mTextDuration, mTextProcessingTime, mTextInterestRate;
    private Button mBtnInstantFund;

    private TextView mTextFormDescription;

    private SessionHandler sessionHandler;
    private GCMHandler gcmHandler;


    @Inject
    InstantLoanPresenter presenter;
    private int mCurrentTab = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            loadDataFromSavedState(savedInstanceState);
        } else {
            loadDataFromArguments();
        }
        sessionHandler = new SessionHandler(getContext());
        gcmHandler = new GCMHandler(getContext());
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


//    protected void initInjector() {
//        SearchComponent component = DaggerSearchComponent.builder()
//                .appComponent(getComponent(AppComponent.class))
//                .build();
//        component.inject(this);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        presenter.attachView(this, this);
        return inflater.inflate(R.layout.content_tanpa_agunan, null);
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
        mBtnInstantFund = view.findViewById(R.id.button_instant_fund);
        mTextAmount = view.findViewById(R.id.text_value_amount);
        mTextDuration = view.findViewById(R.id.text_value_duration);
        mTextProcessingTime = view.findViewById(R.id.text_value_processing_time);
        mTextInterestRate = view.findViewById(R.id.text_value_interest_rate);
        mTextFormDescription = view.findViewById(R.id.text_form_description);

        mTextAmount.setText(getResources().getStringArray(R.array.values_amount)[mCurrentTab]);
        mTextDuration.setText(getResources().getStringArray(R.array.values_duration)[mCurrentTab]);
        mTextProcessingTime.setText(getResources().getStringArray(R.array.values_processing_time)[mCurrentTab]);
        mTextInterestRate.setText(getResources().getStringArray(R.array.values_interest_rate)[mCurrentTab]);
        mTextFormDescription.setText(getResources().getStringArray(R.array.values_description)[mCurrentTab]);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void showLoader() {
//        findViewById(R.id.progress_bar_status).setVisibility(View.VISIBLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private String generateUserId() {
        return sessionHandler.isV4Login() ? sessionHandler.getLoginID() : null;
    }

    private String generateUniqueId() {
        return sessionHandler.isV4Login() ?
                AuthUtil.md5(sessionHandler.getLoginID()) :
                AuthUtil.md5(gcmHandler.getRegistrationId());
    }

    public String getScreenNameId() {
        return "Tanpa Agunan";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return getScreenNameId();
    }

    public static TanpaAgunanFragment createInstance() {
        return new TanpaAgunanFragment();
    }
}
