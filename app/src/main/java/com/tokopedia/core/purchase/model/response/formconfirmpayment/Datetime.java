package com.tokopedia.core.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 20/06/2016.
 */
public class Datetime {
    private static final String TAG = Datetime.class.getSimpleName();

    @SerializedName("date_day")
    @Expose
    private String dateDay;
    @SerializedName("date_year_min")
    @Expose
    private String dateYearMin;
    @SerializedName("date_month")
    @Expose
    private String dateMonth;
    @SerializedName("date_year_max")
    @Expose
    private String dateYearMax;

    public String getDateDay() {
        return dateDay;
    }

    public void setDateDay(String dateDay) {
        this.dateDay = dateDay;
    }

    public String getDateYearMin() {
        return dateYearMin;
    }

    public void setDateYearMin(String dateYearMin) {
        this.dateYearMin = dateYearMin;
    }

    public String getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(String dateMonth) {
        this.dateMonth = dateMonth;
    }

    public String getDateYearMax() {
        return dateYearMax;
    }

    public void setDateYearMax(String dateYearMax) {
        this.dateYearMax = dateYearMax;
    }

    public int getDayInt() {
        return Integer.parseInt(dateDay);
    }

    public int getMonthInt() {
        return Integer.parseInt(dateMonth);
    }

    public int getYearMaxInt() {
        return Integer.parseInt(dateYearMax);
    }

    public int getYearMinInt() {
        return Integer.parseInt(dateYearMin);
    }

    public String getDateFormatted() {
        return dateDay + "/" + dateMonth + "/" + dateYearMax;
    }

    public String formatDate(int year, int monthOfYear, int dayOfMonth) {
        return dayOfMonth + "/" + monthOfYear + 1 + "/" + year;
    }
}
