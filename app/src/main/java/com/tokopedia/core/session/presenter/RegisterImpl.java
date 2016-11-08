package com.tokopedia.core.session.presenter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.session.model.LoginFacebookViewModel;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginModel;
import com.tokopedia.core.session.model.RegisterViewModel;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.List;

/**
 * @author m.normansyah
 * @since 12-11-2015
 */
@Deprecated
public class RegisterImpl implements Register{
    RegisterView registerView;

    RegisterViewModel registerViewModel;
    LoginFacebookViewModel loginFacebookViewModel;
    LoginGoogleModel loginGoogleModel;

    private SimpleFacebook simpleFacebook;

    @Override
    public void updateData(int type, Object... data) {
        switch (type){
            case PHONE:
                String temp = (String)data[0];
                registerViewModel.setmPhone(temp);
                break;
            case NAME:
                temp = (String)data[0];
                registerViewModel.setmName(temp);
                break;
            case BIRTHDAYTTL:
                temp = (String)data[0];
                registerViewModel.setDateText(temp);
                break;
            case GENDER:
                int i = (int)data[0];
                switch(i){
                    case RegisterViewModel.GENDER_MALE:
                        registerViewModel.setmGender(i);
                        break;
                    case RegisterViewModel.GENDER_FEMALE:
                        registerViewModel.setmGender(i);
                        break;
                }
                break;
        }
    }

    DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            registerView.setDateText(formatDateText(
                    dayOfMonth,
                    monthOfYear,
                    year
            ));
            registerViewModel.setmDateYear(year);
            registerViewModel.setmDateMonth(monthOfYear);
            registerViewModel.setmDateDay(dayOfMonth);
        }
    };

    public RegisterImpl(RegisterView registerView){
        this.registerView = registerView;
    }

    @Override
    public void updateDate(int mDateYear, int mDateMonth, int mDateDay) {
        registerViewModel.setmDateYear(mDateYear);
        registerViewModel.setmDateMonth(mDateMonth);
        registerViewModel.setmDateDay(mDateDay);
        registerView.setDateText(
                formatDateText(
                        registerViewModel.getmDateDay(),
                        registerViewModel.getmDateMonth(),
                        registerViewModel.getmDateYear()
                )
        );
    }

    @Override
    public void initDataInstances(Context context) {

        if(!isAfterRotate()){
            registerViewModel = new RegisterViewModel();
             calculateDateTime();
        }
    }

    @Override
    public boolean isAfterRotate() {
        return registerViewModel != null;// &&registerViewModel.getDateText()!=null
    }

    @Override
    public void initData(Context context) {
        Log.d(TAG, messageTAG+" initData "+registerViewModel);
        if(isAfterRotate()){
            registerView.updateData(RegisterView.NAME, registerViewModel.getmName());
            registerView.updateData(RegisterView.DATETEXT, registerViewModel.getDateText());
            registerView.updateData(RegisterView.GENDER, registerViewModel.getmGender());
            registerView.updateData(RegisterView.PHONE, registerViewModel.getmPhone());
            // currently ttl is unused
//            registerView.updateData(RegisterView.TTL, registerViewModel.getmName());
        }
        registerView.showProgress(registerViewModel.isRegisterLoading());
        registerView.initDatePickerDialog(context, callBack,
                registerViewModel.getmDateYear(),
                registerViewModel.getmDateMonth(),
                registerViewModel.getmDateDay());
        registerView.initDatePicker(
                registerViewModel.getMaxDate(),
                registerViewModel.getMinDate()
        );
        registerView.setDateText(formatDateText(
                registerViewModel.getmDateDay(),
                registerViewModel.getmDateMonth(),
                registerViewModel.getmDateYear()
        ));

        if(loginFacebookViewModel!=null&&loginFacebookViewModel.getFullName()!=null){
            registerView.showProgress(false);
        }
        if(loginGoogleModel!=null&&loginGoogleModel.getFullName()!=null){
            registerView.showProgress(false);
        }
    }

    @Override
    public boolean checkRegexName(String param) {
        String regex = "[A-Za-z]+";
        return !param.replaceAll("\\s","").matches(regex);
    }

    /**
     * put methods to this class in order to provide for other class
     */
    public static class RegisterUtil{

        public static boolean checkRegexNameLocal(String param){
            String regex = "[A-Za-z]+";
            return !param.replaceAll("\\s","").matches(regex);
        }

        public static boolean isExceedMaxCharacter(String text) {
            return text.length()>35;
        }

        public static String formatDateText(int mDateDay, int mDateMonth, int mDateYear) {
            return String.format("%d / %d / %d", mDateDay, mDateMonth, mDateYear);
        }
    }

    @Override
    public boolean isExceedMaxCharacter(String text) {
        return text.length()>35;
    }

    @Override
    public void updateGender(int gender) {
        registerViewModel.setmGender(gender);
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
        registerViewModel.setmDateYear(maxDate.get(Calendar.YEAR));
        registerViewModel.setmDateMonth(minDate.get(Calendar.MONTH)+1);
        registerViewModel.setmDateDay(minDate.get(Calendar.DATE));
        registerViewModel.setMaxDate(maxtime);
        registerViewModel.setMinDate(mintime);
    }

    @Override
    public String formatDateText(int mDateDay, int mDateMonth, int mDateYear) {
        return String.format("%d / %d / %d", mDateDay, mDateMonth, mDateYear);
    }

    @Override
    public void setLocalyticFlow(Context context) {
        CommonUtils.dumper("LocalTag : Register");
        String screenName = context.getString(R.string.register_page);
        ScreenTracking.screenLoca(screenName);
    }

    @Override
    public void saveDataBeforeRotation(Bundle outState) {
        outState.putParcelable(VIEW_MODEL, Parcels.wrap(registerViewModel));
    }

    @Override
    public void updateViewModel(RegisterViewModel model) {
        model.setIsRegisterLoading(registerViewModel.isRegisterLoading());
        model.setmGender(registerViewModel.getmGender());
        model.setmDateDay(registerViewModel.getmDateDay());
        model.setmDateMonth(registerViewModel.getmDateMonth());
        model.setmDateYear(registerViewModel.getmDateYear());
        model.setmEmail(registerViewModel.getmEmail());
        registerViewModel = model;// replace with model
    }

    @Override
    public void updateViewModel(int type, Object... data) {
        switch (type){
            case RegisterViewModel.IS_REGISTER_LOADING_TYPE:
                boolean isRegisterLoading = (boolean)data[0];
                registerViewModel.setIsRegisterLoading(isRegisterLoading);
                break;
        }
    }

    @Override
    public void fetchDataAfterRotation(Bundle outState) {
        if(outState!=null){
            registerViewModel = Parcels.unwrap(outState.getParcelable(VIEW_MODEL));
            Log.d(TAG, messageTAG+" day : "+registerViewModel.getmDateDay()+" month "+registerViewModel.getmDateMonth()
                +" year "+ registerViewModel.getmDateYear());
        }
    }

    @Override
    public RegisterViewModel getData() {
        return registerViewModel;
    }

    @Override
    public void loginFacebook() {
        simpleFacebook = simpleFacebook.getInstance();
        simpleFacebook.login(new OnLoginListener() {
            @Override
            public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                Profile.Properties properties = new Profile.Properties.Builder()
                        .add(Profile.Properties.ID)
                        .add(Profile.Properties.FIRST_NAME)
                        .add(Profile.Properties.GENDER)
                        .add(Profile.Properties.EMAIL)
                        .build();
                simpleFacebook.getProfile(properties, new OnProfileListener() {
                    @Override
                    public void onComplete(Profile response) {
                        Log.e(TAG, messageTAG + " start login to facebook !!");
                        super.onComplete(response);
                        loginFacebookViewModel = new LoginFacebookViewModel();
                        loginFacebookViewModel.setFullName(response.getFirstName());// 10
                        loginFacebookViewModel.setGender(response.getGender());// 7
                        loginFacebookViewModel.setBirthday(response.getBirthday());// 2
                        loginFacebookViewModel.setFbToken(simpleFacebook.getAccessToken().getToken());// 6
                        loginFacebookViewModel.setFbId(response.getId());// 8
                        loginFacebookViewModel.setEmail(response.getEmail());// 5
                        loginFacebookViewModel.setEducation(response.getEducation().toString());// 4
                        loginFacebookViewModel.setInterest(response.getRelationshipStatus());// 9
                        loginFacebookViewModel.setWork(response.getWork().toString());
                        Log.e(TAG, messageTAG + " end login Facebook : "+ loginFacebookViewModel);
                        // dismiss progress
                        registerView.showProgress(false);

                        // update data and UI
                        if(registerViewModel!=null){
                            if(loginFacebookViewModel.getFullName()!=null){
                                registerViewModel.setmName(loginFacebookViewModel.getFullName());
                                registerView.updateData(RegisterView.NAME, loginFacebookViewModel.getFullName());
                            }
                            if(loginFacebookViewModel.getGender().contains("male")){
                                registerViewModel.setmGender(RegisterViewModel.GENDER_MALE);
                                registerView.updateData(RegisterView.GENDER, RegisterViewModel.GENDER_MALE);
                            }else{
                                registerViewModel.setmGender(RegisterViewModel.GENDER_FEMALE);
                                registerView.updateData(RegisterView.GENDER, RegisterViewModel.GENDER_FEMALE);
                            }
                            if(loginFacebookViewModel.getBirthday()!=null){
                                Log.d(TAG, messageTAG+" need to verify birthday ");
                            }
                            if(loginFacebookViewModel.getEmail()!=null){
                                registerViewModel.setmEmail(loginFacebookViewModel.getEmail());
                            }
                        }
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        super.onException(throwable);
                        Log.e(TAG, messageTAG+" login facebook : "+throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onFail(String reason) {
                        super.onFail(reason);
                        Log.e(TAG, messageTAG + " login facebook : " + reason);
                    }
                });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onException(Throwable throwable) {

            }

            @Override
            public void onFail(String s) {

            }
        });
    }

    @Override
    public void startLoginWithGoogle(String type, LoginGoogleModel loginGoogleModel) {
        this.loginGoogleModel = loginGoogleModel;
        if(type != null && type.equals(LoginModel.GoogleType) && loginGoogleModel != null){
            // dismiss progress
            registerView.showProgress(false);

            // update data and UI
            if(registerViewModel!=null){
                if(loginGoogleModel.getFullName()!=null){
                    registerView.updateData(RegisterView.NAME, loginGoogleModel.getFullName());
                }
                if(loginGoogleModel.getGender().contains("male")){
                    registerView.updateData(RegisterView.GENDER, RegisterViewModel.GENDER_MALE);
                    registerViewModel.setmGender(RegisterViewModel.GENDER_MALE);
                }else{
                    registerView.updateData(RegisterView.GENDER, RegisterViewModel.GENDER_FEMALE);
                    registerViewModel.setmGender(RegisterViewModel.GENDER_FEMALE);
                }
                if(loginGoogleModel.getBirthday()!=null){
                    Log.d(TAG, messageTAG+" need to verify birthday ");
                }
                if(loginGoogleModel.getEmail()!=null){
                    registerViewModel.setmEmail(loginGoogleModel.getEmail());
                }
            }
        }
    }
}
