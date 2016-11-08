package com.tokopedia.core.session.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.home.ParentIndexHome;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.model.RegisterSuccessModel;
import com.tokopedia.core.session.model.RegisterViewModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.analytics.TrackingUtils;

import org.json.JSONObject;
import org.parceler.Parcels;

/**
 * @author m.normansyah
 * @version 2
 *          modified at 22/11/2015 - Move NetworkHandler to DownloadService
 * @since 13/11/2015
 */
@Deprecated
public class RegisterNextImpl implements RegisterNext {
    public static final int MAX_PHONE_NUMBER = 13;
    public static final int MIN_PHONE_NUMBEr = 6;
    RegisterNextView registerNextView;
    RegisterViewModel model;
    Context context;

    public RegisterNextImpl(RegisterNextView registerNextView) {
        this.registerNextView = registerNextView;
    }

    @Override
    public void initData(Context context) {
        if (isAfterRotate()) {
            registerNextView.setPasswordText(model.getmPassword());
            registerNextView.setConfirmPasswordText(model.getmConfirmPassword());
            registerNextView.setEmailText(model.getmEmail());
            registerNextView.toggleAgreement(model.isAgreedTermCondition());
        }
        registerNextView.showProgress(model.isRegisterLoading());
        if (model.isRegisterLoading()) {
            // hit api again
            registerToServer(context);
        }
        registerNextView.setTermPrivacyText(formatTermAndCondition(
                context.getString(R.string.detail_term_and_privacy),
                context.getString(R.string.link_term_condition),
                context.getString(R.string.link_privacy_policy)
        ));
        this.context = context;
    }

    @Override
    public String formatTermAndCondition(String termAndPrivacy, String linkTermCondition, String linkPrivacyPolicy) {
        return String.format("%s %s serta %s", termAndPrivacy, linkTermCondition, linkPrivacyPolicy);
    }

    @Override
    public void fetchArguments(Bundle argument) {
        if (argument != null) {
            model = new RegisterViewModel();
            model.setmName(argument.getString(RegisterNextView.FULLNAME));
            model.setmPhone(argument.getString(RegisterNextView.PHONENUMBER));
            model.setmGender(argument.getInt(RegisterNextView.GENDER));
            model.setDateText(argument.getString(RegisterNextView.BIRTHDAYTTL));
            model.setmEmail(argument.getString(RegisterNextView.EMAIL, ""));
        }
    }

    @Override
    public void fetchFromPreference(Context context) {
        if (SessionHandler.isRegisterNextEnter(context)) {
            RegisterViewModel reg = SessionHandler.getRegisterNext(context);
            model.setmEmail(reg.getmEmail());
            model.setmPassword(reg.getmPassword());
            model.setmConfirmPassword(reg.getmConfirmPassword());
            model.setIsAgreedTermCondition(reg.isAgreedTermCondition());
        } else {
            Log.i(TAG, messageTAG + " fetchFromPreference(Context context) : no data !!!");
        }
    }

    @Override
    public void fetchRotationData(Bundle argument) {
        if (argument != null) {
            model = Parcels.unwrap(argument.getParcelable(DATA));
        }
    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {
        argument.putParcelable(DATA, Parcels.wrap(model));
    }

    @Override
    public void initDataInstance(Context context) {
        if (!isAfterRotate()) {
            model = new RegisterViewModel();
        }
    }

    @Override
    public boolean isAfterRotate() {
        return model != null;
    }

    @Override
    public void registerToServer(Context context) {
        boolean isNeedLogin = true;
        Bundle bundle = new Bundle();
        bundle.putParcelable(DownloadService.REGISTER_MODEL_KEY, Parcels.wrap(model));
        bundle.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);
        ((SessionView) context).sendDataFromInternet(DownloadService.REGISTER, bundle);
    }

    @Override
    public Object parseJSON(JSONObject jsonObject) {
        throw new RuntimeException("don't use this !!!");
    }

    @Override
    public void updateData(int type, Object... data) {
        switch (type) {
            case CONFIRM_PASSWORD_POS:
                String temp = (String) data[0];
                model.setmConfirmPassword(temp);
                break;
            case PASSWORD_POS:
                temp = (String) data[0];
                model.setmPassword(temp);
                break;
            case EMAIL_POS:
                temp = (String) data[0];
                model.setmEmail(temp);
                break;
            case CHECKED_T_AND_COND:
                boolean x = (boolean) data[0];
                model.setIsAgreedTermCondition(x);
                break;
            case IS_LOADING:
                boolean isLoading = (boolean) data[0];
                model.setIsRegisterLoading(isLoading);
                break;
        }
    }

    @Override
    public void setData(int type, Bundle data) {
        switch (type) {
            case DownloadService.REGISTER:
//                AnalyticsHandler.init(context)
//                        .type(com.tokopedia.tkpd.analytics.Type.APPSFLYER).send("registration", "");

                UnifyTracking.sendAFCompleteRegistrationEvent();

                RegisterSuccessModel registerSuccessModel = Parcels.unwrap(data.getParcelable(DownloadService.REGISTER_MODEL_KEY));
                switch (registerSuccessModel.getIsActive()) {
                    case RegisterSuccessModel.ACTIVATION_PROCESS:
                        UnifyTracking.eventLocaRegister(registerSuccessModel.getUserId()+"");
                        ((SessionView) context).moveToActivationResend(model.getmEmail());
                        break;
                    case RegisterSuccessModel.
                            CREATE_PASSWORD_PROCESS:
                        break;
                    case RegisterSuccessModel.GO_TO_HOME_PROCESS:
                        SessionHandler sessionHandler = new SessionHandler(context);
                        sessionHandler.SetLoginSession(true, registerSuccessModel.getUserId()+"","","", true);
                        TrackingUtils.eventLoca(context.getString(R.string.event_register) + " with e-mail");
                        CommonUtils.dumper("LocalTag : DEFAULT REGISTER");
                        context.startActivity(new Intent(context, ParentIndexHome.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        ((AppCompatActivity) context).finish();
                        break;
                }
                break;
        }
    }

    public static void testPhoneNumberValidation() {
        Log.d(TAG, "Phone number 1234567890 validation result: " + validatePhoneNumber("1234567890"));
        Log.d(TAG, "Phone number 123-456-7890 validation result: " + validatePhoneNumber("123-456-7890"));
        Log.d(TAG, "Phone number 123-456-7890 x1234 validation result: " + validatePhoneNumber("123-456-7890 x1234"));
        Log.d(TAG, "Phone number 123-456-7890 ext1234 validation result: " + validatePhoneNumber("123-456-7890 ext1234"));
        Log.d(TAG, "Phone number (123)-456-7890 validation result: " + validatePhoneNumber("(123)-456-7890"));
        Log.d(TAG, "Phone number 123.456.7890 validation result: " + validatePhoneNumber("123.456.7890"));
        Log.d(TAG, "Phone number 123 456 7890 validation result: " + validatePhoneNumber("123 456 7890"));
    }

    public static boolean validatePhoneNumber(String phoneNo) {
        Log.d(TAG, "Phone number " + phoneNo + " start validating");
        //validate phone numbers of format "1234567890"
        for (int i = MIN_PHONE_NUMBEr; i <= MAX_PHONE_NUMBER; i++) {
            if (phoneNo.matches("\\d{" + i + "}")) return true;
        }
        //validating phone number with -, . or spaces
//        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
        //validating phone number with extension length from 3 to 5
//        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
        //validating phone number where area code is in braces ()
//        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
        //return false if nothing matches the input
//        else
        return false;

    }
}
