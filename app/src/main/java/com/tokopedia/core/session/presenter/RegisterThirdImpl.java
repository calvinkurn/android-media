package com.tokopedia.core.session.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.api.RegisterApi;
import com.tokopedia.core.session.model.RegisterViewModel;
import com.tokopedia.core.session.model.network.ValidateEmailData;
import com.tokopedia.core.session.service.RegisterService;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by m.normansyah on 1/27/16.
 * features :
 * keep validate email till get result from server.
 * check auto verify for register third party
 */
public class RegisterThirdImpl extends RegisterThird  implements DatePickerUtil.onDateSelectedListener{
    public static final String DEMO_EMAIL = "pentolan.jakarta@gmail.com";
    RegisterViewModel registerViewModel;
    RegisterService registerService;
    boolean isValidateEmail;
    String registerType;
    public RegisterThirdImpl(RegisterThirdView view) {
        super(view);
        registerService = new RegisterService();
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        if(data.containsKey(RegisterThird.FULLNAME)){
            String name = (String)data.get(RegisterThird.FULLNAME);
            registerViewModel.setmName(name);
        }else if(data.containsKey(RegisterThird.GENDER)){
            Log.d(TAG, getMessageTAG() + " gender : " + (int) data.get(RegisterThird.GENDER));
        }
        else if(data.containsKey(RegisterThird.EMAIL)){
            String email = (String)data.get(RegisterThird.EMAIL);
            Log.d(TAG, getMessageTAG()+" email : "+email);
        }
        else if(data.containsKey(RegisterThird.BIRTHDAY)){
            String birthDay = (String)data.get(RegisterThird.BIRTHDAY);
            registerViewModel.setDateText(birthDay);
        }
        else if(data.containsKey(RegisterThird.PASSWORD)){
            String password = (String)data.get(RegisterThird.PASSWORD);
            registerViewModel.setmPassword(password);
        }
        else if(data.containsKey(RegisterThird.CONFIRM_PASSWORD)){
            String confirmPassword = (String)data.get(RegisterThird.CONFIRM_PASSWORD);
            registerViewModel.setmConfirmPassword(confirmPassword);
        }
        else if(data.containsKey(RegisterThird.IS_CHECKED)){
            boolean isChecked = (boolean)data.get(RegisterThird.IS_CHECKED);
            registerViewModel.setIsAgreedTermCondition(isChecked);
        }else if(data.containsKey(RegisterThird.PHONENUMBER)){
            String phoneNumber = (String)data.get(RegisterThird.PHONENUMBER);
            registerViewModel.setmPhone(phoneNumber);
        }else if(data.containsKey(RegisterThird.DATE_YEAR)){
            int year = (int)data.get(RegisterThird.DATE_YEAR);
            registerViewModel.setmDateYear(year);
        }else if(data.containsKey(RegisterThird.DATE_MONTH)){
            int month = (int)data.get(RegisterThird.DATE_MONTH);
            registerViewModel.setmDateMonth(month);
        }else if(data.containsKey(RegisterThird.DATE_DAY)){
            int day = (int)data.get(RegisterThird.DATE_DAY);
            registerViewModel.setmDateYear(day);
        }
    }

    @Override
    public void register(Context context) {
        if(context!=null&&context instanceof SessionView){
            boolean isNeedLogin = true;
            Bundle bundle = new Bundle();
            //[DEMO] this is for demo only
//            registerViewModel.setmEmail(DEMO_EMAIL);
//            registerViewModel.setIsAutoVerify(true);
            //[DEMO] this is for demo only
            bundle.putParcelable(DownloadService.REGISTER_MODEL_KEY, Parcels.wrap(registerViewModel));
            bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);
            ((SessionView)context).sendDataFromInternet(DownloadService.REGISTER_THIRD, bundle);
        }
    }

    @Override
    public List<String> getEmailListOfAccountsUserHasLoggedInto(Context context) {
        Set<String> listOfAddresses = new LinkedHashSet<>();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        if (accounts != null) {
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    listOfAddresses.add(account.name);
                }
            }
        }
        return new ArrayList<>(listOfAddresses);
    }

    @Override
    public boolean isEmailAddressFromDevice(Context context) {
        List<String> list = getEmailListOfAccountsUserHasLoggedInto(context);
        boolean result = false;
        if (list.size() > 0) {
            A : for(String email:list) {
                if (email.equals(registerViewModel.getmEmail())) {
                    result = true;
                    break A;
                }
            }
        }
        return result;
    }

    @Override
    public void validateEmail(final Context context, String email) {
        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.GET, context, TkpdBaseURL.User.URL_REGISTER+ RegisterApi.VALIDATE_EMAIL_PL)
                .setIdentity()
                .addParam(RegisterApi.USER_EMAIL, email )// DEMO_EMAIL
                .compileAllParam()
                .finish();
        compositeSubscription.add(
                registerService.getApi().validateEmail(
                        NetworkCalculator.getContentMd5(networkCalculator),
                        NetworkCalculator.getDate(networkCalculator),
                        NetworkCalculator.getAuthorization(networkCalculator),
                        NetworkCalculator.getxMethod(networkCalculator),
                        NetworkCalculator.getUserId(context),
                        NetworkCalculator.getDeviceId(context),
                        NetworkCalculator.getHash(networkCalculator),
                        NetworkCalculator.getDeviceTime(networkCalculator),
                         email // DEMO_EMAIL
                ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<ValidateEmailData>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.i(TAG, getMessageTAG()+"onCompleted()");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e(TAG, getMessageTAG() + e.getLocalizedMessage());
//                                        view.showUnknownError(e);
                                        view.showProgress(false);
                                        view.enView(false);
                                        //[BUGFIX] AN-1500
                                        // [Register v2] Still directed to register form when
                                        // using already registered Facebook
                                        if(context != null && context instanceof SessionView){
                                            ((SessionView)context).prevFragment();
                                        }
                                        //[BUGFIX] AN-1500
                                        // [Register v2] Still directed to register form when
                                        // using already registered Facebook
                                    }

                                    @Override
                                    public void onNext(ValidateEmailData validateEmailData) {
                                        view.showProgress(false);
                                        view.enView(true);
                                        isValidateEmail = true;
                                        if(Integer.parseInt(validateEmailData.getData().getEmail_status())==ValidateEmailData.Data.EMAIL_STATUS_REGISTERED){
                                            view.showErrorValidateEmail();
                                            //[BUGFIX] AN-1500
                                            // [Register v2] Still directed to register form when
                                            // using already registered Facebook
                                            if(context != null && context instanceof SessionView){
                                                ((SessionView)context).prevFragment();
                                            }
                                            //[BUGFIX] AN-1500
                                            // [Register v2] Still directed to register form when
                                            // using already registered Facebooker
                                        }else{
                                            //[START] now can do a lot of things
                                            view.enView(true);
                                            //[END] now can do a lot of things
                                        }
                                    }
                                }
                        )
        );
    }

    @Override
    public Object getData(String type) {
        switch (type){
            case EMAIL:
                return registerViewModel.getmEmail();
            case DATE_YEAR:
                return registerViewModel.getmDateYear();
            case DATE_MONTH:
                return registerViewModel.getmDateMonth();
            case DATE_DAY:
                return registerViewModel.getmDateDay();
            case REGISTER_TYPE:
                return registerType;
            default:
                return null;
        }
    }

    @Override
    public String getMessageTAG() {
        return getMessageTAG(RegisterThirdImpl.class);
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return className.getSimpleName();
    }

    @Override
    public void initData(@NonNull Context context) {
        if(isAfterRotate){
            view.setData(RegisterNewImpl.convertToMap(PASSWORD, registerViewModel.getmPassword()));
            view.setData(RegisterNewImpl.convertToMap(CONFIRM_PASSWORD, registerViewModel.getmConfirmPassword()));
            view.setData(RegisterNewImpl.convertToMap(IS_CHECKED, registerViewModel.isAgreedTermCondition()));
            view.setData(RegisterNewImpl.convertToMap(PHONENUMBER, registerViewModel.getmPhone()));
        }else{
            calculateDateTime();
            registerViewModel.setIsAutoVerify(isEmailAddressFromDevice(context));
        }

        if(!isValidateEmail){
            view.showProgress(true);
            validateEmail(context, registerViewModel.getmEmail());
        }

        view.setData(RegisterNewImpl.convertToMap(FULLNAME, registerViewModel.getmName()));
//        view.setData(RegisterNewImpl.convertToMap(BIRTHDAY, registerViewModel.getDateText()));
        view.initDatePickerDialog(context, registerViewModel.getmDateYear(),
                registerViewModel.getmDateMonth(), registerViewModel.getmDateDay());
        view.initDatePicker(registerViewModel.getMaxDate(), registerViewModel.getMinDate());
        view.setData(RegisterNewImpl.convertToMap(BIRTHDAY,
                RegisterImpl.RegisterUtil.formatDateText(registerViewModel.getmDateDay(),
                        registerViewModel.getmDateMonth(), registerViewModel.getmDateYear())));

    }

    @Override
    public void fetchArguments(Bundle argument) {
        if(argument!=null){
            registerViewModel = Parcels.unwrap(argument.getParcelable(DATA));
            registerType = argument.getString(REGISTER_TYPE);
//            switch (argument.getString(REGISTER_TYPE)){
//                case LoginModel.GoogleType:// email, gender, birthday, fullname
//                case LoginModel.FacebookType:// name, gender birthday, email
//                    Log.d(TAG, getMessageTAG()+" : "+registerViewModel);
//                    break;
//            }
        }
    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {
        registerViewModel = Parcels.unwrap(argument.getParcelable(DATA));
        isValidateEmail = argument.getBoolean(VALIDATE_EMAIL);
    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {
        argument.putParcelable(DATA, Parcels.wrap(registerViewModel));
        argument.putBoolean(VALIDATE_EMAIL, isValidateEmail);
    }

    @Override
    public void initDataInstance(Context context) {

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
        Log.d(TAG, getMessageTAG() + dateDay + " - " + dateMonth + " -" + dateYear + " max time : " + maxtime + " minTime : " + mintime);

        if(registerViewModel!=null
            && registerViewModel.getDateText()!=null
                && registerViewModel.getDateText().length()>0){
            String[] bDay = registerViewModel.getDateText().split("/");
            registerViewModel.setmDateYear(Integer.parseInt(bDay[2].trim()));
            registerViewModel.setmDateMonth(Integer.parseInt(bDay[1].trim()));
            registerViewModel.setmDateDay(Integer.parseInt(bDay[0].trim()));
        }else{
            registerViewModel.setmDateYear(dateYear);
            registerViewModel.setmDateMonth(dateMonth);
            registerViewModel.setmDateDay(dateDay);
        }

        //[START] Save year to model
        registerViewModel.setMinDate(mintime);
        registerViewModel.setMaxDate(maxtime);
        //[END] Save year to model
    }

    @Override
    public void makeLogin(Context context, Bundle data) {

    }

    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        registerViewModel.setmDateYear(year);
        registerViewModel.setmDateMonth(month);
        registerViewModel.setmDateDay(dayOfMonth);
        view.setData(RegisterNewImpl.convertToMap(BIRTHDAY,
                RegisterImpl.RegisterUtil.formatDateText(
                        registerViewModel.getmDateDay(), registerViewModel.getmDateMonth(),
                        registerViewModel.getmDateYear())));
    }
}
