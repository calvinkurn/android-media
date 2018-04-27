package com.tokopedia.otp.cotp.view.viewmodel;

import android.content.Context;

import com.tokopedia.otp.R;
import com.tokopedia.otp.common.OTPAnalytics;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;

/**
 * @author by nisie on 1/4/18.
 */

public class InterruptVerificationViewModel {

    private String mode;
    private String appScreenName;
    private int iconId;
    private String userName;
    private String promptText;
    private String buttonText;
    private boolean hasOtherMethod;


    public InterruptVerificationViewModel(String mode, String appScreenName, int iconId,
                                          String promptText, String buttonText) {
        this.mode = mode;
        this.appScreenName = appScreenName;
        this.iconId = iconId;
        this.promptText = promptText;
        this.buttonText = buttonText;
    }

    public String getMode() {
        return mode;
    }

    public String getAppScreenName() {
        return appScreenName;
    }

    public int getIconId() {
        return iconId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPromptText() {
        return promptText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public static InterruptVerificationViewModel createDefaultSmsInterruptPage(String phone,
                                                                               Context context) {
        return new InterruptVerificationViewModel(
                RequestOtpUseCase.MODE_SMS,
                OTPAnalytics.Screen.SCREEN_INTERRUPT_VERIFICATION_SMS,
                R.drawable.ic_verification_sms,
                context.getString(R.string.to_verify_sms)
                        + "<br><font color='#b3000000'>" + MethodItem.getMaskedPhoneNumber(phone) +
                        "</font>.",
                context.getString(R.string.send_sms_verification)
        );
    }

    public static InterruptVerificationViewModel createDefaultEmailInterruptPage(String email,
                                                                                 Context context) {
        return new InterruptVerificationViewModel(
                RequestOtpUseCase.MODE_EMAIL,
                OTPAnalytics.Screen.SCREEN_INTERRUPT_VERIFICATION_EMAIL,
                R.drawable.ic_verification_email,
                context.getString(R.string.to_verify_email)
                        + "<br><font color='#b3000000'>" + email + "</font>.",
                context.getString(R.string.send_email_verification)
        );
    }

    public void setHasOtherMethod(boolean hasOtherMethod) {
        this.hasOtherMethod = hasOtherMethod;
    }

    public boolean isHasOtherMethod() {
        return hasOtherMethod;
    }
}
