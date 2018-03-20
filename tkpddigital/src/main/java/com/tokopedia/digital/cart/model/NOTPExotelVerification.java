package com.tokopedia.digital.cart.model;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import com.exotel.verification.ConfigBuilder;
import com.exotel.verification.ExotelVerification;
import com.exotel.verification.VerificationListener;
import com.exotel.verification.contracts.Config;
import com.exotel.verification.contracts.VerificationFailed;
import com.exotel.verification.contracts.VerificationStart;
import com.exotel.verification.contracts.VerificationSuccess;

import com.exotel.verification.exceptions.InvalidConfigException;
import com.exotel.verification.exceptions.PermissionNotGrantedException;
import com.exotel.verification.exceptions.VerificationAlreadyInProgressException;
import com.logentries.logger.AndroidLogger;
import com.tkpd.library.utils.AnalyticsLog;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.digital.analytics.NOTPTracking;
import com.tokopedia.digital.utils.DeviceUtil;

import java.util.List;

/**
 * Created by sandeepgoyal on 04/12/17.
 */

public class NOTPExotelVerification {
    private static final int WAIT_SECONDS = 10;
    private static NOTPExotelVerification mInstance = new NOTPExotelVerification();
    public static final String APPLICATION_ID = "2d3a8f96d9e7436a9a5f93cae8d5ddd3";
    public static final String ACCOUNT_SID ="tokopedianotp";
    public static final String SECRET_KEY = AuthUtil.KEY.KEY_NOTP;
    public static final String TAG = "NOTPVerification";


    public static final String FIREBASE_NOTP_REMOTE_CONFIG_KEY = "app_notp_enabled"; // For Dev Testing
    public static final String FIREBASE_NOTP_TEST_REMOTE_CONFIG_KEY = "app_notp_test";

    private static String COUNTRY_CODE = "+62";

    private static NOTPVerificationListener verificationlistener;
    public static NOTPExotelVerification getmInstance() {
        return mInstance;
    }

    public void verifyNo(String phoneNo, Context context, final NOTPVerificationListener verificationListener) {
        this.verificationlistener = verificationListener;
        if(phoneNo == null) {
            verificationListener.onVerificationFail();
            return;
        }
        phoneNo = convertE164Fromat(phoneNo);

       /* Check if Truecaller installed
       *  and If We have number information in phone then verify verification number and phone number are same
       *
       * */

       if(istruecallerInstalled(context)) {
           AnalyticsLog.printNOTPLog("NOTP_Verification True Caller Installed ");
       }
        if(istruecallerInstalled(context) || !isNumberExistInPhone((Activity) context,phoneNo)) {
            verificationListener.onVerificationFail();
            return;
        }





        Config config = null;
        try {

            config= new ConfigBuilder(APPLICATION_ID, SECRET_KEY, ACCOUNT_SID, context.getApplicationContext()).Build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        ExotelVerification eVerification = null;
        try {
            AnalyticsLog.printNOTPLog("NOTP Verification IntializationStart ");

            eVerification = new ExotelVerification(config);

            AnalyticsLog.printNOTPLog("NOTP Verification IntializationComplete ");

        } catch (PermissionNotGrantedException e) {
            e.printStackTrace();
        }
        if(eVerification == null) {
            verificationlistener.onVerificationFail();
            return;
        }
        final String finalPhoneNo = phoneNo;
        class VerifyListener implements VerificationListener {
            public void onVerificationStarted(VerificationStart verificationStart) {
            }

            public void onVerificationSuccess(VerificationSuccess
                                                      verificationSuccess) {
                AnalyticsLog.printNOTPLog("NOTP verification Success ");
                verificationListener.onVerificationSuccess();
            }

            public void onVerificationFailed(VerificationFailed
                                                     verificationFailed) {
                AnalyticsLog.printNOTPLog("NOTP verification Fail ");
                verificationlistener.onVerificationFail();
            }
        }

        try {
            AnalyticsLog.printNOTPLog("NOTP Verification startVerification ");
            eVerification.startVerification(new VerifyListener(), phoneNo,WAIT_SECONDS);
        } catch (VerificationAlreadyInProgressException e) {
            e.printStackTrace();
        }
    }

    public interface NOTPVerificationListener {
        public void onVerificationSuccess();
        public void onVerificationFail();
    }

    private String convertE164Fromat(String phonNumber) {
        phonNumber = DeviceUtil.validatePrefixClientNumber(phonNumber);
        if(phonNumber.charAt(0) == '0') {
            phonNumber = COUNTRY_CODE + phonNumber.substring(1);
        }else {
            phonNumber = COUNTRY_CODE + phonNumber;
        }
        return phonNumber;
    }

    private boolean isNumberExistInPhone(Activity context,String number) {

        List<SubscriptionInfo> subscriptionInfos = null;
        boolean result = true;
        boolean isPhoneMatch = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
            if (subscriptionInfos != null) {
                for (int simIndex = 0; simIndex < subscriptionInfos.size(); simIndex++) {
                    String phoneNumber = DeviceUtil.getMobileNumber(context, simIndex);
                    if (phoneNumber != null) {
                        result = false;
                        if (phoneNumber.isEmpty() || convertE164Fromat(phoneNumber).equals(number)) {
                            isPhoneMatch = !phoneNumber.isEmpty();
                            result = true;
                            break;
                        }
                    }else {
                        result = true;
                    }
                }
            }
        }
        if(result) {
            AnalyticsLog.printNOTPLog("NOTP Verification  Number Exist in Phone"+ (isPhoneMatch?" Match  "+ number:"Empty "));
            NOTPTracking.eventNOTPConfiguration(false,false,true);
        }else {
            AnalyticsLog.printNOTPLog("NOTP Verification  Number not match");

        }

        return result;
    }

    private boolean istruecallerInstalled(Context context) {

        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo("com.truecaller", PackageManager.GET_ACTIVITIES);
            NOTPTracking.eventNOTPConfiguration(false,true,false);

            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

}
