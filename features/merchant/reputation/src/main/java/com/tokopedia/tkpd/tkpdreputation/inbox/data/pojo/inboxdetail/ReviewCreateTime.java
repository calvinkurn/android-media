
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewCreateTime {

    @SerializedName("date_time_fmt1")
    @Expose
    private String dateTimeFmt1;
    @SerializedName("unix_timestamp")
    @Expose
    private String unixTimestamp;
    @SerializedName("date_time_ios")
    @Expose
    private String dateTimeIos;
    @SerializedName("date_time_android")
    @Expose
    private String dateTimeAndroid;

    public String getDateTimeFmt1() {
        return dateTimeFmt1;
    }

    public void setDateTimeFmt1(String dateTimeFmt1) {
        this.dateTimeFmt1 = dateTimeFmt1;
    }

    public String getUnixTimestamp() {
        return unixTimestamp;
    }

    public void setUnixTimestamp(String unixTimestamp) {
        this.unixTimestamp = unixTimestamp;
    }

    public String getDateTimeIos() {
        return dateTimeIos;
    }

    public void setDateTimeIos(String dateTimeIos) {
        this.dateTimeIos = dateTimeIos;
    }

    public String getDateTimeAndroid() {
        return dateTimeAndroid;
    }

    public void setDateTimeAndroid(String dateTimeAndroid) {
        this.dateTimeAndroid = dateTimeAndroid;
    }

}
