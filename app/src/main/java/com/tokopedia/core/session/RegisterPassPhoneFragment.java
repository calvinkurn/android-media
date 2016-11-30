package com.tokopedia.core.session;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.TermPrivacy;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.session.model.CreatePasswordModel;
import com.tokopedia.core.session.model.LoginViewModel;
import com.tokopedia.core.session.presenter.RegisterImpl;
import com.tokopedia.core.session.presenter.RegisterNew;
import com.tokopedia.core.session.presenter.RegisterNewImpl;
import com.tokopedia.core.session.presenter.RegisterNewNextView;
import com.tokopedia.core.session.presenter.RegisterNextImpl;
import com.tokopedia.core.session.presenter.RegisterPassPhoneImpl;
import com.tokopedia.core.session.presenter.RegisterThird;
import com.tokopedia.core.session.presenter.RegisterThirdView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Nisie on 3/28/16.
 */
public class RegisterPassPhoneFragment extends BaseFragment<RegisterThird> implements RegisterThirdView {
    public static final String messageTAG = "RegisterPassPhoneFragment : ";
    public static final String DEFAULT_GENDER = "1";
    private List<String> allowedFieldList;

    public static Fragment newInstance(CreatePasswordModel createPasswordModel, List<String> createPasswordList) {
        RegisterPassPhoneFragment registerPassPhoneFragment = new RegisterPassPhoneFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RegisterNew.DATA, Parcels.wrap(createPasswordModel));
        registerPassPhoneFragment.setArguments(bundle);
        registerPassPhoneFragment.allowedFieldList = createPasswordList;
        return registerPassPhoneFragment;
    }

    @Bind(R2.id.user_name)
    EditText vName;
    @Bind(R2.id.date)
    TextView vBDay;
    @Bind(R2.id.password)
    PasswordView vPassword;
    @Bind(R2.id.password_retype)
    PasswordView vPasswordRetype;
    @Bind(R2.id.phone_number)
    EditText vPhoneNumber;
    @Bind(R2.id.send_button)
    View vSendButton;
    @Bind(R2.id.tos_check)
    CheckBox vTos;
    @Bind(R2.id.error)
    View vError;
    @Bind(R2.id.tos_tos)
    TextView termAndCond;
    @Bind(R2.id.tos_privacy)
    TextView privacy;

    TkpdProgressDialog mProgressDialog;
    DatePickerDialog datePicker;
    DatePicker dp;

    DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            vBDay.setText(RegisterImpl.RegisterUtil.formatDateText(
                    dayOfMonth,
                    monthOfYear,
                    year
            ));
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.DATE_YEAR, year));
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.DATE_MONTH, monthOfYear));
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.DATE_DAY, dayOfMonth));
        }
    };

    @OnClick(R2.id.tos_tos)
    public void onClickTos() {
        TermPrivacy.start(getActivity(), TermPrivacy.T_AND_C);
    }

    @OnClick(R2.id.tos_privacy)
    public void onClickPrivacy() {
        TermPrivacy.start(getActivity(), TermPrivacy.P_P);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showUnknownError(final Throwable e) {
        CommonUtils.ShowError(getActivity(), new ArrayList<String>() {{
            add("unknown error : " + e.getLocalizedMessage());
        }});
        enView(false);
    }

    @Override
    public void showErrorValidateEmail() {
        CommonUtils.ShowError(getActivity(), new ArrayList<String>() {{
            add(getString(R.string.alert_email_address_is_already_registered));
        }});
        enView(false);
    }

    @Override
    protected void initPresenter() {
        presenter = new RegisterPassPhoneImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_third;
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.REGISTER_THIRD;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        SnackbarManager.make(getActivity(), getString(R.string.message_verification_timeout), Snackbar.LENGTH_LONG).show();
        showProgress(false);
    }

    @OnClick(R2.id.date)
    public void onDateClick() {
        //datePicker.show();
        int year = (int) presenter.getData(RegisterThird.DATE_YEAR);
        int month = (int) presenter.getData(RegisterThird.DATE_MONTH);
        int day = (int) presenter.getData(RegisterThird.DATE_DAY);

        if(year==0) year=2002;
        DatePickerUtil datePicker = new DatePickerUtil(getActivity(), day, month, year);
        datePicker.SetMaxYear(2002);
        datePicker.SetMinYear(1934);
        datePicker.SetShowToday(false);
        datePicker.DatePickerCalendar((DatePickerUtil.onDateSelectedListener) presenter);
    }

    @Override
    public void setData(int type, Bundle data) {
        String email = (String) presenter.getData(RegisterThird.EMAIL);
        String password = vPassword.getText().toString();
        switch (type) {
            case DownloadService.REGISTER_PASS_PHONE:
                LoginViewModel loginViewModel = new LoginViewModel();
                loginViewModel.setUsername(email);
                loginViewModel.setPassword(password);

                presenter.makeLogin(getActivity(),data);

//                LoginImpl.login(DownloadService.REGISTER_THIRD_LOGIN, getActivity(), LoginModel.EmailType, loginViewModel);
                break;
            case DownloadService.MAKE_LOGIN:
            case DownloadService.REGISTER_THIRD_LOGIN:
                showProgress(false);
                if (new SessionHandler(getActivity()).isV4Login()) {// go back to home
                    getActivity().startActivity(new Intent(getActivity(), HomeRouter.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().finish();
                }
                break;
        }

    }

    @Override
    public void onNetworkError(int type, Object... data) {
        String text = (String) data[0];
        SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String) data[0];
        SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveData();
        super.onSaveInstanceState(outState);
    }

    private void saveData() {
        if (vName != null) {
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.FULLNAME, vName.getText().toString()));
        }
        if (vBDay != null) {
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.BIRTHDAY, vBDay.getText().toString()));
        }
        if (vPassword != null) {
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.PASSWORD, vPassword.getText().toString()));
        }
        if (vPasswordRetype != null) {
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.CONFIRM_PASSWORD, vPasswordRetype.getText().toString()));
        }
        if (vPhoneNumber != null) {
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.PHONENUMBER, vPhoneNumber.getText().toString()));
        }
        if (vTos != null) {
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.IS_CHECKED, vTos.isChecked()));
        }
        presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.GENDER, DEFAULT_GENDER));
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        if (data.containsKey(RegisterThird.FULLNAME)) {
            String name = (String) data.get(RegisterThird.FULLNAME);
            vName.setText(name);
        } else if (data.containsKey(RegisterThird.GENDER)) {
            Log.d(TAG, messageTAG + " gender : " + (int) data.get(RegisterThird.GENDER));
        } else if (data.containsKey(RegisterThird.EMAIL)) {
            String email = (String) data.get(RegisterThird.EMAIL);
            Log.d(TAG, messageTAG + " email : " + email);
        } else if (data.containsKey(RegisterThird.BIRTHDAY)) {
            String birthDay = (String) data.get(RegisterThird.BIRTHDAY);
            vBDay.setText(birthDay);
            if(birthDay.equals("1 / 1 / 0")){
                vBDay.setText("Tanggal / Bulan / Tahun");
            }
        } else if (data.containsKey(RegisterThird.PASSWORD)) {
            String password = (String) data.get(RegisterThird.PASSWORD);
            vPassword.setText(password);
        } else if (data.containsKey(RegisterThird.CONFIRM_PASSWORD)) {
            String confirmPassword = (String) data.get(RegisterThird.CONFIRM_PASSWORD);
            vPasswordRetype.setText(confirmPassword);
        } else if (data.containsKey(RegisterThird.IS_CHECKED)) {
            boolean isChecked = "1".equals(data.get(RegisterThird.IS_CHECKED));
            vTos.setChecked(isChecked);
        } else if (data.containsKey(RegisterThird.PHONENUMBER)) {
            String phoneNumber = (String) data.get(RegisterThird.PHONENUMBER);
            vPhoneNumber.setText(phoneNumber);
        }


    }

    private void makeItEnabled(TextView tv, boolean status){
        tv.setEnabled(status);
        if(status){
            try {
                tv.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.new_edittext_white));
            }catch (NoSuchMethodError e){
                tv.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.new_edittext_white));
            }

        }else{
            tv.setBackgroundColor(Color.parseColor("#EEEEEE"));
        }
    }

    @Override
    public boolean checkValidation() {
        vName.setError(null);
        vPassword.setError(null);
        vPhoneNumber.setError(null);
        vPasswordRetype.setError(null);
        vBDay.setError(null);
        vError.setVisibility(View.GONE);
        if (vName.length() == 0 && vName.isEnabled()) {
            vName.setError(getText(R.string.error_field_required));
            vName.requestFocus();
            sendGTMRegisterError(AppEventTracking.EventLabel.FULLNAME);
            return false;
        }
        if (RegisterImpl.RegisterUtil.checkRegexNameLocal(vName.getText().toString())&& vName.isEnabled()) {
            vName.setError(getString(R.string.error_illegal_character));
            vName.requestFocus();
            sendGTMRegisterError(AppEventTracking.EventLabel.FULLNAME);
            return false;
        }
        if (vName.length() > 35  && vName.isEnabled()) {
            vName.setError(getString(R.string.error_max_35_character));
            vName.requestFocus();
            sendGTMRegisterError(AppEventTracking.EventLabel.FULLNAME);
            return false;
        }
        if (vPassword.length() == 0 && vPassword.isEnabled()) {
            vPassword.setError(getText(R.string.error_field_required));
            vPassword.requestFocus();
            sendGTMRegisterError(AppEventTracking.EventLabel.PASSWORD);
            return false;
        }
        if (vPasswordRetype.length() == 0 && vPasswordRetype.getVisibility() == View.VISIBLE) {
            vPasswordRetype.setError(getText(R.string.error_field_required));
            vPasswordRetype.requestFocus();
            sendGTMRegisterError(AppEventTracking.EventLabel.PASSWORD_CONFIRMATION);
            return false;
        }
        if (!vPasswordRetype.getText().toString().equals(vPassword.getText().toString())
                && vPasswordRetype.getVisibility() == View.VISIBLE) {
            vPasswordRetype.setError(getText(R.string.error_password_not_same));
            vPasswordRetype.requestFocus();
            sendGTMRegisterError(AppEventTracking.EventLabel.PASSWORD_CONFIRMATION);
            return false;
        }
        if(vBDay.getText().equals("Tanggal / Bulan / Tahun")  && vBDay.isEnabled()){
            vBDay.setError(getText(R.string.error_field_required));
            vBDay.requestFocus();
            sendGTMRegisterError(AppEventTracking.EventLabel.BIRTHDATE);
            return false;
        }

        String mPhone = vPhoneNumber.getText().toString();
        boolean validatePhoneNumber = RegisterNextImpl.validatePhoneNumber(mPhone);
        Log.e(RegisterNewNextView.TAG, messageTAG + " valid nomornya : " + validatePhoneNumber);
        RegisterNextImpl.testPhoneNumberValidation();
        if (vPhoneNumber.length() == 0) {
            vPhoneNumber.setError(getText(R.string.error_field_required));
            vPhoneNumber.requestFocus();
            sendGTMRegisterError(AppEventTracking.EventLabel.HANDPHONE);
            return false;
        } else if (!validatePhoneNumber) {
            vPhoneNumber.setError(getString(R.string.error_invalid_phone_number));
            vPhoneNumber.requestFocus();
            sendGTMRegisterError(AppEventTracking.EventLabel.HANDPHONE);
            return false;
        }

        if (!vTos.isChecked()) {
            vError.setVisibility(View.VISIBLE);
            vError.requestFocus();
            sendGTMRegisterError(AppEventTracking.EventLabel.TOS);
            return false;
        }
        return true;
    }

    @Override
    public void showProgress(boolean showDialog) {
        if (showDialog) {
            mProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
            mProgressDialog.showDialog();
        } else {
            if (mProgressDialog != null) {// &&mProgressDialog.isProgress()
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void enView(boolean enStatus) {
        if (enStatus) {// jika enable semuanya bisa di click dan di edit
            Log.d(TAG, messageTAG + "enable all views");
        } else {// jika disable semuanya tidak bisa diclick dan tidak bisa di edit
            Log.d(TAG, messageTAG + "disable all views");
        }
        vName.setEnabled(enStatus);
        vPassword.setEnabled(enStatus);
        vPhoneNumber.setEnabled(enStatus);
        vPasswordRetype.setEnabled(enStatus);
        vTos.setEnabled(enStatus);
        vBDay.setEnabled(enStatus);
    }

    @OnClick(R2.id.send_button)
    public void sendRegister() {
        if (checkValidation()) {
            saveData();
            presenter.register(getActivity());
        }
    }

    @Override
    public void initDatePickerDialog(Context context, int year, int monthOfYear, int dayOfMonth) {
        datePicker = new DatePickerDialog(getActivity(), callBack, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void initDatePicker(long maxtime, long mintime) {
        dp = datePicker.getDatePicker();
        dp.setMaxDate(maxtime);
        dp.setMinDate(mintime);
    }

    @Override
    public void setAllowedField() {
        if(allowedFieldList!=null){
            makeItEnabled(vName,false);
            makeItEnabled(vPassword,false);
            makeItEnabled(vPhoneNumber, false);
            vPasswordRetype.setVisibility(View.GONE);
            for(String key : allowedFieldList){
                switch (key){
                    case "password":
                        makeItEnabled(vPassword, true);
                        vPasswordRetype.setVisibility(View.VISIBLE);
                        float scale = getResources().getDisplayMetrics().density;
                        int dpAsPixels = (int) (5*scale + 0.5f);
                        vPassword.setPadding(0,0,dpAsPixels,0);
                        vPassword.setText("");
                        vPasswordRetype.setText("");
                        break;
                    case "name":
                        makeItEnabled(vName, true);
                        vName.requestFocus();
                        vName.setText("");
                        break;
                    case "phone":
                        makeItEnabled(vPhoneNumber,true);

                        break;
                    default:
                        break;

                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(this);
    }

    private void sendGTMRegisterError(String label){
        UnifyTracking.eventRegisterError(label);
    }
}
