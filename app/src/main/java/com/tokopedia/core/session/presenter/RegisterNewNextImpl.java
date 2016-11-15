package com.tokopedia.core.session.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.core.R;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.model.RegisterViewModel;
import com.tokopedia.core.util.SessionHandler;

import org.parceler.Parcels;

import java.util.Calendar;

/**
 * Created by m.normansyah on 1/25/16.
 */
public class RegisterNewNextImpl extends RegisterNewNext implements DatePickerUtil.onDateSelectedListener{

    public static final int MAX_PHONE_NUMBER = 13;
    public static final int MIN_PHONE_NUMBER = 6;

    RegisterViewModel registerViewModel;

    public RegisterNewNextImpl(RegisterNewNextView registerNewNextView){
        super(registerNewNextView);
    }

    @Override
    public void initDataInstance(Context context) {
    }

    @Override
    public String getMessageTAG() {
        return message;
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return message;
    }

    @Override
    public void initData(@NonNull Context context) {
        if(!isAfterRotate){
            calculateDateTime();
        }else{
            view.setData(RegisterNewNextView.FULLNAME, registerViewModel.getmName());
            view.setData(RegisterNewNextView.TELEPHONE, registerViewModel.getmPhone());
        }
        view.initDatePickerDialog(context, registerViewModel.getmDateYear(), registerViewModel.getmDateMonth(), registerViewModel.getmDateDay());
        view.initDatePicker(registerViewModel.getMaxDate(), registerViewModel.getMinDate());
        view.setData(RegisterNewNextView.TTL, RegisterNewImpl.RegisterUtil.formatDateText(registerViewModel.getmDateDay(), registerViewModel.getmDateMonth(), registerViewModel.getmDateYear()));
        showTermsAndOptionsTextView(context);

    }

    @Override
    public Object getData(int type) {
        switch (type){
            case EMAIl:
                return registerViewModel.getmEmail();
            case PASSWORD:
                return registerViewModel.getmPassword();
            case FULLNAME:
                return registerViewModel.getmName();
            case DATE_DAY:
                return registerViewModel.getmDateDay();
            case DATE_MONTH:
                return registerViewModel.getmDateMonth();
            case DATE_YEAR:
                return registerViewModel.getmDateYear();
        }
        return null;
    }

    @Override
    public RegisterViewModel compileAll(String fullName, String phoneNumber) {
        RegisterViewModel registerViewModel = new RegisterViewModel();
        //0. full name
        registerViewModel.setmName(fullName);

        //1. email
        registerViewModel.setmEmail(this.registerViewModel.getmEmail());

        // 2. is agree
        registerViewModel.setIsAgreedTermCondition(true);

        // 3. phone
        registerViewModel.setmPhone(phoneNumber);

        // 4. password
        registerViewModel.setmPassword(this.registerViewModel.getmPassword());

        // 5. confirm password
        registerViewModel.setmConfirmPassword(this.registerViewModel.getmPassword());

        // 6. gender
        registerViewModel.setmGender(this.registerViewModel.getmGender());

        // 7. set day
        registerViewModel.setmDateDay(this.registerViewModel.getmDateDay());

        //8. set month
        registerViewModel.setmDateMonth(this.registerViewModel.getmDateMonth());

        //9. set year
        registerViewModel.setmDateYear(this.registerViewModel.getmDateYear());

        //10. set always agree here
        registerViewModel.setIsAgreedTermCondition(true);

        //11. set auto_verify
        registerViewModel.setIsAutoVerify(this.registerViewModel.isAutoVerify());

        return registerViewModel;
    }

    @Override
    public void saveBeforeDestroy(Context context, String fullName, String phone) {
        SessionHandler.saveRegisterNext(context, fullName, phone, registerViewModel.getmGender(),
                RegisterNewImpl.RegisterUtil.formatDateText(registerViewModel.getmDateDay(), registerViewModel.getmDateMonth(), registerViewModel.getmDateYear()));
    }

    @Override
    public void fetchArguments(Bundle bundle) {
        if(bundle!=null){
            registerViewModel = new RegisterViewModel();
            registerViewModel.setmEmail(bundle.getString(RegisterNewNextView.EMAIL, ""));
            registerViewModel.setmPassword(bundle.getString(RegisterNewNextView.PASSWORD, ""));
            registerViewModel.setIsAutoVerify(bundle.getBoolean(RegisterNewNextView.IS_AUTO_VERIFY, false));

            if(registerViewModel.getmEmail().equals("")||registerViewModel.getmPassword().equals(""))
                throw new RuntimeException(message+"please forward some data to it!!!");
        }
    }

    @Override
    public void fetchFromPreference(Context context) {
        RegisterViewModel registerViewModel = SessionHandler.getRegisterNext(context);
        if(registerViewModel!=null){
            this.registerViewModel.setmDateYear(registerViewModel.getmDateYear());
            this.registerViewModel.setmDateMonth(registerViewModel.getmDateMonth());
            this.registerViewModel.setmDateDay(registerViewModel.getmDateDay());
            this.registerViewModel.setmName(registerViewModel.getmName());
            this.registerViewModel.setmGender(registerViewModel.getmGender());
            this.registerViewModel.setmPhone(registerViewModel.getmPhone());
        }
    }

    @Override
    public void getRotationData(Bundle argument) {
        registerViewModel = Parcels.unwrap(argument.getParcelable(DATA));
        Log.d(TAG, message+" fetchRotationData(Bundle argument) "+registerViewModel);
    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {
        Log.d(TAG, message+" saveDataBeforeRotation(Bundle argument) "+registerViewModel);
        argument.putParcelable(DATA, Parcels.wrap(registerViewModel));
    }

    @Override
    public void updateData(int type, Object... data) {
        switch (type){
            case RegisterNewNext.DATE_YEAR:
                int dateYear = (int)data[0];
                registerViewModel.setmDateYear(dateYear);
                break;
            case RegisterNewNext.DATE_MONTH:
                int dateMonth = (int)data[0];
                registerViewModel.setmDateMonth(dateMonth);
                break;
            case RegisterNewNext.DATE_DAY:
                int dateDay = (int)data[0];
                registerViewModel.setmDateDay(dateDay);
            case RegisterNewNext.GENDER:
                int gender = (int)data[0];
                registerViewModel.setmGender(gender);
                break;
            case RegisterNewNext.FULLNAME:
                String fullName = (String)data[0];
                registerViewModel.setmName(fullName);
                break;
            case RegisterNewNext.PHONT:
                String phone = (String)data[0];
                registerViewModel.setmPhone(phone);
                break;
        }
    }

    @Override
    public void setData(int type, Bundle data) {

    }

    /**
     * register to the internet
     * @param context {@link android.support.v7.app.AppCompatActivity}
     * @param registerViewModel object that holds params for register
     */
    @Override
    public void register(@NonNull Context context, RegisterViewModel registerViewModel){
        boolean isNeedLogin = true;
        Bundle bundle = new Bundle();
        bundle.putParcelable(DownloadService.REGISTER_MODEL_KEY, Parcels.wrap(registerViewModel));
        bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);

        if(context!=null&&context instanceof SessionView)
            ((SessionView)context).sendDataFromInternet(DownloadService.REGISTER, bundle);
    }

    @Override
    public void calculateDateTime() {
        Calendar now = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        Calendar minDate = Calendar.getInstance();
        maxDate.set(maxDate.get(Calendar.YEAR)-14, maxDate.getMaximum(Calendar.MONTH), maxDate.getMaximum(Calendar.DATE));
        minDate.set(1933, minDate.getMinimum(Calendar.MONTH), minDate.getMinimum(Calendar.DATE));
        long maxtime = maxDate.getTimeInMillis();
        long mintime = minDate.getTimeInMillis();
        int dateYear = maxDate.get(Calendar.YEAR);
        int dateMonth = minDate.get(Calendar.MONTH)+1;
        int dateDay = minDate.get(Calendar.DATE);
        Log.d(TAG, message + dateDay + " - " + dateMonth + " -" + dateYear+" max time : "+maxtime+" minTime : "+mintime);

        //[START] Save year to model
        registerViewModel.setmDateYear(dateYear);
        registerViewModel.setmDateMonth(dateMonth);
        registerViewModel.setmDateDay(dateDay);
        registerViewModel.setMinDate(mintime);
        registerViewModel.setMaxDate(maxtime);
        //[END] Save year to model
    }

    @Override
    public void showTermsAndOptionsTextView(Context context) {
        String joinString = context.getString(R.string.detail_term_and_privacy) +
                " " + context.getString(R.string.link_term_condition) +
                ", serta " + context.getString(R.string.link_privacy_policy) + " Tokopedia";
        view.setData(RegisterNewNextView.T_AND_C, joinString);
    }


    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        registerViewModel.setmDateYear(year);
        registerViewModel.setmDateMonth(month);
        registerViewModel.setmDateDay(dayOfMonth);
        view.setData(RegisterNewNextView.TTL, RegisterNewImpl.RegisterUtil.formatDateText(registerViewModel.getmDateDay(), registerViewModel.getmDateMonth(), registerViewModel.getmDateYear()));
    }

    public static void testPhoneNumberValidation() {
        Log.d(TAG, "Phone number 1234567890 validation result: " + validatePhoneNumber("1234567890"));
        Log.d(TAG, "Phone number 123-456-7890 validation result: " + validatePhoneNumber("123-456-7890"));
        Log.d(TAG, "Phone number 123-456-7890 x1234 validation result: " + validatePhoneNumber("123-456-7890 x1234"));
        Log.d(TAG, "Phone number 123-456-7890 ext1234 validation result: " + validatePhoneNumber("123-456-7890 ext1234"));
        Log.d(TAG, "Phone number (123)-456-7890 validation result: " + validatePhoneNumber("(123)-456-7890"));
        Log.d(TAG, "Phone number 123.456.7890 validation result: " + validatePhoneNumber("123.456.7890"));
        Log.d(TAG, "Phone number 123 456 7890 validation result: " + validatePhoneNumber("123 456 7890"));
    }

    public static boolean validatePhoneNumber(String phoneNo) {
        Log.d(TAG, "Phone number " + phoneNo + " start validating");
        //validate phone numbers of format "1234567890"
        for (int i = MIN_PHONE_NUMBER; i <= MAX_PHONE_NUMBER; i++) {
            if (phoneNo.matches("\\d{" + i + "}")) return true;
        }
        //validating phone number with -, . or spaces
//        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
        //validating phone number with extension length from 3 to 5
//        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
        //validating phone number where area code is in braces ()
//        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
        //return false if nothing matches the input
//        else
        return false;

    }
}
