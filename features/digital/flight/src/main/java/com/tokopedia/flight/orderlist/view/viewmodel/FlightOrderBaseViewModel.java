package com.tokopedia.flight.orderlist.view.viewmodel;

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

}
