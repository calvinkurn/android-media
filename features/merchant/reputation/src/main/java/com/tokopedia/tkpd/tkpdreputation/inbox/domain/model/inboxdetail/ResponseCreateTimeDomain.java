package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

/**
 * @author by nisie on 8/30/17.
 */

public class ResponseCreateTimeDomain {

    private String dateTimeFmt1;
    private String unixTimestamp;
    private String dateTimeIos;
    private String dateTimeAndroid;

    public ResponseCreateTimeDomain(String dateTimeFmt1, String unixTimestamp,
                                    String dateTimeIos, String dateTimeAndroid) {
        this.dateTimeFmt1 = dateTimeFmt1;
        this.unixTimestamp = unixTimestamp;
        this.dateTimeIos = dateTimeIos;
        this.dateTimeAndroid = dateTimeAndroid;
    }

    public String getDateTimeFmt1() {
        return dateTimeFmt1;
    }

    public String getUnixTimestamp() {
        return unixTimestamp;
    }

    public String getDateTimeIos() {
        return dateTimeIos;
    }

    public String getDateTimeAndroid() {
        return dateTimeAndroid;
    }
}
