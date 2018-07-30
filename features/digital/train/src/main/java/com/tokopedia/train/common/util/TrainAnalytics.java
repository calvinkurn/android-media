package com.tokopedia.train.common.util;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.train.common.constant.TrainEventTracking;

import javax.inject.Inject;

/**
 * Created by Rizky on 30/07/18.
 */
public class TrainAnalytics {

    private AnalyticTracker analyticTracker;

    @Inject
    public TrainAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void eventChooseSingleTrip() {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CHOOSE_SINGLE_TRIP,
                ""
        );
    }

    public void eventChooseRoundTrip() {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CHOOSE_ROUND_TRIP,
                ""
        );
    }

    public void eventClickFindTicket(String trip, String origin, String destination, int numOfPassenger) {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_FIND_TICKET,
                trip + " - " + origin + " - " + destination + " - " + numOfPassenger
        );
    }
}