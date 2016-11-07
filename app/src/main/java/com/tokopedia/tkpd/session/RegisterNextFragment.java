package com.tokopedia.tkpd.session;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.session.presenter.RegisterNext;
import com.tokopedia.tkpd.session.presenter.RegisterNextImpl;
import com.tokopedia.tkpd.session.presenter.RegisterNextView;
import com.tokopedia.tkpd.service.DownloadService;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.var.TkpdState;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * @author m.normansyah
 * @since 13-11-2015
 * This is step two of register
 */
@Deprecated
public class RegisterNextFragment extends Fragment implements RegisterNextView{
    @Bind(R2.id.email)
    EditText mEmailView;
    @Bind(R2.id.password)
    EditText mPasswordView;
    @Bind(R2.id.confirm_password)
    EditText mConfirmPasswordView;
    @Bind(R2.id.checkbox_privacy_policy)
    CheckBox cbTermPrivacy;
    @Bind(R2.id.text_detail_term_privacy)
    TextView detailTermPrivacy;
    @Bind(R2.id.sign_in_button)
    TextView RegisterBut;
    @Bind(R2.id.login_status)
    LinearLayout mLoginStatusView;
    @Bind(R2.id.step_2)
    LinearLayout mLoginFormView;

    RegisterNext registerNext;

    public static RegisterNextFragment newInstance(String fullName, String phoneNum, int gender, String birthDayTTL, String email){
        RegisterNextFragment fragment = new RegisterNextFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FULLNAME,fullName);
        bundle.putString(PHONENUMBER, phoneNum);
        bundle.putInt(GENDER, gender);
        bundle.putString(BIRTHDAYTTL, birthDayTTL);
        if(email!=null)
            bundle.putString(EMAIL,email);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RegisterNextFragment newInstance(String fullName, String phoneNum, int gender, String birthDayTTL){
        return newInstance(fullName, phoneNum, gender, birthDayTTL, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerNext = new RegisterNextImpl(this);
        registerNext.fetchArguments(getArguments());
        registerNext.fetchRotationData(savedInstanceState);
        registerNext.fetchFromPreference(getActivity());
        registerNext.initDataInstance(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_register_2,container,false);
        ButterKnife.bind(this, parent);
        return parent;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerNext.initData(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(isVisible()) {
            if (cbTermPrivacy != null) {
                registerNext.updateData(RegisterNext.CHECKED_T_AND_COND, cbTermPrivacy.isChecked());
            }
            if (mPasswordView != null) {
                registerNext.updateData(RegisterNext.PASSWORD_POS, mPasswordView.getText().toString());
            }
            if (mConfirmPasswordView != null) {
                registerNext.updateData(RegisterNext.CONFIRM_PASSWORD_POS, mConfirmPasswordView.getText().toString());
            }
            if (mEmailView != null) {
                registerNext.updateData(RegisterNext.EMAIL_POS, mEmailView.getText().toString());
            }
        }
        registerNext.saveDataBeforeRotation(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        SessionHandler.saveRegisterNext(getActivity(), cbTermPrivacy.isChecked(),
                mPasswordView.getText().toString(), mConfirmPasswordView.getText().toString(),
                mEmailView.getText().toString()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    @Override
    public void setEmailText(String text) {
        mEmailView.setText(text);
    }

    @Override
    public void setPasswordText(String text) {
        mPasswordView.setText(text);
    }

    @Override
    public void setConfirmPasswordText(String text) {
        mConfirmPasswordView.setText(text);
    }

    @Override
    public void toggleAgreement(boolean agree) {
        cbTermPrivacy.setChecked(agree);
    }

    @Override
    public void showMessageError(List<String> MessageError) {
        throw new RuntimeException("don't use this method !!!");
    }

    @Override
    public void attemptRegister() {
        // Reset errors.
        mEmailView.setError(null);
        mConfirmPasswordView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String mEmail = mEmailView.getText().toString();
        String mPassword = mPasswordView.getText().toString();
        String mConfirmPassword = mConfirmPasswordView.getText().toString();
        boolean isChecked = cbTermPrivacy.isChecked();

        boolean cancel = false;
        View focusView = null;

        registerNext.updateData(RegisterNext.EMAIL_POS, mEmail);
        registerNext.updateData(RegisterNext.PASSWORD_POS, mPassword);
        registerNext.updateData(RegisterNext.CONFIRM_PASSWORD_POS, mConfirmPassword);
        registerNext.updateData(RegisterNext.CHECKED_T_AND_COND, isChecked);

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        if (TextUtils.isEmpty(mConfirmPassword)) {
            mConfirmPasswordView.setError(getString(R.string.error_field_required));
            focusView = mConfirmPasswordView;
            cancel = true;
        } else if (mConfirmPassword.length() < 4) {
            mConfirmPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mConfirmPasswordView;
            cancel = true;
        } else if (!mPassword.equals(mConfirmPassword)) {
            mConfirmPasswordView.setError(getString(R.string.error_password_not_same));
            focusView = mConfirmPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!CommonUtils.EmailValidation(mEmail)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if(!cbTermPrivacy.isChecked()) {
            detailTermPrivacy.setError(getString(R.string.error_must_checked_term_privacy));
            focusView = cbTermPrivacy;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
            registerNext.registerToServer(getActivity());
        }
    }

    @OnClick(R2.id.text_detail_term_privacy)
    public void onTextDetailTermClick(){
        if(cbTermPrivacy.isChecked()) {
            cbTermPrivacy.setChecked(false);
        } else {
            cbTermPrivacy.setChecked(true);
        }
    }



    @OnEditorAction(R2.id.password)
    public boolean onPasswordEdit(KeyEvent key, int id){
//        Toast.makeText(getActivity(), "Pressed: " + key, Toast.LENGTH_SHORT).show();
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
            attemptRegister();
            return true;
        }
        return false;
    }



    @OnEditorAction(R2.id.confirm_password)
    public boolean onConfirmPassEdit(KeyEvent key, int id){
//        Toast.makeText(getActivity(), "Pressed: " + key, Toast.LENGTH_SHORT).show();
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
            attemptRegister();
            return true;
        }
        return false;
    }



    @OnClick(R2.id.sign_in_button)
    public void onRegisterClick(View view){
        attemptRegister();
    }


    @Override
    public void setTermPrivacyText(String text) {
        detailTermPrivacy.setText(Html.fromHtml(text));
        detailTermPrivacy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @Override
    public void showProgress(final boolean show) {
        final WeakReference<LinearLayout> loginstatus = new WeakReference<LinearLayout>(mLoginStatusView);
        final WeakReference<LinearLayout> loginForm = new WeakReference<LinearLayout>(mLoginFormView);
        registerNext.updateData(RegisterNext.IS_LOADING, show);
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(loginstatus.get()!=null)
                                loginstatus.get().setVisibility(show ? View.VISIBLE
                                        : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(loginForm.get()!=null)
                                loginForm.get().setVisibility(show ? View.GONE
                                        : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.REGISTER_NEXT;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        Toast.makeText(getActivity(), getString(R.string.message_verification_timeout), Toast.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String)data[0];
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        String text = (String)data[0];
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void setData(int type, Bundle data) {
        if(type == DownloadService.REGISTER && registerNext != null){
            registerNext.setData(type, data);
        }
    }
}
