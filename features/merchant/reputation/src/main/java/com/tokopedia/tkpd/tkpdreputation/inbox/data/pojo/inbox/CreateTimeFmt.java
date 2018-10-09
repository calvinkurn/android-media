
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateTimeFmt {

    @SerializedName("date_time_fmt1")
    @Expose
    private String dateTimeFmt1;
    @SerializedName("date_time_fmt1x")
    @Expose
    private String dateTimeFmt1x;
    @SerializedName("date_time_fmt2")
    @Expose
    private String dateTimeFmt2;
    @SerializedName("date_time_fmt3")
    @Expose
    private String dateTimeFmt3;
    @SerializedName("date_time_fmt3x")
    @Expose
    private String dateTimeFmt3x;
    @SerializedName("date_fmt1")
    @Expose
    private String dateFmt1;

    public String getDateTimeFmt1() {
        return dateTimeFmt1;
    }

    public void setDateTimeFmt1(String dateTimeFmt1) {
        this.dateTimeFmt1 = dateTimeFmt1;
    }

    public String getDateTimeFmt1x() {
        return dateTimeFmt1x;
    }

    public void setDateTimeFmt1x(String dateTimeFmt1x) {
        this.dateTimeFmt1x = dateTimeFmt1x;
    }

    public String getDateTimeFmt2() {
        return dateTimeFmt2;
    }

    public void setDateTimeFmt2(String dateTimeFmt2) {
        this.dateTimeFmt2 = dateTimeFmt2;
    }

    public String getDateTimeFmt3() {
        return dateTimeFmt3;
    }

    public void setDateTimeFmt3(String dateTimeFmt3) {
        this.dateTimeFmt3 = dateTimeFmt3;
    }

    public String getDateTimeFmt3x() {
        return dateTimeFmt3x;
    }

    public void setDateTimeFmt3x(String dateTimeFmt3x) {
        this.dateTimeFmt3x = dateTimeFmt3x;
    }

    public String getDateFmt1() {
        return dateFmt1;
    }

    public void setDateFmt1(String dateFmt1) {
        this.dateFmt1 = dateFmt1;
    }

}
