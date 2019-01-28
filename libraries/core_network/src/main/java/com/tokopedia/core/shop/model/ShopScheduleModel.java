package com.tokopedia.core.shop.model;

import org.parceler.Parcel;

/**
 * Created by Toped10 on 6/1/2016.
 */

@Deprecated
@Parcel
public class ShopScheduleModel {
    String close_note;
    String close_start;
    String close_end;
    Integer close_action;
    int status;

    public ShopScheduleModel() {
    }

    public ShopScheduleModel(String close_note, String close_start, String close_end, Integer close_action) {
        this.close_note = close_note;
        this.close_start = close_start;
        this.close_end = close_end;
        this.close_action = close_action;
    }

    public String getClose_note() {
        return close_note;
    }

    public void setClose_note(String close_note) {
        this.close_note = close_note;
    }

    public String getClose_start() {
        return close_start;
    }

    public void setClose_start(String close_start) {
        this.close_start = close_start;
    }

    public String getClose_end() {
        return close_end;
    }

    public void setClose_end(String close_end) {
        this.close_end = close_end;
    }

    public Integer getClose_action() {
        return close_action;
    }

    public void setClose_action(Integer close_action) {
        this.close_action = close_action;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
