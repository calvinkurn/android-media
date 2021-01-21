package com.tokopedia.digital.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.digital.newcart.data.entity.requestbody.RequestBodyAppsFlyer;
import com.tokopedia.user.session.UserSession;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

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
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
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
        UserSession userSession = new UserSession(context);
        requestBodyIdentifier.setDeviceToken(userSession.getDeviceId());
        requestBodyIdentifier.setUserId(userSession.getUserId());
        requestBodyIdentifier.setOsType("1");
        return requestBodyIdentifier;
    }

    public static RequestBodyAppsFlyer getAppsFlyerIdentifierParam(String afUniqueId, String adsId) {
        RequestBodyAppsFlyer requestBodyAppsFlyer = new RequestBodyAppsFlyer();
        requestBodyAppsFlyer.setAppsflyerId(afUniqueId);
        requestBodyAppsFlyer.setDeviceId(adsId);
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
}
