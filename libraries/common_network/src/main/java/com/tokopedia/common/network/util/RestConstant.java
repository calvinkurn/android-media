package com.tokopedia.common.network.util;

public interface RestConstant {
    String BASE_URL = "http://tokopedia.com/";
    long HTTP_SUCCESS = 200;
    long MINUTE_MS = 1000 * 60;
    long HOUR_MS = MINUTE_MS * 60;
    long DAY_MS = 24 * HOUR_MS;

    enum ExpiryTimes {
        HOUR(HOUR_MS),
        MINUTE_90(90 * MINUTE_MS),
        DAY(DAY_MS),
        WEEK(7 * DAY_MS),
        MONTH(30 * DAY_MS),
        MONTHS_3(90 * DAY_MS),
        MONTHS_6(180 * DAY_MS),
        YEAR(365 * DAY_MS);

        ExpiryTimes(long expiryTime) {
            this.mMs = expiryTime;
        }

        private long mMs;

        public long val() {
            return this.mMs;
        }
    }
}
