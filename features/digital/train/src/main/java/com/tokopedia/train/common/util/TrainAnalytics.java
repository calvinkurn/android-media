package com.tokopedia.train.common.util;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.train.common.constant.TrainEventTracking;

import java.util.ArrayList;
import java.util.List;

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

    public void eventClickTransactionList() {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.PROMO_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_TRANSACTION_LIST,
                ""
        );
    }

    public void eventCheckOrder() {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CHECK_ORDER,
                ""
        );
    }

    public void eventClickPromoList() {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.PROMO_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_PROMO_LIST,
                ""
        );
    }

    public void eventClickHelp() {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_HELP,
                ""
        );
    }

    public void eventClickSortOnBottomBar() {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_SORT_ON_BOTTOM_BAR,
                ""
        );
    }

    public void eventClickFilterOnBottomBar() {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_FILTER_ON_BOTTOM_BAR,
                ""
        );
    }

    public void eventViewRouteNotAvailablePage(String origin, String destination, String date) {
        analyticTracker.sendEventTracking(
                "",
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.VIEW_ROUTE_NOT_AVAILABLE_PAGE,
                origin + " - " + destination + " - " + date
        );
    }

    public void eventProductDetailImpressions(String origin, String destination,
                                              String trainClass, String trainName, String price,
                                              int numOfTotalPassenger) {
        Object productItem = DataLayer.mapOf(
                "name", trainName,
                "variant", trainClass,
                "price", price,
                "quantity", numOfTotalPassenger
        );

        List<Object> dataLayerList = new ArrayList<>();
        dataLayerList.add(productItem);

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "",
                        "eventCategory", TrainEventTracking.Category.DIGITAL_TRAIN,
                        "eventAction", TrainEventTracking.Action.PRODUCT_DETAIL_IMPRESSIONS,
                        "eventLabel", origin + " - " + destination + " - " + trainClass + " - " + trainName + " - " + price,
                        "ecommerce", DataLayer.mapOf(
                                "detail", DataLayer.mapOf(
                                        "products", DataLayer.listOf(
                                                dataLayerList.toArray(new Object[dataLayerList.size()])
                                        ))
                        )
                )
        );
    }

    public void eventAddToCart(String origin, String destination,
                                              String trainClass, String trainName, String price,
                                              int numOfTotalPassenger) {
        Object productItem = DataLayer.mapOf(
                "name", trainName,
                "variant", trainClass,
                "price", price,
                "quantity", numOfTotalPassenger
        );

        List<Object> dataLayerList = new ArrayList<>();
        dataLayerList.add(productItem);

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "",
                        "eventCategory", TrainEventTracking.Category.DIGITAL_TRAIN,
                        "eventAction", TrainEventTracking.Action.ADD_TO_CART,
                        "eventLabel", origin + " - " + destination + " - " + trainClass + " - " + trainName + " - " + price,
                        "ecommerce", DataLayer.mapOf(
                                "add", DataLayer.mapOf(
                                        "products", DataLayer.listOf(
                                                dataLayerList.toArray(new Object[dataLayerList.size()])
                                        ))
                        )
                )
        );
    }

}