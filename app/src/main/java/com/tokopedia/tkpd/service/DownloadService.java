package com.tokopedia.tkpd.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.data.DataManagerImpl;
import com.tkpd.library.utils.data.DataReceiver;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.SplashScreen;
import com.tokopedia.tkpd.analytics.nishikino.model.Authenticated;
import com.tokopedia.tkpd.database.model.Bank;
import com.tokopedia.tkpd.database.model.CategoryDB;
import com.tokopedia.tkpd.database.model.City;
import com.tokopedia.tkpd.database.model.District;
import com.tokopedia.tkpd.database.model.Province;
import com.tokopedia.tkpd.home.model.HotListModel;
import com.tokopedia.tkpd.home.presenter.HotList;
import com.tokopedia.tkpd.home.presenter.HotListImpl;
import com.tokopedia.tkpd.network.NetworkHandler;
import com.tokopedia.tkpd.network.apiservices.accounts.AccountsService;
import com.tokopedia.tkpd.network.apiservices.search.HotListService;
import com.tokopedia.tkpd.network.apiservices.user.InterruptActService;
import com.tokopedia.tkpd.network.apiservices.user.InterruptService;
import com.tokopedia.tkpd.network.apiservices.user.SessionService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ResponseStatus;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.network.retrofit.utils.MapNulRemover;
import com.tokopedia.tkpd.network.v4.NetworkConfig;
import com.tokopedia.tkpd.rxjava.RxUtils;
import com.tokopedia.tkpd.service.constant.DownloadServiceConstant;
import com.tokopedia.tkpd.session.model.AccountsModel;
import com.tokopedia.tkpd.session.model.AccountsParameter;
import com.tokopedia.tkpd.session.model.CreatePasswordModel;
import com.tokopedia.tkpd.session.model.ErrorModel;
import com.tokopedia.tkpd.session.model.InfoModel;
import com.tokopedia.tkpd.session.model.LoginBypassModel;
import com.tokopedia.tkpd.session.model.LoginBypassSuccessModel;
import com.tokopedia.tkpd.session.model.LoginEmailModel;
import com.tokopedia.tkpd.session.model.LoginFacebookViewModel;
import com.tokopedia.tkpd.session.model.LoginGoogleModel;
import com.tokopedia.tkpd.session.model.LoginInterruptErrorModel;
import com.tokopedia.tkpd.session.model.LoginInterruptModel;
import com.tokopedia.tkpd.session.model.LoginProviderModel;
import com.tokopedia.tkpd.session.model.LoginSecurityModel;
import com.tokopedia.tkpd.session.model.LoginThirdModel;
import com.tokopedia.tkpd.session.model.LoginViewModel;
import com.tokopedia.tkpd.session.model.OTPModel;
import com.tokopedia.tkpd.session.model.QuestionFormModel;
import com.tokopedia.tkpd.session.model.RegisterSuccessModel;
import com.tokopedia.tkpd.session.model.RegisterViewModel;
import com.tokopedia.tkpd.session.model.SecurityModel;
import com.tokopedia.tkpd.session.model.SecurityQuestionViewModel;
import com.tokopedia.tkpd.session.model.ShopRepModel;
import com.tokopedia.tkpd.session.model.TokenModel;
import com.tokopedia.tkpd.session.model.UserRepModel;
import com.tokopedia.tkpd.session.presenter.Login;
import com.tokopedia.tkpd.session.presenter.LoginImpl;
import com.tokopedia.tkpd.session.presenter.Register;
import com.tokopedia.tkpd.session.presenter.RegisterNext;
import com.tokopedia.tkpd.session.presenter.RegisterPassPhone;
import com.tokopedia.tkpd.session.presenter.SecurityQuestion;
import com.tokopedia.tkpd.session.subscriber.AccountSubscriber;
import com.tokopedia.tkpd.analytics.AppEventTracking;
import com.tokopedia.tkpd.util.PagingHandler;
import com.tokopedia.tkpd.util.PasswordGenerator;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.analytics.TrackingUtils;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * this is for critical data only for session
 * <p>
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class DownloadService extends IntentService implements DownloadServiceConstant, DataReceiver {
    public static final String TAG = "MNORMANSYAH";
    public static final String messageTAG = DownloadService.class.getSimpleName() + " ";

    private AuthService service;
    private Gson gson;
    private ResultReceiver receiver;
    private LocalCacheHandler cache;
    SessionHandler sessionHandler;
    CompositeSubscription compositeSubscription = new CompositeSubscription();

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.tokopedia.tkpd.action.FOO";
    private static final String ACTION_BAZ = "com.tokopedia.tkpd.action.BAZ";

    private static final String EXTRA_PARAM1 = "com.tokopedia.tkpd.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.tokopedia.tkpd.extra.PARAM2";

    //[REMOVE] after V4 already finished
    static String emailV2;
    static String passwordV2;
    static int loginType;
    static LoginGoogleModel loginGoogleModel;
    static LoginFacebookViewModel loginFacebookViewModel;
    static int loginVia;

    public static void startDownload(Context context, DownloadResultReceiver receiver, Bundle bundle, int type) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, DownloadService.class);
        intent.putExtras(bundle);
        boolean isNeedLogin = bundle.getBoolean(IS_NEED_LOGIN, false);
        if (receiver != null)
            intent.putExtra(DownloadService.RECEIVER, receiver);

        // set mandatory param
        intent.putExtra(TYPE, type);
        intent.putExtra(IS_NEED_LOGIN, isNeedLogin);

        /* Send optional extras to Download IntentService */
        switch (type) {
            case DownloadServiceConstant.FETCH_DEPARTMENT:
                Log.d(TAG, CategoryDB.class.getSimpleName() + " sedang diambil !!!");
                break;
            case REGISTER_THIRD:
            case REGISTER:
                RegisterViewModel registerViewModel = Parcels.unwrap(bundle.getParcelable(REGISTER_MODEL_KEY));
                Log.d(TAG, Register.class.getSimpleName() + " register : " + registerViewModel);
                intent.putExtra(REGISTER_MODEL_KEY, Parcels.wrap(registerViewModel));
                break;
            case ANSWER_SECURITY_QUESTION:
                SecurityQuestionViewModel answer = Parcels.unwrap(bundle.getParcelable(ANSWER_QUESTION_MODEL));
                Log.d(TAG, SecurityQuestion.class.getSimpleName() + " try to answer security question : " + answer);
                intent.putExtra(ANSWER_QUESTION_MODEL, Parcels.wrap(answer));
                break;
            case REQUEST_OTP:
                SecurityQuestionViewModel securityQuestionViewModel = Parcels.unwrap(bundle.getParcelable(REQUEST_OTP_MODEL));
                Log.d(TAG, SecurityQuestion.class.getSimpleName() + " request otp " + securityQuestionViewModel);
                intent.putExtra(REQUEST_OTP_MODEL, Parcels.wrap(securityQuestionViewModel));
                break;
            case SECURITY_QUESTION_GET:
                securityQuestionViewModel = Parcels.unwrap(bundle.getParcelable(SECURITY_QUESTION_GET_MODEL));
                Log.d(TAG, SecurityQuestion.class.getSimpleName() + " try to fetch security question form : " + securityQuestionViewModel);
                intent.putExtra(SECURITY_QUESTION_GET_MODEL, Parcels.wrap(securityQuestionViewModel));
                break;
            case LOGIN_FACEBOOK:
            case REGISTER_FACEBOOK:
                LoginFacebookViewModel loginFacebookViewModel = Parcels.unwrap(bundle.getParcelable(LOGIN_FACEBOOK_MODEL_KEY));
                Log.d(TAG, LoginImpl.class.getSimpleName() + " try to login facebook : " + loginFacebookViewModel);
                intent.putExtra(LOGIN_FACEBOOK_MODEL_KEY, Parcels.wrap(loginFacebookViewModel));
                break;
            case LOGIN_GOOGLE:
                LoginGoogleModel loginGoogleModel = Parcels.unwrap(bundle.getParcelable(LOGIN_GOOGLE_MODEL_KEY));
                Log.d(TAG, LoginImpl.class.getSimpleName() + " try to login google : " + loginGoogleModel);
                intent.putExtra(LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(loginGoogleModel));
                break;
            case REGISTER_THIRD_LOGIN:
            case REGISTER_LOGIN:
            case LOGIN_EMAIL:
                LoginViewModel loginViewModel = Parcels.unwrap(bundle.getParcelable(LOGIN_VIEW_MODEL_KEY));
                Log.d(TAG, LoginImpl.class.getSimpleName() + " try to login email : " + loginViewModel);
                intent.putExtra(LOGIN_VIEW_MODEL_KEY, Parcels.wrap(loginViewModel));
                break;
            case HOTLIST:
                int page = bundle.getInt(PAGE_KEY);
                int perpage = bundle.getInt(PER_PAGE_KEY);
                Log.d(TAG, HotList.class.getSimpleName() + "get Hot List page " + page + " perpage " + perpage + " !!!");
                intent.putExtra(PAGE_KEY, page);
                intent.putExtra(PER_PAGE_KEY, perpage);
                break;
            case LOGIN_BYPASS:
                LoginBypassModel loginBypassModel = Parcels.unwrap(bundle.getParcelable(LOGIN_BYPASS_MODEL_KEY));
                Log.d(TAG, LoginImpl.class.getSimpleName() + " try to bypass : " + loginBypassModel);
                intent.putExtra(LOGIN_BYPASS_MODEL_KEY, Parcels.wrap(loginBypassModel));
                break;
            case REGISTER_PASS_PHONE:
                CreatePasswordModel model = Parcels.unwrap(bundle.getParcelable(CREATE_PASSWORD_MODEL_KEY));
                Log.d(TAG, LoginImpl.class.getSimpleName() + " create password : " + model);
                intent.putExtra(CREATE_PASSWORD_MODEL_KEY, Parcels.wrap(model));
                break;
            case LOGIN_ACCOUNTS_TOKEN:
                LoginViewModel xxx = Parcels.unwrap(bundle.getParcelable(LOGIN_VIEW_MODEL_KEY));
                Log.d(TAG, LoginImpl.class.getSimpleName() + " try to login email : " + xxx);
                intent.putExtra(LOGIN_VIEW_MODEL_KEY, Parcels.wrap(xxx));
                break;
            case LOGIN_ACCOUNTS_INFO:
                intent.putExtra(EXTRA_PARAM1, bundle.getParcelable(EXTRA_TYPE));
                intent.putExtra(TOKEN_BUNDLE, bundle.getParcelable(TOKEN_BUNDLE));
                break;
            case MAKE_LOGIN:
                intent.putExtra(Login.UUID_KEY, bundle.getString(Login.UUID_KEY));
                intent.putExtra(PROFILE_BUNDLE, bundle.getParcelable(PROFILE_BUNDLE));
                break;
            case LOGIN_WEBVIEW:
                intent.putExtra(Login.CODE, bundle.getString("code"));
                intent.putExtra(Login.REDIRECT_URI, bundle.getString("server") + bundle.getString("path"));
                break;
            case DISCOVER_LOGIN:
                break;
            case RESET_PASSWORD:
                intent.putExtra(Login.EMAIL, bundle.getString("email"));
                break;
            default:
                throw new RuntimeException("unknown type for starting download !!!");
        }

        context.startService(intent);
    }

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, messageTAG + " Service Started!");

        receiver = intent.getParcelableExtra(RECEIVER);
        int type = intent.getIntExtra(TYPE, INVALID_TYPE);
        gson = new GsonBuilder().create();
        sessionHandler = new SessionHandler(getApplicationContext());
        boolean isNeedLogin = intent.getBooleanExtra(IS_NEED_LOGIN, false);

        switch (type) {
            case DownloadServiceConstant.FETCH_DEPARTMENT:
                Bundle running = new Bundle();
                running.putInt(TYPE, type);
                receiver.send(STATUS_RUNNING, running);
                DataManagerImpl.getDataManager()
                        .getListDepartment2(this.getApplication(),
                                this, 0, true);
                break;
            case HOTLIST:
                /* Update UI: Download Service is Running */
                running = new Bundle();
                running.putInt(TYPE, type);
                receiver.send(STATUS_RUNNING, running);
                int page = intent.getIntExtra(PAGE_KEY, 1);
                Map<String, String> params = new HashMap<String, String>();
                params.put(HotList.QUERY_KEY, "");
                params.put(HotList.PAGE_KEY, page + "");
                params.put(HotList.PER_PAGE_KEY, HotList.PER_PAGE_VALUE + "");
                service = new HotListService();
                ((HotListService) service).getApi().getHotList(AuthUtil.generateParams(getApplicationContext(), params))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type))
                ;
                break;
            case REGISTER_THIRD_LOGIN:
            case REGISTER_LOGIN:
            case LOGIN_EMAIL:
                /* Update UI: Download Service is Running */
                running = new Bundle();
                running.putInt(TYPE, type);
                running.putBoolean(LOGIN_SHOW_DIALOG, true);
                receiver.send(STATUS_RUNNING, running);
                LoginViewModel loginViewModel = Parcels.unwrap(intent.getParcelableExtra(LOGIN_VIEW_MODEL_KEY));

                emailV2 = loginViewModel.getUsername();
                passwordV2 = loginViewModel.getPassword();
                loginType = LOGIN_EMAIL;

                loginV2(loginViewModel.getUuid());

                service = new SessionService();
                params = new HashMap<>();
                params.put(Login.USER_EMAIL, loginViewModel.getUsername());
                params.put(Login.USER_PASSWORD, loginViewModel.getPassword());
                params.put(Login.UUID_KEY, loginViewModel.getUuid());
                ((SessionService) service).getApi().login(AuthUtil.generateParams(getApplicationContext(), params))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));
                break;

            case DISCOVER_LOGIN:
                running = new Bundle();
                running.putInt(TYPE, type);
                receiver.send(STATUS_RUNNING, running);

                Bundle bundle = new Bundle();
                bundle.putBoolean(AccountsService.USING_HMAC, true);
                bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

                AccountsService accountsService = new AccountsService(bundle);
                accountsService.getApi().discoverLogin()
                        .subscribe(new Subscriber(type));
                break;

            case LOGIN_ACCOUNTS_TOKEN:
                running = new Bundle();
                running.putInt(TYPE, type);
                running.putBoolean(LOGIN_SHOW_DIALOG, true);
                receiver.send(STATUS_RUNNING, running);
                LoginViewModel xxx = Parcels.unwrap(intent.getParcelableExtra(LOGIN_VIEW_MODEL_KEY));
                emailV2 = xxx.getUsername();
                passwordV2 = xxx.getPassword();
                loginType = LOGIN_EMAIL;
                loginVia = Login.EmailType;

                AccountsParameter data = new AccountsParameter();
                data.setEmail(xxx.getUsername());
                data.setPassword(xxx.getPassword());
                data.setLoginType(LOGIN_EMAIL);
                data.setGrantType(Login.GRANT_PASSWORD);
                data.setUUID(xxx.getUuid());
                handleAccounts(data);

                break;

            case LOGIN_ACCOUNTS_INFO:
                running = new Bundle();
                running.putInt(TYPE, type);
                running.putBoolean(LOGIN_SHOW_DIALOG, true);
                receiver.send(STATUS_RUNNING, running);
                Parcelable parcelable = intent.getParcelableExtra(EXTRA_PARAM1);

                String authKey = sessionHandler.getAccessToken(this);
                authKey = sessionHandler.getTokenType(this) + " " + authKey;

                bundle = new Bundle();
                bundle.putString(AccountsService.AUTH_KEY, authKey);

                Map<String, String> map = new HashMap<>();
                map = AuthUtil.generateParams(getApplicationContext(), map);

                accountsService = new AccountsService(bundle);
                accountsService.getApi().getInfo(map)
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new AccountSubscriber(type, receiver, sessionHandler, parcelable));
                break;

            case MAKE_LOGIN:
                loginV2(intent.getStringExtra(Login.UUID_KEY));

                running = new Bundle();
                running.putInt(TYPE, type);
                running.putBoolean(LOGIN_SHOW_DIALOG, true);
                receiver.send(STATUS_RUNNING, running);
                params = new HashMap<>();
                params.put(Login.UUID_KEY, intent.getStringExtra(Login.UUID_KEY));
                params.put(Login.USER_ID, SessionHandler.getTempLoginSession(this));
                params = AuthUtil.generateParams(this, params);

                authKey = sessionHandler.getAccessToken(this);
                authKey = sessionHandler.getTokenType(this) + " " + authKey;

                bundle = new Bundle();
                bundle.putString(AccountsService.AUTH_KEY, authKey);
                bundle.putString(AccountsService.WEB_SERVICE, AccountsService.WS);

                accountsService = new AccountsService(bundle);
                accountsService.getApi().makeLogin(params)
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));


                break;

            case LOGIN_WEBVIEW:
                running = new Bundle();
                running.putInt(TYPE, type);
                running.putBoolean(LOGIN_SHOW_DIALOG, true);
                receiver.send(STATUS_RUNNING, running);
                params = new HashMap<>();
                params = AuthUtil.generateParams(this, params);
                params.put(Login.CODE, intent.getStringExtra(Login.CODE));
                params.put(Login.GRANT_TYPE, Login.GRANT_WEBVIEW);
                params.put(Login.REDIRECT_URI,"https://"+ intent.getStringExtra(Login.REDIRECT_URI));
                loginVia = Login.WebViewType;

                data = new AccountsParameter();
                data.setCode(intent.getStringExtra(Login.CODE));
                data.setRedirectUri("https://" + intent.getStringExtra(Login.REDIRECT_URI));
                data.setGrantType(Login.GRANT_WEBVIEW);
                data.setUUID(intent.getStringExtra(Login.UUID_KEY));
                handleAccounts(data);
                break;

            case LOGIN_GOOGLE:
                /* Update UI: Download Service is Running */
                running = new Bundle();
                running.putInt(TYPE, type);
                running.putBoolean(LOGIN_SHOW_DIALOG, true);
                receiver.send(STATUS_RUNNING, running);
                LoginGoogleModel loginGoogleModel = Parcels.unwrap(intent.getParcelableExtra(LOGIN_GOOGLE_MODEL_KEY));

                //[REMOVE] for sake of V2
                loginType = LOGIN_GOOGLE;
                this.loginGoogleModel = loginGoogleModel;

                loginThirdAppV2(loginGoogleModel);
                data = new AccountsParameter();
                data.setGrantType(Login.GRANT_SDK);
                data.setSocialType(Login.GooglePlusType);
                data.setParcelable(Parcels.wrap(loginGoogleModel));
                data.setUUID(loginGoogleModel.getUuid());
                handleAccounts(data);

                break;
            case LOGIN_FACEBOOK:
            case REGISTER_FACEBOOK:
                /* Update UI: Download Service is Running */
                running = new Bundle();
                running.putInt(TYPE, type);
                running.putBoolean(LOGIN_SHOW_DIALOG, true);
                receiver.send(STATUS_RUNNING, running);
                LoginFacebookViewModel loginFacebookViewModel = Parcels.unwrap(intent.getParcelableExtra(LOGIN_FACEBOOK_MODEL_KEY));

                //[REMOVE] for sake of V2
                loginType = LOGIN_FACEBOOK;
                this.loginFacebookViewModel = loginFacebookViewModel;
                loginThirdAppV2(loginFacebookViewModel);

                data = new AccountsParameter();
                data.setGrantType(Login.GRANT_SDK);
                data.setSocialType(Login.FacebookType);
                data.setParcelable(Parcels.wrap(loginFacebookViewModel));
                data.setUUID(loginFacebookViewModel.getUuid());
                handleAccounts(data);
                break;
            case SECURITY_QUESTION_GET:
                running = new Bundle();
                running.putInt(TYPE, type);
                running.putBoolean(SECURITY_QUESTION_LOADING, true);
                receiver.send(STATUS_RUNNING, running);
                SecurityQuestionViewModel securityQuestionViewModel = Parcels.unwrap(intent.getParcelableExtra(SECURITY_QUESTION_GET_MODEL));
                params = new HashMap<>();
                params.put(SecurityQuestion.USER_CHECK_SECURITY_ONE, securityQuestionViewModel.getSecurity1() + "");
                params.put(SecurityQuestion.USER_CHECK_SECURITY_TWO, securityQuestionViewModel.getSecurity2() + "");
                params.put("user_id", SessionHandler.getTempLoginSession(getApplicationContext()));
                service = new InterruptService();
                ((InterruptService) service).getApi().getQuestionForm(AuthUtil.generateParams(getApplicationContext(), params, SessionHandler.getTempLoginSession(getApplicationContext())))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));
                break;
            case ANSWER_SECURITY_QUESTION:
                /* Update UI: Download Service is Running */
                running = new Bundle();
                running.putInt(TYPE, type);
                running.putBoolean(SECURITY_QUESTION_LOADING, true);
                receiver.send(STATUS_RUNNING, running);
                securityQuestionViewModel = Parcels.unwrap(intent.getParcelableExtra(ANSWER_QUESTION_MODEL));
                params = new HashMap<>();
                params.put(SecurityQuestion.ANSWER, securityQuestionViewModel.getvAnswer());
                params.put(SecurityQuestion.QUESTION, securityQuestionViewModel.getQuestion());
                params.put(SecurityQuestion.USER_CHECK_SECURITY_ONE, securityQuestionViewModel.getSecurity1() + "");
                params.put(SecurityQuestion.USER_CHECK_SECURITY_TWO, securityQuestionViewModel.getSecurity2() + "");
                params.put("user_id", SessionHandler.getTempLoginSession(getApplicationContext()));
                service = new InterruptActService();
                ((InterruptActService) service).getApi().answerQuestion(AuthUtil.generateParams(getApplicationContext(), params, SessionHandler.getTempLoginSession(getApplicationContext())))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));
                break;
            case REQUEST_OTP:
                /* Update UI: Download Service is Running */
                running = new Bundle();
                running.putInt(TYPE, type);
                running.putBoolean(SECURITY_QUESTION_LOADING, true);
                receiver.send(STATUS_RUNNING, running);
                securityQuestionViewModel = Parcels.unwrap(intent.getParcelableExtra(REQUEST_OTP_MODEL));
                params = new HashMap<>();
                params.put(SecurityQuestion.USER_CHECK_SECURITY_TWO, securityQuestionViewModel.getSecurity2() + "");
                params.put("user_id", SessionHandler.getTempLoginSession(getApplicationContext()));
                params = AuthUtil.generateParams(getApplicationContext(), params, SessionHandler.getTempLoginSession(getApplicationContext()));
                service = new InterruptActService();
                ((InterruptActService) service).getApi().requestOTP(params)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));
                break;

            case REGISTER_THIRD:
            case REGISTER:
                /* Update UI: Download Service is Running */
                running = new Bundle();
                running.putInt(TYPE, type);
                running.putBoolean(REGISTER_QUESTION_LOADING, true);
                receiver.send(STATUS_RUNNING, running);

                RegisterViewModel registerViewModel = Parcels.unwrap(intent.getParcelableExtra(REGISTER_MODEL_KEY));

                params = new HashMap<>();
                params.put(RegisterNext.BIRTHDAY, registerViewModel.getmDateDay() + "");
                params.put(RegisterNext.BIRTHMONTH, registerViewModel.getmDateMonth() + "");
                params.put(RegisterNext.BIRTHYEAR, registerViewModel.getmDateYear() + "");

                params.put(RegisterNext.CONFIRM_PASSWORD, registerViewModel.getmConfirmPassword());
                params.put(RegisterNext.EMAIL, registerViewModel.getmEmail());
                params.put(RegisterNext.FACEBOOK_USERID, "");
                params.put(RegisterNext.FULLNAME, registerViewModel.getmName());
                params.put(RegisterNext.GENDER, registerViewModel.getmGender() + "");
                params.put(RegisterNext.PASSWORD, registerViewModel.getmPassword());
                params.put(RegisterNext.PHONE, registerViewModel.getmPhone());
                params.put(RegisterNext.IS_AUTO_VERIFY, (registerViewModel.isAutoVerify() ? 1 : 0) + "");// change to this "1" to auto verify

                bundle = new Bundle();
                bundle.putBoolean(AccountsService.USING_HMAC, true);
                bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

                accountsService = new AccountsService(bundle);
                accountsService.getApi().doRegister(AuthUtil.generateParams(getApplicationContext(), params))
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));

                break;
            case LOGIN_BYPASS:
                service = new SessionService();
                params = new HashMap<>();
                LoginBypassModel loginBypassModel = Parcels.unwrap(intent.getParcelableExtra(LOGIN_BYPASS_MODEL_KEY));

                params.put("user_id", loginBypassModel.getUserID());
                params.put("device_id", loginBypassModel.getDeviceID());

                ((SessionService) service).getApi().loginBypass(AuthUtil.generateParams(getApplicationContext(), params))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));
                break;
            case REGISTER_PASS_PHONE:
                running = new Bundle();
                running.putInt(TYPE, type);
                running.putBoolean(LOGIN_SHOW_DIALOG, true);
                receiver.send(STATUS_RUNNING, running);
                CreatePasswordModel model = Parcels.unwrap(intent.getParcelableExtra(CREATE_PASSWORD_MODEL_KEY));

                params = new HashMap<>();

                params.put(RegisterPassPhone.BIRTHDAY, String.valueOf(model.getBdayDay()));
                params.put(RegisterPassPhone.BIRTHMONTH, String.valueOf(model.getBdayMonth()));
                params.put(RegisterPassPhone.BIRTHYEAR, String.valueOf(model.getBdayYear()));

                params.put(RegisterPassPhone.CONFIRM_PASSWORD, model.getConfirmPass());
                params.put(RegisterPassPhone.NEW_PASSWORD, model.getNewPass());
                params.put(RegisterPassPhone.MSISDN, model.getMsisdn());
                params.put(RegisterPassPhone.FULLNAME, model.getFullName());
                params.put(RegisterPassPhone.GENDER, model.getGender());
                params.put(RegisterPassPhone.REGISTER_TOS, model.getRegisterTos());
                params.put(Login.USER_ID, SessionHandler.getTempLoginSession(this));

                authKey = sessionHandler.getAccessToken(this);
                authKey = sessionHandler.getTokenType(this) + " " + authKey;

                bundle = new Bundle();
                bundle.putString(AccountsService.AUTH_KEY, authKey);

                params = AuthUtil.generateParams(getApplicationContext(), params);

                accountsService = new AccountsService(bundle);
                accountsService.getApi().createPassword(params)
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(type));

                break;

            case RESET_PASSWORD:
                params = new HashMap<>();

                params.put(Login.EMAIL, intent.getStringExtra(Login.EMAIL));
                params = AuthUtil.generateParams(getApplicationContext(), params);

                bundle = new Bundle();
                bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);
                bundle.putBoolean(AccountsService.USING_HMAC, true);

                accountsService = new AccountsService(bundle);
                accountsService.getApi().resetPassword(params)
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber(type));

                break;

            default:

                break;
        }
    }

    @Override
    public CompositeSubscription getSubscription() {
        return RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    @Override
    public void setDistricts(List<District> districts) {
        throw new RuntimeException("Not supported yet");
    }

    @Override
    public void setCities(List<City> cities) {
        throw new RuntimeException("Not supported yet");
    }

    @Override
    public void setProvinces(List<Province> provinces) {
        // step 3- get list shipping city
        DataManagerImpl.getDataManager().getListShippingCity(this, this);
    }

    @Override
    public void setBank(List<Bank> banks) {
        throw new RuntimeException("Not supported yet");
    }

    @Override
    public void setDepartments(List<CategoryDB> departments) {
        // step-2 get provinces name
        DataManagerImpl.getDataManager().getListProvince(this, this);
    }

    @Override
    public void setShippingCity(List<District> districts) {
        LocalCacheHandler cacheHandler = new LocalCacheHandler(
                DownloadService.this,
                FETCH_DEPARTMENT_
        );
        cacheHandler.setExpire(SplashScreen.DAYS_IN_SECONDS);

        Bundle finish = new Bundle();
        finish.putInt(TYPE, DownloadServiceConstant.FETCH_DEPARTMENT);
        receiver.send(STATUS_FINISHED, finish);
    }

    @Override
    public void onNetworkError(String message) {
        Bundle resultData = new Bundle();
        resultData.putInt(TYPE, DownloadServiceConstant.FETCH_DEPARTMENT);
        //                                        resultData.putInt(NETWORK_ERROR_FLAG, 10101);
        resultData.putString(MESSAGE_ERROR_FLAG, message);
        receiver.send(STATUS_ERROR, resultData);
    }

    @Override
    public void onMessageError(String message) {
        Bundle resultData = new Bundle();
        resultData.putInt(TYPE, DownloadServiceConstant.FETCH_DEPARTMENT);
//                                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
        resultData.putString(MESSAGE_ERROR_FLAG, message);
        receiver.send(STATUS_ERROR, resultData);
    }

    @Override
    public void onUnknownError(String message) {
        Bundle resultData = new Bundle();
        resultData.putInt(TYPE, DownloadServiceConstant.FETCH_DEPARTMENT);
//                                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
        resultData.putString(MESSAGE_ERROR_FLAG, message);
        receiver.send(STATUS_ERROR, resultData);
    }

    @Override
    public void onTimeout() {
        Bundle resultData = new Bundle();
        resultData.putInt(TYPE, DownloadServiceConstant.FETCH_DEPARTMENT);
//                                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
        resultData.putString(MESSAGE_ERROR_FLAG, "Time Out");
        receiver.send(STATUS_ERROR, resultData);
    }

    @Override
    public void onFailAuth() {
        Bundle resultData = new Bundle();
        resultData.putInt(TYPE, DownloadServiceConstant.FETCH_DEPARTMENT);
//                                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
        resultData.putString(MESSAGE_ERROR_FLAG, "On Fail Auth");
        receiver.send(STATUS_ERROR, resultData);
    }


    private class Subscriber extends rx.Subscriber<Response<TkpdResponse>> {
        int type;
        ErrorListener listener;

        public Subscriber(int type) {
            this.type = type;
            listener = new ErrorListener(type);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, messageTAG + e.getLocalizedMessage());
//            if(e.getLocalizedMessage().contains("Unable to resolve host")){
            if (e instanceof UnknownHostException) {
                listener.noConnection();
            } else if (e instanceof SocketTimeoutException) {
                listener.onTimeout();
            } else if (e instanceof IOException) {
                listener.onServerError();
            } else {
                listener.onUnknown();
            }
//            }
        }

        @Override
        public void onNext(Response<TkpdResponse> responseData) {
            if (responseData.isSuccessful()) {
                TkpdResponse response = responseData.body();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.getStringData());
                } catch (JSONException je) {
                    Log.e(TAG, messageTAG + je.getLocalizedMessage());
                }
                if (!response.isError()) {
                    switch (type) {
                        case REGISTER_THIRD_LOGIN:
                        case REGISTER_LOGIN:
                        case LOGIN_EMAIL:
                            LoginEmailModel loginEmailModel = null;
                            LoginSecurityModel loginSecurityModel = null;
                            Bundle result = new Bundle();
                            result.putInt(TYPE, type);
                            if (jsonObject.optBoolean("is_login", false) == true) {
                                // parse success json
                                loginEmailModel = (LoginEmailModel) parseJSON(LOGIN_EMAIL, jsonObject);
                                // if isLogin is false ( default LoginModel value ) or other default value
                                sendAuthenticateGTMEvent(loginEmailModel);
                                CommonUtils.dumper("appdata login " + jsonObject);
                                sendLocalyticsUserAttr(loginEmailModel.getUserID() + "", loginEmailModel.getFullName(), "");
                                if (loginEmailModel.isLogin()) {
                                    sessionHandler.SetLoginSession(loginEmailModel.isLogin(), loginEmailModel.getUserID() + "",
                                            loginEmailModel.getFullName(), loginEmailModel.getShopId() + "",
                                            loginEmailModel.getMsisdnIsVerified());
                                    sessionHandler.setGoldMerchant(getApplicationContext(), loginEmailModel.getShopIsGold());
                                    // don't have security question the leave it blank
                                    // don't have activation resent flag, leave it false
                                    result.putBoolean(LOGIN_MOVE_SECURITY, false);
                                    result.putBoolean(LOGIN_ACTIVATION_RESENT, false);
                                    result.putInt(VALIDATION_OF_DEVICE_ID, loginEmailModel.getIsRegisterDevice());
                                }
                            } else {
                                // parse enter security json
                                loginSecurityModel = (LoginSecurityModel) parseJSON(SECURITY, jsonObject);
                                // if isLogin is false ( default LoginModel value ) or other default value
                                if (loginSecurityModel.getUserId() != 0) {
                                    // save user id
                                    sessionHandler.setTempLoginSession(Integer.toString(loginSecurityModel.getUserId()));
                                    //[START] Save Security Question
                                    result.putParcelable(LOGIN_SECURITY_QUESTION_DATA, Parcels.wrap(loginSecurityModel));
                                    result.putBoolean(LOGIN_MOVE_SECURITY, true);
                                    result.putBoolean(LOGIN_ACTIVATION_RESENT, false);
                                }
                            }

                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case DISCOVER_LOGIN:
                            result = new Bundle();
                            result.putInt(TYPE, type);
                            Log.d("steven", "berhasil discover login");
                            LoginProviderModel loginProviderModel = new GsonBuilder().create()
                                    .fromJson(jsonObject.toString(), LoginProviderModel.class);
                            result.putParcelable(LOGIN_PROVIDER, loginProviderModel);
                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case MAKE_LOGIN:
                            result = new Bundle();
                            result.putInt(TYPE, type);
                            SecurityModel securityModel = new GsonBuilder().create()
                                    .fromJson(jsonObject.toString(), SecurityModel.class);
                            if (securityModel.getIs_login().equals(false) || securityModel.getIs_login().equals("false")) {
                                // save user id
                                sessionHandler.setTempLoginSession(Integer.toString(securityModel.getUser_id()));
                                //[START] Save Security Question
                                result.putParcelable(LOGIN_SECURITY_QUESTION_DATA, securityModel);
                                result.putBoolean(LOGIN_MOVE_SECURITY, true);
                                result.putBoolean(LOGIN_ACTIVATION_RESENT, false);
                            } else {
                                AccountsModel accountsModel = new GsonBuilder()
                                        .create().fromJson(jsonObject.toString(), AccountsModel.class);
                                Log.d("steven", "berhasil make login");

                                sessionHandler.SetLoginSession(Boolean.parseBoolean(accountsModel.getIsLogin()),
                                        accountsModel.getUserId() + "",
                                        accountsModel.getFullName(), accountsModel.getShopId() + "",
                                        accountsModel.getMsisdnIsVerifiedBoolean());
                                result.putBoolean(LOGIN_MOVE_SECURITY, false);
                                result.putBoolean(LOGIN_ACTIVATION_RESENT, false);
                                result.putInt(VALIDATION_OF_DEVICE_ID, accountsModel.getIsRegisterDevice());
                                sessionHandler.setGoldMerchant(getApplicationContext(), accountsModel.getShopIsGold());
                            }

                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case LOGIN_FACEBOOK:
                        case LOGIN_GOOGLE:
                            result = new Bundle();
                            result.putInt(TYPE, LOGIN_GOOGLE);

                            if (jsonObject.optBoolean("is_login", false)) {
                                // parse success json
                                LoginThirdModel loginThirdModel = (LoginThirdModel) parseJSON(LOGIN_GOOGLE, jsonObject);
                                sendAuthenticateGTMEvent(loginThirdModel);
                                CommonUtils.dumper("appdata login thirdParty " + jsonObject);
                                // if isLogin is false ( default LoginModel value ) or other default value
                                if (loginThirdModel.isLogin()) {// && model.getAllowLogin()!=LoginModel.FORBIDEN_TO_LOGIN
                                    sessionHandler.SetLoginSession(loginThirdModel.isLogin(), loginThirdModel.getUserID() + "",
                                            loginThirdModel.getFullName(), loginThirdModel.getShopId() + "",
                                            loginThirdModel.isMsisdnVerified());
                                    sessionHandler.setGoldMerchant(getApplicationContext(), loginThirdModel.getShopIsGold());
                                    // don't have security question the leave it blank
                                    // don't have activation resent flag, leave it false
                                    result.putBoolean(LOGIN_MOVE_SECURITY, false);
                                    result.putBoolean(LOGIN_ACTIVATION_RESENT, false);
                                    result.putInt(VALIDATION_OF_DEVICE_ID, loginThirdModel.getIsRegisterDevice());
                                }

                                if (type == LOGIN_FACEBOOK) {
                                    sendLocalyticsUserAttr(loginThirdModel.getUserID() + "", loginThirdModel.getFullName(), loginFacebookViewModel.getEmail());
                                } else if (type == LOGIN_GOOGLE) {
                                    sendLocalyticsUserAttr(loginThirdModel.getUserID() + "", loginThirdModel.getFullName(), loginGoogleModel.getEmail());
                                }

                                if (jsonObject.optInt("status", 0) == 1) {

                                    CreatePasswordModel createPasswordModel = new CreatePasswordModel();

                                    if (loginGoogleModel != null) {
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
                                    }
                                    result.putBoolean(LOGIN_MOVE_REGISTER_THIRD, true);
                                    result.putParcelable(LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(createPasswordModel));

                                }

                            } else {
                                // parse enter security json
                                loginSecurityModel = (LoginSecurityModel) parseJSON(SECURITY, jsonObject);
                                // if isLogin is false ( default LoginModel value ) or other default value
                                if (loginSecurityModel.getUserId() != 0) {
                                    // save user id
                                    sessionHandler.setTempLoginSession(Integer.toString(loginSecurityModel.getUserId()));
                                    // start getting device_id
                                    //[START] Save Security Question
                                    result.putParcelable(LOGIN_SECURITY_QUESTION_DATA, Parcels.wrap(loginSecurityModel));
                                    result.putBoolean(LOGIN_MOVE_SECURITY, true);
                                    result.putBoolean(LOGIN_ACTIVATION_RESENT, false);
                                }
                            }

                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case HOTLIST:
                            Log.d(TAG, HotListImpl.class.getSimpleName() + " " + jsonObject.toString());
                            // set paging
                            PagingHandler mPaging = new PagingHandler();
                            mPaging.setNewParameter(jsonObject);
                            boolean hasNext = mPaging.CheckNextPage();
                            mPaging.nextPage();
                            int nextPage = mPaging.getPage();

                            // add to real container data
                            List<RecyclerViewItem> items = (List<RecyclerViewItem>) parseJSON(type, jsonObject);
                            cache(jsonObject, nextPage - 1);

                            result = new Bundle();
                            result.putInt(TYPE, HOTLIST);
                            result.putParcelable(HOTLIST_DATA, Parcels.wrap(items));
                            result.putBoolean(HOTLIST_HAS_NEXT, hasNext);
                            result.putInt(HOTLIST_NEXT_PAGE, nextPage);
                            result.putBoolean(RETRY_FLAG, false);
                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case SECURITY_QUESTION_GET:
                            result = new Bundle();
                            result.putInt(TYPE, SECURITY_QUESTION_GET);
                            QuestionFormModel questionFormModel = (QuestionFormModel) parseJSON(SECURITY_QUESTION_GET, jsonObject);
                            result.putParcelable(SECURITY_QUESTION_GET_MODEL, Parcels.wrap(questionFormModel));
                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case ANSWER_SECURITY_QUESTION:
                            result = new Bundle();
                            result.putInt(TYPE, ANSWER_SECURITY_QUESTION);
                            if (jsonObject.optBoolean("is_login", false) == true) {
                                final LoginInterruptModel loginInterruptModel = (LoginInterruptModel) parseJSON(ANSWER_SECURITY_QUESTION, jsonObject);
                                sessionHandler.SetLoginSession(loginInterruptModel.isLogin(),
                                        loginInterruptModel.getUserId(), loginInterruptModel.getFullName(), loginInterruptModel.getShopId() + "",
                                        loginInterruptModel.getMsisdnIsVerified());
                                sessionHandler.setGoldMerchant(getApplicationContext(), loginInterruptModel.getShopIsGold());
                                storeUUID(getApplicationContext(), loginInterruptModel.getUuid());

                                switch (loginType) {
                                    case LOGIN_EMAIL:
                                        PasswordGenerator.clearTokenStorage(getApplicationContext());
                                        PasswordGenerator generator = new PasswordGenerator(getApplicationContext());
                                        generator.generateAPPID(new PasswordGenerator.PGListener() {
                                            @Override
                                            public void onSuccess(int status) {
                                                loginV2(loginInterruptModel.getUuid());
                                            }
                                        });
                                        break;
                                    case LOGIN_GOOGLE:
                                        loginThirdAppV2(loginGoogleModel);
                                        break;
                                    case LOGIN_FACEBOOK:
                                        loginThirdAppV2(loginFacebookViewModel);
                                        break;
                                }
                                result.putParcelable(ANSWER_QUESTION_MODEL, Parcels.wrap(loginInterruptModel));
                            } else {
                                LoginInterruptErrorModel loginErrorModel = (LoginInterruptErrorModel) parseJSON(ANSWER_SECURITY_QUESTION_FALSE, jsonObject);
                                result.putParcelable(ANSWER_SECURITY_QUESTION_FALSE_MODEL, Parcels.wrap(loginErrorModel));
                            }
                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case REQUEST_OTP:
                            result = new Bundle();
                            result.putInt(TYPE, REQUEST_OTP);
                            OTPModel otpModel = (OTPModel) parseJSON(REQUEST_OTP, jsonObject);
                            result.putParcelable(REQUEST_OTP_MODEL, Parcels.wrap(otpModel));
                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case REGISTER_THIRD:
                        case REGISTER:
                            result = new Bundle();
                            result.putInt(TYPE, type);
                            RegisterSuccessModel registerSuccessModel = (RegisterSuccessModel) parseJSON(REGISTER, jsonObject);
                            result.putParcelable(REGISTER_MODEL_KEY, Parcels.wrap(registerSuccessModel));
                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case LOGIN_BYPASS:
                            LoginBypassSuccessModel loginBypassSuccessModel = (LoginBypassSuccessModel) parseJSON(LOGIN_BYPASS, jsonObject);
                            if (loginBypassSuccessModel.getIsRegisterDevice() == 1) {
                                SessionHandler session = new SessionHandler(getApplicationContext());
                                session.SetLoginSession(true, session.getLoginID(), session.getLoginName(), session.getShopID(), SessionHandler.isMsisdnVerified());
                            }
                            result = new Bundle();
                            result.putInt(TYPE, type);
                            result.putParcelable(LOGIN_BYPASS_MODEL_KEY, Parcels.wrap(loginBypassSuccessModel));
                            receiver.send(STATUS_FINISHED, result);
                            break;
                        case REGISTER_PASS_PHONE:
                            result = new Bundle();
                            result.putInt(TYPE, type);
                            if (jsonObject.optInt("is_success", 0) == 1) {
                                receiver.send(STATUS_FINISHED, result);
                            } else {

                            }
                            break;
                        case RESET_PASSWORD:
                            result = new Bundle();
                            result.putInt(TYPE, type);
                            if (jsonObject.optInt("is_success", 0) == 1) {
                                receiver.send(STATUS_FINISHED, result);
                            } else {

                            }break;
                    }
                } else {
                    onMessageError(response.getErrorMessages());
                }
            } else {
                new ErrorHandler(listener, responseData.code());
            }
        }

        /**
         * No connection still not known
         */
        public class ErrorListener implements com.tokopedia.tkpd.network.retrofit.response.ErrorListener {
            int errorCode;
            String error;

            public ErrorListener(int errorCode) {
                this.errorCode = errorCode;
                switch (errorCode) {
                    case ResponseStatus.SC_REQUEST_TIMEOUT:
                        error = NetworkConfig.TIMEOUT_TEXT;
                        break;
                    case ResponseStatus.SC_GATEWAY_TIMEOUT:
                        error = NetworkConfig.TIMEOUT_TEXT;
                        break;
                    case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                        error = "SERVER ERROR";
                        break;
                    case ResponseStatus.SC_FORBIDDEN:
                        error = "FORBIDDEN ACCESS";
                        break;
                    case ResponseStatus.SC_BAD_GATEWAY:
                        error = "INVALID INPUT";
                        break;
                    case ResponseStatus.SC_BAD_REQUEST:
                        error = "INVALID INPUT";
                        break;
                }
            }

            public void onResponse() {
                Bundle resultData = new Bundle();
                switch (type) {
                    case SECURITY_QUESTION_GET:
                    case REQUEST_OTP:
                    case ANSWER_SECURITY_QUESTION:
                    case LOGIN_FACEBOOK:
                    case LOGIN_GOOGLE:
                    case LOGIN_EMAIL:
                    case HOTLIST:
                    case REGISTER:
                    case DISCOVER_LOGIN:
                    case RESET_PASSWORD:
                    case REGISTER_THIRD:
                        resultData.putInt(TYPE, type);
                        resultData.putInt(NETWORK_ERROR_FLAG, errorCode);
                        resultData.putString(MESSAGE_ERROR_FLAG, error.toString());
                        receiver.send(STATUS_ERROR, resultData);
                        break;
                }
            }

            public void noConnection() {
                error = DownloadServiceConstant.noNetworkConnection;
                onResponse();
            }

            @Override
            public void onUnknown() {
                error = getString(R.string.default_request_error_unknown);
                onResponse();
            }

            @Override
            public void onTimeout() {
                error = getString(R.string.default_request_error_timeout);
                onResponse();
            }

            @Override
            public void onServerError() {
                error = getString(R.string.default_request_error_internal_server);
                onResponse();
            }

            @Override
            public void onBadRequest() {
                error = getString(R.string.default_request_error_bad_request);
                onResponse();
            }

            @Override
            public void onForbidden() {
                error = getString(R.string.msg_connection_timeout);
                onResponse();
            }
        }

        public void onMessageError(List<String> MessageError) {
            Log.d(TAG, DownloadService.class.getSimpleName() + " onMessageError " + MessageError.toString());
            if (MessageError == null || !(MessageError.size() > 0))
                return;

            Bundle resultData = new Bundle();
            switch (type) {
                case SECURITY_QUESTION_GET:
                case REQUEST_OTP:
                case ANSWER_SECURITY_QUESTION:
                case LOGIN_FACEBOOK:
                case LOGIN_GOOGLE:
                case LOGIN_EMAIL:
                case REGISTER_THIRD:
                case REGISTER:
                case HOTLIST:
                case REGISTER_PASS_PHONE:
                case RESET_PASSWORD:
                case DISCOVER_LOGIN:
                    resultData.putInt(TYPE, type);
                    resultData.putString(MESSAGE_ERROR_FLAG, MessageError.toString().replace("[", "").replace("]", ""));
                    receiver.send(STATUS_ERROR, resultData);
                    break;
            }
        }

        private void storeUUID(Context context, String UUID) {
            LocalCacheHandler cache = new LocalCacheHandler(context, "LOGIN_UUID");
            String prevUUID = cache.getString("uuid", "");
            String currUUID;
            if (prevUUID.equals("")) {
                currUUID = UUID;

            } else {
                currUUID = prevUUID + "*~*" + UUID;
            }
            cache.putString("uuid", currUUID);
            cache.applyEditor();
        }

        private Object parseJSON(int type, JSONObject response) {
            switch (type) {
                case HOTLIST:
                    List<RecyclerViewItem> temps = new ArrayList<>();
                    try {
                        JSONArray listHot = new JSONArray(response.getString(HotList.LIST_KEY));
                        java.lang.reflect.Type listType = new TypeToken<List<HotListModel>>() {
                        }.getType();
                        temps = gson.fromJson(listHot.toString(), listType);
                    } catch (JSONException json) {
                        Log.e(TAG, HotListImpl.class.getSimpleName() + " is error : " + json.getLocalizedMessage());
                    }
                    return temps;
                case SECURITY:
                    LoginSecurityModel result = gson.fromJson(response.toString(), LoginSecurityModel.class);
                    JSONObject Security = response.optJSONObject("security");
                    if (Security != null) {
                        LoginSecurityModel.SecurityQuestion temp = gson.fromJson(Security.toString(), LoginSecurityModel.SecurityQuestion.class);
                        result.setSecurityQuestion(temp);
                    }
                    return result;
                case LOGIN_EMAIL:
                    LoginEmailModel result2 = gson.fromJson(response.toString(), LoginEmailModel.class);
                    JSONObject shopReputation = response.optJSONObject("shop_reputation");
                    JSONObject userReputation = response.optJSONObject("user_reputation");

                    if (shopReputation != null) {
                        ShopRepModel temp = gson.fromJson(shopReputation.toString(), ShopRepModel.class);
                        JSONObject reputationBage = shopReputation.optJSONObject("reputation_badge");
                        if (reputationBage != null) {
                            ShopRepModel.ReputationBadge x = gson.fromJson(reputationBage.toString(), ShopRepModel.ReputationBadge.class);
                            temp.setReputationBadge(x);
                        }
                        result2.setShopRepModel(temp);
                    }
                    if (userReputation != null) {
                        UserRepModel temp = gson.fromJson(userReputation.toString(), UserRepModel.class);
                        result2.setUserRepModel(temp);
                    }
                    return result2;
                case LOGIN_FACEBOOK:
                case LOGIN_GOOGLE:
                    LoginThirdModel result3 = gson.fromJson(response.toString(), LoginThirdModel.class);
                    shopReputation = response.optJSONObject("shop_reputation");
                    userReputation = response.optJSONObject("user_reputation");

                    if (shopReputation != null) {
                        ShopRepModel temp = gson.fromJson(shopReputation.toString(), ShopRepModel.class);
                        JSONObject reputationBage = shopReputation.optJSONObject("reputation_badge");
                        if (reputationBage != null) {
                            ShopRepModel.ReputationBadge x = gson.fromJson(reputationBage.toString(), ShopRepModel.ReputationBadge.class);
                            temp.setReputationBadge(x);
                        }
                        result3.setShopRepModel(temp);
                    }
                    if (userReputation != null) {
                        UserRepModel temp = gson.fromJson(userReputation.toString(), UserRepModel.class);
                        result3.setUserRepModel(temp);
                    }
                    return result3;
                case SECURITY_QUESTION_GET:
                    QuestionFormModel questionFormModel = gson.fromJson(response.toString(), QuestionFormModel.class);
                    return questionFormModel;
                case ANSWER_SECURITY_QUESTION:
                    LoginInterruptModel loginInterruptModel = gson.fromJson(response.toString(), LoginInterruptModel.class);
                    return loginInterruptModel;
                case REQUEST_OTP:
                    OTPModel otpModel = new OTPModel();
                    otpModel.setSuccess(response.optInt("is_success", OTPModel.DEFAULT));
                    return otpModel;
                case ANSWER_SECURITY_QUESTION_FALSE:
                    LoginInterruptErrorModel loginInterruptErrorModel = gson.fromJson(response.toString(), LoginInterruptErrorModel.class);
                    return loginInterruptErrorModel;
                case REGISTER:
                    RegisterSuccessModel model = gson.fromJson(response.toString(), RegisterSuccessModel.class);
                    return model;
                case LOGIN_BYPASS:
                    LoginBypassSuccessModel bypassmodel = gson.fromJson(response.toString(), LoginBypassSuccessModel.class);
                    return bypassmodel;
            }
            return null;
        }

        private void cache(JSONObject jsonObject, Object... data) {
            switch (type) {
                case HOTLIST:
                    // only cache first page
                    int page = (int) data[0];
                    if (page == 1) {
                        cache = new LocalCacheHandler(getApplicationContext(), HotList.CACHE_KEY);
                        cache.putString(HotList.HOT_LIST_PAGE_1, jsonObject.toString());
                        cache.putLong(HotList.EXPIRY, System.currentTimeMillis() / 1000);
                        cache.applyEditor();
                    }
                    break;
            }
        }
    }

    private void handleAccounts(final AccountsParameter data) {
        rx.Observable.just(data)
                .flatMap(new Func1<AccountsParameter, rx.Observable<AccountsParameter>>() {
                    @Override
                    public rx.Observable<AccountsParameter> call(AccountsParameter accountsParameter) {
                        return getObservableAccountsToken(accountsParameter);
                    }
                })

                .flatMap(new Func1<AccountsParameter, rx.Observable<AccountsParameter>>() {
                    @Override
                    public rx.Observable<AccountsParameter> call(AccountsParameter accountsParameter) {
                        if (accountsParameter.getErrorModel() == null) {
                            TokenModel tokenModel = accountsParameter.getTokenModel();
                            sessionHandler.setToken(tokenModel.getAccessToken(),
                                    tokenModel.getTokenType(),
                                    tokenModel.getRefreshToken());
                        }
                        return rx.Observable.just(accountsParameter);
                    }
                })

                .flatMap(new Func1<AccountsParameter, rx.Observable<AccountsParameter>>() {
                    @Override
                    public rx.Observable<AccountsParameter> call(AccountsParameter accountsParameter) {
                        if (accountsParameter.getErrorModel() == null) {
                            return getObservableAccountsInfo(accountsParameter);
                        } else {
                            return rx.Observable.just(accountsParameter);
                        }
                    }
                })

                .flatMap(new Func1<AccountsParameter, rx.Observable<AccountsParameter>>() {
                    @Override
                    public rx.Observable<AccountsParameter> call(AccountsParameter accountsParameter) {
                        if (accountsParameter.isCreated_password() && accountsParameter.getErrorModel() == null) {
                            return getObservableMakeLogin(accountsParameter);
                        } else {
                            return rx.Observable.just(accountsParameter);
                        }
                    }
                })

                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<AccountsParameter>() {
                    @Override
                    public void onCompleted() {
                        Log.d("steven flatmap", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("steven flatmap", "onError " + e.getMessage());
                        Bundle result = new Bundle();
                        result.putInt(TYPE, DownloadService.LOGIN_ACCOUNTS_TOKEN);
                        if (e instanceof SocketTimeoutException) {
                            result.putString(MESSAGE_ERROR_FLAG, "Terjadi kesalahan koneksi, silahkan coba lagi");
                        } else {
                            result.putString(MESSAGE_ERROR_FLAG, "Silahkan coba lagi");
                        }
                        receiver.send(DownloadService.STATUS_ERROR, result);
                    }

                    @Override
                    public void onNext(AccountsParameter accountsParameter) {
                        Log.d("steven flatmap", "onNext");
                        Bundle result = new Bundle();
                        // make login
                        if (accountsParameter.isCreated_password()) {
                            result.putInt(TYPE, DownloadService.MAKE_LOGIN);
                            SecurityModel securityModel = accountsParameter.getSecurityModel();
                            if (securityModel != null) {
                                sessionHandler.setTempLoginSession(Integer.toString(securityModel.getUser_id()));
                                result.putParcelable(LOGIN_SECURITY_QUESTION_DATA, securityModel);
                            } else {
                                Log.d("steven", "berhasil make login");
                                sendLocalyticsUserAttr(data.getUserID() + "", data.getAccountsModel().getFullName(), data.getEmail());
                                AccountsModel accountsModel = accountsParameter.getAccountsModel();
                                sessionHandler.SetLoginSession(Boolean.parseBoolean(accountsModel.getIsLogin()),
                                        accountsModel.getUserId() + "",
                                        accountsModel.getFullName(), accountsModel.getShopId() + "",
                                        accountsModel.getMsisdnIsVerifiedBoolean());
                                result.putString(AppEventTracking.USER_ID_KEY,
                                        accountsModel.getUserId() + "");
                                result.putString(AppEventTracking.FULLNAME_KEY,
                                        accountsModel.getFullName());
                                result.putString(AppEventTracking.EMAIL_KEY,
                                        accountsParameter.getEmail());
                                result.putInt(VALIDATION_OF_DEVICE_ID, accountsModel.getIsRegisterDevice());
                                sessionHandler.setGoldMerchant(getApplicationContext(), accountsModel.getShopIsGold());
                            }
                            result.putBoolean(LOGIN_MOVE_SECURITY, accountsParameter.isMoveSecurity());
                            result.putBoolean(LOGIN_ACTIVATION_RESENT, accountsParameter.isActivationResent());
                            receiver.send(DownloadService.STATUS_FINISHED, result);

                            switch (loginVia){
                                case Login.EmailType:
                                    loginV2(accountsParameter.getUUID());
                                    break;
                                case Login.WebViewType:
                                    loginThirdAppV2(accountsParameter);
                                    break;
                                default:
                                    break;
                            }
                        }
                        //showing error
                        else if (accountsParameter.getErrorModel() != null) {
                            result.putInt(TYPE, DownloadService.LOGIN_ACCOUNTS_TOKEN);
                            result.putString(MESSAGE_ERROR_FLAG, accountsParameter.getErrorModel().getError_description());
                            receiver.send(DownloadService.STATUS_ERROR, result);
                        }
                        // need create password
                        else {
                            result.putInt(TYPE, DownloadService.LOGIN_ACCOUNTS_INFO);
                            result.putBoolean(DO_LOGIN, true);
                            result.putParcelable(INFO_BUNDLE, accountsParameter.getInfoModel());
                            result.putParcelable(EXTRA_TYPE, accountsParameter.getParcelable());
                            sessionHandler.setTempLoginSession(String.valueOf(accountsParameter.getInfoModel().getUserId()));
                            receiver.send(DownloadService.STATUS_FINISHED, result);
                        }
                    }
                });
    }


    private rx.Observable<AccountsParameter> getObservableAccountsToken(AccountsParameter accountsParameter) {
        Bundle bundle = new Bundle();
        Map<String, String> params = new HashMap<>();
        Parcelable parcelable = accountsParameter.getParcelable();

        params.put(Login.GRANT_TYPE, accountsParameter.getGrantType());

        switch (accountsParameter.getGrantType()) {
            case Login.GRANT_PASSWORD:
                params.put(Login.USER_NAME, accountsParameter.getEmail());
                params.put(Login.PASSWORD, accountsParameter.getPassword());
                break;
            case Login.GRANT_SDK:
                params.put(Login.SOCIAL_TYPE, String.valueOf(accountsParameter.getSocialType()));
                if (Parcels.unwrap(parcelable) instanceof LoginFacebookViewModel) {
                    LoginFacebookViewModel loginFacebookViewModel = Parcels.unwrap(parcelable);
                    params.put(Login.SOCIAL_ID, loginFacebookViewModel.getFbId());
                    params.put(Login.EMAIL_ACCOUNTS, loginFacebookViewModel.getEmail());
                    params.put(Login.FULL_NAME, loginFacebookViewModel.getFullName());
                    params.put(Login.BIRTHDATE, loginFacebookViewModel.getBirthday());
                    params.put(Login.GENDER_ACCOUNTS, loginFacebookViewModel.getGender());
                } else if (Parcels.unwrap(parcelable) instanceof LoginGoogleModel) {
                    LoginGoogleModel loginGoogleModel = Parcels.unwrap(parcelable);
                    params.put(Login.SOCIAL_ID, loginGoogleModel.getGoogleId());
                    params.put(Login.EMAIL_ACCOUNTS, loginGoogleModel.getEmail());
                    params.put(Login.PICTURE_ACCOUNTS, loginGoogleModel.getImageUrl());
                    params.put(Login.FULL_NAME, loginGoogleModel.getFullName());
                    params.put(Login.BIRTHDATE, loginGoogleModel.getBirthday());
                    params.put(Login.GENDER_ACCOUNTS, loginGoogleModel.getGender());
                }
                break;
            case Login.GRANT_WEBVIEW:
                params.put(Login.CODE, accountsParameter.getCode());
                params.put(Login.REDIRECT_URI, accountsParameter.getRedirectUri());
                break;
            default:
                throw new RuntimeException("Invalid Observable to get Token");
        }

        AccountsService accountService = new AccountsService(bundle);
        rx.Observable<Response<String>> observable = accountService.getApi()
                .getToken(AuthUtil
                        .generateParams(getApplicationContext(), params));
        return rx.Observable.zip(rx.Observable.just(accountsParameter), observable, new Func2<AccountsParameter, Response<String>, AccountsParameter>() {
            @Override
            public AccountsParameter call(AccountsParameter accountsParameter, Response<String> stringResponse) {
                String response = String.valueOf(stringResponse.body());
                ErrorModel errorModel = new GsonBuilder().create().fromJson(response, ErrorModel.class);
                if (errorModel.getError() == null) {
                    TokenModel model = new GsonBuilder().create().fromJson(response, TokenModel.class);
                    accountsParameter.setTokenModel(model);
                } else {
                    accountsParameter.setErrorModel(errorModel);
                }
                return accountsParameter;
            }
        });
    }

    private rx.Observable<AccountsParameter> getObservableAccountsInfo(AccountsParameter accountsParameter) {
        TokenModel tokenModel = accountsParameter.getTokenModel();
        String authKey = tokenModel.getTokenType() + " " + tokenModel.getAccessToken();
        Map<String, String> params = new HashMap<>();
        params = AuthUtil.generateParams(getApplicationContext(), params);
        Bundle bundle = new Bundle();
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        AccountsService accountService = new AccountsService(bundle);
        rx.Observable<Response<String>> observable = accountService.getApi()
                .getInfo(params);
        return rx.Observable.zip(rx.Observable.just(accountsParameter), observable, new Func2<AccountsParameter, Response<String>, AccountsParameter>() {
            @Override
            public AccountsParameter call(AccountsParameter accountsParameter, Response<String> stringResponse) {
                String response = String.valueOf(stringResponse.body());
                ErrorModel errorModel = new GsonBuilder().create().fromJson(response, ErrorModel.class);
                if (errorModel.getError() == null) {
                    InfoModel infoModel = new GsonBuilder().create().fromJson(response, InfoModel.class);
                    accountsParameter.setInfoModel(infoModel);
                    accountsParameter.setIsCreated_password(infoModel.isCreatedPassword());
                    accountsParameter.setUserID(infoModel.getUserId());
                } else {
                    accountsParameter.setErrorModel(errorModel);
                }
                return accountsParameter;
            }
        });
    }

    private rx.Observable<AccountsParameter> getObservableMakeLogin(AccountsParameter accountsParameter) {
        Map<String, String> params = new HashMap<>();
        params = AuthUtil.generateParams(getApplicationContext(), params);
        params.put(Login.UUID_KEY, accountsParameter.getUUID());
        params.put(Login.USER_ID, String.valueOf(accountsParameter.getUserID()));
        TokenModel tokenModel = accountsParameter.getTokenModel();
        String authKey = tokenModel.getTokenType() + " " + tokenModel.getAccessToken();
        Bundle bundle = new Bundle();
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        bundle.putString(AccountsService.WEB_SERVICE, AccountsService.WS);
        AccountsService accountService = new AccountsService(bundle);
        rx.Observable<Response<TkpdResponse>> observable = accountService.getApi()
                .makeLogin(MapNulRemover.removeNull(params));
        return rx.Observable.zip(rx.Observable.just(accountsParameter), observable, new Func2<AccountsParameter, Response<TkpdResponse>, AccountsParameter>() {
            @Override
            public AccountsParameter call(AccountsParameter accountsParameter, Response<TkpdResponse> responseData) {
                if (responseData.isSuccessful()) {
                    TkpdResponse response = responseData.body();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.getStringData());
                    } catch (JSONException je) {
                        Log.e(TAG, messageTAG + je.getLocalizedMessage());
                    }
                    if (!response.isError()) {
                        SecurityModel securityModel = new GsonBuilder().create()
                                .fromJson(jsonObject.toString(), SecurityModel.class);
                        if (securityModel.getIs_login().equals(false) || securityModel.getIs_login().equals("false")) {
                            //[START] Save Security Question
                            accountsParameter.setSecurityModel(securityModel);
                            accountsParameter.setMoveSecurity(true);
                            accountsParameter.setActivationResent(false);
                        } else {
                            AccountsModel accountsModel = new GsonBuilder()
                                    .create().fromJson(jsonObject.toString(), AccountsModel.class);
                            accountsParameter.setMoveSecurity(false);
                            accountsParameter.setActivationResent(false);
                            accountsParameter.setAccountsModel(accountsModel);
                        }
                    } else {
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setError_description(response.getErrorMessages().toString());
                        accountsParameter.setErrorModel(errorModel);
                    }

                } else {
                    throw new RuntimeException(String.valueOf(responseData.code()));
                }
                return accountsParameter;
            }
        });
    }

    /**
     * send Localytics user attributes
     * by : Hafizh Herdi
     */
    private void sendLocalyticsUserAttr(String userId, String fullName, String email) {
        if (getApplicationContext() != null) {
            TrackingUtils.eventLocaUserAttributes(userId, fullName, email);
        }
    }

    /**
     * send GTM authenticate event
     * by : Hafizh Herdi
     */
    private void sendAuthenticateGTMEvent(@NonNull Object modelObject) {

        Authenticated authEvent = new Authenticated();

        if (modelObject instanceof LoginEmailModel) {
            authEvent.setUserFullName(((LoginEmailModel) modelObject).getFullName());
            authEvent.setUserID(((LoginEmailModel) modelObject).getUserID());
            authEvent.setUserMSISNVer(((LoginEmailModel) modelObject).getMsisdnIsVerified());
            authEvent.setShopID(((LoginEmailModel) modelObject).getShopId());
            authEvent.setUserSeller(((LoginEmailModel) modelObject).getShopId() == 0 ? 0 : 1);

        } else if (modelObject instanceof LoginThirdModel) {
            authEvent.setUserFullName(((LoginThirdModel) modelObject).getFullName());
            authEvent.setUserID(((LoginThirdModel) modelObject).getUserID());
            authEvent.setUserMSISNVer(((LoginThirdModel) modelObject).getMsisdnIsVerified());
            authEvent.setShopID(((LoginThirdModel) modelObject).getShopId());
            authEvent.setUserSeller(((LoginThirdModel) modelObject).getShopId().equals("0") ? 0 : 1);

        } else {

        }

        CommonUtils.dumper("GAv4 appdata " + new JSONObject(authEvent.getAuthDataLayar()).toString());

        if (getApplicationContext() != null) {
            TrackingUtils.eventAuthenticateLogin(authEvent);
        }
    }

    private void loginV2(String uuid){
        com.tokopedia.tkpd.network.NetworkHandler network
                = new com.tokopedia.tkpd.network.NetworkHandler(getApplicationContext(), "http://www.tokopedia.com/ws-new/login.pl");
        network.AddParam("user_email", emailV2);
        network.AddParam("user_pass", passwordV2);
        network.AddParam("uuid", uuid);
        network.AddParam("app_id", PasswordGenerator.getAppId(getApplicationContext()));
        network.Commit(new com.tokopedia.tkpd.network.NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {
                Log.d("onSuccess", String.valueOf(status));
            }

            @Override
            public void getResponse(JSONObject Result) {
                Log.d("onResponse",Result.toString());
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
                Log.d("onError",MessageError.toString());
            }
        });
    }

    public void loginThirdAppV2(LoginGoogleModel loginGoogleModel){
        NetworkHandler network = new NetworkHandler(getApplicationContext(), "http://www.tokopedia.com/ws-new/third-app-login.pl");
        network.AddParam("act", "do_login");
        network.AddParam("name", loginGoogleModel.getFullName());
        network.AddParam("app_type", Login.GooglePlusType);
        network.AddParam("birthday", loginGoogleModel.getBirthday());
        network.AddParam("gender", loginGoogleModel.getGender());
        network.AddParam("email", loginGoogleModel.getEmail());
        network.AddParam("id", loginGoogleModel.getGoogleId());
        network.AddParam("app_id", PasswordGenerator.getAppId(getApplicationContext()));
        network.Commit(new com.tokopedia.tkpd.network.NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {
            }

            @Override
            public void getResponse(JSONObject Result) {
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
            }
        });
    }

    private void loginThirdAppV2(LoginFacebookViewModel loginFacebookViewModel) {
        NetworkHandler network = new NetworkHandler(getApplicationContext(), "http://www.tokopedia.com/ws-new/third-app-login.pl");
        network.AddParam("act", "do_login");
        network.AddParam("name", loginFacebookViewModel.getFullName());
        network.AddParam("app_type", Login.FacebookType);
        network.AddParam("birthday", loginFacebookViewModel.getBirthday());
        network.AddParam("gender", loginFacebookViewModel.getGender());
        network.AddParam("fb_token", loginFacebookViewModel.getFbToken());
        network.AddParam("app_id", PasswordGenerator.getAppId(getApplicationContext()));
        network.Commit(new com.tokopedia.tkpd.network.NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {
            }

            @Override
            public void getResponse(JSONObject Result) {
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
            }
        });
    }


    private void loginThirdAppV2(AccountsParameter accountsParameter) {
        NetworkHandler network = new NetworkHandler(getApplicationContext(), "http://www.tokopedia.com/ws-new/third-app-login.pl");
        TokenModel tokenModel = accountsParameter.getTokenModel();
        String authKey = tokenModel.getTokenType() + " "+ tokenModel.getAccessToken();
        network.AddHeader("authorization",authKey);
        network.AddParam("act", "do_login_yahoo");
        network.AddParam("app_type", Login.WebViewType);
        network.AddParam("birthday", accountsParameter.getInfoModel().getBday());
        network.AddParam("app_id", PasswordGenerator.getAppId(getApplicationContext()));
        network.Commit(new com.tokopedia.tkpd.network.NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {
                Log.d("onSuccess", String.valueOf(status));
            }

            @Override
            public void getResponse(JSONObject Result) {
                Log.d("onResponse",Result.toString());
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
                Log.d("onError",MessageError.toString());
            }
        });
    }

}
