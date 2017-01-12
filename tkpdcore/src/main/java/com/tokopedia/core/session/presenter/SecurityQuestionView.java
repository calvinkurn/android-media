package com.tokopedia.core.session.presenter;

import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.session.model.LoginInterruptModel;
import com.tokopedia.core.session.model.OTPModel;
import com.tokopedia.core.session.model.QuestionFormModel;

/**
 * Created by m.normansyah on 05/11/2015.
 */
public interface SecurityQuestionView extends BaseView {
    String EXAMPLE_KEY = "example";
    String QUESTION_KEY = "question";
    String TITLE_KEY = "title";
    String SECURITY_1_KEY = "security_1";
    String SECURITY_2_KEY = "security_2";
    String USER_ID_KEY = "user_id";
    String TAG = "MNORMANSYAH";
    String messageTAG = "SecurityQuestionView : ";

    void setModel(QuestionFormModel data);

    void requestOTP(OTPModel model);

    void initListener();

    void showViewOtp();

    void setAnswerSecurity(String text);

    void setAnswerOTP(String text);

    void displayError(boolean isError);

    void displayProgress(boolean isShow);

    void startTimer();

    void destroyTimer();

    void disableOtpButton();

}
