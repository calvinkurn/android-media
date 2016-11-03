package com.tokopedia.tkpd.session.presenter;

import android.view.View;

import com.tokopedia.tkpd.presenter.BaseView;
import com.tokopedia.tkpd.session.model.LoginInterruptModel;
import com.tokopedia.tkpd.session.model.OTPModel;
import com.tokopedia.tkpd.session.model.QuestionFormModel;

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

    void finishSecurityQuestion(LoginInterruptModel loginInterruptModel);

    void initListener();

    void showViewSecurity();

    void showViewOtp();

    void setAnswerSecurity(String text);

    void setAnswerOTP(String text);

    void displayError(boolean isError);

    void displayProgress(boolean isShow);

}
