package com.tokopedia.core.session.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
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
    private static final String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    private int ACTION_REQUEST_OTP = 101;
    private int ACTION_REQUEST_OTP_WITH_CALL = 102;
    SecurityQuestionView view;
    Context mContext;
    SessionHandler sessionHandler;

    SecurityQuestionViewModel viewModel;
    QuestionFormModel questionFormModel;
    int errorCount;

    VerifyPhoneInteractor interactor;

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
            case ANSWER_SECURITY_QUESTION_TYPE:
                String answer = (String) object[0];
                String question = "";
                if (object[1] == null) {
                    question += questionFormModel.getQuestion();
                } else {
                    question = (String) object[1];
                }
                security1 = (Integer) object[2];
                security2 = (Integer) object[3];

                securityQuestionViewModel = new SecurityQuestionViewModel();
                securityQuestionViewModel.setvAnswer(answer);
                securityQuestionViewModel.setQuestion(question);
                securityQuestionViewModel.setSecurity1(security1);
                securityQuestionViewModel.setSecurity2(security2);

                bundle = new Bundle();
                bundle.putParcelable(DownloadService.ANSWER_QUESTION_MODEL, Parcels.wrap(securityQuestionViewModel));
                bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);
                ((SessionView) mContext).sendDataFromInternet(DownloadService.ANSWER_SECURITY_QUESTION, bundle);


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
                ((SessionView)mContext).sendDataFromInternet(DownloadService.MAKE_LOGIN, bundle);
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

        fetchDataFromInternet(ANSWER_SECURITY_QUESTION_TYPE
                , new Object[]{
                        answer, null, viewModel.getSecurity1(), viewModel.getSecurity2()
                });
    }

    @Override
    public void doRequestOtp() {
        view.displayProgress(true);
        view.disableOtpButton();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_BUNDLE, String.valueOf(viewModel.getSecurity2()));
        ((SessionView) mContext).sendDataFromInternet(ACTION_REQUEST_OTP, bundle);
    }

    @Override
    public void doRequestOtpWithCall() {
        view.displayProgress(true);
        view.disableOtpButton();
        Bundle bundle = new Bundle();
        ((SessionView) mContext).sendDataFromInternet(ACTION_REQUEST_OTP_WITH_CALL, bundle);
    }

    @Override
    public void getDataFromArgument(Bundle argument) {
        if (argument != null) {
            int Security1 = argument.getInt("security_1");
            int Security2 = argument.getInt("security_2");
            String UserID = argument.getString("user_id", "");

            viewModel.setSecurity1(Security1);
            viewModel.setSecurity2(Security2);
            viewModel.setUserID(UserID);

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
        if (text == null && text.equals("")) {
            return false;
        } else {
            return true;
        }
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
                if(result == 0){
                    view.showError(context.getString(R.string.error_user_truecaller));
                }else {
                    storeUUID(context, uuid);
                    fetchDataFromInternet(SecurityQuestionPresenter.MAKE_LOGIN,null);
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
        ((SessionView)mContext).verifyTruecaller();
    }

    @Override
    public void showTrueCaller(Context context) {
        view.showTrueCaller(appInstalledOrNot(context, "com.truecaller"));
    }

    @Override
    public void unSubscribe() {
        interactor.unSubscribe();
    }

    private boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public void doSendAnalytics() {
        ScreenTracking.screen(AppScreen.SCREEN_OTP_SQ);
    }
}
