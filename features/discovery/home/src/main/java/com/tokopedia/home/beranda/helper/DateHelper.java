package com.tokopedia.home.beranda.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by errysuprayogi on 3/23/18.
 */

public class DateHelper {

    public static Date getExpiredTime(String expiredTimeString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZ");
        try {
            return format.parse(expiredTimeString);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static boolean isExpired(long serverTimeOffset, Date expiredTime) {
        Date serverTime = new Date(System.currentTimeMillis());
        serverTime.setTime(
                serverTime.getTime() + serverTimeOffset
        );
        return serverTime.after(expiredTime);
    }
}
