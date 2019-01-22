package com.tokopedia.updateinactivephone.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.activity.ChangeInactiveFormRequestActivity;
import com.tokopedia.updateinactivephone.activity.ChangeInactivePhoneRequestSubmittedActivity;
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventConstants;
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventTracking;
import com.tokopedia.updateinactivephone.presenter.ChangeInactivePhonePresenter;
import com.tokopedia.updateinactivephone.view.ChangeInactivePhone;
import com.tokopedia.updateinactivephone.di.DaggerUpdateInactivePhoneComponent;

import javax.inject.Inject;

import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.IS_DUPLICATE_REQUEST;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.OLD_PHONE;
import static com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.USER_ID;

public class ChangeInactivePhoneFragment extends BaseDaggerFragment implements ChangeInactivePhone.View {
    private EditText inputMobileNumber;
    private Button buttonContinue;
    private TextView errorText;
    private TextView phoneHintTextView;

    private TkpdProgressDialog tkpdProgressDialog;

    @Inject
    ChangeInactivePhonePresenter presenter;

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerUpdateInactivePhoneComponent daggerUpdateInactivePhoneComponent = (DaggerUpdateInactivePhoneComponent)
                DaggerUpdateInactivePhoneComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerUpdateInactivePhoneComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return UpdateInactivePhoneEventConstants.Screen.INPUT_OLD_PHONE_SCREEN;
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(MainApplication.getAppContext(),getScreenName());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_inactive_phone, parent, false);

        inputMobileNumber = view.findViewById(R.id.phone_number);
        buttonContinue = view.findViewById(R.id.button_continue);
        phoneHintTextView = view.findViewById(R.id.phone_hint_text_view);
        errorText = view.findViewById(R.id.error);
        presenter.attachView(this);
        prepareView();
        return view;
    }

    private void prepareView() {

        inputMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setErrorText("");
                if (charSequence.toString().length() == 0) {
                    buttonContinue.setEnabled(false);
                    buttonContinue.setClickable(false);
                    buttonContinue.setTextColor(getResources().getColor(R.color.black_26));
                } else {
                    buttonContinue.setEnabled(true);
                    buttonContinue.setClickable(true);
                    buttonContinue.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputMobileNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    handled = true;
                }
                return handled;
            }
        });

        buttonContinue.setOnClickListener(v -> {
            setErrorText("");
            presenter.checkPhoneNumberStatus(inputMobileNumber.getText().toString());
            hideKeyboard(v);
            UpdateInactivePhoneEventTracking.eventInactivePhoneClick(v.getContext());
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setErrorText(String text) {
        if (TextUtils.isEmpty(text)) {
            errorText.setVisibility(View.GONE);
            phoneHintTextView.setVisibility(View.VISIBLE);
        } else {
            errorText.setText(text);
            errorText.setVisibility(View.VISIBLE);
            phoneHintTextView.setVisibility(View.GONE);
        }
    }

    public static Fragment getInstance() {
        return new ChangeInactivePhoneFragment();
    }

    @Override
    public void showErrorPhoneNumber(int resId) {
        setErrorText(getString(resId));
    }

    @Override
    public void showErrorPhoneNumber(String errorMessage) {
        setErrorText(errorMessage);
    }

    @Override
    public void dismissLoading() {
        if (tkpdProgressDialog != null)
            tkpdProgressDialog.dismiss();
    }

    @Override
    public void showLoading() {
        if (tkpdProgressDialog == null)
            tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog
                    .NORMAL_PROGRESS);

        tkpdProgressDialog.showDialog();
    }

    @Override
    public void onForbidden() {

    }

    @Override
    public void onPhoneStatusSuccess(String userid) {
        setErrorText("");
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userid);
        bundle.putString(OLD_PHONE, inputMobileNumber.getText().toString());
        startActivity(ChangeInactiveFormRequestActivity.createIntent(getContext(), bundle));
    }

    @Override
    public void onPhoneRegisteredWithEmail() {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.registered_email_dialog_title));
        dialog.setDesc(getString(R.string.registered_email_dialog_message));
        dialog.setBtnOk(getString(R.string.drawer_title_login));
        dialog.setOnOkClickListener(v -> {
            UpdateInactivePhoneEventTracking.eventLoginDialogClick(v.getContext());
            RouteManager.route(getContext(), ApplinkConst.LOGIN);
        });
        dialog.setBtnCancel(getString(R.string.title_cancel));
        dialog.setOnCancelClickListener(v -> {
            UpdateInactivePhoneEventTracking.eventCancelDialogClick(v.getContext());
            dialog.dismiss();
        });
        dialog.show();

        dialog.getBtnCancel().setTextColor(getResources().getColor(R.color.black_54));
        dialog.getBtnOk().setTextColor(getResources().getColor(R.color.tkpd_main_green));
    }

    @Override
    public void onPhoneDuplicateRequest() {
        setErrorText("");
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_DUPLICATE_REQUEST, true);
        Intent intent = ChangeInactivePhoneRequestSubmittedActivity.createNewIntent(getContext(), bundle);
        startActivity(intent);
    }

    @Override
    public void onPhoneServerError() {
        setErrorText("");
    }

    @Override
    public void onPhoneBlackListed() {
        setErrorText(getString(R.string.phone_blacklisted));
    }

    @Override
    public void onPhoneInvalid() {
        setErrorText(getString(R.string.error_invalid_phone_number));
    }

    @Override
    public void onPhoneNotRegistered() {
        setErrorText(getString(R.string.phone_not_registered));
    }

    @Override
    public void onPhoneTooShort() {
        setErrorText(getString(R.string.phone_number_invalid_min_8));
    }

    @Override
    public void onPhoneTooLong() {
        setErrorText(getString(R.string.phone_number_invalid_max_15));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
