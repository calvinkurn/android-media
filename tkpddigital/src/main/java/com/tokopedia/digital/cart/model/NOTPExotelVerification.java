package com.tokopedia.digital.cart.model;

import android.content.Context;
import android.util.Log;

import com.exotel.verification.ExotelVerification;
import com.exotel.verification.VerificationListener;
import com.exotel.verification.contracts.Config;
import com.exotel.verification.contracts.VerificationFailed;
import com.exotel.verification.contracts.VerificationStart;
import com.exotel.verification.contracts.VerificationSuccess;
import com.exotel.verification.exceptions.ClientBuilderException;
import com.exotel.verification.exceptions.InvalidConfigException;
import com.exotel.verification.exceptions.PermissionNotGrantedException;
import com.exotel.verification.exceptions.VerificationAlreadyInProgressException;
import com.tokopedia.digital.utils.DeviceUtil;

/**
 * Created by sandeepgoyal on 04/12/17.
 */

public class NOTPExotelVerification {
    private static final int WAIT_SECONDS = 5;
    private static NOTPExotelVerification mInstance = new NOTPExotelVerification();
    public static final String APPLICATION_ID = "2d3a8f96d9e7436a9a5f93cae8d5ddd3";
    public static final String ACCOUNT_SID ="tokopedianotp";
    public static final String SECRET_KEY = "nulayukawoju";
    public static final String TAG = NOTPExotelVerification.class.getName();

    private static NOTPVerificationListener verificationlistener;
    public static NOTPExotelVerification getmInstance() {
        return mInstance;
    }

    public void verifyNo(String phoneNo, Context context, final NOTPVerificationListener verificationListener) {
        this.verificationlistener = verificationListener;
        phoneNo = DeviceUtil.validatePrefixClientNumber(phoneNo);
        phoneNo = "+62"+phoneNo.substring(1);
        ExotelVerification eVerification = new ExotelVerification();
        Config config = null;
        try {
            config = eVerification.configBuilder().
                    applicationId(APPLICATION_ID).
                    accountSid(ACCOUNT_SID).
                    sharedSecretKey(SECRET_KEY).
                    context(context).
                    Build();
        } catch (ClientBuilderException e) {
            e.printStackTrace();
        }
        class VerifyListener implements VerificationListener {
            public void onVerificationStarted(VerificationStart verificationStart) {
                Log.e(TAG,"start");
            }

            public void onVerificationSuccess(VerificationSuccess
                                                      verificationSuccess) {
                Log.e(TAG,"success");
                verificationListener.onVerificationSuccess();
            }

            public void onVerificationFailed(VerificationFailed
                                                     verificationFailed) {
                Log.e(TAG,"failed");
                verificationlistener.onVerificationFail();
            }
        }

        try {
            Log.e(TAG,"intializationStart");
            eVerification.initializeVerification(config);
            Log.e(TAG,"intializationComplete");
        } catch (PermissionNotGrantedException e) {
            e.printStackTrace();
        } catch (InvalidConfigException e) {
            e.printStackTrace();
        }
        try {
            eVerification.startVerification(new VerifyListener(), phoneNo,WAIT_SECONDS);
        } catch (VerificationAlreadyInProgressException e) {
            e.printStackTrace();
        }
    }

    public interface NOTPVerificationListener {
        public void onVerificationSuccess();
        public void onVerificationFail();
    }

}
