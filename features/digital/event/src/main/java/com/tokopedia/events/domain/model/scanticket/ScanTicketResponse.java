package com.tokopedia.events.domain.model.scanticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScanTicketResponse {

    @SerializedName("product")
    @Expose
    private ScanProductDetail product;
    @SerializedName("schedule")
    @Expose
    private Schedule schedule;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("action")
    @Expose
    private List<Action> action = null;

    public ScanProductDetail getProduct() {
        return product;
    }

    public void setProduct(ScanProductDetail product) {
        this.product = product;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Action> getAction() {
        return action;
    }

    public void setAction(List<Action> action) {
        this.action = action;
    }


}

