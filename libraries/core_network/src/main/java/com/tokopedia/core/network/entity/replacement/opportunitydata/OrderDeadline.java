
package com.tokopedia.core.network.entity.replacement.opportunitydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class OrderDeadline {

    @SerializedName("deadline_process_day_left")
    @Expose
    private int deadlineProcessDayLeft;
    @SerializedName("deadline_process_hour_left")
    @Expose
    private int deadlineProcessHourLeft;
    @SerializedName("deadline_process")
    @Expose
    private String deadlineProcess;
    @SerializedName("deadline_po_process_day_left")
    @Expose
    private int deadlinePoProcessDayLeft;
    @SerializedName("deadline_shipping_day_left")
    @Expose
    private int deadlineShippingDayLeft;
    @SerializedName("deadline_shipping_hour_left")
    @Expose
    private int deadlineShippingHourLeft;
    @SerializedName("deadline_shipping")
    @Expose
    private String deadlineShipping;
    @SerializedName("deadline_finish_day_left")
    @Expose
    private int deadlineFinishDayLeft;
    @SerializedName("deadline_finish_hour_left")
    @Expose
    private int deadlineFinishHourLeft;
    @SerializedName("deadline_finish_date")
    @Expose
    private Object deadlineFinishDate;
    @SerializedName("deadline_color")
    @Expose
    private String deadlineColor;

    public int getDeadlineProcessDayLeft() {
        return deadlineProcessDayLeft;
    }

    public void setDeadlineProcessDayLeft(int deadlineProcessDayLeft) {
        this.deadlineProcessDayLeft = deadlineProcessDayLeft;
    }

    public int getDeadlineProcessHourLeft() {
        return deadlineProcessHourLeft;
    }

    public void setDeadlineProcessHourLeft(int deadlineProcessHourLeft) {
        this.deadlineProcessHourLeft = deadlineProcessHourLeft;
    }

    public String getDeadlineProcess() {
        return deadlineProcess;
    }

    public void setDeadlineProcess(String deadlineProcess) {
        this.deadlineProcess = deadlineProcess;
    }

    public int getDeadlinePoProcessDayLeft() {
        return deadlinePoProcessDayLeft;
    }

    public void setDeadlinePoProcessDayLeft(int deadlinePoProcessDayLeft) {
        this.deadlinePoProcessDayLeft = deadlinePoProcessDayLeft;
    }

    public int getDeadlineShippingDayLeft() {
        return deadlineShippingDayLeft;
    }

    public void setDeadlineShippingDayLeft(int deadlineShippingDayLeft) {
        this.deadlineShippingDayLeft = deadlineShippingDayLeft;
    }

    public int getDeadlineShippingHourLeft() {
        return deadlineShippingHourLeft;
    }

    public void setDeadlineShippingHourLeft(int deadlineShippingHourLeft) {
        this.deadlineShippingHourLeft = deadlineShippingHourLeft;
    }

    public String getDeadlineShipping() {
        return deadlineShipping;
    }

    public void setDeadlineShipping(String deadlineShipping) {
        this.deadlineShipping = deadlineShipping;
    }

    public int getDeadlineFinishDayLeft() {
        return deadlineFinishDayLeft;
    }

    public void setDeadlineFinishDayLeft(int deadlineFinishDayLeft) {
        this.deadlineFinishDayLeft = deadlineFinishDayLeft;
    }

    public int getDeadlineFinishHourLeft() {
        return deadlineFinishHourLeft;
    }

    public void setDeadlineFinishHourLeft(int deadlineFinishHourLeft) {
        this.deadlineFinishHourLeft = deadlineFinishHourLeft;
    }

    public Object getDeadlineFinishDate() {
        return deadlineFinishDate;
    }

    public void setDeadlineFinishDate(Object deadlineFinishDate) {
        this.deadlineFinishDate = deadlineFinishDate;
    }

    public String getDeadlineColor() {
        return deadlineColor;
    }

    public void setDeadlineColor(String deadlineColor) {
        this.deadlineColor = deadlineColor;
    }

}
