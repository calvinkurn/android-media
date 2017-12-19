package com.tokopedia.core.session.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.otp.data.RequestOtpModel;
import com.tokopedia.core.otp.data.ValidateOtpModel;
import com.tokopedia.core.otp.data.factory.OtpSourceFactory;
import com.tokopedia.core.otp.data.repository.OtpRepositoryImpl;
import com.tokopedia.core.otp.domain.interactor.RequestOtpEmailUseCase;
import com.tokopedia.core.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.core.otp.domain.interactor.ValidateOtpUseCase;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.VerifyPhoneInteractor;
import com.tokopedia.core.session.VerifyPhoneInteractorImpl;
import com.tokopedia.core.session.model.LoginInterruptModel;
import com.tokopedia.core.session.model.OTPModel;
import com.tokopedia.core.session.model.QuestionFormModel;
import com.tokopedia.core.session.model.SecurityQuestionViewModel;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import rx.Subscriber;

import static com.tokopedia.core.service.constant.DownloadServiceConstant.REQUEST_OTP_MODEL;
import static com.tokopedia.core.session.presenter.Login.DEFAULT_UUID_VALUE;
import static com.tokopedia.core.session.presenter.Login.LOGIN_UUID_KEY;
import static com.tokopedia.core.session.presenter.Login.UUID_KEY;

/**
 * Created by m.normansyah on 05/11/2015.
 * modified by m.normansyah on 21/11/2015 - move download or upload to DownloadService
 */
public class SecurityQuestionPresenterImpl implements SecurityQuestionPresenter {
    public static final int SWITCH_REQUEST_OTP = 2;

    private static final String OTP_TYPE_PHONE_NUMBER_VERIFICATION = "13";

    SecurityQuestionView view;
    Context mContext;
    SessionHandler sessionHandler;

    SecurityQuestionViewModel viewModel;
    QuestionFormModel questionFormModel;
    int errorCount;

    VerifyPhoneInteractor interactor;
    private RequestOtpUseCase requestOtpUseCase;
    private ValidateOtpUseCase validateOtpUseCase;
    private RequestOtpEmailUseCase requestOtpEmailUseCase;
    private ErrorListener errorListener;

    public SecurityQuestionPresenterImpl(SecurityQuestionView view) {
        this.view = view;
    }

    @Override
    public void initInstances(Context context) {
        mContext = context;
        sessionHandler = new SessionHandler(context);
        if (!isAfterRotate())// if not after rotate
        {
            viewModel = new SecurityQuestionViewModel();
        }
        interactor = VerifyPhoneInteractorImpl.createInstance();

        OtpRepositoryImpl otpRepository = new OtpRepositoryImpl(
                new OtpSourceFactory(context));
        this.requestOtpUseCase = new RequestOtpUseCase(new JobExecutor(),
                new UIThread(), otpRepository);
        this.validateOtpUseCase = new ValidateOtpUseCase(new JobExecutor(),
                new UIThread(), otpRepository);
        this.requestOtpEmailUseCase = new RequestOtpEmailUseCase(new JobExecutor(),
                new UIThread(), otpRepository);

        errorListener = new ErrorListener() {
            @Override
            public void onUnknown() {
                view.showError(
                        view.getString(R.string.default_request_error_unknown));
            }

            @Override
            public void onTimeout() {
                view.showError(
                        view.getString(R.string.default_request_error_timeout));
            }

            @Override
            public void onServerError() {
                view.showError(
                        view.getString(R.string.default_request_error_internal_server));
            }

            @Override
            public void onBadRequest() {
                view.showError(
                        view.getString(R.string.default_request_error_bad_request));
            }

            @Override
            public void onForbidden() {
                view.showError(
                        view.getString(R.string.default_request_error_forbidden_auth));
            }
        };
    }

    @Override
    public void getDataAfterRotate(Bundle bundle) {
        if (bundle != null) {
//            questionFormModel = Parcels.unwrap(bundle.getParcelable("QuestionFormModel"));
            viewModel = Parcels.unwrap(bundle.getParcelable("SecurityQuestionViewModel"));
//            otpModel = Parcels.unwrap(bundle.getParcelable("OTPModel"));
//            loginInterruptModel = Parcels.unwrap(bundle.getParcelable("LoginInterruptModel"));
//            loginErrorModel = Parcels.unwrap(bundle.getParcelable("LoginInterruptErrorModel"));
        }
    }

    @Override
    public void saveDataBeforeRotate(Bundle bundle) {
//        bundle.putParcelable("QuestionFormModel", Parcels.wrap(questionFormModel));
        bundle.putParcelable("SecurityQuestionViewModel", Parcels.wrap(viewModel));
//        bundle.putParcelable("OTPModel", Parcels.wrap(otpModel));
//        bundle.putParcelable("LoginInterruptModel", Parcels.wrap(loginInterruptModel));
//        bundle.putParcelable("LoginInterruptErrorModel", Parcels.wrap(loginErrorModel));
    }

    @Override
    public boolean isAfterRotate() {
        return viewModel != null && questionFormModel != null && questionFormModel.getExample() != null;//&&otpModel!=null&&loginInterruptModel!=null&& questionFormModel!=null&&questionFormModel.getExample()!=null
    }

    @Override
    public void fetchDataFromInternet(int type, Object object[]) {
        boolean isNeedLogin = true;
        switch (type) {
            case SECURITY_QUESTION_TYPE:
                int security1 = (Integer) object[0];
                int security2 = (Integer) object[1];
                SecurityQuestionViewModel securityQuestionViewModel = new SecurityQuestionViewModel();
                securityQuestionViewModel.setSecurity1(security1);
                securityQuestionViewModel.setSecurity2(security2);
                Bundle bundle = new Bundle();
                bundle.putParcelable(DownloadService.SECURITY_QUESTION_GET_MODEL, Parcels.wrap(securityQuestionViewModel));
                bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);
                ((SessionView) mContext).sendDataFromInternet(DownloadService.SECURITY_QUESTION_GET, bundle);
                break;

            case REQUEST_OTP_PHONE_TYPE:
                String phone = (String) object[0];
                securityQuestionViewModel = new SecurityQuestionViewModel();
                securityQuestionViewModel.setPhone(phone);
                bundle = new Bundle();
                bundle.putParcelable(REQUEST_OTP_MODEL, Parcels.wrap(securityQuestionViewModel));
                bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);
                ((SessionView) mContext).sendDataFromInternet(DownloadService.REQUEST_OTP, bundle);
                break;

            case MAKE_LOGIN:
                bundle = new Bundle();
                LocalCacheHandler loginUuid = new LocalCacheHandler(mContext, LOGIN_UUID_KEY);
                String uuid = loginUuid.getString(UUID_KEY, DEFAULT_UUID_VALUE);
                bundle.putString(UUID_KEY, uuid);
                ((SessionView) mContext).sendDataFromInternet(DownloadService.MAKE_LOGIN, bundle);
                break;
        }
    }

    @Override
    public Object parseJSON(int type, JSONObject jsonObject) {
        throw new RuntimeException("don't use this anymore!!!");
    }

    @Override
    public int determineQuestionType(int question, String title) {
//        if(title.contains("OTP")) {
        if (question == 1)
            return QuestionFormModel.OTP_No_HP_TYPE;
        else if (question == 2)
            return QuestionFormModel.OTP_Email_TYPE;
//        }else if(title.contains("Masukkan nomor")){
//            if(question ==1)
//                return QuestionFormModel.ANSWER_NO_HP_TYPE;
//            else
//                return QuestionFormModel.ANSWER_NO_REKENING_TYPE;
//        }

        return 0;
    }

    @Override
    public void storeNecessaryID() {
        throw new RuntimeException("don't use this method !!!");
    }

    @Override
    public boolean isLogin(JSONObject response) {
        throw new RuntimeException("don't use this method !!!");
    }

    @Override
    public boolean isSecurityQuestion() {
        if (questionFormModel != null)
            return questionFormModel.getType() == QuestionFormModel.ANSWER_NO_REKENING_TYPE || questionFormModel.getType() == QuestionFormModel.ANSWER_NO_HP_TYPE;
        else
            return false;
    }

    @Override
    public boolean isOtp() {
        if (questionFormModel != null)
            return questionFormModel.getType() == QuestionFormModel.OTP_No_HP_TYPE || questionFormModel.getType() == QuestionFormModel.OTP_Email_TYPE;
        else
            return false;
    }


    @Override
    public void getQuestionForm() {
        fetchDataFromInternet(SECURITY_QUESTION_TYPE, new Object[]{viewModel.getSecurity1(), viewModel.getSecurity2()});
    }

    @Override
    public void doAnswerQuestion(String answer) {
        view.displayProgress(true);
        validateOtpUseCase.execute(getValidateOtpParam(answer), new Subscriber<ValidateOtpModel>() {
            @Override
            public void onCompleted() {
                view.displayProgress(false);
            }

            @Override
            public void onError(Throwable e) {
                view.showError(view.getString(R.string.default_request_error_unknown));
            }

            @Override
            public void onNext(ValidateOtpModel validateOtpModel) {
                if (validateOtpModel.isSuccess()) {
                    storeUUID(mContext, validateOtpModel.getValidateOtpData().getUuid());
                    fetchDataFromInternet(SecurityQuestionPresenter.MAKE_LOGIN, null);
                } else {
                    if (validateOtpModel.getResponseCode() == ResponseStatus.SC_OK) {
                        if (++errorCount == SWITCH_REQUEST_OTP && viewModel.getEmail() != null) {
                            viewModel.setIsErrorDisplay(false);
                            view.displayError(false);
                            questionFormModel = new QuestionFormModel();
                            questionFormModel.setType(QuestionFormModel.OTP_Email_TYPE);
                            questionFormModel.setTitle(" coba pakai OTP ");
                            questionFormModel.setQuestion(2);
                            viewModel.setSecurity2(2);
                            view.setModel(questionFormModel);
                        } else if (validateOtpModel.getErrorMessage() != null) {
                            view.showError(validateOtpModel.getErrorMessage());
                        } else {
                            viewModel.setIsErrorDisplay(true);
                            view.displayError(true);
                        }
                    } else {
                        new ErrorHandler(new ErrorListener() {
                            @Override
                            public void onUnknown() {
                                view.showError(view.getString(R.string.default_request_error_unknown));
                            }

                            @Override
                            public void onTimeout() {
                                view.showError(view.getString(R.string.default_request_error_timeout));
                            }

                            @Override
                            public void onServerError() {
                                view.showError(view.getString(R.string.default_request_error_internal_server));
                            }

                            @Override
                            public void onBadRequest() {
                                view.showError(view.getString(R.string.default_request_error_bad_request));
                            }

                            @Override
                            public void onForbidden() {
                                view.showError(view.getString(R.string.default_request_error_forbidden_auth));
                            }
                        }, validateOtpModel.getResponseCode());
                    }
                }
            }
        });
    }

    @Override
    public void doRequestOtp() {
        view.displayProgress(true);
        view.disableOtpButton();
        doRequestOtpToDevice(getRequestOTPParam());
    }


    @Override
    public void doRequestOtpToEmail() {
        if (viewModel != null && !TextUtils.isEmpty(viewModel.getEmail())) {
            view.displayProgress(true);
            view.disableOtpButton();
            doRequestOtpToEmail(getRequestOTPWithEmailParam());
        } else {
            view.showError(mContext.getString(R.string.email_otp_error));
        }
    }

    @Override
    public void doRequestOtpWithCall() {
        view.displayProgress(true);
        view.disableOtpButton();
        doRequestOtpToDevice(getRequestOTPWithCallParam());
    }

    private void doRequestOtpToDevice(RequestParams requestParams) {
        requestOtpUseCase.execute(requestParams, new Subscriber<RequestOtpModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
                    view.showError(view.getString(R.string.msg_no_connection));
                } else if (e instanceof RuntimeException &&
                        e.getLocalizedMessage() != null &&
                        e.getLocalizedMessage().length() <= 3) {
                    new ErrorHandler(errorListener, Integer.parseInt(e.getLocalizedMessage()));
                } else if (e instanceof ErrorMessageException
                        && e.getLocalizedMessage() != null) {
                    view.showError(e.getLocalizedMessage());
                } else {
                    view.showError(
                            view.getString(R.string.default_request_error_unknown));
                }
            }

            @Override
            public void onNext(RequestOtpModel requestOtpModel) {
                if (requestOtpModel.isSuccess() &&
                        requestOtpModel.getRequestOtpData().isSuccess()
                        && requestOtpModel.getErrorMessage() == null
                        && requestOtpModel.getStatusMessage() != null) {
                    view.onSuccessRequestOTP(requestOtpModel.getStatusMessage());
                } else if (requestOtpModel.getErrorMessage() != null) {
                    view.showError(requestOtpModel.getErrorMessage());
                } else {
                    new ErrorHandler(errorListener, requestOtpModel.getResponseCode());
                }
            }
        });
    }

    private void doRequestOtpToEmail(RequestParams requestParams) {
        requestOtpEmailUseCase.execute(requestParams, new Subscriber<RequestOtpModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
                    view.showError(view.getString(R.string.msg_no_connection));
                } else if (e instanceof RuntimeException &&
                        e.getLocalizedMessage() != null &&
                        e.getLocalizedMessage().length() <= 3) {
                    new ErrorHandler(errorListener, Integer.parseInt(e.getLocalizedMessage()));
                } else {
                    view.showError(
                            view.getString(R.string.default_request_error_unknown));
                }
            }

            @Override
            public void onNext(RequestOtpModel requestOtpModel) {
//                if (requestOtpModel.isSuccess() &&
//                        requestOtpModel.getRequestOtpData().isSuccess()
//                        && requestOtpModel.getErrorMessage() == null
//                        && requestOtpModel.getStatusMessage() != null) {
//                    view.onSuccessRequestOTP(requestOtpModel.getStatusMessage());
//                } else if (requestOtpModel.getErrorMessage() != null) {
//                    view.showError(requestOtpModel.getErrorMessage());
//                } else {
//                    new ErrorHandler(errorListener, requestOtpModel.getResponseCode());
//                }

                if (requestOtpModel.isSuccess()) {
                    if (requestOtpModel.getStatusMessage() != null && !requestOtpModel.getStatusMessage().isEmpty()) {
                        view.onSuccessRequestOTP(requestOtpModel.getStatusMessage());
                    } else {
                        view.onSuccessRequestOTP(requestOtpModel.getErrorMessage());
                    }
                } else {
                    if (requestOtpModel.getErrorMessage() != null) {
                        view.showError(requestOtpModel.getErrorMessage());
                    } else {
                        new ErrorHandler(errorListener, requestOtpModel.getResponseCode());
                    }
                }
            }
        });
    }


    @Override
    public void getDataFromArgument(Bundle argument) {
        if (argument != null) {
            int Security1 = argument.getInt("security_1");
            int Security2 = argument.getInt("security_2");
            String UserID = argument.getString("user_id", "");
            String email = argument.getString(SecurityQuestionView.EMAIL);

            viewModel.setSecurity1(Security1);
            viewModel.setSecurity2(Security2);
            viewModel.setUserID(UserID);
            viewModel.setEmail(email);

            Log.d(TAG, messageTAG + " getDataFromArgument :  " + viewModel);
        }
    }

    @Override
    public void saveAnswer(String text) {
        viewModel.setvAnswer(text);
    }

    @Override
    public void saveOTPAnswer(String text) {
        viewModel.setvInputOtp(text);
    }

    @Override
    public String getOtpSendString() {
        switch (determineQuestionType(questionFormModel.getQuestion(), questionFormModel.getTitle())) {
            case QuestionFormModel.ANSWER_NO_HP_TYPE:
            case QuestionFormModel.ANSWER_NO_REKENING_TYPE:
                return null;
            case QuestionFormModel.OTP_No_HP_TYPE:
                return mContext.getResources().getString(R.string.title_otp_phone);
            case QuestionFormModel.OTP_Email_TYPE:
                return mContext.getResources().getString(R.string.title_otp_email);
        }
        return null;
    }

    @Override
    public boolean isValidForm(String text) {
        return !(TextUtils.isEmpty(text));
    }

    @Override
    public void initDataAfterRotate() {
        if (questionFormModel != null) {
            view.setModel(questionFormModel);
        }
        if (viewModel != null) {
            view.setAnswerSecurity(viewModel.getvAnswer());
            view.setAnswerOTP(viewModel.getvInputOtp());
        }
        if (viewModel.isErrorDisplay()) {
            view.displayError(true);
        } else {
            view.displayError(false);
        }
        if (SessionHandler.isV4Login(mContext)) {
            ((AppCompatActivity) mContext).finish();
        }
        if (viewModel.isLoading()) {
            view.displayProgress(viewModel.isLoading());
        }
    }

    @Override
    public void setData(int type, Bundle data) {
        switch (type) {
            case DownloadService.SECURITY_QUESTION_GET:
                questionFormModel = Parcels.unwrap(data.getParcelable(DownloadService.SECURITY_QUESTION_GET_MODEL));
                questionFormModel.setType(determineQuestionType(questionFormModel.getQuestion(), questionFormModel.getTitle()));
                view.setModel(questionFormModel);
                break;
            case DownloadService.REQUEST_OTP:
                OTPModel otpModel = Parcels.unwrap(data.getParcelable(REQUEST_OTP_MODEL));
                view.requestOTP(otpModel);
                view.displayProgress(false);
                view.startTimer();
                break;
            case DownloadService.ANSWER_SECURITY_QUESTION:
                if (data.getParcelable(DownloadService.ANSWER_SECURITY_QUESTION_FALSE_MODEL) != null) {
                    if (++errorCount == SWITCH_REQUEST_OTP) {
                        viewModel.setIsErrorDisplay(false);
                        view.displayError(false);
                        questionFormModel = new QuestionFormModel();
                        questionFormModel.setType(QuestionFormModel.OTP_Email_TYPE);
                        questionFormModel.setTitle(" coba pakai OTP ");
                        questionFormModel.setQuestion(2);
                        viewModel.setSecurity2(2);
                        view.setModel(questionFormModel);
                    } else {
                        viewModel.setIsErrorDisplay(true);
                        view.displayError(true);
                    }
                } else {
                    LoginInterruptModel loginInterruptModel = Parcels.unwrap(data.getParcelable(DownloadService.ANSWER_QUESTION_MODEL));
                    if (mContext != null && mContext instanceof SessionView) {
                        view.destroyTimer();
                        ((SessionView) mContext).destroy();
                    }
                    viewModel.setIsErrorDisplay(false);
                    view.displayError(false);
                }
                view.displayProgress(false);
                break;
        }
    }

    @Override
    public void updateViewModel(int type, Object... data) {
        switch (type) {
            case SecurityQuestionViewModel.IS_SECURITY_LOADING_TYPE:
                boolean isSecurityLoadingShow = (boolean) data[0];
                viewModel.setIsLoading(isSecurityLoadingShow);
                break;
            case SecurityQuestionViewModel.IS_ERROR_SHOWN:
                boolean is = (boolean) data[0];
                viewModel.setIsErrorDisplay(is);
                break;
            case SecurityQuestionViewModel.SEC_2:
                int in = (int) data[0];
                viewModel.setSecurity2(in);
                break;
        }
    }

    @Override
    public void updateModel(QuestionFormModel model) {
        questionFormModel = model;
    }

    @Override
    public void verifyTruecaller(final Context context, String phoneNumber) {
        VerifyPhoneInteractor.VerifyPhoneListener listener = new VerifyPhoneInteractor.VerifyPhoneListener() {
            @Override
            public void onError(String error) {
                view.showError(error);
            }

            @Override
            public void onThrowable(Throwable e) {
                view.showError(context.getString(R.string.default_request_error_unknown));
            }

            @Override
            public void onTimeout() {
                view.showError(context.getString(R.string.default_request_error_timeout));
            }

            @Override
            public void onSuccess(int result, String uuid) {
                if (result == 0) {
                    view.showError(context.getString(R.string.error_user_truecaller));
                } else {
                    storeUUID(context, uuid);
                    fetchDataFromInternet(SecurityQuestionPresenter.MAKE_LOGIN, null);
                }
            }
        };
        interactor.verifyPhone(context, phoneNumber, SessionHandler.getTempLoginSession(context),
                listener);
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

    @Override
    public void getPhoneTrueCaller() {
        ((SessionView) mContext).verifyTruecaller();
    }

    @Override
    public void showTrueCaller(Context context) {
        view.showTrueCaller(appInstalledOrNot(context, "com.truecaller"));
    }

    @Override
    public void unSubscribe() {
        interactor.unSubscribe();
        requestOtpUseCase.unsubscribe();
        validateOtpUseCase.unsubscribe();
    }

    private boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        } catch (Exception ignored) {
            app_installed = appInstalledOrNotV2(uri);
        }
        return app_installed;
    }

    private static boolean appInstalledOrNotV2(final String uri) {
        BufferedReader bufferedReader = null;
        boolean app_installed = false;
        try {
            Process process = Runtime.getRuntime().exec("pm list packages");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String packageName = line.substring(line.indexOf(':') + 1);
                if (uri != null && packageName.toLowerCase().contains(uri.toLowerCase())) {
                    app_installed = true;
                    break;
                }
            }
            closeQuietly(bufferedReader);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(bufferedReader);
        }
        return app_installed;
    }

    private static void closeQuietly(final Closeable closeable) {
        if (closeable == null)
            return;
        try {
            closeable.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doSendAnalytics() {
        ScreenTracking.screen(AppScreen.SCREEN_OTP_SQ);
    }

    private RequestParams getRequestOTPParam() {
        RequestParams param = RequestParams.create();
        param.putString(RequestOtpUseCase.PARAM_MODE, RequestOtpUseCase.MODE_SMS);
        param.putString(RequestOtpUseCase.PARAM_OTP_TYPE, OTP_TYPE_PHONE_NUMBER_VERIFICATION);
        return param;
    }

    private RequestParams getRequestOTPWithCallParam() {
        RequestParams param = RequestParams.create();
        param.putString(RequestOtpUseCase.PARAM_MODE, RequestOtpUseCase.MODE_CALL);
        param.putString(RequestOtpUseCase.PARAM_OTP_TYPE, OTP_TYPE_PHONE_NUMBER_VERIFICATION);
        return param;
    }

    private RequestParams getRequestOTPWithEmailParam() {
        RequestParams param = RequestParams.create();
        param.putString("user_email", viewModel.getEmail());
        param.putString("os_type", "1");
        param.putString(RequestOtpUseCase.PARAM_TYPE, OTP_TYPE_PHONE_NUMBER_VERIFICATION);
        param.putString(ValidateOtpUseCase.PARAM_USER, SessionHandler.getTempLoginSession(mContext));
        return param;
    }


    private RequestParams getValidateOtpParam(String answer) {
        RequestParams param = RequestParams.create();
        param.putString(RequestOtpUseCase.PARAM_OTP_TYPE, OTP_TYPE_PHONE_NUMBER_VERIFICATION);
        param.putString("os_type", "1");
        param.putString(ValidateOtpUseCase.PARAM_CODE, answer);
        param.putString(ValidateOtpUseCase.PARAM_USER, SessionHandler.getTempLoginSession(mContext));
        return param;
    }
}
