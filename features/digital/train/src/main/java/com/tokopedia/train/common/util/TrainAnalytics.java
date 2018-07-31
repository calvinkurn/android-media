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

    public void eventProductDetailImpressions(String scheduleId, String origin, String destination,
                                              String trainClass, String trainName, double price) {
        Object productItem = DataLayer.mapOf(
                "name", trainName,
                "id", scheduleId,
                "category", "train ticket",
                "variant", trainClass,
                "price", price,
                "list", "/kereta-api"
        );

        List<Object> dataLayerList = new ArrayList<>();
        dataLayerList.add(productItem);

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", TrainEventTracking.Event.VIEW_PRODUCT,
                        "eventCategory", TrainEventTracking.Category.DIGITAL_TRAIN,
                        "eventAction", TrainEventTracking.Action.PRODUCT_DETAIL_IMPRESSIONS,
                        "eventLabel", origin + " - " + destination + " - " + trainClass + " - " + trainName,
                        "ecommerce",
                        DataLayer.mapOf("detail",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf("list", "/kereta-api"),
                                        "products", DataLayer.listOf(
                                                dataLayerList.toArray(new Object[dataLayerList.size()])
                                        )
                                )
                        )
                )
        );
    }

    public void eventAddToCart(String trip,
                               String origin, String destination,
                               String departureScheduleId,
                               String departureTrainClass, String departureTrainName,
                               double departurePrice,
                               String returnScheduleId,
                               String returnTrainClass, String returnTrainName,
                               double returnPrice,
                               int numOfTotalPassenger) {
        Object departureItem = DataLayer.mapOf(
                "name", departureTrainName,
                "id", departureScheduleId,
                "category", "train ticket",
                "variant", departureTrainClass,
                "price", departurePrice,
                "list", "/kereta-api",
                "quantity", numOfTotalPassenger
        );

        Object returnItem = DataLayer.mapOf(
                "name", returnTrainName,
                "id", returnScheduleId,
                "category", "train ticket",
                "variant", returnTrainClass,
                "price", returnPrice,
                "list", "/kereta-api",
                "quantity", numOfTotalPassenger
        );

        List<Object> dataLayerList = new ArrayList<>();
        dataLayerList.add(departureItem);
        dataLayerList.add(returnItem);

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", TrainEventTracking.Event.ADD_TO_CART,
                        "eventCategory", TrainEventTracking.Category.DIGITAL_TRAIN,
                        "eventAction", TrainEventTracking.Action.ADD_TO_CART,
                        "eventLabel", origin + " - " + destination + " - " +
                                departureTrainClass + " - " + departureTrainName + " - " + trip,
                        "ecommerce", DataLayer.mapOf(
                                "add", DataLayer.mapOf(
                                        "products", DataLayer.listOf(
                                                dataLayerList.toArray(new Object[dataLayerList.size()])
                                        ))
                        )
                )
        );
    }

    public void eventClickDetail(String origin, String destination, String trainClass, String trainName) {
        analyticTracker.sendEventTracking(
                "",
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.VIEW_ROUTE_NOT_AVAILABLE_PAGE,
                origin + " - " + destination + " - " + trainClass + " - " + trainName
        );
    }

    public void eventProductClick(String scheduleId, String origin, String destination, String trainClass,
                                  String trainName, String specialTagging) {
        Object productItem = DataLayer.mapOf(
                "name", trainName,
                "id", scheduleId,
                "category", "train ticket",
                "variant", trainClass,
                "list", "/kereta-api"
        );

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", TrainEventTracking.Event.PRODUCT_CLICK,
                        "eventCategory", TrainEventTracking.Category.DIGITAL_TRAIN,
                        "eventAction", TrainEventTracking.Action.PRODUCT_CLICK,
                        "eventLabel", origin + " - " + destination + " - " + trainClass + " - " +
                                trainName + " - " + specialTagging,
                        "ecommerce", DataLayer.mapOf("click",
                                DataLayer.mapOf(
                                        "actionField", DataLayer.mapOf(
                                                "list", "/kereta-api"),
                                        "products", DataLayer.listOf(productItem)
                                )
                        )
                )
        );
    }

    public void eventProductImpression(String scheduleId, String origin, String destination, String trainClass, String trainName) {
        Object productItem = DataLayer.mapOf(
                "name", trainName,
                "id", scheduleId,
                "category", "train ticket",
                "variant", trainClass,
                "list", "/kereta-api"
        );

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", TrainEventTracking.Event.PRODUCT_VIEW,
                        "eventCategory", TrainEventTracking.Category.DIGITAL_TRAIN,
                        "eventAction", TrainEventTracking.Action.PRODUCT_IMPRESSIONS,
                        "eventLabel", origin + " - " + destination + " - " + trainClass + " - " +
                                trainName,
                        "ecommerce", DataLayer.mapOf(
                                "impression", DataLayer.mapOf(
                                        "products", DataLayer.listOf(productItem))
                        )
                )
        );
    }

    public void eventClickUseVoucherCode(String voucherCode) {
        analyticTracker.sendEventTracking(
                "",
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_USE_VOUCHER_CODE,
                voucherCode
        );
    }

}