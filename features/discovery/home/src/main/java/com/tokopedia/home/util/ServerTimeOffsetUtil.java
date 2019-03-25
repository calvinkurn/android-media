package com.tokopedia.home.util;

import java.util.Date;

public class ServerTimeOffsetUtil {
    private static final long ONE_SECOND = 1000;

    public static long getServerTimeOffset(long serverTimeMillisecond){
        Date localDate = new Date();
        long localTimeMillis = localDate.getTime();
        return serverTimeMillisecond-localTimeMillis;
    }

    public static long getServerTimeOffsetFromUnix(long serverTimeUnix) {
        long serverTimemillis = serverTimeUnix * ONE_SECOND;
        return ServerTimeOffsetUtil.getServerTimeOffset(serverTimemillis);
    }
}
