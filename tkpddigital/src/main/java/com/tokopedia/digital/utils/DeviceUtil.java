package com.tokopedia.digital.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;

import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.Validation;
import com.tokopedia.digital.utils.data.RequestBodyAppsFlyer;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author anggaprasetiyo on 3/6/17.
 */

public class DeviceUtil {

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                     enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            return "127.0.0.1";
        }
        return "127.0.0.1";
    }

    private static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static String getUserAgentForApiCall() {
        return "Android Tokopedia Application/"
                + GlobalConfig.getPackageApplicationName()
                + " v." + GlobalConfig.VERSION_NAME
                + " (" + DeviceUtil.getDeviceName()
                + "; Android; API_"
                + Build.VERSION.SDK_INT
                + "; Version"
                + Build.VERSION.RELEASE + ") ";
    }

    public static RequestBodyIdentifier getDigitalIdentifierParam(Context context) {
        RequestBodyIdentifier requestBodyIdentifier = new RequestBodyIdentifier();
        requestBodyIdentifier.setDeviceToken(GCMHandler.getRegistrationId(context));
        requestBodyIdentifier.setUserId(SessionHandler.getLoginID(context));
        requestBodyIdentifier.setOsType("1");
        return requestBodyIdentifier;
    }

    public static RequestBodyAppsFlyer getAppsFlyerIdentifierParam() {
        AnalyticsCacheHandler analHandler = new AnalyticsCacheHandler();
        RequestBodyAppsFlyer requestBodyAppsFlyer = new RequestBodyAppsFlyer();
        requestBodyAppsFlyer.setAppsflyerId(TrackingUtils.getAfUniqueId());
        requestBodyAppsFlyer.setDeviceId(analHandler.getAdsId());
        return requestBodyAppsFlyer;
    }

    public static String validatePrefixClientNumber(String phoneNumber) {
        if (phoneNumber.startsWith("62")) {
            phoneNumber = phoneNumber.replaceFirst("62", "0");
        }
        if (phoneNumber.startsWith("+62")) {
            phoneNumber = phoneNumber.replace("+62", "0");
        }
        phoneNumber = phoneNumber.replace(".", "");

        return phoneNumber.replaceAll("[^0-9]+", "");
    }

    public static String formatPrefixClientNumber(String phoneNumber) {
        if(phoneNumber==null || "".equalsIgnoreCase(phoneNumber.trim())){
            return phoneNumber;
        }
        phoneNumber=validatePrefixClientNumber(phoneNumber);
        if (!phoneNumber.startsWith("0")) {
            phoneNumber = "0" + phoneNumber;
        }
        return phoneNumber;
    }

    public static String getMobileNumber(Activity context, int simIndex) {
        String phoneNumber = null;
        if (RequestPermissionUtil.checkHasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                if (subscriptionInfos != null && subscriptionInfos.size() > simIndex) {
                    SubscriptionInfo lsuSubscriptionInfo = subscriptionInfos.get(simIndex);
                    if (lsuSubscriptionInfo != null && lsuSubscriptionInfo.getSimSlotIndex() == simIndex) {
                        phoneNumber = lsuSubscriptionInfo.getNumber();
                        phoneNumber= formatPrefixClientNumber(phoneNumber);
                        return phoneNumber;
                    }
                }

            }
        }

        return phoneNumber;
    }

    public static String getOperatorName(Activity context, int simIndex) {
        String operatorName = null;
        if (RequestPermissionUtil.checkHasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                if (subscriptionInfos != null) {
                    for (SubscriptionInfo lsuSubscriptionInfo : subscriptionInfos) {
                        if (lsuSubscriptionInfo != null && lsuSubscriptionInfo.getSimSlotIndex() == simIndex) {
                            if (lsuSubscriptionInfo.getCarrierName() != null) {
                                operatorName = lsuSubscriptionInfo.getCarrierName().toString();
                                return operatorName;
                            }
                        }
                    }
                }

            }
        }
        return operatorName;
    }

    public static PhoneAccountHandle getPhoneHandle(Activity context, int simIndex) {
        PhoneAccountHandle sim1 = null;
        if (RequestPermissionUtil.checkHasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
                    final List<PhoneAccountHandle> enabledAccounts = telecomManager.getCallCapablePhoneAccounts();
                    if (enabledAccounts != null && enabledAccounts.size() > simIndex) {
                        return enabledAccounts.get(simIndex);
                    }
                }
            }
        }
        return sim1;
    }

    public static boolean verifyUssdOperator(String simOperatorName, String selectedOperatorName) {
        simOperatorName = getOperatorFirstName(simOperatorName);
        selectedOperatorName = getOperatorFirstName(selectedOperatorName);
        if (simOperatorName == null || selectedOperatorName == null) {
            return false;
        }
        if ("Tri".equalsIgnoreCase(selectedOperatorName.trim()) && "3".equalsIgnoreCase(simOperatorName.trim())) {
            return true;
        }
        if (simOperatorName.trim().equalsIgnoreCase(selectedOperatorName.trim())) {
            return true;
        }
        return false;


    }

    public static String validateNumber(List<Validation> validationList, String number) {
        String errorString = null;
        if (number == null || validationList == null) {
            errorString = "not valid";
            return errorString;
        }
        for (Validation validation : validationList) {
            if (!Pattern.matches(validation.getRegex(), number)) {
                errorString = validation.getError();
                break;
            }
        }
        return errorString;

    }

    public static boolean matchOperatorAndNumber(Operator operator, String number) {
        if (number == null || operator == null) {
            return false;
        }
        for (String prefix : operator.getPrefixList()) {
            if (number.startsWith(prefix)) {
                return true;
            }

        }
        return false;
    }

    public static boolean validateNumberAndMatchOperator(List<Validation> validationList, Operator operator, String number) {
        if (number == null || operator == null || validationList == null) {
            return false;
        }
        if (validateNumber(validationList, number) == null) {
            if (matchOperatorAndNumber(operator, number)) {
                return true;
            }
        }
        return false;
    }

    public static String getOperatorFirstName(String operatorName) {
        if (operatorName != null && !"".equalsIgnoreCase(operatorName.trim())) {
            return operatorName.split(" ")[0];
        }
        return null;
    }

    public static String loadJSONFromAsset(String nameJson) {
        String json;

        try {
            InputStream is = MainApplication.getAppContext().getAssets().open("json/" + nameJson);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
