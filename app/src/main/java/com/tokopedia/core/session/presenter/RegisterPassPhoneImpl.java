package com.tokopedia.core.session.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.api.RegisterApi;
import com.tokopedia.core.session.model.CreatePasswordModel;
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
 * Created by Nisie on 3/28/16.
 */
public class RegisterPassPhoneImpl extends RegisterThird implements DatePickerUtil.onDateSelectedListener {
    public static final String DEMO_EMAIL = "pentolan.jakarta@gmail.com";
    CreatePasswordModel createPassModel;
    RegisterService registerService;
    boolean isValidateEmail;

    public RegisterPassPhoneImpl(RegisterThirdView view) {
        super(view);
        registerService = new RegisterService();
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        if (data.containsKey(RegisterThird.FULLNAME)) {
            String name = (String) data.get(RegisterThird.FULLNAME);
            createPassModel.setFullName(name);
        } else if (data.containsKey(RegisterThird.GENDER)) {
            String gender = (String) data.get(RegisterThird.GENDER);
            createPassModel.setGender(gender);
        } else if (data.containsKey(RegisterThird.EMAIL)) {
            String email = (String) data.get(RegisterThird.EMAIL);
            Log.d(TAG, getMessageTAG() + " email : " + email);
        } else if (data.containsKey(RegisterThird.PASSWORD)) {
            String password = (String) data.get(RegisterThird.PASSWORD);
            createPassModel.setNewPass(password);
        } else if (data.containsKey(RegisterThird.CONFIRM_PASSWORD)) {
            String confirmPassword = (String) data.get(RegisterThird.CONFIRM_PASSWORD);
            createPassModel.setConfirmPass(confirmPassword);
        } else if (data.containsKey(RegisterThird.IS_CHECKED)) {
            boolean isChecked = (boolean) data.get(RegisterThird.IS_CHECKED);
            createPassModel.setRegisterTos(isChecked ? "1" : "0");
        } else if (data.containsKey(RegisterThird.PHONENUMBER)) {
            String phoneNumber = (String) data.get(RegisterThird.PHONENUMBER);
            createPassModel.setMsisdn(phoneNumber);
        } else if (data.containsKey(RegisterThird.DATE_YEAR)) {
            int year = (int) data.get(RegisterThird.DATE_YEAR);
            createPassModel.setBdayYear(year);
        } else if (data.containsKey(RegisterThird.DATE_MONTH)) {
            int month = (int) data.get(RegisterThird.DATE_MONTH);
            createPassModel.setBdayMonth(month);
        } else if (data.containsKey(RegisterThird.DATE_DAY)) {
            int day = (int) data.get(RegisterThird.DATE_DAY);
            createPassModel.setBdayDay(day);
        }
    }

    @Override
    public void register(Context context) {
        if (context != null && context instanceof SessionView) {
            view.showProgress(true);
            boolean isNeedLogin = true;
            Bundle bundle = new Bundle();
            bundle.putParcelable(DownloadService.CREATE_PASSWORD_MODEL_KEY, Parcels.wrap(createPassModel));
            bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);
            ((SessionView) context).sendDataFromInternet(DownloadService.REGISTER_PASS_PHONE, bundle);
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
            A:
            for (String email : list) {
                if (email.equals(createPassModel.getEmail())) {
                    result = true;
                    break A;
                }
            }
        }
        return result;
    }

    @Override
    public void validateEmail(final Context context, String email) {
        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.GET, context, TkpdBaseURL.User.URL_REGISTER + RegisterApi.VALIDATE_EMAIL_PL)
                .setIdentity()
                .addParam(RegisterApi.USER_EMAIL, email)// DEMO_EMAIL
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
                                        Log.i(TAG, getMessageTAG() + "onCompleted()");
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
                                        if (context != null && context instanceof SessionView) {
                                            ((SessionView) context).prevFragment();
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
                                        if (Integer.parseInt(validateEmailData.getData().getEmail_status()) == ValidateEmailData.Data.EMAIL_STATUS_REGISTERED) {
                                            view.showErrorValidateEmail();
                                            //[BUGFIX] AN-1500
                                            // [Register v2] Still directed to register form when
                                            // using already registered Facebook
                                            if (context != null && context instanceof SessionView) {
                                                ((SessionView) context).prevFragment();
                                            }
                                            //[BUGFIX] AN-1500
                                            // [Register v2] Still directed to register form when
                                            // using already registered Facebooker
                                        } else {
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
        switch (type) {
            case EMAIL:
                return createPassModel.getEmail();
            case DATE_YEAR:
                return createPassModel.getBdayYear();
            case DATE_MONTH:
                return createPassModel.getBdayMonth();
            case DATE_DAY:
                return createPassModel.getBdayDay();
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
        if (isAfterRotate) {
            view.setData(RegisterNewImpl.convertToMap(PASSWORD, createPassModel.getNewPass()));
            view.setData(RegisterNewImpl.convertToMap(CONFIRM_PASSWORD, createPassModel.getConfirmPass()));
            view.setData(RegisterNewImpl.convertToMap(IS_CHECKED, createPassModel.getRegisterTos()));
            view.setData(RegisterNewImpl.convertToMap(PHONENUMBER, createPassModel.getMsisdn()));
        } else {
            calculateDateTime();
        }

        view.setData(RegisterNewImpl.convertToMap(FULLNAME, createPassModel.getFullName()));
        if (createPassModel.getMsisdn() != null)
            view.setData(RegisterNewImpl.convertToMap(PHONENUMBER, createPassModel.getMsisdn()));

        if (createPassModel.getBdayYear() == 0) {
            view.initDatePickerDialog(context, 2002,
                    createPassModel.getBdayMonth(), createPassModel.getBdayDay());
        } else {
            view.initDatePickerDialog(context, createPassModel.getBdayYear(),
                    createPassModel.getBdayMonth(), createPassModel.getBdayDay());
        }
        view.setData(RegisterNewImpl.convertToMap(BIRTHDAY,
                RegisterImpl.RegisterUtil.formatDateText(createPassModel.getBdayDay(),
                        createPassModel.getBdayMonth(), createPassModel.getBdayYear())));

        view.setAllowedField();

    }

    @Override
    public void fetchArguments(Bundle argument) {
        if (argument != null) {
            createPassModel = Parcels.unwrap(argument.getParcelable(DATA));
//            switch (argument.getString(REGISTER_TYPE)){
//                case LoginModel.GoogleType:// email, gender, birthday, fullname
//                case LoginModel.FacebookType:// name, gender birthday, email
            Log.d(TAG, getMessageTAG() + " : " + createPassModel);
//                    break;
//            }
        }
    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {
        createPassModel = Parcels.unwrap(argument.getParcelable(DATA));
        isValidateEmail = argument.getBoolean(VALIDATE_EMAIL);
    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {
        argument.putParcelable(DATA, Parcels.wrap(createPassModel));
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
        maxDate.set(maxDate.get(Calendar.YEAR) - 14, maxDate.getMaximum(Calendar.MONTH), maxDate.getMaximum(Calendar.DATE));
        minDate.set(1933, minDate.getMinimum(Calendar.MONTH), minDate.getMinimum(Calendar.DATE));
        long maxtime = maxDate.getTimeInMillis();
        long mintime = minDate.getTimeInMillis();
        int dateYear = maxDate.get(Calendar.YEAR);
        int dateMonth = minDate.get(Calendar.MONTH) + 1;
        int dateDay = minDate.get(Calendar.DATE);
        Log.d(TAG, getMessageTAG() + dateDay + " - " + dateMonth + " -" + dateYear + " max time : " + maxtime + " minTime : " + mintime);

        //[REVIEW CODE] http://dexter.tkpd:9000/issues/search#issues=AVMciZ5ushXTbmCrwh9m
        if (createPassModel != null) {
            if (createPassModel.getDateText() != null
                    && createPassModel.getDateText().length() > 0) {
                String[] bDay = createPassModel.getDateText().split("/");
                createPassModel.setBdayYear(Integer.parseInt(bDay[2].trim()));
                createPassModel.setBdayMonth(Integer.parseInt(bDay[1].trim()));
                createPassModel.setBdayDay(Integer.parseInt(bDay[0].trim()));
            } else {
                createPassModel.setBdayDay(dateYear);
                createPassModel.setBdayMonth(dateMonth);
                createPassModel.setBdayDay(dateDay);
            }
        }
    }

    @Override
    public void makeLogin(Context context, Bundle data) {
        LocalCacheHandler loginUuid = new LocalCacheHandler(context, LOGIN_UUID_KEY);
        String uuid = loginUuid.getString(UUID_KEY, DEFAULT_UUID_VALUE);
        data.putString(UUID_KEY, uuid);
        ((SessionView) context).sendDataFromInternet(DownloadService.MAKE_LOGIN, data);
    }

    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        createPassModel.setBdayYear(year);
        createPassModel.setBdayMonth(month);
        createPassModel.setBdayDay(dayOfMonth);
        view.setData(RegisterNewImpl.convertToMap(BIRTHDAY,
                RegisterImpl.RegisterUtil.formatDateText(
                        createPassModel.getBdayDay(), createPassModel.getBdayMonth(),
                        createPassModel.getBdayYear())));
    }
}
