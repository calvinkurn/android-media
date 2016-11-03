package com.tokopedia.tkpd.facade;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.interfaces.PhoneVerificationInterfaces;
import com.tokopedia.tkpd.network.NetworkHandler;

import org.json.JSONException;
import org.json.JSONObject;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.v4.NetworkConfig;
import com.tokopedia.tkpd.network.v4.NetworkHandlerBuilder;
import com.tokopedia.tkpd.network.v4.OnNetworkResponseListener;
import com.tokopedia.tkpd.network.v4.OnNetworkTimeout;
import com.tokopedia.tkpd.service.DownloadService;
import com.tokopedia.tkpd.service.model.GetVerificationNumberForm;
import com.tokopedia.tkpd.util.StringVariable;
import com.tokopedia.tkpd.var.TkpdUrl;

import java.util.ArrayList;

import nope.yuuji.kirisame.network.entity.NetError;

/**
 * Created by Kris on 4/20/2015.
 * Modified by m.normansyah 23/11/2015
 * need to move to DownloadService
 */
@Deprecated
public class FacadePhoneVerification {
    private Context context;
    private String URL;
    Gson gson;
    public static final String TAG = "MNORMANSYAH";
    public static final String messageTAG = "FacadePhoneVerification : ";

    // look at getVerificationNumber Result
    // 1 if verified
    // 0 if not verified
    int verified;

    private FacadePhoneVerification(){}

    public static FacadePhoneVerification createInstance(Context context, int verified){
        FacadePhoneVerification facadePhoneVerification = new FacadePhoneVerification();
        facadePhoneVerification.context = context;
        facadePhoneVerification.verified = verified;
        return facadePhoneVerification;
    }

    public static FacadePhoneVerification createInstance(Context context){
        FacadePhoneVerification facadePhoneVerification = new FacadePhoneVerification();
        facadePhoneVerification.context = context;
        return facadePhoneVerification;
    }

    public void getVerificationNumber(final PhoneVerificationInterfaces.getVerificationResponseListener listener){
        gson = new GsonBuilder().create();
        boolean isNeedLogin = true;
        com.tokopedia.tkpd.network.v4.NetworkHandler networkHandler = new NetworkHandlerBuilder(NetworkConfig.GET, context,
                TkpdBaseURL.User.URL_MSISDN+ TkpdBaseURL.User.PATH_GET_VERIFICATION_NUMBER_FORM)
                .setNeedLogin(isNeedLogin)
                .setIdentity()
                .setAllParamSupply(true)
                .setNetworkResponse(new OnNetworkResponseListener() {
                    @Override
                    public void onResponse(JSONObject Response) {
                        Log.d(TAG, messageTAG + Response.toString());
                        JSONObject jsonObject = Response.optJSONObject(GetVerificationNumberForm.MSISDN_TAG);
                        GetVerificationNumberForm form = null;
                        if(jsonObject!=null) {
                            form = gson.fromJson(jsonObject.toString(), GetVerificationNumberForm.class);
                            listener.onPhoneNumberResult(form);
                        }
                    }

                    @Override
                    public void onMessageError(ArrayList<String> MessageError) {
                        CommonUtils.dumper("FAILED GET VERIFICATION STATUS "+MessageError.toString());
                        ErrorMessage(MessageError);
                    }

                    @Override
                    public void onNetworkError(NetError error, int errorCode) {
                        CommonUtils.dumper("FAILED GET VERIFICATION STATUS "+error);
                    }
                })
                .setNetworkTimeout(new OnNetworkTimeout() {
                    @Override
                    public void onNetworkTimeout(com.tokopedia.tkpd.network.v4.NetworkHandler network) {
                        CommonUtils.dumper("TIMEOUT GET VERIFICATION STATUS");
                    }
                })
                .setRetryPolicy(DownloadService.TIMEOUT_TIME, DownloadService.NUMBER_OF_TRY)
                .finish();
        networkHandler.commit();
    }
    // https://<host_url>/v4/action/msisdn/send_verification_otp.pl
    public void requestVerificationCode(String phoneNumber, final PhoneVerificationInterfaces.requestCodeListener listener){
        boolean isNeedLogin = true;
        com.tokopedia.tkpd.network.v4.NetworkHandler networkHandler = new NetworkHandlerBuilder(NetworkConfig.POST, context,
                TkpdBaseURL.User.URL_MSISDN_ACTION+ TkpdBaseURL.User.PATH_SEND_VERIFICATION_OTP)
                .setNeedLogin(isNeedLogin)
                .setIdentity()
                .addParam("phone", phoneNumber)
                .addParam("update_flag", 0+"")
                .addParam("email_code","")
                .setAllParamSupply(true)
                .setNetworkResponse(new OnNetworkResponseListener() {
                    @Override
                    public void onResponse(JSONObject Response) {
                        Log.d(TAG, messageTAG+Response);
                        listener.onProcessDone();
                    }

                    @Override
                    public void onMessageError(ArrayList<String> MessageError) {
                        ErrorMessage(MessageError);
                    }

                    @Override
                    public void onNetworkError(NetError error, int errorCode) {
                        Log.e(TAG, messageTAG + error);
                    }
                })
                .finish();
        networkHandler.commit();
    }
    // https://<host_url>/v4/action/msisdn/do_verification_msisdn.pl
    public void sendVerification(String inputConfirmationCode, String phoneNumber, final PhoneVerificationInterfaces.sendVerificationListener listener){
        boolean isNeedLogin = true;
        com.tokopedia.tkpd.network.v4.NetworkHandler networkHandler = new NetworkHandlerBuilder(NetworkConfig.POST, context,
                TkpdBaseURL.User.URL_MSISDN_ACTION+ TkpdBaseURL.User.PATH_DO_VERIFICATION_MSISDN)
                .setNeedLogin(isNeedLogin)
                .setIdentity()
                .addParam("phone", phoneNumber)
                .addParam("code",inputConfirmationCode)
                .setAllParamSupply(true)
                .setNetworkResponse(new OnNetworkResponseListener() {
                    @Override
                    public void onResponse(JSONObject Response) {
                        Log.d(TAG, messageTAG+Response);
                        try {
                            if(Response.getInt("is_success") >= 1)
                                listener.onVerificationSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMessageError(ArrayList<String> MessageError) {
                        ErrorMessage(MessageError);
                    }

                    @Override
                    public void onNetworkError(NetError error, int errorCode) {
                        Log.e(TAG, messageTAG + error);
                    }
                }).finish();
        networkHandler.commit();
    }

    private void ErrorMessage(ArrayList<String> MessageError){
        for(int i=0; i<MessageError.size(); i++){
            Toast.makeText(context, MessageError.get(i), Toast.LENGTH_LONG).show();
//            if((i+1)<MessageError.size())
//                Toast.makeText(context, "No Error", Toast.LENGTH_LONG).show();
        }
    }
}
