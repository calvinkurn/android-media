package com.tokopedia.core.session.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnNewPermissionsListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.interactor.RegisterInteractor;
import com.tokopedia.core.session.interactor.RegisterInteractorImpl;
import com.tokopedia.core.session.model.CreatePasswordModel;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.LoginFacebookViewModel;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginModel;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.core.session.model.RegisterViewModel;
import com.tokopedia.core.session.service.RegisterService;
import com.tokopedia.core.analytics.AppEventTracking;

import org.parceler.Parcels;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by m.normansyah on 1/25/16.
 */
public class RegisterNewImpl extends RegisterNew implements TextWatcher{
    RegisterService registerService;
    RegisterViewModel registerViewModel;
    SimpleFacebook simpleFacebook;
    LocalCacheHandler loginUuid;
    LocalCacheHandler providerListCache;
    RegisterInteractor facade;
    LocalCacheHandler cacheGTM;
    boolean isSelect;
    boolean isEmailEdit;
    boolean isSaving;
    boolean isChecked;


    public RegisterNewImpl(RegisterNewView view){
        super(view);
        registerService = new RegisterService();
        facade = RegisterInteractorImpl.createInstance(this);
    }

    @Override
    public void validateEmail(final Context context, final String email, final String password) {

//        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, context, RegisterApi.frenky+RegisterApi.VALIDATE_EMAIL_FRENKY)
//                .setIdentity()
//                .addParam(RegisterApi.USER_EMAIL, email)
//                .compileAllParam()
//                .finish();
//
//        RetrofitUtils.createRetrofit(RegisterApi.frenky).create(RegisterApi.class)
//                .validateEmailDev(
//                        NetworkCalculator.getContentMd5(networkCalculator),
//                        NetworkCalculator.getDate(networkCalculator),
//                        NetworkCalculator.getAuthorization(networkCalculator),
//                        NetworkCalculator.getxMethod(networkCalculator),
//                        NetworkCalculator.getUserId(context),
//                        NetworkCalculator.getDeviceId(context),
//                        NetworkCalculator.getHash(networkCalculator),
//                        NetworkCalculator.getDeviceTime(networkCalculator),
//                        email
//                ).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribe(
//                        new Subscriber<ValidateEmailData>() {
//                            @Override
//                            public void onCompleted() {
//                                Log.i(TAG, getMessageTAG() + "onCompleted()");
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.e(TAG, getMessageTAG() + e.getLocalizedMessage());
//                                view.showUnknownError(e);
//                            }
//
//                            @Override
//                            public void onNext(ValidateEmailData validateEmailData) {
//                                Log.d(TAG, getMessageTAG()+validateEmailData);
//                                if (Integer.parseInt(validateEmailData.getData().getEmail_status()) == ValidateEmailData.Data.EMAIL_STATUS_REGISTERED) {
//                                            view.showErrorValidateEmail();
//                                        } else {
//                                            //[START] move to register next
//                                            view.enableDisableAllFieldsForEmailValidationForm(true);
//                                            view.moveToRegisterNext(email, password);
//                                            //[END] move to register next
//                                        }
//                            }
//                        }
//                );

        //lama
//        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.GET, context, TkpdBaseURL.User.URL_REGISTER+ RegisterApi.VALIDATE_EMAIL_PL)
//                .setIdentity()
//                .addParam(RegisterApi.USER_EMAIL, email)
//                .compileAllParam()
//                .finish();
//
//        compositeSubscription.add(
//                registerService.getApi().validateEmail(
//                        NetworkCalculator.getContentMd5(networkCalculator),
//                        NetworkCalculator.getDate(networkCalculator),
//                        NetworkCalculator.getAuthorization(networkCalculator),
//                        NetworkCalculator.getxMethod(networkCalculator),
//                        NetworkCalculator.getUserId(context),
//                        NetworkCalculator.getDeviceId(context),
//                        NetworkCalculator.getHash(networkCalculator),
//                        NetworkCalculator.getDeviceTime(networkCalculator),
//                        email
//                ).subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .unsubscribeOn(Schedulers.io())
//                        .subscribe(
//                                new Subscriber<ValidateEmailData>() {
//                                    @Override
//                                    public void onCompleted() {
//                                        Log.i(TAG, getMessageTAG() + "onCompleted()");
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        Log.e(TAG, getMessageTAG() + e.getLocalizedMessage());
//                                        view.showUnknownError(e);
//                                    }
//
//                                    @Override
//                                    public void onNext(ValidateEmailData validateEmailData) {
//                                        if (Integer.parseInt(validateEmailData.getData().getEmail_status()) == ValidateEmailData.Data.EMAIL_STATUS_REGISTERED) {
//                                            view.showErrorValidateEmail();
//                                        } else {
//                                            //[START] move to register next
//                                            view.enableDisableAllFieldsForEmailValidationForm(true);
//                                            view.moveToRegisterNext(email, password);
//                                            //[END] move to register next
//                                        }
//                                    }
//                                }
//                        )
//        );
        Map<String, String> params = new HashMap<>();
        params.put("email",email);
        facade.validateEmail(context, params , new RegisterInteractor.ValidateEmailListener() {
            @Override
            public void onSuccess(boolean isActive) {
                if(isActive){
                    view.showErrorValidateEmail();
                }else{
                    view.enableDisableAllFieldsForEmailValidationForm(true);
                    view.moveToRegisterNext(email,password);
                    sendGTMClick();
                    storeCacheGTM(AppEventTracking.GTMCacheKey.REGISTER_TYPE,
                            AppEventTracking.GTMCacheValue.EMAIL);
                }
            }

            @Override
            public void onError(String s) {
                view.showErrorValidateEmail(s);
            }

            @Override
            public void onTimeout() {
                view.showErrorValidateEmail(context.getString(R.string.default_request_error_timeout));
            }

            @Override
            public void onThrowable(Throwable e) {
                if(e instanceof UnknownHostException){
                    view.showErrorValidateEmail(context.getString(R.string.default_request_error_unknown));
                }else if(e instanceof SocketTimeoutException) {
                    view.showErrorValidateEmail(context.getString(R.string.default_request_error_timeout));
                }else{
                    view.showErrorValidateEmail(context.getString(R.string.msg_network_error));
                }
            }
        });
    }

    @Override
    public void saveData(HashMap<String, Object> data) {
        if(data.containsKey(EMAIL)){
            String email = (String)data.get(EMAIL);
            registerViewModel.setmEmail(email);
        }else if(data.containsKey(PASSWORD)){
            String password = (String)data.get(PASSWORD);
            registerViewModel.setmPassword(password);
        }else if(data.containsKey(IS_SELECT_FROM_AUTO_TEXT_VIEW)){
            isSelect = (boolean)data.get(IS_SELECT_FROM_AUTO_TEXT_VIEW);
        }else if(data.containsKey(IS_EMAIL_EDITTED_FOR_THE_FIRSTTIME)){
            isEmailEdit = (boolean)data.get(IS_EMAIL_EDITTED_FOR_THE_FIRSTTIME);
        }else if(data.containsKey(IS_SAVING)){
            isSaving = (boolean)data.get(IS_SAVING);
        }else if(data.containsKey(IS_CHECKED)){
            isChecked = (boolean)data.get(IS_CHECKED);
        }
    }

    @Override
    public void startLoginWithGoogle(Context context,String type, LoginGoogleModel loginGoogleModel) {
        if(type != null && type.equals(LoginModel.GoogleType) && loginGoogleModel != null){
            // dismiss progress
            view.showProgress(false);
            RegisterViewModel registerViewModel = new RegisterViewModel();

            // update data and UI
//            if(registerViewModel!=null){
                if(loginGoogleModel.getFullName()!=null){
//                    registerView.updateData(RegisterView.NAME, loginGoogleModel.getFullName());
                    registerViewModel.setmName(loginGoogleModel.getFullName());
                }
                if(loginGoogleModel.getGender().contains("male")){
//                    registerView.updateData(RegisterView.GENDER, RegisterViewModel.GENDER_MALE);
                    registerViewModel.setmGender(RegisterViewModel.GENDER_MALE);
                }else{
//                    registerView.updateData(RegisterView.GENDER, RegisterViewModel.GENDER_FEMALE);
                    registerViewModel.setmGender(RegisterViewModel.GENDER_FEMALE);
                }
                if(loginGoogleModel.getBirthday()!=null){
                    Log.d(TAG, getMessageTAG()+" need to verify birthday : "+loginGoogleModel.getBirthday());
                    registerViewModel.setDateText(loginGoogleModel.getBirthday());
                }
                if(loginGoogleModel.getEmail()!=null){
                    registerViewModel.setmEmail(loginGoogleModel.getEmail());
                }
            loginGoogleModel.setUuid(getUUID());

            Bundle bundle = new Bundle();
            bundle.putParcelable(DownloadService.LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(loginGoogleModel));
            bundle.putBoolean(DownloadService.IS_NEED_LOGIN, false);

            ((SessionView)context).sendDataFromInternet(DownloadService.LOGIN_GOOGLE, bundle);
            //[START] move to RegisterPassPhone
//            if(context!=null&&context instanceof SessionView){
//                ((SessionView)context).moveToRegisterThird(registerViewModel);
//            }
            //[END] move to RegisterPassPhone
//            }
        }
    }

    @Override
    public void loginFacebook(final Context context) {
        simpleFacebook = simpleFacebook.getInstance();
        Log.d("steven isLogin?", String.valueOf(simpleFacebook.isLogin()));
        if(simpleFacebook.isLogin()){
            simpleFacebook.logout(new OnLogoutListener() {
                @Override
                public void onLogout() {
                    Log.d("steven logout", "you are logged out");
                }
            });
        }

        Permission[] permissions = new Permission[] {
                Permission.EMAIL,
        };

        OnNewPermissionsListener onNewPermissionsListener = new OnNewPermissionsListener() {
            @Override
            public void onSuccess(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                Log.d("steven permissions succ", acceptedPermissions.toString());
                askToLogin(context);
            }

            @Override
            public void onCancel() {
                Log.d("steven permissions canc", "you are out");
                view.showProgress(false);
            }

            @Override
            public void onException(Throwable throwable) {
                Log.d("steven permissions excn", throwable.toString());
                view.showError(context.getString(R.string.msg_network_error));
                view.showProgress(false);
            }

            @Override
            public void onFail(String reason) {
                Log.d("steven permissions fail", reason);
                view.showProgress(false);
            }
        };

        simpleFacebook.requestNewPermissions(permissions, onNewPermissionsListener);
    }

    private void askToLogin(final Context context) {
        simpleFacebook.login(new OnLoginListener() {
            @Override
            public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                Profile.Properties properties = new Profile.Properties.Builder()
                        .add(Profile.Properties.ID)
                        .add(Profile.Properties.FIRST_NAME)
                        .add(Profile.Properties.GENDER)
                        .add(Profile.Properties.EMAIL)
                        .add(Profile.Properties.WORK)
                        .add(Profile.Properties.BIRTHDAY)
                        .add(Profile.Properties.NAME)
                        .build();
                simpleFacebook.getProfile(properties, new OnProfileListener() {
                    @Override
                    public void onComplete(Profile response) {
                        Log.e(TAG, getMessageTAG() + " start login to facebook !!");
                        super.onComplete(response);
                        LoginFacebookViewModel loginFacebookViewModel = new LoginFacebookViewModel();
                        loginFacebookViewModel.setFullName(response.getName());// 10
                        loginFacebookViewModel.setGender(response.getGender());// 7
                        setBirthday(loginFacebookViewModel,response.getBirthday());// 2
                        loginFacebookViewModel.setFbToken(simpleFacebook.getAccessToken().getToken());// 6
                        loginFacebookViewModel.setFbId(response.getId());// 8
                        loginFacebookViewModel.setEmail(response.getEmail());// 5
                        loginFacebookViewModel.setEducation(response.getEducation() + "");// 4
                        loginFacebookViewModel.setInterest(response.getRelationshipStatus());// 9
                        loginFacebookViewModel.setWork(response.getWork() + "");
                        Log.e(TAG, getMessageTAG() + " end login Facebook : " + loginFacebookViewModel);

                        if(context!=null&&context instanceof SessionView){
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(DownloadService.LOGIN_FACEBOOK_MODEL_KEY, Parcels.wrap(loginFacebookViewModel));
                            bundle.putBoolean(DownloadService.IS_NEED_LOGIN, false);


                            ((SessionView)context).sendDataFromInternet(DownloadService.REGISTER_FACEBOOK
                                    , bundle);
                        }
                        // dismiss progress
                        view.showProgress(false);

//                        RegisterViewModel registerViewModel = new RegisterViewModel();
//                        // update data and UI
////                        if(registerViewModel!=null){
//                            if(loginFacebookViewModel.getFullName()!=null){
//                                registerViewModel.setmName(loginFacebookViewModel.getFullName());
////                                registerView.updateData(RegisterView.NAME, loginFacebookViewModel.getFullName());
//                            }
//                            if(loginFacebookViewModel.getGender().contains("male")){
//                                registerViewModel.setmGender(RegisterViewModel.GENDER_MALE);
////                                registerView.updateData(RegisterView.GENDER, RegisterViewModel.GENDER_MALE);
//                            }else{
//                                registerViewModel.setmGender(RegisterViewModel.GENDER_FEMALE);
////                                registerView.updateData(RegisterView.GENDER, RegisterViewModel.GENDER_FEMALE);
//                            }
//                            if(loginFacebookViewModel.getBirthday()!=null){
//                                Log.d(TAG, getMessageTAG()+" need to verify birthday ");
//                                registerViewModel.setDateText(loginFacebookViewModel.getBirthday());
//                            }
//                            if(loginFacebookViewModel.getEmail()!=null){
//                                registerViewModel.setmEmail(loginFacebookViewModel.getEmail());
//                            }
////                        }
//
//                        if(response.getEmail()==null){
//                            Toast.makeText(context, "tidak bisa mendapatkan email dari facebook", Toast.LENGTH_LONG).show();
//                            return;
//                        }
//
//                        //[START] move to RegisterPassPhone
//                        if(context!=null&&context instanceof SessionView){
//                            ((SessionView)context).moveToRegisterThird(registerViewModel);
//                        }
                        //[END] move to RegisterPassPhone
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        super.onException(throwable);
                        Log.e(TAG, getMessageTAG()+" login facebook : "+throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onFail(String reason) {
                        super.onFail(reason);
                        Log.e(TAG, getMessageTAG() + " login facebook : " + reason);
                    }
                });
            }

            @Override
            public void onCancel() {
                view.showProgress(false);
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e(TAG, getMessageTAG()+" login facebook "+ throwable.getLocalizedMessage());
                view.showError(context.getString(R.string.msg_network_error));
                view.showProgress(false);
            }

            @Override
            public void onFail(String s) {
                Log.e(TAG, getMessageTAG()+" login facebook "+ s);
//                Toast.makeText(context, context.getString(R.string.message_verification_timeout), Toast.LENGTH_LONG).show();
                view.showProgress(false);
            }
        });
    }

    private void setBirthday(LoginFacebookViewModel loginFacebookViewModel, String birthday) {
        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(birthday);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(date!=null) loginFacebookViewModel.setBirthday(outputFormat.format(date));
    }

    @Override
    public void setData(Context context, int type, Bundle data) {
        switch (type){
            case DownloadService.LOGIN_ACCOUNTS_INFO:
                data.putString(UUID_KEY, getUUID());
                InfoModel infoModel = data.getParcelable(DownloadServiceConstant.INFO_BUNDLE);
                Parcelable parcelable = data.getParcelable(DownloadServiceConstant.EXTRA_TYPE);

                if (infoModel.isCreatedPassword()) {
                    ((SessionView) context).sendDataFromInternet(DownloadService.MAKE_LOGIN, data);
                } else {
                    CreatePasswordModel createPasswordModel = new CreatePasswordModel();
                    createPasswordModel = setModelFromParcelable(createPasswordModel,parcelable,infoModel);
                    data.putBoolean(DownloadServiceConstant.LOGIN_MOVE_REGISTER_THIRD, true);
                    data.putParcelable(DownloadServiceConstant.LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(createPasswordModel));
                    ((SessionView) context).moveToRegisterPassPhone(createPasswordModel, infoModel.getCreatePasswordList());
                }
            break;

            case DownloadServiceConstant.MAKE_LOGIN:
                view.showProgress(false);
                view.finishActivity();
                break;

            case DownloadServiceConstant.DISCOVER_LOGIN:
                view.removeProgressBar();
                LoginProviderModel loginProviderModel = data.getParcelable(DownloadServiceConstant.LOGIN_PROVIDER);
                view.showProvider(loginProviderModel.getProviders());
                break;
        }
    }

    private CreatePasswordModel setModelFromParcelable(CreatePasswordModel createPasswordModel, Parcelable parcelable, InfoModel infoModel) {
        if (Parcels.unwrap(parcelable) instanceof LoginGoogleModel) {
            LoginGoogleModel loginGoogleModel = Parcels.unwrap(parcelable);
            if (loginGoogleModel.getFullName() != null) {
                createPasswordModel.setFullName(loginGoogleModel.getFullName());
            }
            if (loginGoogleModel.getGender().contains("male")) {
                createPasswordModel.setGender(RegisterViewModel.GENDER_MALE + "");
            } else {
                createPasswordModel.setGender(RegisterViewModel.GENDER_FEMALE + "");
            }
            if (loginGoogleModel.getBirthday() != null) {
                createPasswordModel.setDateText(loginGoogleModel.getBirthday());
            }
            if (loginGoogleModel.getEmail() != null) {
                createPasswordModel.setEmail(loginGoogleModel.getEmail());
            }
        }else if(Parcels.unwrap(parcelable) instanceof LoginFacebookViewModel){
            LoginFacebookViewModel loginFacebookViewModel = Parcels.unwrap(parcelable);
            if (loginFacebookViewModel.getFullName() != null) {
                createPasswordModel.setFullName(loginFacebookViewModel.getFullName());
            }
            if (loginFacebookViewModel.getGender().contains("male")) {
                createPasswordModel.setGender(RegisterViewModel.GENDER_MALE + "");
            } else {
                createPasswordModel.setGender(RegisterViewModel.GENDER_FEMALE + "");
            }
            if (loginFacebookViewModel.getBirthday() != null) {
                createPasswordModel.setDateText(loginFacebookViewModel.getBirthday());
            }
            if (loginFacebookViewModel.getEmail() != null) {
                createPasswordModel.setEmail(loginFacebookViewModel.getEmail());
            }
        }else{
            createPasswordModel.setFullName(infoModel.getName());
            createPasswordModel.setEmail(infoModel.getEmail());
        }
        return createPasswordModel;
    }

    @Override
    public void downloadProviderLogin(Context context) {
//        ((SessionView)context).sendDataFromInternet(DownloadService.DISCOVER_LOGIN, new Bundle());
        facade.downloadProvider(context, new RegisterInteractor.DiscoverLoginListener() {
            @Override
            public void onSuccess(LoginProviderModel result) {
                view.removeProgressBar();
                view.showProvider(result.getProviders());
            }

            @Override
            public void onError(String s) {
                view.onMessageError(DownloadService.DISCOVER_LOGIN, s);
            }

            @Override
            public void onTimeout() {
                view.onMessageError(DownloadService.DISCOVER_LOGIN, "");
            }

            @Override
            public void onThrowable(Throwable e) {
                view.onMessageError(DownloadService.DISCOVER_LOGIN, "");
            }
        });
    }

    @Override
    public void loginWebView(Context context, Bundle data) {
        Bundle bundle = data;
        bundle.putBoolean(DownloadService.IS_NEED_LOGIN, false);

        ((SessionView)context).sendDataFromInternet(DownloadService.LOGIN_WEBVIEW, bundle);
    }

    @Override
    public void saveProvider(List<LoginProviderModel.ProvidersBean> listProvider) {
        String cache = new GsonBuilder().create().toJson(loadProvider());
        String listProviderString = new GsonBuilder().create().toJson(listProvider);
        if(!cache.equals(listProviderString)){
            providerListCache.putString(PROVIDER_CACHE_KEY, listProviderString);
            providerListCache.setExpire(3600);
            providerListCache.applyEditor();
        }
    }

    private List<LoginProviderModel.ProvidersBean> loadProvider(){
        String cache = providerListCache.getString(PROVIDER_CACHE_KEY);
        Type type = new TypeToken<List<LoginProviderModel.ProvidersBean>>(){}.getType();
        return new GsonBuilder().create().fromJson(cache, type);
    }

    @Override
    public void initData(@NonNull Context context) {
        if(isAfterRotate){
            view.setData(convertToMap(EMAIL, registerViewModel.getmEmail()));// 1. email
            view.setData(convertToMap(PASSWORD, registerViewModel.getmPassword()));// 2. password
            view.setData(convertToMap(IS_CHECKED, isChecked));
        }
        loginUuid = new LocalCacheHandler(context, LOGIN_UUID_KEY);

        getProvider(context);
    }

    @Override
    public void getProvider(Context context) {
        if(view.checkHasNoProvider()){
            view.addProgressBar();
            List<LoginProviderModel.ProvidersBean> providerList= loadProvider();
            if(providerList == null || providerListCache.isExpired()){
                downloadProviderLogin(context);
            }else {
                view.removeProgressBar();
                view.showProvider(providerList);
            }
        }
    }

    @Override
    public void unSubscribeFacade() {
        facade.unSubscribe();
    }

    @Override
    public void initCacheGTM(Context context) {
        cacheGTM = new LocalCacheHandler(context, AppEventTracking.GTM_CACHE);
        cacheGTM.putString(AppEventTracking.GTMCacheKey.SESSION_STATE,
                AppEventTracking.GTMCacheValue.REGISTER);
        cacheGTM.applyEditor();
    }

    @Override
    public void storeCacheGTM(String key, String value) {
        cacheGTM.putString(key, value);
        cacheGTM.applyEditor();
    }


    public static HashMap<String, Object> convertToMap(String key, Object value){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put(key, value);
        return temp;
    }

    @Override
    public void fetchArguments(Bundle argument) {

    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {
        registerViewModel = Parcels.unwrap(argument.getParcelable(DATA));
        isSelect = argument.getBoolean(IS_SELECT_FROM_AUTO_TEXT_VIEW);
        isEmailEdit = argument.getBoolean(IS_EMAIL_EDITTED_FOR_THE_FIRSTTIME);
        isSaving = argument.getBoolean(IS_SAVING);
        isChecked = argument.getBoolean(IS_CHECKED);
    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {
        argument.putParcelable(DATA, Parcels.wrap(registerViewModel));
        argument.putBoolean(IS_SELECT_FROM_AUTO_TEXT_VIEW, isSelect);
        argument.putBoolean(IS_EMAIL_EDITTED_FOR_THE_FIRSTTIME, isEmailEdit);
        argument.putBoolean(IS_SAVING, isSaving);
        argument.putBoolean(IS_CHECKED, isChecked);
    }

    @Override
    public void initDataInstance(Context context) {
        if(!isAfterRotate){
            registerViewModel = new RegisterViewModel();
        }
        providerListCache = new LocalCacheHandler(context,PROVIDER_LIST);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (before < s.length()) {
            // means user deleted character
            if (!isEmailEdit && (CommonUtils.EmailValidation(s.toString())) &&
                    !isSelect) {
                isEmailEdit = true;
                view.alertBox();// registerName.getText().toString()
            }
        }
        isSelect = false;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public String getUUID() {
        return loginUuid.getString(UUID_KEY, DEFAULT_UUID_VALUE);
    }

    private void sendGTMClick(){
        UnifyTracking.eventRegister(AppEventTracking.EventLabel.REGISTER_STEP_1);

    }
}
