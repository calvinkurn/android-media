package com.tokopedia.core.session;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.TermPrivacy;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.home.ParentIndexHome;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.session.model.LoginModel;
import com.tokopedia.core.session.model.LoginViewModel;
import com.tokopedia.core.session.model.RegisterSuccessModel;
import com.tokopedia.core.session.model.RegisterViewModel;
import com.tokopedia.core.session.presenter.LoginImpl;
import com.tokopedia.core.session.presenter.RegisterImpl;
import com.tokopedia.core.session.presenter.RegisterNew;
import com.tokopedia.core.session.presenter.RegisterNewImpl;
import com.tokopedia.core.session.presenter.RegisterNewNextView;
import com.tokopedia.core.session.presenter.RegisterNextImpl;
import com.tokopedia.core.session.presenter.RegisterThird;
import com.tokopedia.core.session.presenter.RegisterThirdImpl;
import com.tokopedia.core.session.presenter.RegisterThirdView;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by m.normansyah on 1/27/16.
 */
public class RegisterThirdFragment extends BaseFragment<RegisterThird> implements RegisterThirdView{
    public static final String messageTAG = "RegisterThirdFragment : ";

    public static Fragment newInstance(RegisterViewModel registerViewModel, String type){
        RegisterThirdFragment registerThirdFragment = new RegisterThirdFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RegisterNew.DATA, Parcels.wrap(registerViewModel));
        bundle.putString(RegisterThird.REGISTER_TYPE, type);
        registerThirdFragment.setArguments(bundle);
        return registerThirdFragment;
    }

    @Bind(R2.id.user_name)
    EditText vName;
    @Bind(R2.id.date)
    TextView vBDay;
    @Bind(R2.id.password)
    EditText vPassword;
    @Bind(R2.id.password_retype)
    EditText vPasswordRetype;
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
    public void onClickTos(){
        TermPrivacy.start(getActivity(), TermPrivacy.T_AND_C);
    }

    @OnClick(R2.id.tos_privacy)
    public void onClickPrivacy(){
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
        CommonUtils.ShowError(getActivity(), new ArrayList<String>(){{add(getString(R.string.alert_email_address_is_already_registered));}});
        enView(false);
    }

    @Override
    protected void initPresenter() {
        presenter = new RegisterThirdImpl(this);
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
        Toast.makeText(getActivity(), getString(R.string.message_verification_timeout), Toast.LENGTH_LONG).show();
        showProgress(false);
    }

    @OnClick(R2.id.date)
    public void onDateClick(){
        //datePicker.show();
        int year = (int)presenter.getData(RegisterThird.DATE_YEAR);
        int month = (int)presenter.getData(RegisterThird.DATE_MONTH);
        int day = (int)presenter.getData(RegisterThird.DATE_DAY);

        DatePickerUtil datePicker = new DatePickerUtil(getActivity(), day, month, year);
        datePicker.SetMaxYear(2002);
        datePicker.SetMinYear(1934);
        datePicker.SetShowToday(false);
        datePicker.DatePickerCalendar((DatePickerUtil.onDateSelectedListener)presenter);
    }

    @Override
    public void setData(int type, Bundle data) {
        String email = (String)presenter.getData(RegisterThird.EMAIL);
        String password = vPassword.getText().toString();
        switch (type){
            case DownloadService.REGISTER_THIRD:
//                AnalyticsHandler.init(getActivity())
//                        .type(com.tokopedia.tkpd.analytics.Type.APPSFLYER).send("registration", "");

                TrackingUtils.fragmentBasedAFEvent(this);

                RegisterSuccessModel registerSuccessModel = Parcels.unwrap(data.getParcelable(DownloadService.REGISTER_MODEL_KEY));
                int tipe = data.getInt(DownloadServiceConstant.TYPE);
                switch (registerSuccessModel.getIsActive()){
                    case RegisterSuccessModel.ACTIVATION_PROCESS:
                        ((SessionView)getActivity()).moveToActivationResend(email);
                        break;
                    case RegisterSuccessModel.CREATE_PASSWORD_PROCESS:
                    case RegisterSuccessModel.GO_TO_HOME_PROCESS:
                        // [START] do some login here.
                        LoginViewModel loginViewModel = new LoginViewModel();
                        loginViewModel.setUsername(email);
                        loginViewModel.setPassword(password);
                        LoginImpl.login(DownloadService.REGISTER_THIRD_LOGIN, getActivity(), LoginModel.EmailType, loginViewModel);
                        //[END] do some login here.


                        // [START] send register event localytics
                        sendLocalyticsRegisterEvent(registerSuccessModel.getUserId());
                        sendGTMRegisterEvent();
                        //[START] Old code just move to home
//                        SessionHandler sessionHandler = new SessionHandler(getActivity());
//                        sessionHandler.SetLoginSession(true, registerSuccessModel.getUserId()+"","","");
//                        LocalyticsContainer handler = AnalyticsHandler.init(getActivity()).Localytics();
//                        handler.tagEvent(getString(R.string.event_register) + " with e-mail");
//                        CommonUtils.dumper("LocalTag : DEFAULT REGISTER");
//                        getActivity().startActivity(new Intent(getActivity(), ParentIndexHome.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                        ((AppCompatActivity) getActivity()).finish();
                        //[END] Old code just move to home
                        break;
                }
                break;
            case DownloadService.REGISTER_THIRD_LOGIN:
                if(new SessionHandler(getActivity()).isV4Login()) {// go back to home
                    TrackingUtils.eventLoca(getString(R.string.event_register) + " with e-mail");
                    CommonUtils.dumper("LocalTag : DEFAULT REGISTER");
                    getActivity().startActivity(new Intent(getActivity(), ParentIndexHome.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        String text = (String)data[0];
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String)data[0];
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveData();
        super.onSaveInstanceState(outState);
    }

    private void saveData() {
        if(vName!=null){
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.FULLNAME, vName.getText().toString()));
        }
        if(vBDay!=null){
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.BIRTHDAY, vBDay.getText().toString()));
        }
        if(vPassword!=null){
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.PASSWORD, vPassword.getText().toString()));
        }
        if(vPasswordRetype!=null){
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.CONFIRM_PASSWORD, vPasswordRetype.getText().toString()));
        }
        if(vPhoneNumber!=null){
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.PHONENUMBER, vPhoneNumber.getText().toString()));
        }
        if(vTos!=null){
            presenter.setData(RegisterNewImpl.convertToMap(RegisterThird.IS_CHECKED, vTos.isChecked()));
        }
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        if(data.containsKey(RegisterThird.FULLNAME)){
            String name = (String)data.get(RegisterThird.FULLNAME);
            vName.setText(name);
        }else if(data.containsKey(RegisterThird.GENDER)){
            Log.d(TAG, messageTAG+" gender : "+(int)data.get(RegisterThird.GENDER));
        }
        else if(data.containsKey(RegisterThird.EMAIL)){
            String email = (String)data.get(RegisterThird.EMAIL);
            Log.d(TAG, messageTAG+" email : "+email);
        }
        else if(data.containsKey(RegisterThird.BIRTHDAY)){
            String birthDay = (String)data.get(RegisterThird.BIRTHDAY);
            vBDay.setText(birthDay);
        }
        else if(data.containsKey(RegisterThird.PASSWORD)){
            String password = (String)data.get(RegisterThird.PASSWORD);
            vPassword.setText(password);
        }
        else if(data.containsKey(RegisterThird.CONFIRM_PASSWORD)){
            String confirmPassword = (String)data.get(RegisterThird.CONFIRM_PASSWORD);
            vPasswordRetype.setText(confirmPassword);
        }
        else if(data.containsKey(RegisterThird.IS_CHECKED)){
            boolean isChecked = (boolean)data.get(RegisterThird.IS_CHECKED);
            vTos.setChecked(isChecked);
        }else if(data.containsKey(RegisterThird.PHONENUMBER)){
            String phoneNumber = (String)data.get(RegisterThird.PHONENUMBER);
            vPhoneNumber.setText(phoneNumber);
        }

    }

    @Override
    public boolean checkValidation() {
        vName.setError(null);
        vPassword.setError(null);
        vPhoneNumber.setError(null);
        vPasswordRetype.setError(null);
        vError.setVisibility(View.GONE);
        if(RegisterImpl.RegisterUtil.checkRegexNameLocal(vName.getText().toString())) {
            vName.setError(getString(R.string.error_illegal_character));
            vName.requestFocus();
            return false;
        }
        if(vName.length() > 35) {
            vName.setError(getString(R.string.error_max_35_character));
            vName.requestFocus();
            return false;
        }
        if(vPassword.length() == 0){
            vPassword.setError(getText(R.string.error_field_required));
            vPassword.requestFocus();
            return false;
        }
        if(vPasswordRetype.length() == 0){
            vPasswordRetype.setError(getText(R.string.error_field_required));
            vPasswordRetype.requestFocus();
            return false;
        }
        if(!vPasswordRetype.getText().toString().equals(vPassword.getText().toString())){
            vPasswordRetype.setError(getText(R.string.error_password_not_same));
            vPasswordRetype.requestFocus();
            return false;
        }

        //[BUGFIX] AN-1429 [Register] Error message should appear for invalid Telephone field.
        String mPhone = vPhoneNumber.getText().toString();
        boolean validatePhoneNumber = RegisterNextImpl.validatePhoneNumber(mPhone);
        Log.e(RegisterNewNextView.TAG, messageTAG + " valid nomornya : " + validatePhoneNumber);
        RegisterNextImpl.testPhoneNumberValidation();
        if(vPhoneNumber.length() == 0){
            vPhoneNumber.setError(getText(R.string.error_field_required));
            vPhoneNumber.requestFocus();
            return false;
        }else if(!validatePhoneNumber){
            vPhoneNumber.setError(getString(R.string.error_invalid_phone_number));
            vPhoneNumber.requestFocus();
            return false;
        }

        //[BUGFIX] AN-1429 [Register] Error message should appear for invalid Telephone field.
        if(!vTos.isChecked()){
            vError.setVisibility(View.VISIBLE);
            vError.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void showProgress(boolean showDialog) {
        if(showDialog) {
            mProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
            mProgressDialog.showDialog();
        }else{
            if(mProgressDialog!=null){// &&mProgressDialog.isProgress()
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void enView(boolean enStatus) {
        if(enStatus){// jika enable semuanya bisa di click dan di edit
            Log.d(TAG, messageTAG+"enable all views");
        }else{// jika disable semuanya tidak bisa diclick dan tidak bisa di edit
            Log.d(TAG, messageTAG+"disable all views");
        }
        vName.setEnabled(enStatus);
        vPassword.setEnabled(enStatus);
        vPhoneNumber.setEnabled(enStatus);
        vPasswordRetype.setEnabled(enStatus);
        vTos.setEnabled(enStatus);
        vBDay.setEnabled(enStatus);
    }

    @OnClick(R2.id.send_button)
    public void sendRegister(){
        if(checkValidation()){
            //[START] update data here
            saveData();
            //[END] update data here
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

    }

    private void sendLocalyticsRegisterEvent(int userId){
        Map<String, String> attributes = new HashMap<String, String>();
        ActivityManager mngr = (ActivityManager) getActivity()
                .getSystemService( getActivity().ACTIVITY_SERVICE );

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(2);
        if (!taskList.isEmpty()){
            if (taskList.get(0).numActivities> 0){
                attributes.put("Previous Screen", taskList.get(0).topActivity.getShortClassName());
            }
        }
        CustomerWrapper customerRegister = new CustomerWrapper();
        customerRegister.setCustomerId(Integer.toString(userId));
        customerRegister.setFullName((String)presenter.getData(RegisterThird.FULLNAME));
        customerRegister.setEmailAddress((String)presenter.getData(RegisterThird.EMAIL));
        customerRegister.setExtraAttr(attributes);
        customerRegister.setMethod((String)presenter.getData(RegisterThird.REGISTER_TYPE));
        UnifyTracking.eventRegisterLoca(customerRegister);
    }
    private void sendGTMRegisterEvent(){
        UnifyTracking.eventRegisterSuccess((String)presenter.getData(RegisterThird.REGISTER_TYPE));
    }
}
