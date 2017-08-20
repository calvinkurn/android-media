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
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

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

    public static String getMobileNumber(Activity context, int simPosition) {
        String phoneNumber = null;
        if (RequestPermissionUtil.checkHasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                if (subscriptionInfos != null && subscriptionInfos.size() > simPosition) {
                    SubscriptionInfo lsuSubscriptionInfo = subscriptionInfos.get(simPosition);
                    if (lsuSubscriptionInfo != null && lsuSubscriptionInfo.getSimSlotIndex() == simPosition) {
                        phoneNumber = lsuSubscriptionInfo.getNumber();
                        return phoneNumber;
                    }
                }

            }
        }

        return phoneNumber;
    }

    public static String getMobileNumberForSim2(Activity context) {
        String phoneNumber = null;
        if (RequestPermissionUtil.checkHasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                if (subscriptionInfos != null && subscriptionInfos.size() > 1) {
                    SubscriptionInfo lsuSubscriptionInfo = subscriptionInfos.get(1);
                    if (lsuSubscriptionInfo != null && lsuSubscriptionInfo.getSimSlotIndex() == 1) {
                        phoneNumber = lsuSubscriptionInfo.getNumber();
                        return phoneNumber;
                    }
                }

            }
        }

        return phoneNumber;
    }

    public static String getOperatorName(Activity context, int simPosition) {
        String operatorName = null;
        if (RequestPermissionUtil.checkHasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                if (subscriptionInfos != null && subscriptionInfos.size() > simPosition) {
                    SubscriptionInfo lsuSubscriptionInfo = subscriptionInfos.get(simPosition);
                    if (lsuSubscriptionInfo != null && lsuSubscriptionInfo.getSimSlotIndex() == simPosition) {
                        if (lsuSubscriptionInfo.getCarrierName() != null) {
                            operatorName = lsuSubscriptionInfo.getCarrierName().toString();
                            return operatorName;
                        }
                    }
                }

            }
        }
        return operatorName;
    }

    public static String getOperatorNameForSim2(Activity context) {
        String operatorName = null;
        if (RequestPermissionUtil.checkHasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                if (subscriptionInfos != null && subscriptionInfos.size() > 1) {
                    SubscriptionInfo lsuSubscriptionInfo = subscriptionInfos.get(1);
                    if (lsuSubscriptionInfo != null && lsuSubscriptionInfo.getSimSlotIndex() == 1) {
                        if (lsuSubscriptionInfo.getCarrierName() != null) {
                            operatorName = lsuSubscriptionInfo.getCarrierName().toString();
                            return operatorName;
                        }
                    }
                }

            }
        }
        return operatorName;
    }

    public static PhoneAccountHandle getPhoneHandle(Activity context, int simPosition) {
        PhoneAccountHandle sim1 = null;
        if (RequestPermissionUtil.checkHasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
                    final List<PhoneAccountHandle> enabledAccounts = telecomManager.getCallCapablePhoneAccounts();
                    if (enabledAccounts != null && enabledAccounts.size() > simPosition) {
                        return enabledAccounts.get(simPosition);
                    }
                }
            }
        }
        return sim1;
    }
}
