package com.tokopedia.instantloan.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.instantloan.InstantLoanComponentInstance;
import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity;
import com.tokopedia.instantloan.di.component.InstantLoanComponent;
import com.tokopedia.instantloan.router.InstantLoanRouter;
import com.tokopedia.instantloan.view.contractor.InstantLoanContractor;
import com.tokopedia.instantloan.view.model.PhoneDataViewModel;
import com.tokopedia.instantloan.view.presenter.InstantLoanPresenter;

import javax.inject.Inject;

import static com.tokopedia.instantloan.network.InstantLoanUrl.LOAN_AMOUNT_QUERY_PARAM;
import static com.tokopedia.instantloan.network.InstantLoanUrl.WEB_LINK_NO_COLLATERAL;
import static com.tokopedia.instantloan.view.fragment.DanaInstantFragment.LOGIN_REQUEST_CODE;


public class TanpaAgunanFragment extends BaseDaggerFragment implements InstantLoanContractor.View {

    private Spinner mSpinnerLoanAmount;

    private SessionHandler sessionHandler;
    private GCMHandler gcmHandler;


    @Inject
    InstantLoanPresenter presenter;

    private int mCurrentTab = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionHandler = new SessionHandler(getContext());
        gcmHandler = new GCMHandler(getContext());
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
    }

    @Override
    protected void initInjector() {
        InstantLoanComponent daggerInstantLoanComponent = InstantLoanComponentInstance.get(getActivity().getApplication());
        daggerInstantLoanComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        Button mBtnInstantFund = view.findViewById(R.id.button_instant_fund);
        TextView mTextAmount = view.findViewById(R.id.text_value_amount);
        TextView mTextDuration = view.findViewById(R.id.text_value_duration);
        TextView mTextProcessingTime = view.findViewById(R.id.text_value_processing_time);
        TextView mTextInterestRate = view.findViewById(R.id.text_value_interest_rate);
        TextView mTextFormDescription = view.findViewById(R.id.text_form_description);

        mTextAmount.setText(getResources().getStringArray(R.array.values_amount)[mCurrentTab]);
        mTextDuration.setText(getResources().getStringArray(R.array.values_duration)[mCurrentTab]);
        mTextProcessingTime.setText(getResources().getStringArray(R.array.values_processing_time)[mCurrentTab]);
        mTextInterestRate.setText(getResources().getStringArray(R.array.values_interest_rate)[mCurrentTab]);
        mTextFormDescription.setText(getResources().getStringArray(R.array.values_description)[mCurrentTab]);

        view.findViewById(R.id.button_search_pinjaman).setOnClickListener(view1 -> {

            if (mSpinnerLoanAmount.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.label_select_nominal))) {
                TextView errorText = (TextView) mSpinnerLoanAmount.getSelectedView();
                errorText.setError("Please select");
                errorText.setTextColor(Color.RED);
                return;
            }

            if (sessionHandler.isV4Login()) {
                openWebView(WEB_LINK_NO_COLLATERAL + LOAN_AMOUNT_QUERY_PARAM +
                        mSpinnerLoanAmount.getSelectedItem().toString().split(" ")[1].replace(".", ""));
            } else {
                navigateToLoginPage();
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public String getScreenNameId() {
        return "Tanpa Agunan";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (!SessionHandler.isV4Login(getContext())) {
                showToastMessage("Please login to access instant loan features", Toast.LENGTH_SHORT);
//                getActivity().finish();
            } else {
                openWebView(WEB_LINK_NO_COLLATERAL + LOAN_AMOUNT_QUERY_PARAM + mSpinnerLoanAmount.getSelectedItem().toString().split(" ")[1]);
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

    public static TanpaAgunanFragment createInstance() {
        return new TanpaAgunanFragment();
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
    public void onSuccessLoanProfileStatus(UserProfileLoanEntity status) {

    }

    @Override
    public void onErrorLoanProfileStatus(String onErrorLoanProfileStatus) {

    }

    @Override
    public void onSuccessPhoneDataUploaded(PhoneDataViewModel data) {

    }

    @Override
    public void onErrorPhoneDataUploaded(String errorMessage) {

    }

    @Override
    public void navigateToLoginPage() {
        Intent intent = ((InstantLoanRouter) MainApplication.getAppContext()).getLoginIntent(getContext());
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    @Override
    public void startIntroSlider() {

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

    }

    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showLoaderIntroDialog() {

    }

    @Override
    public void hideLoaderIntroDialog() {

    }

    @Override
    public void hideIntroDialog() {

    }
}
