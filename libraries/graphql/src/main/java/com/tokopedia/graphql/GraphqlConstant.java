package com.tokopedia.graphql;

public interface GraphqlConstant {
    String LOG_TAG = "Graphql-Datalayer ";
    String TIMBER_JSON_PARSE_TAG = "P1#GQL_PARSE_ERROR#json;err='%s';req='%s";
    long MINUTE_MS = 1000 * 60;
    long HOUR_MS = MINUTE_MS * 60;
    long DAY_MS = 24 * HOUR_MS;

    interface GqlApiKeys {
        String ANDROID_FLAG = "x-android: 1";
        String CACHE = "x-tkpd-clc";
        String QUERYHASH = "queryhash";
        String QUERY = "query";
        String VARIABLES = "variables";
        String OPERATION_NAME = "operationName";
        String DATA = "data";
        String ERROR = "errors";
    }

    enum ExpiryTimes {
        MINUTE_1(MINUTE_MS),
        MINUTE_30(30 * MINUTE_MS),
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
