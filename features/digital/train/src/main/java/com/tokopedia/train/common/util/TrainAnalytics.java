package com.tokopedia.train.common.util;

import android.support.annotation.NonNull;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.train.common.constant.TrainEventTracking;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

import javax.inject.Inject;

/**
 * Created by Rizky on 30/07/18.
 */
public class TrainAnalytics {

    private TrainDateUtil trainDateUtil;

    @Inject
    public TrainAnalytics(TrainDateUtil trainDateUtil) {
        this.trainDateUtil = trainDateUtil;
    }

    public void eventChooseSingleTrip() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CHOOSE_SINGLE_TRIP,
                ""
        ));
    }

    public void eventChooseRoundTrip() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CHOOSE_ROUND_TRIP,
                ""
        ));
    }

    public void eventClickFindTicket(String trip, String origin, String destination, int numOfPassenger) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_FIND_TICKET,
                trip + " - " + origin + " - " + destination + " - " + numOfPassenger
        ));
    }

    public void eventClickPromoList() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.PROMO_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_PROMO_LIST,
                ""
        ));
    }

    public void eventClickHelp() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_HELP,
                ""
        ));
    }

    public void eventClickTransactionList() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.PROMO_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_TRANSACTION_LIST,
                ""
        ));
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

        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
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

        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf(TrainEventTracking.EVENT, TrainEventTracking.Event.PRODUCT_CLICK,
                        TrainEventTracking.EVENT_CATEGORY, TrainEventTracking.Category.DIGITAL_TRAIN,
                        TrainEventTracking.EVENT_ACTION, TrainEventTracking.Action.PRODUCT_CLICK,
                        TrainEventTracking.EVENT_LABEL, origin.toLowerCase() + " - " + destination.toLowerCase() + " - " + trainClass.toLowerCase() + " - " +
                                trainName.toLowerCase() + " - " + specialTagging.toLowerCase(),
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
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_SORT_ON_BOTTOM_BAR,
                ""
        ));
    }

    public void eventClickFilterOnBottomBar() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.GENERIC_TRAIN_EVENT,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_FILTER_ON_BOTTOM_BAR,
                ""
        ));
    }

    public void eventViewRouteNotAvailablePage(String origin, String destination, String date) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                "",
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.VIEW_ROUTE_NOT_AVAILABLE_PAGE,
                origin + " - " + destination + " - " + date
        ));
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

        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
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


    public void eventAddToCart(TrainScheduleViewModel scheduleViewModel, int numOfTotalPassenger) {
        Object departureItem = buildProductEnhanceEcommerce(scheduleViewModel, numOfTotalPassenger);

        List<Object> dataLayerList = new ArrayList<>();
        dataLayerList.add(departureItem);

        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf(TrainEventTracking.EVENT, TrainEventTracking.Event.ADD_TO_CART,
                        TrainEventTracking.EVENT_CATEGORY, TrainEventTracking.Category.DIGITAL_TRAIN,
                        TrainEventTracking.EVENT_ACTION, TrainEventTracking.Action.ADD_TO_CART,
                        TrainEventTracking.EVENT_LABEL, String.format("%s - %s - %s - %s",
                                scheduleViewModel.getTrainName().toLowerCase(),
                                scheduleViewModel.getOrigin().toLowerCase(),
                                String.valueOf(trainDateUtil.getDayDiff(scheduleViewModel.getDepartureTimestamp())),
                                String.valueOf(scheduleViewModel.getAdultFare())),
                        TrainEventTracking.ECOMMERCE, DataLayer.mapOf(
                                "add", DataLayer.mapOf(
                                        "products", DataLayer.listOf(
                                                dataLayerList.toArray(new Object[dataLayerList.size()])
                                        ))
                        )
                )
        );
    }

    private Map<String, Object> buildProductEnhanceEcommerce(TrainScheduleViewModel scheduleViewModel, int numOfTotalPassenger) {
        return DataLayer.mapOf(
                TrainEventTracking.EnhanceEcommerce.NAME, scheduleViewModel.getOrigin() + " - " + scheduleViewModel.getDestination(),
                TrainEventTracking.EnhanceEcommerce.ID, scheduleViewModel.getIdSchedule(),
                TrainEventTracking.EnhanceEcommerce.BRAND, scheduleViewModel.getTrainName(),
                TrainEventTracking.EnhanceEcommerce.CATEGORY, "train ticket",
                TrainEventTracking.EnhanceEcommerce.VARIANT, scheduleViewModel.getDisplayClass(),
                TrainEventTracking.EnhanceEcommerce.PRICE, String.valueOf(scheduleViewModel.getAdultFare()),
                TrainEventTracking.EnhanceEcommerce.LIST, "/kereta-api",
                TrainEventTracking.EnhanceEcommerce.QUANTITY, numOfTotalPassenger
        );
    }

    public void eventAddToCart(TrainScheduleViewModel scheduleViewModel, TrainScheduleViewModel returnScheduleViewModel, int numOfTotalPassenger) {
        Object departureItem = buildProductEnhanceEcommerce(scheduleViewModel, numOfTotalPassenger);

        Object returnItem = buildProductEnhanceEcommerce(returnScheduleViewModel, numOfTotalPassenger);

        List<Object> dataLayerList = new ArrayList<>();
        dataLayerList.add(departureItem);
        dataLayerList.add(returnItem);

        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf(TrainEventTracking.EVENT, TrainEventTracking.Event.ADD_TO_CART,
                        TrainEventTracking.EVENT_CATEGORY, TrainEventTracking.Category.DIGITAL_TRAIN,
                        TrainEventTracking.EVENT_ACTION, TrainEventTracking.Action.ADD_TO_CART,
                        TrainEventTracking.EVENT_LABEL, String.format("%s - %s - %s - %s",
                                (scheduleViewModel.getTrainName().toLowerCase() + ", " + returnScheduleViewModel.getTrainName().toLowerCase()),
                                (scheduleViewModel.getOrigin().toLowerCase() + ", " + returnScheduleViewModel.getOrigin().toLowerCase()),
                                (trainDateUtil.getDayDiff(scheduleViewModel.getDepartureTimestamp()) + ", " + trainDateUtil.getDayDiff(returnScheduleViewModel.getDepartureTimestamp())),
                                (String.valueOf(scheduleViewModel.getAdultFare()) + ", " + String.valueOf(returnScheduleViewModel.getAdultFare()))),
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
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_DETAIL,
                origin.toLowerCase() + " - " + destination.toLowerCase() + " - " + trainClass.toLowerCase() + " - " + trainName.toLowerCase()
        ));
    }

    public void eventClickNextOnCustomersPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_NEXT_ON_CUSTOMERS_PAGE,
                ""
        ));
    }

    public void eventClickUseVoucherCode(String voucherCode) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.CLICK_USE_VOUCHER_CODE,
                voucherCode
        ));
    }

    public void eventVoucherSuccess(String successMessage) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.VOUCHER_SUCCESS,
                successMessage.toLowerCase()
        ));
    }

    public void eventVoucherError(String errorMessage) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.VOUCHER_ERROR,
                errorMessage.toLowerCase()
        ));
    }

    public void eventProceedToPayment(String origin, String destination, String trainClass,
                                      String trainName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.PROCEED_TO_PAYMENT,
                origin.toLowerCase() + " - " + destination.toLowerCase() + " - " + trainClass.toLowerCase() + " - " + trainName.toLowerCase()
        ));
    }

    public void eventProceedToPayment(TrainScheduleDetailViewModel departureTripViewModel, TrainScheduleDetailViewModel returnTripViewModel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                TrainEventTracking.Event.KAI_CLICK,
                TrainEventTracking.Category.DIGITAL_TRAIN,
                TrainEventTracking.Action.PROCEED_TO_PAYMENT,
                buildRoundTripLabel(departureTripViewModel, returnTripViewModel)
        ));
    }

    @NonNull
    private String buildRoundTripLabel(TrainScheduleDetailViewModel departureTripViewModel, TrainScheduleDetailViewModel returnTripViewModel) {
        return String.format("%s, %s - %s, %s - %s, %s - %s, %s",
                departureTripViewModel.getOriginStationCode().toLowerCase(), returnTripViewModel.getOriginStationCode().toLowerCase(),
                departureTripViewModel.getDestinationStationCode().toLowerCase(), returnTripViewModel.getDestinationStationCode().toLowerCase(),
                departureTripViewModel.getTrainClass().toLowerCase(), returnTripViewModel.getTrainClass().toLowerCase(),
                departureTripViewModel.getTrainName().toLowerCase(), returnTripViewModel.getTrainName().toLowerCase());
    }
}