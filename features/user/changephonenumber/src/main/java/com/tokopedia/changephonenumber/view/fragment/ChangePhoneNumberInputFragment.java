package com.tokopedia.changephonenumber.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.changephonenumber.ChangePhoneNumberInstance;
import com.tokopedia.changephonenumber.R;
import com.tokopedia.changephonenumber.analytics.ChangePhoneNumberAnalytics;
import com.tokopedia.changephonenumber.di.input.ChangePhoneNumberInputComponent;
import com.tokopedia.changephonenumber.di.input.ChangePhoneNumberInputModule;
import com.tokopedia.changephonenumber.di.input.DaggerChangePhoneNumberInputComponent;
import com.tokopedia.changephonenumber.view.customview.BottomSheetInfo;
import com.tokopedia.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;
import com.tokopedia.otp.verification.domain.data.OtpConstant;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.utils.phonenumber.PhoneNumberUtil;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by milhamj on 20/12/17.
 */

public class ChangePhoneNumberInputFragment extends BaseDaggerFragment implements
        ChangePhoneNumberInputFragmentListener.View {
    public static final String PARAM_PHONE_NUMBER = "phone_number";
    public static final String PARAM_WARNING_LIST = "warning_list";
    public static final String PARAM_EMAIL = "email";
    public static final int REQUEST_VERIFY_CODE = 1;
    public static final String SCREEN_CHANGE_PHONE_NUMBER_INPUT = "Change Number";
    private static final int OTP_CHANGE_PHONE_NUMBER = 20;
    private static final int OTP_REGISTER_PHONE_NUMBER = 116;

    @Inject
    ChangePhoneNumberInputFragmentListener.Presenter presenter;

    @Inject
    UserSession userSession;

    private RelativeLayout progressDialog;
    private TextView oldPhoneNumber;
    private EditText newPhoneNumber;
    private TextView nextButton;
    private String phoneNumber;
    private ArrayList<String> warningList;
    private String email;
    private BottomSheetInfo bottomSheetInfo;
    private TextWatcher phoneNumberTextWatcher;

    @Inject
    ChangePhoneNumberAnalytics changePhoneNumberAnalytics;

    public static ChangePhoneNumberInputFragment newInstance(String phoneNumber, String email,
                                                             ArrayList<String> warningList) {
        ChangePhoneNumberInputFragment fragment = new ChangePhoneNumberInputFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putStringArrayList(PARAM_WARNING_LIST, warningList);
        bundle.putString(PARAM_EMAIL, email);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_input_phone_number,
                container, false);
        presenter.attachView(this);
        initVar();
        initView(parentView);
        setViewListener();

        if (warningList != null) {
            if (warningList.size() > 0) {
                setHasOptionsMenu(true);
                createBottomSheetView();
            }
        }

        return parentView;
    }

    private void initVar() {
        phoneNumber = getArguments().getString(PARAM_PHONE_NUMBER);
        warningList = getArguments().getStringArrayList(PARAM_WARNING_LIST);
        email = getArguments().getString(PARAM_EMAIL);
    }

    private void initView(View view) {
        oldPhoneNumber = view.findViewById(R.id.old_phone_number_value);
        newPhoneNumber = view.findViewById(R.id.new_phone_number_value);
        nextButton = view.findViewById(R.id.next_button);
        progressDialog = (RelativeLayout) view.findViewById(R.id.loading_view);

        oldPhoneNumber.setText(PhoneNumberUtil.transform(phoneNumber));
    }

    private void setViewListener() {
        phoneNumberTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.onNewNumberTextChanged(editable.toString(), newPhoneNumber.getSelectionStart());
            }
        };
        newPhoneNumber.addTextChangedListener(phoneNumberTextWatcher);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePhoneNumberAnalytics.getEventChangePhoneNumberClickOnNext();
                presenter.validateNumber(cleanPhoneNumber(newPhoneNumber));
            }
        });
    }

    private void createBottomSheetView() {
        bottomSheetInfo = new BottomSheetInfo(getContext(), warningList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_change_phone_number_input, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            changePhoneNumberAnalytics.getEventChangePhoneNumberClickOnInfo();
            bottomSheetInfo.show();
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected String getScreenName() {
        return SCREEN_CHANGE_PHONE_NUMBER_INPUT;
    }

    @Override
    protected void initInjector() {
        ChangePhoneNumberInputComponent changePhoneNumberInputComponent =
                DaggerChangePhoneNumberInputComponent.builder()
                        .changePhoneNumberComponent(ChangePhoneNumberInstance
                                .getChangePhoneNumberComponent(this.getActivity().getApplication()))
                        .changePhoneNumberInputModule(new ChangePhoneNumberInputModule())
                        .build();
        changePhoneNumberInputComponent.inject(this);
    }

    @Override
    public void enableNextButton() {
        nextButton.setClickable(true);
        nextButton.setEnabled(true);
        nextButton.setBackground(MethodChecker.getDrawable(getContext(), com.tokopedia.resources.common.R.drawable
                .green_button_rounded));
        nextButton.setTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0));
    }

    @Override
    public void disableNextButton() {
        nextButton.setClickable(false);
        nextButton.setEnabled(false);
        nextButton.setBackground(MethodChecker.getDrawable(getContext(), com.tokopedia.resources.common.R.drawable
                .grey_button_rounded));
        nextButton.setTextColor(MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_20));
    }

    @Override
    public void correctPhoneNumber(String newNumber, int selection) {
        newPhoneNumber.removeTextChangedListener(phoneNumberTextWatcher);
        newPhoneNumber.setText(newNumber);
        newPhoneNumber.setSelection(selection);
        newPhoneNumber.addTextChangedListener(phoneNumberTextWatcher);
    }

    @Override
    public void showLoading() {
        progressDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoading() {
        progressDialog.setVisibility(View.GONE);
    }

    @Override
    public void onValidateNumberSuccess() {
        goToVerification();
    }

    @Override
    public void onValidateNumberError(String message) {
        showErrorSnackbar(message);
    }

    @Override
    public void onSubmitNumberSuccess() {
        userSession.setPhoneNumber(cleanPhoneNumber(newPhoneNumber));

        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onSubmitNumberError(String message) {
        showErrorSnackbar(message);
    }

    @SuppressWarnings("Range")
    private void showErrorSnackbar(String message) {
        if (message != null) {
            SnackbarManager.make(getActivity(),
                    message,
                    Snackbar.LENGTH_LONG)
                    .show();
        } else {
            NetworkErrorHelper.showSnackbar(getActivity());
        }
    }

    private void goToVerification() {
        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.COTP);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, cleanPhoneNumber(newPhoneNumber));
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_CHANGE_PHONE_NUMBER);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, OtpConstant.OtpMode.SMS);
        startActivityForResult(intent, REQUEST_VERIFY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VERIFY_CODE && resultCode == Activity.RESULT_OK) {
            presenter.submitNumber(cleanPhoneNumber(newPhoneNumber));
        }
    }

    private String cleanPhoneNumber(EditText newPhoneNumber) {
        String newPhoneNumberString = newPhoneNumber.getText().toString();
        return newPhoneNumberString.replace("-", "");
    }
}
