package com.tokopedia.instantdebitbca.data.view.utils;

import android.os.Build;

import com.tokopedia.abstraction.common.utils.GlobalConfig;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by nabillasabbaha on 27/03/19.
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

    private static final String userAgentFormat = "TkpdConsumer/%s (%s;)";

    public static String getUserAgent(){
        return String.format(userAgentFormat, GlobalConfig.VERSION_NAME, "Android "+ Build.VERSION.RELEASE);
    }
}
