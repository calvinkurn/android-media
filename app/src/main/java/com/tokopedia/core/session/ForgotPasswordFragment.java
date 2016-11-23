package com.tokopedia.core.session;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.session.presenter.ForgotPassword;
import com.tokopedia.core.session.presenter.ForgotPasswordImpl;
import com.tokopedia.core.session.presenter.ForgotPasswordView;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.var.TkpdState;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author m.normansyah
 * @since 18/11/2015
 * @version 2
 */
public class ForgotPasswordFragment extends TkpdBaseV4Fragment implements ForgotPasswordView{

    @Bind(R2.id.front_view)
     View FrontView;
    @Bind(R2.id.success_view)
     View SuccessView;
    @Bind(R2.id.email_send)
    TextView EmailSend;
    @Bind(R2.id.send_button)
     TextView SendButton;
    @Bind(R2.id.email)
    EditText Email;
    @Bind(R2.id.til_email)
    TextInputLayout tilEmail;

    ForgotPassword forgotPassword;
    TkpdProgressDialog progressDialog;
    boolean isProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotPassword = new ForgotPasswordImpl(this);
        forgotPassword.fetchDataAfterRotate(savedInstanceState);
        forgotPassword.initDataInstances(getActivity());
        setProgressDialog();
    }

    private void setProgressDialog() {
        if(isProgressDialog)
        {
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
            progressDialog.showDialog();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_forgot_password, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_FORGOT_PASSWORD;
    }

    @Override
    public void onResume() {
        super.onResume();
        setLocalyticFlow();
        ScreenTracking.screen(getScreenName());
        forgotPassword.subscribe();
        forgotPassword.initData(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        forgotPassword.unSubscribe();
    }

    @OnClick(R2.id.send_button)
    public void onSendButtonClick(){
        Email.setError(null);
        if(Email.length() > 0)
            //SendRequest();
            if(CommonUtils.EmailValidation(Email.getText().toString())) {
                KeyboardHandler.DropKeyboard(getActivity(),Email);
                forgotPassword.resetPassword(Email.getText().toString());
                showProgressDialog();
                tilEmail.setErrorEnabled(false);
                tilEmail.setError(null);
                UnifyTracking.eventForgotPassword();
            }else {
                tilEmail.setErrorEnabled(true);
                tilEmail.setError(getString(R.string.error_invalid_email));
            }
        else {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError(getString(R.string.error_field_required));
        }
    }

    public void showProgressDialog() {
        if(progressDialog == null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        progressDialog.setCancelable(false);
        progressDialog.showDialog();
        isProgressDialog = true;
    }

    public void dismissProgressDialog(){
        if (isProgressDialog && progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public boolean checkIsLoading() {
        if(progressDialog != null && progressDialog.isProgress()){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void setIsProgressDialog(boolean aBoolean) {
        isProgressDialog = aBoolean;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KeyboardHandler.DropKeyboard(getActivity(), Email);
        ButterKnife.unbind(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        forgotPassword.saveDataBeforeRotate(outState);
    }

    @Override
    public void setLocalyticFlow() {
        CommonUtils.dumper("LocalTag : Forgot Password");
        String screenName = getString(R.string.forgot_password_page);
        ScreenTracking.screenLoca(screenName);
    }

    @Override
    public void displaySuccessView(boolean isSuccess) {
        if(isSuccess)
            SuccessView.setVisibility(View.VISIBLE);
        else
            SuccessView.setVisibility(View.GONE);

        KeyboardHandler.DropKeyboard(getActivity(),Email);
    }

    @Override
    public void displayFrontView(boolean isFrontView) {
        if(isFrontView)
            FrontView.setVisibility(View.VISIBLE);
        else
            FrontView.setVisibility(View.GONE);
    }

    @Override
    public void setTextEmailSend(String email) {
//        EmailSend.setText(Email.getText().toString());
        String myData = getString(R.string.title_reset_success_hint_1) + "\n"
                            + email + ".\n"
                            + getString(R.string.title_reset_success_hint_2);

        EmailSend.setText(myData);
    }

    @Override
    public void moveToRegister(String email) {
        if(getActivity() instanceof SessionView){
            ((SessionView)getActivity()).moveToRegister();
        }
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.FORGOT_PASSWORD;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        throw new RuntimeException("don't call this method !!");
    }

    @Override
    public void setData(int type, Bundle data) {
        forgotPassword.setData(type,data);
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        throw new RuntimeException("don't call this method !!");
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String) data[0];
        forgotPassword.setError(type, text);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        forgotPassword.unSubscribe();
    }
}
