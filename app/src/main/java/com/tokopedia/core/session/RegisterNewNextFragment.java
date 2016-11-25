package com.tokopedia.core.session;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.home.ParentIndexHome;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.session.model.LoginModel;
import com.tokopedia.core.session.model.LoginViewModel;
import com.tokopedia.core.session.model.RegisterSuccessModel;
import com.tokopedia.core.session.model.RegisterViewModel;
import com.tokopedia.core.session.presenter.LoginImpl;
import com.tokopedia.core.session.presenter.RegisterImpl;
import com.tokopedia.core.session.presenter.RegisterNewNext;
import com.tokopedia.core.session.presenter.RegisterNewNextImpl;
import com.tokopedia.core.session.presenter.RegisterNewNextView;
import com.tokopedia.core.session.presenter.RegisterNextImpl;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by m.normansyah on 1/25/16.
 * modified by m.normansyah on 2/10/16 AN-1382
 */
public class RegisterNewNextFragment extends BaseFragment<RegisterNewNext> implements RegisterNewNextView{

    DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            registerNextDate.setText(RegisterImpl.RegisterUtil.formatDateText(
                    dayOfMonth,
                    monthOfYear,
                    year
            ));
            presenter.updateData(RegisterNewNext.DATE_YEAR, year);
            presenter.updateData(RegisterNewNext.DATE_MONTH, monthOfYear);
            presenter.updateData(RegisterNewNext.DATE_DAY, dayOfMonth);
        }
    };

    public static final String TAG = RegisterNewNextFragment.class.getSimpleName();

    DatePickerDialog datePicker;
    DatePicker dp;

    public static Fragment newInstance(String email, String password, boolean isAutoVerify){
        RegisterNewNextFragment fragment = new RegisterNewNextFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EMAIL, email);
        bundle.putString(PASSWORD, password);
        bundle.putBoolean(IS_AUTO_VERIFY, isAutoVerify);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Deprecated
    public static Fragment newInstance(String email, String password){
        return newInstance(email, password, false);
    }

    @Bind(R2.id.register_next_status)
    LinearLayout registerNextStatus;
    @Bind(R2.id.register_next_status_message)
    TextView registerNextStatusMessage;
    @Bind(R2.id.register_next_step_2)
    LinearLayout registerNextStep2;
    @Bind(R2.id.register_next_full_name)
    EditText registerNextFullName;
    @Bind(R2.id.register_next_phone_number)
    EditText registerNextPhoneNumber;
    @Bind(R2.id.register_next_male)
    RadioButton registerNextMale;
    @Bind(R2.id.register_next_female)
    RadioButton registerNextFemale;
    @Bind(R2.id.register_next_date)
    TextView registerNextDate;
    @Bind(R2.id.register_finish_button)
    TextView registerFinish;
    @Bind(R2.id.register_next_detail_t_and_p)
    TextView registerNextTAndC;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(registerNextFullName!=null){
            presenter.updateData(RegisterNewNext.FULLNAME, registerNextFullName.getText().toString());
        }
        if(registerNextPhoneNumber!=null){
            presenter.updateData(RegisterNewNext.PHONT, registerNextPhoneNumber.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.saveBeforeDestroy(getActivity(), registerNextFullName.getText().toString(), registerNextPhoneNumber.getText().toString());
    }

    @Override
    protected void initPresenter() {
        presenter = new RegisterNewNextImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_4;
    }

    @OnClick(R2.id.register_finish_button)
    public void registerFinish(){
        String mName = registerNextFullName.getText().toString();
        String mPhone = registerNextPhoneNumber.getText().toString();
        String mBirthDay = registerNextDate.getText().toString();

        View focusView = null;
        boolean cancel = false;

        registerNextFullName.setError(null);
        registerNextPhoneNumber.setError(null);

        if(TextUtils.isEmpty(mName)){
            registerNextFullName.setError(getString(R.string.error_field_required));
            focusView = registerNextFullName;
            cancel = true;
            sendGTMRegisterError(AppEventTracking.EventLabel.FULLNAME);
        } else if(RegisterImpl.RegisterUtil.checkRegexNameLocal(mName)
                || RegisterImpl.RegisterUtil.isExceedMaxCharacter(mName) ){
            registerNextFullName.setError(getString(R.string.error_illegal_character));
            focusView = registerNextFullName;
            cancel = true;
            sendGTMRegisterError(AppEventTracking.EventLabel.FULLNAME);
        }

        if(TextUtils.isEmpty(mPhone)){
            registerNextPhoneNumber.setError(getString(R.string.error_field_required));
            focusView = registerNextPhoneNumber;
            cancel = true;
            sendGTMRegisterError(AppEventTracking.EventLabel.HANDPHONE);
        }else {
            boolean validatePhoneNumber = RegisterNextImpl.validatePhoneNumber(mPhone);
            Log.e(RegisterNewNextView.TAG, messageTAG + " valid nomornya : " + validatePhoneNumber);
            RegisterNextImpl.testPhoneNumberValidation();
            if(!validatePhoneNumber){
                registerNextPhoneNumber.setError(getString(R.string.error_invalid_phone_number));
                focusView = registerNextPhoneNumber;
                cancel = true;
                sendGTMRegisterError(AppEventTracking.EventLabel.HANDPHONE);
            }
        }

        if(!registerNextMale.isChecked() && !registerNextFemale.isChecked()){
            SnackbarManager.make(getActivity(), getString(R.string.message_need_to_select_gender), Snackbar.LENGTH_SHORT).show();
            sendGTMRegisterError(AppEventTracking.EventLabel.GENDER);
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if(focusView!=null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            View view = getActivity().getCurrentFocus();
            KeyboardHandler.DropKeyboard(getActivity(),view);
            RegisterViewModel registerViewModel = presenter.compileAll(registerNextFullName.getText().toString(), registerNextPhoneNumber.getText().toString());
            sendGTMClickStepTwo();
            presenter.register(getActivity(), registerViewModel);
        }

    }

    @OnClick(R2.id.register_next_date)
    public void onDateTextClick(){
        //datePicker.show();
        DatePickerUtil datePicker = new DatePickerUtil(getActivity(), 1, 1, 2002);
        datePicker.SetMaxYear(2002);
        datePicker.SetMinYear(1934);
        datePicker.SetShowToday(false);
        datePicker.DatePickerCalendar((DatePickerUtil.onDateSelectedListener)presenter);
    }

    @OnClick({R2.id.register_next_male, R2.id.register_next_female})
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R2.id.register_next_male:
                if (checked)
                    registerNextFemale.setChecked(false);

                presenter.updateData(RegisterNewNext.GENDER, RegisterViewModel.GENDER_MALE);
                break;
            case R2.id.register_next_female:
                if (checked)
                    registerNextMale.setChecked(false);

                presenter.updateData(RegisterNewNext.GENDER, RegisterViewModel.GENDER_FEMALE);
                break;
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
    public void setData(int type, Object... data) {
        switch (type){
            case FULLNAME:
                String text = (String)data[0];
                registerNextFullName.setText(text);
                break;
            case TELEPHONE:
                text = (String)data[0];
                registerNextPhoneNumber.setText(text);
                break;
            case T_AND_C:
                String joinString = (String)data[0];
                registerNextTAndC.setText(Html.fromHtml(joinString));
                registerNextTAndC.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case TTL:
                text = (String)data[0];
                registerNextDate.setText(text);
                break;
            default:
                throw new RuntimeException(messageTAG+"please register type here!!!");
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @Override
    public void showProgress(final boolean show) {
        final WeakReference<LinearLayout> loginstatus = new WeakReference<LinearLayout>(registerNextStatus);
        final WeakReference<LinearLayout> loginForm = new WeakReference<LinearLayout>(registerNextStep2);
//        registerNext.updateData(RegisterNext.IS_LOADING, show);
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            registerNextStatus.setVisibility(View.VISIBLE);
            registerNextStatus.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(loginstatus.get()!=null)
                                loginstatus.get().setVisibility(show ? View.VISIBLE
                                        : View.GONE);
                        }
                    });

            registerNextStep2.setVisibility(View.VISIBLE);
            registerNextStep2.animate().setDuration(shortAnimTime)
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
            registerNextStatus.setVisibility(show ? View.VISIBLE : View.GONE);
            registerNextStep2.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.REGISTER_NEXT;
    }

    @Override
    public void setData(int type, Bundle data) {
        String email = (String)presenter.getData(RegisterNewNext.EMAIl);
        String password = (String)presenter.getData(RegisterNewNext.PASSWORD);
        switch (type){
            case DownloadService.REGISTER:

                TrackingUtils.fragmentBasedAFEvent(TAG);

                RegisterSuccessModel registerSuccessModel = Parcels.unwrap(data.getParcelable(DownloadService.REGISTER_MODEL_KEY));
                switch (registerSuccessModel.getIsActive()){
                    case RegisterSuccessModel.USER_PENDING:
                        sendLocalyticsRegisterEvent(registerSuccessModel.getUserId());
                        sendGTMRegisterEvent();
                        ((SessionView)getActivity()).moveToActivationResend(email);
                        break;
                    case RegisterSuccessModel.USER_ACTIVE:
                        // [START] do some login here.
                        LoginViewModel loginViewModel = new LoginViewModel();
                        loginViewModel.setUsername(email);
                        loginViewModel.setPassword(password);
                        LoginImpl.login(DownloadService.REGISTER_LOGIN, getActivity(), LoginModel.EmailType, loginViewModel);
                        //[END] do some login here.

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
                    default:
                        break;
                }
                break;
            case DownloadService.REGISTER_LOGIN:
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
    public void ariseRetry(int type, Object... data) {
        SnackbarManager.make(getActivity(), getString(R.string.message_verification_timeout), Snackbar.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String)data[0];
        SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        String text = (String)data[0];
        SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_LONG).show();
        showProgress(false);
    }
    private void sendLocalyticsRegisterEvent(int userId){
        Map<String, String> attributesLogin = new HashMap<String, String>();
        CustomerWrapper customerLogin = new CustomerWrapper();
        customerLogin.setCustomerId(Integer.toString(userId));
        customerLogin.setFullName((String)presenter.getData(RegisterNewNext.FULLNAME));
        customerLogin.setEmailAddress((String)presenter.getData(RegisterNewNext.EMAIl));
        customerLogin.setExtraAttr(attributesLogin);
        customerLogin.setMethod(getString(R.string.title_email));
        UnifyTracking.eventLoginLoca(customerLogin);
    }
    private void sendGTMRegisterEvent(){
        UnifyTracking.eventRegisterSuccess(getString(R.string.title_email));
    }

    private void sendGTMClickStepTwo(){
        UnifyTracking.eventRegister(AppEventTracking.EventLabel.REGISTER_STEP_2);
    }

    private void sendGTMRegisterError(String label){
        UnifyTracking.eventRegisterError(label);
    }
}
