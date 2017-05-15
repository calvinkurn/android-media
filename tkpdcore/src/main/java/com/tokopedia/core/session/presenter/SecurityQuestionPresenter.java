package com.tokopedia.core.session.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.session.model.QuestionFormModel;

import org.json.JSONObject;

/**
 * Created by m.normansyah on 05/11/2015.
 */
public interface SecurityQuestionPresenter {
    String TAG = "MNORMANSYAH";
    String messageTAG = SecurityQuestionPresenter.class.getSimpleName()+" : ";
    int timeoutTime = 10000;
    int numberOftry = 10;

    String ANSWER = "answer";
    String QUESTION = "question";
    String USER_CHECK_SECURITY_ONE = "user_check_security_1";
    String USER_CHECK_SECURITY_TWO = "user_check_security_2";

    int SECURITY_QUESTION_TYPE = 0;
    int ANSWER_SECURITY_QUESTION_TYPE = 1;
    int REQUEST_OTP_TYPE =2;
    int ANSWER_SECURITY_QUESTION_FALSE_TYPE =3;
    int REQUEST_OTP_PHONE_TYPE =4;
    int VERIFY_PHONE = 5;
    int MAKE_LOGIN = 6;

    int networkHandlerSize = 3;

    void initInstances(Context context);

    void fetchDataFromInternet(int type, Object... data);

    Object parseJSON(int type, JSONObject jsonObject);

    int determineQuestionType(int question, String title);

    boolean isSecurityQuestion();

    boolean isOtp();

    void storeNecessaryID();

    void getQuestionForm();

    void doAnswerQuestion(String answer);

    void doRequestOtp();

    void getDataFromArgument(Bundle savedInstanceState);

    void saveAnswer(String text);

    void saveOTPAnswer(String text);

    String getOtpSendString();

    boolean isValidForm(String text);

    boolean isAfterRotate();

    void saveDataBeforeRotate(Bundle bundle);

    void getDataAfterRotate(Bundle bundle);

    void initDataAfterRotate();

    boolean isLogin(JSONObject response);

    void setData(int type, Bundle data);

    void updateViewModel(int type, Object... data);

    void updateModel(QuestionFormModel model);

    void doRequestOtpWithCall();

    void verifyTruecaller(Context activity, String phoneNumber);

    void getPhoneTrueCaller();

    void showTrueCaller(Context context);

    void unSubscribe();

    void doSendAnalytics();

    void doRequestOtpToEmail();
}
