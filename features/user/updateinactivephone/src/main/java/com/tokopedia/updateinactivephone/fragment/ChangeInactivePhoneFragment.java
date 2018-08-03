package com.tokopedia.updateinactivephone.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.presenter.ChangeInactivePhonePresenter;
import com.tokopedia.updateinactivephone.view.ChangeInactivePhone;
import com.tokpedia.updateinactivephone.di.DaggerUpdateInactivePhoneComponent;

import javax.inject.Inject;

public class ChangeInactivePhoneFragment extends BaseDaggerFragment implements ChangeInactivePhone.View {
    private EditText inputMobileNumber;
    private Button buttonContinue;
    private TextView errorText;

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
        return "";
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_inactive_phone, parent, false);

        inputMobileNumber = view.findViewById(R.id.phone_number);
        buttonContinue = view.findViewById(R.id.button_continue);
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
//                    UnifyTracking.eventTracking(LoginPhoneNumberAnalytics.getLoginWithPhoneTracking());
//                    presenter.loginWithPhoneNumber(inputMobileNumber.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorText.setText("");
//                UnifyTracking.eventTracking(LoginPhoneNumberAnalytics.getLoginWithPhoneTracking());
//                presenter.loginWithPhoneNumber(inputMobileNumber.getText().toString());
                presenter.checkPhoneNumberStatus(inputMobileNumber.getText().toString());
            }
        });


    }

    public static Fragment getInstance() {
        return new ChangeInactivePhoneFragment();
    }

    @Override
    public void showErrorPhoneNumber(int resId) {

    }

    @Override
    public void showErrorPhoneNumber(String errorMessage) {

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
    public void onPhoneStatusSuccess() {
        errorText.setText("");
    }

    @Override
    public void onPhoneRegisteredWithEmail() {
        errorText.setText("");
    }

    @Override
    public void onPhoneDuplicateRequest() {
        errorText.setText("");
    }

    @Override
    public void onPhoneServerError() {
        errorText.setText("");
    }

    @Override
    public void onPhoneBlackListed() {
        errorText.setText("");
    }

    @Override
    public void onPhoneInvalid() {
        errorText.setText("");
    }

    @Override
    public void onPhoneNotRegistered() {
        errorText.setText("Nomor ponsel belum terdaftar.");
    }

    @Override
    public void onPhoneTooShort() {
        errorText.setText("");
    }

    @Override
    public void onPhoneTooLong() {
        errorText.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
