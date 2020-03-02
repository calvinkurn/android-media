package com.tokopedia.flight.orderlist.view.viewmodel;

import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

import java.util.List;

/**
 * Created by alvarisi on 12/7/17.
 */

public class FlightOrderBaseViewModel {
    protected String title;
    protected String id;
    protected String createTime;
    protected int status;
    protected List<FlightOrderJourney> orderJourney;
    protected String contactUsUrl;
    private List<CancellationEntity> cancellations;
    private String cancellationInfo;

    protected String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected String getCreateTime() {
        return createTime;
    }

    protected void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    protected int getStatus() {
        return status;
    }

    protected void setStatus(int status) {
        this.status = status;
    }

    protected List<FlightOrderJourney> getOrderJourney() {
        return orderJourney;
    }

    protected void setOrderJourney(List<FlightOrderJourney> orderJourney) {
        this.orderJourney = orderJourney;
    }

    protected String getTitle() {
        return title;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    protected String getContactUsUrl() { return contactUsUrl; }

    protected void setContactUsUrl(String contactUsUrl) { this.contactUsUrl = contactUsUrl; }

    public List<CancellationEntity> getCancellations() {
        return cancellations;
    }

    public void setCancellations(List<CancellationEntity> cancellations) {
        this.cancellations = cancellations;
    }

    public String getCancellationInfo() {
        return cancellationInfo;
    }

    public void setCancellationInfo(String cancellationInfo) {
        this.cancellationInfo = cancellationInfo;
    }
}
