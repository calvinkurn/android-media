package com.tokopedia.train.common.util;

import android.support.annotation.NonNull;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.train.common.constant.TrainEventTracking;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;

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

    public void eventClickTransactionList() {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.PROMO_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_TRANSACTION_LIST,
                ""
        );
    }

    public void eventProductImpression(String scheduleId, String origin, String destination,
                                       String trainClass, String trainName) {
        Object productItem = DataLayer.mapOf(
                TrainEventTracking.EnhanceEcommerce.NAME, trainName,
                TrainEventTracking.EnhanceEcommerce.ID, scheduleId,
                TrainEventTracking.EnhanceEcommerce.CATEGORY, "train ticket",
                TrainEventTracking.EnhanceEcommerce.VARIANT, trainClass,
                TrainEventTracking.EnhanceEcommerce.LIST, "/kereta-api"
        );

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf(TrainEventTracking.EVENT, TrainEventTracking.Event.PRODUCT_VIEW,
                        TrainEventTracking.EVENT_CATEGORY, TrainEventTracking.Category.DIGITAL_TRAIN,
                        TrainEventTracking.EVENT_ACTION, TrainEventTracking.Action.PRODUCT_IMPRESSIONS,
                        TrainEventTracking.EVENT_LABEL, origin + " - " + destination + " - " + trainClass + " - " +
                                trainName,
                        TrainEventTracking.ECOMMERCE, DataLayer.mapOf(
                                "impression", DataLayer.mapOf(
                                        "products", DataLayer.listOf(productItem))
                        )
                )
        );
    }

    public void eventProductClick(String scheduleId, String origin, String destination, String trainClass,
                                  String trainName, String specialTagging) {
        Object productItem = DataLayer.mapOf(
                TrainEventTracking.EnhanceEcommerce.NAME, trainName,
                TrainEventTracking.EnhanceEcommerce.ID, scheduleId,
                TrainEventTracking.EnhanceEcommerce.CATEGORY, "train ticket",
                TrainEventTracking.EnhanceEcommerce.VARIANT, trainClass,
                TrainEventTracking.EnhanceEcommerce.LIST, "/kereta-api"
        );

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf(TrainEventTracking.EVENT, TrainEventTracking.Event.PRODUCT_CLICK,
                        TrainEventTracking.EVENT_CATEGORY, TrainEventTracking.Category.DIGITAL_TRAIN,
                        TrainEventTracking.EVENT_ACTION, TrainEventTracking.Action.PRODUCT_CLICK,
                        TrainEventTracking.EVENT_LABEL, origin + " - " + destination + " - " + trainClass + " - " +
                                trainName + " - " + specialTagging,
                        TrainEventTracking.ECOMMERCE, DataLayer.mapOf("click",
                                DataLayer.mapOf(
                                        "actionField", DataLayer.mapOf(
                                                TrainEventTracking.EnhanceEcommerce.LIST, "/kereta-api"),
                                        "products", DataLayer.listOf(productItem)
                                )
                        )
                )
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
                TrainEventTracking.EnhanceEcommerce.NAME, trainName,
                TrainEventTracking.EnhanceEcommerce.ID, scheduleId,
                TrainEventTracking.EnhanceEcommerce.CATEGORY, "train ticket",
                TrainEventTracking.EnhanceEcommerce.VARIANT, trainClass,
                TrainEventTracking.EnhanceEcommerce.PRICE, price,
                TrainEventTracking.EnhanceEcommerce.LIST, "/kereta-api"
        );

        List<Object> dataLayerList = new ArrayList<>();
        dataLayerList.add(productItem);

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf(TrainEventTracking.EVENT, TrainEventTracking.Event.VIEW_PRODUCT,
                        TrainEventTracking.EVENT_CATEGORY, TrainEventTracking.Category.DIGITAL_TRAIN,
                        TrainEventTracking.EVENT_ACTION, TrainEventTracking.Action.PRODUCT_DETAIL_IMPRESSIONS,
                        TrainEventTracking.EVENT_LABEL, origin + " - " + destination + " - " + trainClass + " - " + trainName,
                        TrainEventTracking.ECOMMERCE,
                        DataLayer.mapOf("detail",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf(TrainEventTracking.EnhanceEcommerce.LIST, "/kereta-api"),
                                        "products", DataLayer.listOf(
                                                dataLayerList.toArray(new Object[dataLayerList.size()])
                                        )
                                )
                        )
                )
        );
    }


    public void eventAddToCart(String trip, String origin, String destination, String departureScheduleId, String departureTrainClass, String departureTrainName, double departurePrice, int numOfTotalPassenger) {
        Object departureItem = DataLayer.mapOf(
                TrainEventTracking.EnhanceEcommerce.NAME, departureTrainName,
                TrainEventTracking.EnhanceEcommerce.ID, departureScheduleId,
                TrainEventTracking.EnhanceEcommerce.CATEGORY, "train ticket",
                TrainEventTracking.EnhanceEcommerce.VARIANT, departureTrainClass,
                TrainEventTracking.EnhanceEcommerce.PRICE, departurePrice,
                TrainEventTracking.EnhanceEcommerce.LIST, "/kereta-api",
                TrainEventTracking.EnhanceEcommerce.QUANTITY, numOfTotalPassenger
        );

        List<Object> dataLayerList = new ArrayList<>();
        dataLayerList.add(departureItem);

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf(TrainEventTracking.EVENT, TrainEventTracking.Event.ADD_TO_CART,
                        TrainEventTracking.EVENT_CATEGORY, TrainEventTracking.Category.DIGITAL_TRAIN,
                        TrainEventTracking.EVENT_ACTION, TrainEventTracking.Action.ADD_TO_CART,
                        TrainEventTracking.EVENT_LABEL, String.format("%s - %s - %s - %s - %s",
                                origin, destination, departureTrainClass, departureTrainName, trip),
                        TrainEventTracking.ECOMMERCE, DataLayer.mapOf(
                                "add", DataLayer.mapOf(
                                        "products", DataLayer.listOf(
                                                dataLayerList.toArray(new Object[dataLayerList.size()])
                                        ))
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
                TrainEventTracking.EnhanceEcommerce.NAME, departureTrainName,
                TrainEventTracking.EnhanceEcommerce.ID, departureScheduleId,
                TrainEventTracking.EnhanceEcommerce.CATEGORY, "train ticket",
                TrainEventTracking.EnhanceEcommerce.VARIANT, departureTrainClass,
                TrainEventTracking.EnhanceEcommerce.PRICE, departurePrice,
                TrainEventTracking.EnhanceEcommerce.LIST, "/kereta-api",
                TrainEventTracking.EnhanceEcommerce.QUANTITY, numOfTotalPassenger
        );

        Object returnItem = DataLayer.mapOf(
                TrainEventTracking.EnhanceEcommerce.NAME, returnTrainName,
                TrainEventTracking.EnhanceEcommerce.ID, returnScheduleId,
                TrainEventTracking.EnhanceEcommerce.CATEGORY, "train ticket",
                TrainEventTracking.EnhanceEcommerce.VARIANT, returnTrainClass,
                TrainEventTracking.EnhanceEcommerce.PRICE, returnPrice,
                TrainEventTracking.EnhanceEcommerce.LIST, "/kereta-api",
                TrainEventTracking.EnhanceEcommerce.QUANTITY, numOfTotalPassenger
        );

        List<Object> dataLayerList = new ArrayList<>();
        dataLayerList.add(departureItem);
        dataLayerList.add(returnItem);

        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf(TrainEventTracking.EVENT, TrainEventTracking.Event.ADD_TO_CART,
                        TrainEventTracking.EVENT_CATEGORY, TrainEventTracking.Category.DIGITAL_TRAIN,
                        TrainEventTracking.EVENT_ACTION, TrainEventTracking.Action.ADD_TO_CART,
                        TrainEventTracking.EVENT_LABEL, String.format("%s - %s - %s - %s - %s",
                                origin,
                                destination,
                                (departureTrainClass + ", " + returnTrainClass),
                                (departureTrainName + ", " + returnTrainName),
                                trip),
                        TrainEventTracking.ECOMMERCE, DataLayer.mapOf(
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
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_DETAIL,
                origin + " - " + destination + " - " + trainClass + " - " + trainName
        );
    }

    public void eventClickNextOnCustomersPage() {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_NEXT_ON_CUSTOMERS_PAGE,
                ""
        );
    }

    public void eventClickUseVoucherCode(String voucherCode) {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_USE_VOUCHER_CODE,
                voucherCode
        );
    }

    public void eventVoucherSuccess(String successMessage) {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.VOUCHER_SUCCESS,
                successMessage
        );
    }

    public void eventVoucherError(String errorMessage) {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.VOUCHER_ERROR,
                errorMessage
        );
    }

    public void eventProceedToPayment(String origin, String destination, String trainClass,
                                      String trainName) {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.PROCEED_TO_PAYMENT,
                origin + " - " + destination + " - " + trainClass + " - " + trainName
        );
    }

    public void eventProceedToPayment(TrainScheduleDetailViewModel departureTripViewModel, TrainScheduleDetailViewModel returnTripViewModel) {
        analyticTracker.sendEventTracking(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.PROCEED_TO_PAYMENT,
                buildRoundTripLabel(departureTripViewModel, returnTripViewModel)
        );
    }

    @NonNull
    private String buildRoundTripLabel(TrainScheduleDetailViewModel departureTripViewModel, TrainScheduleDetailViewModel returnTripViewModel) {
        return String.format("%s, %s - %s, %s - %s, %s - %s, %s",
                departureTripViewModel.getOriginStationCode(), returnTripViewModel.getOriginStationCode(),
                departureTripViewModel.getDestinationStationCode(), returnTripViewModel.getDestinationStationCode(),
                departureTripViewModel.getTrainClass(), returnTripViewModel.getTrainClass(),
                departureTripViewModel.getTrainName(), returnTripViewModel.getTrainName());
    }
}