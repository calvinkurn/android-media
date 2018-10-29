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
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.instantloan.InstantLoanComponentInstance;
import com.tokopedia.instantloan.R;
import com.tokopedia.instantloan.common.analytics.InstantLoanAnalytics;
import com.tokopedia.instantloan.common.analytics.InstantLoanEventConstants;
import com.tokopedia.instantloan.data.model.response.PhoneDataEntity;
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity;
import com.tokopedia.instantloan.di.component.InstantLoanComponent;
import com.tokopedia.instantloan.router.InstantLoanRouter;
import com.tokopedia.instantloan.view.contractor.InstantLoanContractor;
import com.tokopedia.instantloan.view.presenter.InstantLoanPresenter;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import static com.tokopedia.instantloan.network.InstantLoanUrl.LOAN_AMOUNT_QUERY_PARAM;
import static com.tokopedia.instantloan.network.InstantLoanUrl.WEB_LINK_NO_COLLATERAL;
import static com.tokopedia.instantloan.view.fragment.DanaInstantFragment.LOGIN_REQUEST_CODE;


public class TanpaAgunanFragment extends BaseDaggerFragment implements InstantLoanContractor.View {

    private static final String TAB_POSITION = "tab_position";
    private Spinner mSpinnerLoanAmount;

    @Inject
    InstantLoanPresenter presenter;
    @Inject
    InstantLoanAnalytics instantLoanAnalytics;

    @Inject
    UserSession userSession;

    private int mCurrentTab;
    private Context context;

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
    protected void onAttachActivity(Context context) {
        this.context = context;
        super.onAttachActivity(context);
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

            if (mSpinnerLoanAmount.getSelectedItem().toString()
                    .equalsIgnoreCase(getString(R.string.label_select_nominal))) {
                TextView errorText = (TextView) mSpinnerLoanAmount.getSelectedView();
                errorText.setTextColor(Color.RED);
                return;
            }

            sendCariPinjamanClickEvent();
            openWebView(WEB_LINK_NO_COLLATERAL + LOAN_AMOUNT_QUERY_PARAM +
                    mSpinnerLoanAmount.getSelectedItem().toString()
                            .split(" ")[1].replace(".", ""));
        });
    }

    private void sendCariPinjamanClickEvent() {
        String eventLabel = getScreenName() + " - " + mSpinnerLoanAmount.getSelectedItem().toString();
        instantLoanAnalytics.eventCariPinjamanClick(eventLabel);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (userSession != null && userSession.isLoggedIn()) {
                showToastMessage(getResources().getString(R.string.login_to_proceed), Toast.LENGTH_SHORT);
            } else {
                openWebView(WEB_LINK_NO_COLLATERAL + LOAN_AMOUNT_QUERY_PARAM +
                        mSpinnerLoanAmount.getSelectedItem().toString().split(" ")[1]);
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
        return InstantLoanEventConstants.Screen.TANPA_AGUNAN_SCREEN_NAME;
    }

    public static TanpaAgunanFragment createInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(TAB_POSITION, position);
        TanpaAgunanFragment tanpaAgunanFragment = new TanpaAgunanFragment();
        tanpaAgunanFragment.setArguments(bundle);
        return tanpaAgunanFragment;
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
    public void setUserOnGoingLoanStatus(boolean status, int id) {

    }

    @Override
    public void onErrorLoanProfileStatus(String onErrorLoanProfileStatus) {

    }

    @Override
    public void onSuccessPhoneDataUploaded(PhoneDataEntity data) {

    }

    @Override
    public void onErrorPhoneDataUploaded(String errorMessage) {

    }

    @Override
    public void navigateToLoginPage() {
        if (getActivity() != null && getActivity().getApplication() instanceof InstantLoanRouter) {
            startActivityForResult(((InstantLoanRouter) getActivity().getApplication()).getLoginIntent(getContext()), LOGIN_REQUEST_CODE);
        }
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
        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url));
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
