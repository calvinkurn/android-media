package com.tokopedia.flight.common.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel;
import com.tokopedia.common.travel.presentation.model.TravelVideoBannerModel;
import com.tokopedia.flight.detail.view.model.FlightDetailModel;
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel;
import com.tokopedia.flight.homepage.presentation.model.FlightHomepageModel;
import com.tokopedia.flight.promo_chips.data.model.AirlinePrice;
import com.tokopedia.flight.search.data.cloud.single.Route;
import com.tokopedia.flight.search.presentation.model.FlightAirlineModel;
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel;
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum;
import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author by alvarisi on 1/19/18.
 */

public class FlightAnalytics {
    private FlightDateUtil flightDateUtil;
    private String GENERIC_EVENT = "genericFlightEvent";
    private String ATC_EVENT = "addToCart";
    private String CHECKOUT_EVENT = "checkout";
    private String PROMO_VIEW_EVENT = "promoView";
    private String PROMO_CLICK_EVENT = "promoClick";
    private String FLIGHT_CLICK_EVENT = "clickFlight";
    private String PRODUCT_CLICK_EVENT = "productClick";
    private String PRODUCT_VIEW_EVENT = "productView";
    private String SEARCH_RESULT_EVENT = "viewFlightIris";
    private String VIEW_SEARCH_EVENT = "viewSearchPage";
    private String CLICK_SEARCH_EVENT = "clickSearch";
    private String GENERIC_CATEGORY = "digital - flight";
    private String EVENT_CATEGORY = "eventCategory";
    private String EVENT_ACTION = "eventAction";
    private String EVENT_LABEL = "eventLabel";
    private String EVENT = "event";
    private String ECOMMERCE = "ecommerce";
    private String IMPRESSIONS = "impressions";
    private String CURRENCY_CODE = "currencyCode";
    private String DEFAULT_CURRENCY_CODE = "IDR";
    private String OPEN_SCREEN_EVENT = "openScreen";
    private String EVENT_NAME = "eventName";
    private String SCREEN_NAME = "screenName";
    private String CLIENT_ID = "clientId";
    private String CURRENT_SITE = "currentSite";
    private String BUSSINESS_UNIT = "businessUnit";
    private String CATEGORY = "category";
    private String  USER_ID = "userId";

    private String FLIGHT_CURRENT_SITE = "tokopediadigitalflight";
    private String FLIGHT_BU = "travel & entertainment";

    @Inject
    public FlightAnalytics(FlightDateUtil flightDateUtil) {
        this.flightDateUtil = flightDateUtil;
    }

    public void eventOpenScreen(String screenName) {
        Map<String, String> mapOpenScreen = new HashMap<>();
        mapOpenScreen.put(EVENT_NAME, OPEN_SCREEN_EVENT);
        mapOpenScreen.put(CURRENT_SITE, FLIGHT_CURRENT_SITE);
        mapOpenScreen.put(BUSSINESS_UNIT, FLIGHT_BU);
        mapOpenScreen.put(CATEGORY, Label.FLIGHT_SMALL);
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(
                screenName, mapOpenScreen);
    }

    public void eventClickTransactions(String screenName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_TRANSACTIONS,
                screenName
        ));
    }

    public void eventPromotionClick(int position,
                                    TravelCollectiveBannerModel.Banner banner,
                                    String screenName,
                                    String userId) {
        List<Object> promos = new ArrayList<>();
        promos.add(DataLayer.mapOf(
                EnhanceEccomerce.ID, banner.getId(),
                EnhanceEccomerce.NAME, String.format(getDefaultLocale(), "%s - %s",
                        banner.getAttribute().getPromoCode(), "slider banner"),
                "position", String.valueOf(position),
                "creative", banner.getAttribute().getDescription().toLowerCase(),
                "creative_url", banner.getAttribute().getAppUrl()
        ));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PROMO_CLICK_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Action.PROMOTION_CLICK,
                        SCREEN_NAME, screenName,
                        CURRENT_SITE, FLIGHT_CURRENT_SITE,
                        CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString(),
                        BUSSINESS_UNIT, FLIGHT_BU,
                        CATEGORY, Label.FLIGHT_SMALL,
                        USER_ID, userId,
                        EVENT_LABEL, String.format(getDefaultLocale(), "%s - %d - %s",
                                Label.FLIGHT_SMALL,
                                position,
                                banner.getAttribute().getPromoCode()
                        ),
                        ECOMMERCE, DataLayer.mapOf(
                                "promoClick",
                                DataLayer.mapOf(
                                        "promotions",
                                        DataLayer.listOf(
                                                promos.toArray(new Object[promos.size()])
                                        )
                                )
                        )
                )
        );
    }

    private Locale getDefaultLocale() {
        return Locale.getDefault();
    }

    public void eventTripTypeClick(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_TRIP_TYPE,
                label
        ));
    }

    public void eventOriginClick(String cityName, String airportId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.SELECT_ORIGIN,
                cityName + "|" + airportId
        ));
    }

    public void eventDestinationClick(String cityName, String airportId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.SELECT_DESTINATION,
                cityName + "|" + airportId
        ));
    }

    public void eventClassClick(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.SELECT_CLASS,
                label
        ));
    }

    public void eventSearchClick(FlightHomepageModel dashboardViewModel,
                                 String screenName,
                                 String userId) {
        Map<String, Object> params = TrackAppUtils.gtmData(CLICK_SEARCH_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH,
                String.format("%s - %s-%s - %s - %s-%s-%s - %s - %s%s",
                        Label.FLIGHT_SMALL,
                        (dashboardViewModel.getDepartureAirport().getAirportCode() == null || dashboardViewModel.getDepartureAirport().getAirportCode().isEmpty()) ?
                                dashboardViewModel.getDepartureAirport().getCityCode() : dashboardViewModel.getDepartureAirport().getAirportCode(),
                        (dashboardViewModel.getArrivalAirport().getAirportCode() == null || dashboardViewModel.getArrivalAirport().getAirportCode().isEmpty()) ?
                                dashboardViewModel.getArrivalAirport().getCityCode() : dashboardViewModel.getArrivalAirport().getAirportCode(),
                        dashboardViewModel.isOneWay() ? "oneway" : "roundtrip",
                        dashboardViewModel.getFlightPassengerViewModel().getAdult(),
                        dashboardViewModel.getFlightPassengerViewModel().getChildren(),
                        dashboardViewModel.getFlightPassengerViewModel().getInfant(),
                        dashboardViewModel.getFlightClass().getTitle(),
                        FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, dashboardViewModel.getDepartureDate()),
                        dashboardViewModel.isOneWay() ? "" : String.format(" - %s", FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, dashboardViewModel.getReturnDate()))
                ));
        params.put(SCREEN_NAME, screenName);
        params.put(CURRENT_SITE, FLIGHT_CURRENT_SITE);
        params.put(CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString());
        params.put(BUSSINESS_UNIT, FLIGHT_BU);
        params.put(CATEGORY, Label.FLIGHT_SMALL);
        params.put(USER_ID, userId);

        TrackApp.getInstance().getGTM().sendGeneralEvent(params);
    }

    public void eventQuickFilterClick(String filterName, String userId) {
        Map<String, Object> mapModel = new HashMap<>();
        mapModel.put(EVENT, FLIGHT_CLICK_EVENT);
        mapModel.put(EVENT_CATEGORY, GENERIC_CATEGORY);
        mapModel.put(EVENT_ACTION, Action.WIDGET_CLICK_FILTER);
        mapModel.put(EVENT_LABEL, String.format(Label.WIDGET_FLIGHT_FILTER, filterName));
        mapModel.put(SCREEN_NAME, Screen.SEARCH);
        mapModel.put(CURRENT_SITE, FLIGHT_CURRENT_SITE);
        mapModel.put(CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString());
        mapModel.put(BUSSINESS_UNIT, FLIGHT_BU);
        mapModel.put(CATEGORY, Label.FLIGHT_SMALL);
        mapModel.put(USER_ID, userId);

        TrackApp.getInstance().getGTM().sendGeneralEvent(mapModel);
    }

    public void eventChangeSearchClick(String userId) {
        Map<String, Object> mapModel = new HashMap<>();
        mapModel.put(EVENT, FLIGHT_CLICK_EVENT);
        mapModel.put(EVENT_CATEGORY, GENERIC_CATEGORY);
        mapModel.put(EVENT_ACTION, Action.CLICK_CHANGE_SEARCH);
        mapModel.put(EVENT_LABEL, Label.FLIGHT_CHANGE_SEARCH);
        mapModel.put(SCREEN_NAME, Screen.SEARCH);
        mapModel.put(CURRENT_SITE, FLIGHT_CURRENT_SITE);
        mapModel.put(CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString());
        mapModel.put(BUSSINESS_UNIT, FLIGHT_BU);
        mapModel.put(CATEGORY, Label.FLIGHT_SMALL);
        mapModel.put(USER_ID, userId);

        TrackApp.getInstance().getGTM().sendGeneralEvent(mapModel);
    }

    public void eventSearchView(FlightSearchPassDataModel passDataViewModel, boolean searchFound) {
        Map<String, Object> mapModel = new HashMap<>();
        mapModel.put(EVENT, VIEW_SEARCH_EVENT);
        mapModel.put(EVENT_CATEGORY, GENERIC_CATEGORY);
        mapModel.put(EVENT_ACTION, Category.VIEW_SEARCH);
        mapModel.put(EVENT_LABEL, String.format("%s - %s-%s - %s - %s-%s-%s - %s - %s%s",
                Label.FLIGHT_SMALL,
                (passDataViewModel.getDepartureAirport().getAirportCode() == null || passDataViewModel.getDepartureAirport().getAirportCode().isEmpty()) ?
                        passDataViewModel.getDepartureAirport().getCityCode() : passDataViewModel.getDepartureAirport().getAirportCode(),
                (passDataViewModel.getArrivalAirport().getAirportCode() == null || passDataViewModel.getArrivalAirport().getAirportCode().isEmpty()) ?
                        passDataViewModel.getArrivalAirport().getCityCode() : passDataViewModel.getArrivalAirport().getAirportCode(),
                passDataViewModel.isOneWay() ? "oneway" : "roundtrip",
                passDataViewModel.getFlightPassengerModel().getAdult(),
                passDataViewModel.getFlightPassengerModel().getChildren(),
                passDataViewModel.getFlightPassengerModel().getInfant(),
                passDataViewModel.getFlightClass().getTitle(),
                FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, passDataViewModel.getDepartureDate()),
                passDataViewModel.isOneWay() ? "" : String.format(" - %s", FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, passDataViewModel.getReturnDate()))
        ));
        mapModel.put("from", (passDataViewModel.getDepartureAirport().getAirportCode() == null || passDataViewModel.getDepartureAirport().getAirportCode().isEmpty()) ?
                passDataViewModel.getDepartureAirport().getCityCode() : passDataViewModel.getDepartureAirport().getAirportCode());
        mapModel.put("destination", (passDataViewModel.getArrivalAirport().getAirportCode() == null || passDataViewModel.getArrivalAirport().getAirportCode().isEmpty()) ?
                passDataViewModel.getArrivalAirport().getCityCode() : passDataViewModel.getArrivalAirport().getAirportCode());
        mapModel.put("departureDate", FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, passDataViewModel.getDepartureDate()));
        mapModel.put("departureDateFormatted", passDataViewModel.getDepartureDate());
        mapModel.put("returnDate", passDataViewModel.isOneWay() ? "" : FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, passDataViewModel.getReturnDate()));
        mapModel.put("returnDateFormatted", passDataViewModel.isOneWay() ? "" : passDataViewModel.getReturnDate());
        mapModel.put("returnTicket", passDataViewModel.isOneWay() ? "false" : "true");
        mapModel.put("passenger", passDataViewModel.getFlightPassengerModel().getAdult() + passDataViewModel.getFlightPassengerModel().getChildren() +
                passDataViewModel.getFlightPassengerModel().getInfant());
        mapModel.put("travelWithKids", passDataViewModel.getFlightPassengerModel().getChildren() > 0 ||
                passDataViewModel.getFlightPassengerModel().getInfant() > 0 ? "true" : "false");
        mapModel.put("class", passDataViewModel.getFlightClass().getTitle());

        if (passDataViewModel.getLinkUrl().contains("tokopedia://pesawat") ||
                passDataViewModel.getLinkUrl().contains("tokopedia-android-internal://pesawat")) {
            mapModel.put("deeplinkUrl", passDataViewModel.getLinkUrl());
            mapModel.put("url", "");
        } else {
            mapModel.put("deeplinkUrl", "");
            mapModel.put("url", passDataViewModel.getLinkUrl());
        }
        mapModel.put("searchFound", searchFound ? "true" : "false");
        TrackApp.getInstance().getGTM().sendGeneralEvent(mapModel);
    }

    public void eventSearchProductClickFromDetail(FlightSearchPassDataModel flightSearchPassData, FlightJourneyModel viewModel) {
        StringBuilder result = transformSearchProductClickLabel(viewModel);
        result.append(String.format("%s%s", Label.PRICE_PREFIX, String.valueOf(viewModel.getFare().getAdultNumeric())));
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_PRODUCT,
                result.toString()
        ));
        productClickEnhanceEcommerce(Action.PRODUCT_CLICK_SEARCH_DETAIL, flightSearchPassData, viewModel, result);
    }

    private void productClickEnhanceEcommerce(String action, FlightSearchPassDataModel flightSearchPassData, FlightJourneyModel viewModel, StringBuilder result) {
        List<Object> products = new ArrayList<>();
        if (flightSearchPassData.getFlightPassengerModel().getAdult() > 0) {
            products.add(DataLayer.mapOf(
                    EnhanceEccomerce.ID, viewModel.getId(),
                    EnhanceEccomerce.NAME, viewModel.getDepartureAirportCity() + "-" + viewModel.getArrivalAirportCity() + " - Flight",
                    EnhanceEccomerce.PRICE, String.valueOf(viewModel.getFare().getAdultNumeric()),
                    EnhanceEccomerce.BRAND, viewModel.getRouteList().get(0).getAirlineName(),
                    EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                    EnhanceEccomerce.VARIANT, flightSearchPassData.getFlightClass().getTitle() + " - Adult",
                    EnhanceEccomerce.QUANTITY, String.valueOf(flightSearchPassData.getFlightPassengerModel().getAdult()),
                    "list", "/flight"
            ));
        }
        if (flightSearchPassData.getFlightPassengerModel().getChildren() > 0) {
            products.add(DataLayer.mapOf(
                    EnhanceEccomerce.ID, viewModel.getId(),
                    EnhanceEccomerce.NAME, viewModel.getDepartureAirportCity() + "-" + viewModel.getArrivalAirportCity() + " - Flight",
                    EnhanceEccomerce.PRICE, String.valueOf(viewModel.getFare().getChildNumeric()),
                    EnhanceEccomerce.BRAND, viewModel.getRouteList().get(0).getAirlineName(),
                    EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                    EnhanceEccomerce.VARIANT, flightSearchPassData.getFlightClass().getTitle() + " - Child",
                    EnhanceEccomerce.QUANTITY, String.valueOf(flightSearchPassData.getFlightPassengerModel().getChildren()),
                    "list", "/flight"
            ));
        }
        if (flightSearchPassData.getFlightPassengerModel().getInfant() > 0) {
            products.add(DataLayer.mapOf(
                    EnhanceEccomerce.ID, viewModel.getId(),
                    EnhanceEccomerce.NAME, viewModel.getDepartureAirportCity() + "-" + viewModel.getArrivalAirportCity() + " - Flight",
                    EnhanceEccomerce.PRICE, String.valueOf(viewModel.getFare().getInfantNumeric()),
                    EnhanceEccomerce.BRAND, viewModel.getRouteList().get(0).getAirlineName(),
                    EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                    EnhanceEccomerce.VARIANT, flightSearchPassData.getFlightClass().getTitle() + " - Infant",
                    EnhanceEccomerce.QUANTITY, String.valueOf(flightSearchPassData.getFlightPassengerModel().getInfant()),
                    "list", "/flight"
            ));
        }

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PRODUCT_CLICK_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, action,
                        EVENT_LABEL, result.toString(),
                        ECOMMERCE, DataLayer.mapOf(
                                "products", DataLayer.listOf(
                                        products.toArray(new Object[products.size()])),
                                "actionField", DataLayer.mapOf(
                                        "list", "/flight"
                                )
                        )
                )
        );
    }

    public void eventProductViewNotFound(FlightSearchPassDataModel searchPassDataViewModel, String userId) {
        Map<String, Object> mapModel = new HashMap<>();
        mapModel.put(EVENT, SEARCH_RESULT_EVENT);
        mapModel.put(EVENT_CATEGORY, GENERIC_CATEGORY);
        mapModel.put(EVENT_ACTION, Category.CLICK_SEARCH_PRODUCT_NOT_FOUND);
        mapModel.put(EVENT_LABEL, String.format("%s - %s-%s - %s - %s-%s-%s - %s - %s%s",
                Label.FLIGHT_SMALL,
                (searchPassDataViewModel.getDepartureAirport().getAirportCode() == null || searchPassDataViewModel.getDepartureAirport().getAirportCode().isEmpty()) ?
                        searchPassDataViewModel.getDepartureAirport().getCityCode() : searchPassDataViewModel.getDepartureAirport().getAirportCode(),
                (searchPassDataViewModel.getArrivalAirport().getAirportCode() == null || searchPassDataViewModel.getArrivalAirport().getAirportCode().isEmpty()) ?
                        searchPassDataViewModel.getArrivalAirport().getCityCode() : searchPassDataViewModel.getArrivalAirport().getAirportCode(),
                searchPassDataViewModel.isOneWay() ? "oneway" : "roundtrip",
                searchPassDataViewModel.getFlightPassengerModel().getAdult(),
                searchPassDataViewModel.getFlightPassengerModel().getChildren(),
                searchPassDataViewModel.getFlightPassengerModel().getInfant(),
                searchPassDataViewModel.getFlightClass().getTitle(),
                FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, searchPassDataViewModel.getDepartureDate()),
                searchPassDataViewModel.isOneWay() ? "" : String.format(" - %s", FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, searchPassDataViewModel.getReturnDate()))
        ));
        mapModel.put(SCREEN_NAME, Screen.SEARCH);
        mapModel.put(CURRENT_SITE, FLIGHT_CURRENT_SITE);
        mapModel.put(CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString());
        mapModel.put(BUSSINESS_UNIT, FLIGHT_BU);
        mapModel.put(CATEGORY, Label.FLIGHT_SMALL);
        mapModel.put(USER_ID, userId);

        TrackApp.getInstance().getGTM().sendGeneralEvent(mapModel);
    }

    public void eventProductViewV2EnchanceEcommerce(FlightSearchPassDataModel searchPassDataViewModel,
                                                    List<FlightJourneyModel> listJourneyViewModel,
                                                    String screenName,
                                                    String userId) {

        List<Object> products = new ArrayList<>();
        int position = 0;
        for (FlightJourneyModel item : listJourneyViewModel) {
            position++;
            products.add(transformSearchProductViewV2(searchPassDataViewModel, item, position));
        }

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PRODUCT_VIEW_EVENT,
                        EVENT_CATEGORY, Category.DIGITAL_FLIGHT,
                        EVENT_ACTION, Action.PRODUCT_VIEW_ACTION_V2,
                        SCREEN_NAME, screenName,
                        CURRENT_SITE, FLIGHT_CURRENT_SITE,
                        CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString(),
                        BUSSINESS_UNIT, FLIGHT_BU,
                        CATEGORY, Label.FLIGHT_SMALL,
                        USER_ID, userId,
                        EVENT_LABEL, String.format(Label.PRODUCT_VIEW,
                                (searchPassDataViewModel.getDepartureAirport().getAirportCode() == null || searchPassDataViewModel.getDepartureAirport().getAirportCode().isEmpty()) ?
                                        searchPassDataViewModel.getDepartureAirport().getCityCode() :
                                        searchPassDataViewModel.getDepartureAirport().getAirportCode(),
                                (searchPassDataViewModel.getArrivalAirport().getAirportCode() == null || searchPassDataViewModel.getArrivalAirport().getAirportCode().isEmpty()) ?
                                        searchPassDataViewModel.getArrivalAirport().getCityCode() :
                                        searchPassDataViewModel.getArrivalAirport().getAirportCode()),
                        ECOMMERCE, DataLayer.mapOf(CURRENCY_CODE, DEFAULT_CURRENCY_CODE, IMPRESSIONS, products)
                )
        );
    }

    public void eventSearchProductClickV2FromList(FlightSearchPassDataModel flightSearchPassData,
                                                  FlightJourneyModel viewModel,
                                                  String screenName,
                                                  String userId) {
        List<Object> products = new ArrayList<>();
        products.add(transformSearchProductClickV2(flightSearchPassData, viewModel, 0));

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PRODUCT_CLICK_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Action.PRODUCT_CLICK_SEARCH_LIST,
                        SCREEN_NAME, screenName,
                        CURRENT_SITE, FLIGHT_CURRENT_SITE,
                        CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString(),
                        BUSSINESS_UNIT, FLIGHT_BU,
                        CATEGORY, Label.FLIGHT_SMALL,
                        USER_ID, userId,
                        EVENT_LABEL, String.format(Label.PRODUCT_VIEW,
                                viewModel.getDepartureAirport(),
                                viewModel.getArrivalAirport()),
                        ECOMMERCE, DataLayer.mapOf(
                                CURRENCY_CODE, DEFAULT_CURRENCY_CODE,
                                "click", DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/flight"),
                                        "products", DataLayer.listOf(
                                                products.toArray(new Object[products.size()])))
                        )
                )
        );

    }

    public void eventSearchProductClickV2FromList(FlightSearchPassDataModel flightSearchPassData,
                                                  FlightJourneyModel viewModel,
                                                  int adapterPosition,
                                                  String screenName,
                                                  String userId) {
        List<Object> products = new ArrayList<>();
        products.add(transformSearchProductClickV2(flightSearchPassData, viewModel, adapterPosition));

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PRODUCT_CLICK_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Action.PRODUCT_CLICK_SEARCH_LIST,
                        SCREEN_NAME, screenName,
                        CURRENT_SITE, FLIGHT_CURRENT_SITE,
                        CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString(),
                        BUSSINESS_UNIT, FLIGHT_BU,
                        CATEGORY, Label.FLIGHT_SMALL,
                        USER_ID, userId,
                        EVENT_LABEL, String.format(Label.PRODUCT_VIEW,
                                viewModel.getDepartureAirport(),
                                viewModel.getArrivalAirport()),
                        ECOMMERCE, DataLayer.mapOf(
                                CURRENCY_CODE, DEFAULT_CURRENCY_CODE,
                                "click", DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/flight"),
                                        "products", DataLayer.listOf(
                                                products.toArray(new Object[products.size()])))
                        )
                )
        );
    }

    private Object transformSearchProductViewV2(FlightSearchPassDataModel searchPassDataViewModel, FlightJourneyModel journeyViewModel, int position) {
        String isRefundable = "false";
        for (Route route : journeyViewModel.getRouteList()) {
            if (route.isRefundable()) {
                isRefundable = "true";
                break;
            }
        }

        long totalAdultPrice, totalChildPrice, totalInfantPrice;
        if (journeyViewModel.getFare().getAdultNumericCombo() > 0) {
            totalAdultPrice = journeyViewModel.getFare().getAdultNumericCombo() * searchPassDataViewModel.getFlightPassengerModel().getAdult();
            totalChildPrice = journeyViewModel.getFare().getChildNumericCombo() * searchPassDataViewModel.getFlightPassengerModel().getChildren();
            totalInfantPrice = journeyViewModel.getFare().getInfantNumericCombo() * searchPassDataViewModel.getFlightPassengerModel().getInfant();
        } else {
            totalAdultPrice = journeyViewModel.getFare().getAdultNumeric() * searchPassDataViewModel.getFlightPassengerModel().getAdult();
            totalChildPrice = journeyViewModel.getFare().getChildNumeric() * searchPassDataViewModel.getFlightPassengerModel().getChildren();
            totalInfantPrice = journeyViewModel.getFare().getInfantNumeric() * searchPassDataViewModel.getFlightPassengerModel().getInfant();
        }

        Object product = DataLayer.mapOf(
                EnhanceEccomerce.NAME, journeyViewModel.getDepartureAirportCity() + "-" + journeyViewModel.getArrivalAirportCity(),
                EnhanceEccomerce.PRICE, totalAdultPrice + totalChildPrice + totalInfantPrice,
                EnhanceEccomerce.DIMENSION66, FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.YYYYMMDD, journeyViewModel.getRouteList().get(0).getDepartureTimestamp()),
                EnhanceEccomerce.DIMENSION67, searchPassDataViewModel.isOneWay() ? "oneway" : "roundtrip",
                EnhanceEccomerce.DIMENSION68, searchPassDataViewModel.getFlightClass().getTitle().toLowerCase(),
                EnhanceEccomerce.DIMENSION69, "",
                EnhanceEccomerce.DIMENSION70, isRefundable,
                EnhanceEccomerce.DIMENSION71, journeyViewModel.getTotalTransit() > 0 ? "true" : "false",
                EnhanceEccomerce.DIMENSION72, journeyViewModel.getBeforeTotal().equals("") ? "normal" : "strike",
                EnhanceEccomerce.DIMENSION73, searchPassDataViewModel.getFlightPassengerModel().getAdult() + " - " +
                        searchPassDataViewModel.getFlightPassengerModel().getChildren() + " - " + searchPassDataViewModel.getFlightPassengerModel().getInfant(),
                EnhanceEccomerce.ID, journeyViewModel.getId(),
                EnhanceEccomerce.BRAND, journeyViewModel.getRouteList().get(0).getAirlineName(),
                EnhanceEccomerce.DIMENSION74, journeyViewModel.getRouteList().get(0).getAirline() + " - " + journeyViewModel.getRouteList().get(0).getFlightNumber(),
                EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                EnhanceEccomerce.DIMENSION75, journeyViewModel.getDepartureTime(),
                EnhanceEccomerce.DIMENSION76, journeyViewModel.getArrivalTime() + ((journeyViewModel.getAddDayArrival() > 0) ? String.format(" +%s", journeyViewModel.getAddDayArrival()) : ""),
                EnhanceEccomerce.DIMENSION107, String.format("%s|%s", journeyViewModel.isSeatDistancing(), journeyViewModel.getHasFreeRapidTest()),
                EnhanceEccomerce.POSITIONS, position,
                EnhanceEccomerce.VARIANT, totalAdultPrice + " - " + totalChildPrice + " - " + totalInfantPrice,
                EnhanceEccomerce.LIST, "/flight"
        );

        return product;
    }

    private Object transformSearchProductClickV2(FlightSearchPassDataModel searchPassDataViewModel, FlightJourneyModel journeyViewModel, int position) {
        String isRefundable = "false";
        for (Route route : journeyViewModel.getRouteList()) {
            if (route.isRefundable()) {
                isRefundable = "true";
                break;
            }
        }

        long totalAdultPrice, totalChildPrice, totalInfantPrice;
        if (journeyViewModel.getFare().getAdultNumericCombo() > 0) {
            totalAdultPrice = journeyViewModel.getFare().getAdultNumericCombo() * searchPassDataViewModel.getFlightPassengerModel().getAdult();
            totalChildPrice = journeyViewModel.getFare().getChildNumericCombo() * searchPassDataViewModel.getFlightPassengerModel().getChildren();
            totalInfantPrice = journeyViewModel.getFare().getInfantNumericCombo() * searchPassDataViewModel.getFlightPassengerModel().getInfant();
        } else {
            totalAdultPrice = journeyViewModel.getFare().getAdultNumeric() * searchPassDataViewModel.getFlightPassengerModel().getAdult();
            totalChildPrice = journeyViewModel.getFare().getChildNumeric() * searchPassDataViewModel.getFlightPassengerModel().getChildren();
            totalInfantPrice = journeyViewModel.getFare().getInfantNumeric() * searchPassDataViewModel.getFlightPassengerModel().getInfant();
        }

        Object product = DataLayer.mapOf(
                EnhanceEccomerce.NAME, journeyViewModel.getDepartureAirportCity() + "-" + journeyViewModel.getArrivalAirportCity(),
                EnhanceEccomerce.PRICE, totalAdultPrice + totalChildPrice + totalInfantPrice,
                EnhanceEccomerce.DIMENSION66, FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.YYYYMMDD, journeyViewModel.getRouteList().get(0).getDepartureTimestamp()),
                EnhanceEccomerce.DIMENSION67, searchPassDataViewModel.isOneWay() ? "oneway" : "roundtrip",
                EnhanceEccomerce.DIMENSION68, searchPassDataViewModel.getFlightClass().getTitle().toLowerCase(),
                EnhanceEccomerce.DIMENSION69, "",
                EnhanceEccomerce.DIMENSION70, isRefundable,
                EnhanceEccomerce.DIMENSION71, journeyViewModel.getTotalTransit() > 0 ? "true" : "false",
                EnhanceEccomerce.DIMENSION72, journeyViewModel.getBeforeTotal().equals("") ? "normal" : "strike",
                EnhanceEccomerce.DIMENSION73, searchPassDataViewModel.getFlightPassengerModel().getAdult() + " - " +
                        searchPassDataViewModel.getFlightPassengerModel().getChildren() + " - " + searchPassDataViewModel.getFlightPassengerModel().getInfant(),
                EnhanceEccomerce.ID, journeyViewModel.getId(),
                EnhanceEccomerce.BRAND, journeyViewModel.getRouteList().get(0).getAirlineName(),
                EnhanceEccomerce.DIMENSION74, journeyViewModel.getRouteList().get(0).getAirline() + " - " + journeyViewModel.getRouteList().get(0).getFlightNumber(),
                EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                EnhanceEccomerce.DIMENSION75, journeyViewModel.getDepartureTime(),
                EnhanceEccomerce.DIMENSION76, journeyViewModel.getArrivalTime() + ((journeyViewModel.getAddDayArrival() > 0) ? String.format(" +%s", journeyViewModel.getAddDayArrival()) : ""),
                EnhanceEccomerce.DIMENSION107, String.format("%s|%s", journeyViewModel.isSeatDistancing(), journeyViewModel.getHasFreeRapidTest()),
                EnhanceEccomerce.POSITIONS, position,
                EnhanceEccomerce.VARIANT, totalAdultPrice + " - " + totalChildPrice + " - " + totalInfantPrice,
                EnhanceEccomerce.LIST, "/flight"
        );

        return product;
    }

    @NonNull
    private StringBuilder transformSearchProductClickLabel(FlightJourneyModel viewModel) {
        StringBuilder result = new StringBuilder();
        if (viewModel != null && viewModel.getAirlineDataList() != null) {
            List<String> airlines = new ArrayList<>();
            for (FlightAirlineModel airlineDB : viewModel.getAirlineDataList()) {
                airlines.add(airlineDB.getShortName().toLowerCase());
            }
            result.append(TextUtils.join(",", airlines));
        }

        if (viewModel != null && viewModel.getRouteList() != null && viewModel.getRouteList().size() > 0) {
            String timeResult = String.valueOf(flightDateUtil.getDayDiff(viewModel.getRouteList().get(0).getDepartureTimestamp()));
            timeResult += " - " + String.valueOf(flightDateUtil.getDayDiff(viewModel.getRouteList().get(viewModel.getRouteList().size() - 1).getArrivalTimestamp()));
            result.append(String.format(" - %s", timeResult));
        }
        return result;
    }

    public void eventSearchDetailClick(FlightJourneyModel viewModel, int adapterPosition) {
        StringBuilder result = transformSearchDetailLabel(viewModel, adapterPosition);
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_DETAIL,
                result.toString()
        ));
    }

    public void eventDetailPriceTabClick(FlightDetailModel viewModel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_PRICE_TAB,
                transformEventDetailLabel(viewModel)
        ));
    }

    public void eventDetailFacilitiesTabClick(FlightDetailModel viewModel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_FACILITIES_TAB,
                transformEventDetailLabel(viewModel)
        ));
    }

    public void eventDetailTabClick(FlightDetailModel viewModel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_DETAIL_TAB,
                transformEventDetailLabel(viewModel)
        ));
    }

    private String transformEventDetailLabel(FlightDetailModel viewModel) {
        StringBuilder result = new StringBuilder();
        if (viewModel.getRouteList() != null && viewModel.getRouteList().size() > 0) {
            String airlines = viewModel.getRouteList().get(0).getAirlineName().toLowerCase();
            result.append(airlines);
            String timeResult = String.format(" - %s", String.valueOf(flightDateUtil.getDayDiff(viewModel.getRouteList().get(0).getDepartureTimestamp())));
            timeResult += String.format(" - %s ", String.valueOf(flightDateUtil.getDayDiff(viewModel.getRouteList().get(viewModel.getRouteList().size() - 1).getArrivalTimestamp())));
            result.append(timeResult);
        }
        result.append(transformRefundableLabel(viewModel.isRefundable()));
        result.append(String.format(" - %s", String.valueOf(viewModel.getAdultNumericPrice())));
        return result.toString();
    }

    @NonNull
    private StringBuilder transformSearchDetailLabel(FlightJourneyModel viewModel, int adapterPosition) {
        StringBuilder result = new StringBuilder();
        if (viewModel.getAirlineDataList() != null) {
            List<String> airlines = new ArrayList<>();
            for (FlightAirlineModel airlineDB : viewModel.getAirlineDataList()) {
                airlines.add(airlineDB.getId());
            }
            result.append(TextUtils.join(",", airlines));
        }

        if (viewModel.getRouteList() != null && viewModel.getRouteList().size() > 0) {
            String timeResult = String.format(" - %s", String.valueOf(flightDateUtil.getDayDiff(viewModel.getRouteList().get(0).getDepartureTimestamp())));
            timeResult += String.format(" - %s", String.valueOf(flightDateUtil.getDayDiff(viewModel.getRouteList().get(viewModel.getRouteList().size() - 1).getArrivalTimestamp())));
            result.append(timeResult);
        }
        result.append(transformRefundableLabel(viewModel.isRefundable()));
        result.append(String.format(getDefaultLocale(), " - %d - ", adapterPosition));
        result.append(String.valueOf(viewModel.getFare().getAdultNumeric()));
        return result;
    }

    @NonNull
    private String transformRefundableLabel(RefundableEnum refundableEnum) {
        String refundable;
        if (refundableEnum == RefundableEnum.REFUNDABLE) {
            refundable = Label.REFUNDABLE;
        } else if (refundableEnum == RefundableEnum.PARTIAL_REFUNDABLE) {
            refundable = Label.PARTIALLY_REFUNDABLE;
        } else {
            refundable = Label.NOT_REFUNDABLE;
        }
        return refundable;
    }

    public void eventCheckoutClick(FlightDetailModel departureTrip,
                                   FlightDetailModel returnTrip,
                                   FlightSearchPassDataModel searchParam,
                                   String comboKey,
                                   String userId) {

        List<Object> products = new ArrayList<>();

        if (departureTrip != null) {
            products.addAll(constructEnhanceEcommerceProduct(departureTrip, comboKey,
                    searchParam.getFlightClass().getTitle(), returnTrip == null));
            if (returnTrip != null) {
                products.addAll(constructEnhanceEcommerceProduct(returnTrip, comboKey,
                        searchParam.getFlightClass().getTitle(), false));
            }
        }

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, CHECKOUT_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Category.BOOKING_NEXT,
                        SCREEN_NAME, Screen.BOOKING,
                        CURRENT_SITE, FLIGHT_CURRENT_SITE,
                        CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString(),
                        BUSSINESS_UNIT, FLIGHT_BU,
                        CATEGORY, Label.FLIGHT_SMALL,
                        USER_ID, userId,
                        EVENT_LABEL, String.format(Label.PRODUCT_VIEW,
                                departureTrip.getDepartureAirport(),
                                departureTrip.getArrivalAirport()),
                        ECOMMERCE, DataLayer.mapOf(
                                "checkout", DataLayer.mapOf(
                                        CURRENCY_CODE, DEFAULT_CURRENCY_CODE,
                                        "actionField", DataLayer.mapOf(
                                                "step", "1",
                                                "option", Category.BOOKING_NEXT),
                                        "products", products
                                )
                        )
                )
        );
    }

    public void eventPassengerClick(int adult, int children, int infant) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.SELECT_PASSENGER,
                String.format(getDefaultLocale(),
                        "%d %s - %d %s - %d %s",
                        adult,
                        Label.ADULT,
                        children,
                        Label.CHILD,
                        infant,
                        Label.INFANT
                )
        ));
    }

    public void eventAddToCart(FlightClassModel flightClass,
                               FlightDetailModel departureViewModel,
                               FlightDetailModel returnViewModel,
                               String comboKey,
                               String userId) {

        List<Object> products = new ArrayList<>();

        if (departureViewModel != null) {
            products.addAll(constructEnhanceEcommerceProduct(departureViewModel, comboKey, flightClass.getTitle(), returnViewModel == null));
            if (returnViewModel != null) {
                products.addAll(constructEnhanceEcommerceProduct(returnViewModel, comboKey, flightClass.getTitle(), false));
            }
        }

        try {
            TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                    DataLayer.mapOf(EVENT, ATC_EVENT,
                            EVENT_CATEGORY, GENERIC_CATEGORY,
                            EVENT_ACTION, Category.ADD_TO_CART,
                            SCREEN_NAME, Screen.BOOKING,
                            CURRENT_SITE, FLIGHT_CURRENT_SITE,
                            CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString(),
                            BUSSINESS_UNIT, FLIGHT_BU,
                            CATEGORY, Label.FLIGHT_SMALL,
                            USER_ID, userId,
                            EVENT_LABEL, String.format(Label.PRODUCT_VIEW,
                                    departureViewModel.getDepartureAirport(),
                                    departureViewModel.getArrivalAirport()),
                            ECOMMERCE, DataLayer.mapOf(
                                    CURRENCY_CODE, DEFAULT_CURRENCY_CODE,
                                    "add", DataLayer.mapOf(
                                            "products", DataLayer.listOf(
                                                    products.toArray(new Object[products.size()]))
                                    )
                            )
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Object> constructEnhanceEcommerceProduct(FlightDetailModel flightViewModel, String comboKey, String flightClass, Boolean isOneWay) {
        List<Object> products = new ArrayList<>();
        String name = flightViewModel.getDepartureAirportCity() + "-" + flightViewModel.getArrivalAirportCity();

        int totalPriceAdult = flightViewModel.getAdultNumericPrice() * flightViewModel.getCountAdult();
        int totalPriceChild = flightViewModel.getChildNumericPrice() * flightViewModel.getCountChild();
        int totalPriceInfant = flightViewModel.getInfantNumericPrice() * flightViewModel.getCountInfant();

        long layoverDayDiff = FlightDateUtil.countDayDifference(FlightDateUtil.YYYYMMDD, FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.YYYYMMDD, flightViewModel.getRouteList().get(0).getDepartureTimestamp()),
                FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.YYYYMMDD, flightViewModel.getRouteList().get(flightViewModel.getRouteList().size() - 1).getArrivalTimestamp()));
        String dayDiffString = layoverDayDiff > 0 ? String.format(" +%s", layoverDayDiff) : "";

        products.add(DataLayer.mapOf(
                EnhanceEccomerce.NAME, name,
                EnhanceEccomerce.PRICE, totalPriceAdult + totalPriceChild + totalPriceInfant,
                EnhanceEccomerce.ID, (comboKey != null && !comboKey.isEmpty()) ? comboKey : flightViewModel.getId(),
                EnhanceEccomerce.BRAND, flightViewModel.getRouteList().get(0).getAirlineName(),
                EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                EnhanceEccomerce.QUANTITY, flightViewModel.getCountAdult() + flightViewModel.getCountChild() + flightViewModel.getCountInfant(),
                EnhanceEccomerce.DIMENSION66, FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.YYYYMMDD, flightViewModel.getRouteList().get(0).getDepartureTimestamp()),
                EnhanceEccomerce.DIMENSION67, isOneWay ? "oneway" : "roundtrip",
                EnhanceEccomerce.DIMENSION68, flightClass,
                EnhanceEccomerce.DIMENSION69, "",
                EnhanceEccomerce.DIMENSION70, (flightViewModel.isRefundable() == RefundableEnum.NOT_REFUNDABLE) ? "false" : "true",
                EnhanceEccomerce.DIMENSION71, flightViewModel.getTotalTransit() > 0 ? "true" : "false",
                EnhanceEccomerce.DIMENSION72, flightViewModel.getBeforeTotal().equals("") ? "normal" : "strike",
                EnhanceEccomerce.DIMENSION73, flightViewModel.getCountAdult() + " - " + flightViewModel.getCountChild() + " - " + flightViewModel.getCountInfant(),
                EnhanceEccomerce.DIMENSION74, flightViewModel.getRouteList().get(0).getAirlineCode() + " - " + flightViewModel.getRouteList().get(0).getFlightNumber(),
                EnhanceEccomerce.DIMENSION75, flightViewModel.getDepartureTime(),
                EnhanceEccomerce.DIMENSION76, flightViewModel.getArrivalTime() + dayDiffString,
                EnhanceEccomerce.VARIANT, totalPriceAdult + " - " + totalPriceChild + " - " + totalPriceInfant
        ));
        return products;
    }

    public void eventPromoImpression(int position,
                                     TravelCollectiveBannerModel.Banner banner,
                                     String screenName,
                                     String userId) {
        List<Object> promos = new ArrayList<>();
        promos.add(DataLayer.mapOf(
                EnhanceEccomerce.ID, banner.getId(),
                EnhanceEccomerce.NAME, String.format(getDefaultLocale(), "%s - %s",
                        banner.getAttribute().getPromoCode(), "slider banner"),
                "creative", banner.getAttribute().getDescription().toLowerCase(),
                "creative_url", banner.getAttribute().getAppUrl(),
                "position", String.valueOf(position + 1)
        ));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PROMO_VIEW_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Action.PROMOTION_VIEW,
                        SCREEN_NAME, screenName,
                        CURRENT_SITE, FLIGHT_CURRENT_SITE,
                        CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString(),
                        BUSSINESS_UNIT, FLIGHT_BU,
                        CATEGORY, Label.FLIGHT_SMALL,
                        USER_ID, userId,
                        EVENT_LABEL, String.format(getDefaultLocale(), "%s - %d - %s",
                                Label.FLIGHT_SMALL,
                                position + 1,
                                banner.getAttribute().getPromoCode()
                        ),
                        ECOMMERCE, DataLayer.mapOf(
                                "promoView",
                                DataLayer.mapOf(
                                        "promotions",
                                        DataLayer.listOf(
                                                promos.toArray(new Object[promos.size()])
                                        )
                                )
                        )
                )
        );
    }

    public void eventProductDetailImpression(FlightJourneyModel flightSearchViewModel, int adapterPosition) {
        StringBuilder result = transformSearchDetailLabel(flightSearchViewModel, adapterPosition);
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.PRODUCT_DETAIL_IMPRESSION,
                result.toString()
        ));
    }

    public void eventBranchCheckoutFlight(String productName, String journeyId, String invoiceId,
                                          String paymentId, String userId, String totalPrice) {
        LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_PURCHASE_FLIGHT,
                createLinkerData(productName, journeyId, invoiceId, paymentId, userId, totalPrice)));
    }

    private LinkerData createLinkerData(String productName, String journeyId, String invoiceId,
                                        String paymentId, String userId, String totalPrice) {
        LinkerData linkerData = new LinkerData();
        linkerData.setProductCategory(Label.FLIGHT_SMALL);
        linkerData.setProductName(productName);
        linkerData.setJourneyId(journeyId);
        linkerData.setUserId(userId);
        linkerData.setInvoiceId(invoiceId);
        linkerData.setPaymentId(paymentId);
        linkerData.setPrice(totalPrice);
        return linkerData;
    }

    public void openOrderDetail(String eventLabel,
                                String userId) {
        Map<String, Object> params = TrackAppUtils.gtmData(SEARCH_RESULT_EVENT,
                GENERIC_CATEGORY,
                Category.VIEW_ORDER_DETAIL,
                String.format("%s - %s", Label.FLIGHT_SMALL, eventLabel));
        params.put(USER_ID, userId);
        params.put(SCREEN_NAME, Screen.ORDER_DETAIL);
        buildGeneralFlightParams(params);

        TrackApp.getInstance().getGTM().sendGeneralEvent(params);
    }

    public void eventSendETicketOrderDetail(String eventLabel,
                                            String userId) {
        Map<String, Object> params = TrackAppUtils.gtmData(FLIGHT_CLICK_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEND_ETICKET,
                String.format("%s - %s", Label.FLIGHT_SMALL, eventLabel));
        params.put(USER_ID, userId);
        params.put(SCREEN_NAME, Screen.ORDER_DETAIL);
        buildGeneralFlightParams(params);

        TrackApp.getInstance().getGTM().sendGeneralEvent(params);
    }

    public void eventDownloadETicketOrderDetail(String eventLabel,
                                            String userId) {
        Map<String, Object> params = TrackAppUtils.gtmData(FLIGHT_CLICK_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_DOWNLOAD_ETICKET,
                String.format("%s - %s", Label.FLIGHT_SMALL, eventLabel));
        params.put(USER_ID, userId);
        params.put(SCREEN_NAME, Screen.ORDER_DETAIL);
        buildGeneralFlightParams(params);

        TrackApp.getInstance().getGTM().sendGeneralEvent(params);
    }

    public void eventWebCheckInOrderDetail(String eventLabel,
                                           String userId) {
        Map<String, Object> params = TrackAppUtils.gtmData(FLIGHT_CLICK_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_WEB_CHECKIN,
                String.format("%s - %s", Label.FLIGHT_SMALL, eventLabel));
        params.put(USER_ID, userId);
        params.put(SCREEN_NAME, Screen.ORDER_DETAIL);
        buildGeneralFlightParams(params);

        TrackApp.getInstance().getGTM().sendGeneralEvent(params);
    }

    public void eventCancelTicketOrderDetail(String eventLabel,
                                             String userId) {
        Map<String, Object> params = TrackAppUtils.gtmData(FLIGHT_CLICK_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_CANCEL_TICKET,
                String.format("%s - %s", Label.FLIGHT_SMALL, eventLabel));
        params.put(USER_ID, userId);
        params.put(SCREEN_NAME, Screen.ORDER_DETAIL);
        buildGeneralFlightParams(params);

        TrackApp.getInstance().getGTM().sendGeneralEvent(params);
    }

    public void eventClickOnWebCheckIn(String eventLabel,
                                       String userId,
                                       boolean isDeparture) {
        String action = isDeparture ? Category.CLICK_CHECKIN_DEPARTURE : Category.CLICK_CHECKIN_RETURN;
        Map<String, Object> params = TrackAppUtils.gtmData(FLIGHT_CLICK_EVENT,
                GENERIC_CATEGORY,
                action,
                String.format("%s - %s", Label.FLIGHT_SMALL, eventLabel));
        params.put(USER_ID, userId);
        params.put(SCREEN_NAME, Screen.WEB_CHECKIN);
        buildGeneralFlightParams(params);

        TrackApp.getInstance().getGTM().sendGeneralEvent(params);
    }

    public void eventClickNextOnCancellationPassenger(String eventLabel,
                                                      String userId) {
        Map<String, Object> params = TrackAppUtils.gtmData(FLIGHT_CLICK_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_NEXT_CANCELLATION_PASSENGER,
                String.format("%s - %s", Label.FLIGHT_SMALL, eventLabel));
        params.put(USER_ID, userId);
        params.put(SCREEN_NAME, Screen.CANCELLATION_PASSENGER);
        buildGeneralFlightParams(params);

        TrackApp.getInstance().getGTM().sendGeneralEvent(params);
    }

    public void eventClickNextOnCancellationReason(String eventLabel,
                                                   String userId) {
        Map<String, Object> params = TrackAppUtils.gtmData(FLIGHT_CLICK_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_NEXT_CANCELLATION_REASON,
                String.format("%s - %s", Label.FLIGHT_SMALL, eventLabel));
        params.put(USER_ID, userId);
        params.put(SCREEN_NAME, Screen.CANCELLATION_REASON);
        buildGeneralFlightParams(params);

        TrackApp.getInstance().getGTM().sendGeneralEvent(params);
    }

    public void eventClickNextOnCancellationSubmit(String eventLabel,
                                                   String userId) {
        Map<String, Object> params = TrackAppUtils.gtmData(FLIGHT_CLICK_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SUBMIT_CANCELLATION,
                String.format("%s - %s", Label.FLIGHT_SMALL, eventLabel));
        params.put(USER_ID, userId);
        params.put(SCREEN_NAME, Screen.CANCELLATION_SUMMARY);
        buildGeneralFlightParams(params);

        TrackApp.getInstance().getGTM().sendGeneralEvent(params);
    }

    public void eventFlightPromotionClick(int position,
                                    AirlinePrice airlinePrice,
                                    FlightSearchPassDataModel flightSearchPassDataModel,
                                    String screenName,
                                    String userId, boolean isReturn) {
        String actionLabel = Action.CLICK_DEPARTURE_PROMOTION_CHIPS;
        if(isReturn){
            actionLabel = Action.CLICK_RETURN_PROMOTION_CHIPS;
        }
        List<Object> promos = new ArrayList<>();
        promos.add(DataLayer.mapOf(
                EnhanceEccomerce.ID, airlinePrice.getAirlineID(),
                EnhanceEccomerce.NAME, airlinePrice.getShortName(),
                "position", String.valueOf(position),
                "creative", airlinePrice.getShortName(),
                "creative_url", airlinePrice.getLogo()));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PROMO_CLICK_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, actionLabel,
                        SCREEN_NAME, screenName,
                        CURRENT_SITE, FLIGHT_CURRENT_SITE,
                        CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString(),
                        BUSSINESS_UNIT, FLIGHT_BU,
                        CATEGORY, Label.FLIGHT_SMALL,
                        USER_ID, userId,
                        EVENT_LABEL, String.format(getDefaultLocale(), "%s - %s - %s - %d",
                                airlinePrice.getAirlineID(),
                                flightSearchPassDataModel.getDepartureAirport(isReturn),
                                flightSearchPassDataModel.getArrivalAirport(isReturn),
                                airlinePrice.getPriceNumeric()
                        ),
                        ECOMMERCE, DataLayer.mapOf(
                                "promoClick",
                                DataLayer.mapOf(
                                        "promotions",
                                        DataLayer.listOf(
                                                promos.toArray(new Object[promos.size()])
                                        )
                                )
                        )
                )
        );
    }

    public void eventVideoBannerImpression(TravelVideoBannerModel travelVideoBannerModel, String screenName, String userId){
        List<Object> promos = new ArrayList<>();
        promos.add(DataLayer.mapOf(
                EnhanceEccomerce.ID, travelVideoBannerModel.getId(),
                EnhanceEccomerce.NAME, Label.SLASH_FLIGHT,
                "creative_name", travelVideoBannerModel.getTitle(),
                "position", 0));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PROMO_VIEW_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Action.VIDEO_BANNER_VIEW,
                        SCREEN_NAME, screenName,
                        CURRENT_SITE, FLIGHT_CURRENT_SITE,
                        CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString(),
                        BUSSINESS_UNIT, FLIGHT_BU,
                        CATEGORY, Label.FLIGHT_SMALL,
                        USER_ID, userId,
                        EVENT_LABEL, String.format(getDefaultLocale(), "%s - %s",
                                Label.FLIGHT_SMALL,
                                Label.FLIGHT_TRAVEL_VIDEO_BANNER
                        ),
                        ECOMMERCE, DataLayer.mapOf(
                                "promoView",
                                DataLayer.mapOf(
                                        "promotions",
                                        DataLayer.listOf(
                                                promos.toArray(new Object[promos.size()])
                                        )
                                )
                        )
                )
        );
    }

    public void eventVideoBannerClick(TravelVideoBannerModel travelVideoBannerModel, String screenName, String userId){
        List<Object> promos = new ArrayList<>();
        promos.add(DataLayer.mapOf(
                EnhanceEccomerce.ID, travelVideoBannerModel.getId(),
                EnhanceEccomerce.NAME, Label.SLASH_FLIGHT,
                "creative_name", travelVideoBannerModel.getTitle(),
                "position", 0));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PROMO_CLICK_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Action.VIDEO_BANNER_CLICK,
                        SCREEN_NAME, screenName,
                        CURRENT_SITE, FLIGHT_CURRENT_SITE,
                        CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString(),
                        BUSSINESS_UNIT, FLIGHT_BU,
                        CATEGORY, Label.FLIGHT_SMALL,
                        USER_ID, userId,
                        EVENT_LABEL, String.format(getDefaultLocale(), "%s - %s",
                                Label.FLIGHT_SMALL,
                                Label.FLIGHT_TRAVEL_VIDEO_BANNER
                        ),
                        ECOMMERCE, DataLayer.mapOf(
                                "promoClick",
                                DataLayer.mapOf(
                                        "promotions",
                                        DataLayer.listOf(
                                                promos.toArray(new Object[promos.size()])
                                        )
                                )
                        )
                )
        );
    }

    private void buildGeneralFlightParams(Map<String, Object> params) {
        params.put(CURRENT_SITE, FLIGHT_CURRENT_SITE);
        params.put(CLIENT_ID, TrackApp.getInstance().getGTM().getClientIDString());
        params.put(BUSSINESS_UNIT, FLIGHT_BU);
        params.put(CATEGORY, Label.FLIGHT_SMALL);
    }

    public static final class Screen {

        public static String FLIGHT_CANCELLATION_STEP_TWO = "Flight Cancellation Reason and Proof";
        public static String HOMEPAGE = "/flight/homepage";
        public static String SEARCH = "/flight/search";
        public static String SEARCH_RETURN = "Search Return";
        public static String REVIEW = "/flight/summary";
        public static String BOOKING = "/flight/booking";
        public static String ORDER_DETAIL = "/flight/orderdetail";
        public static String WEB_CHECKIN = "/flight/webcheckindetail";
        public static String CANCELLATION_PASSENGER = "/flight/cancellationpassenger";
        public static String CANCELLATION_REASON = "/flight/cancellationreason";
        public static String CANCELLATION_SUMMARY = "/flight/cancellationsummary";
    }

    private static class Category {
        static String CLICK_TRANSACTIONS = "click transaction list";
        static String CLICK_PROMOTION = "promotion click";
        static String PROMOTION_IMPRESSION = "promotion impressions";
        static String CLICK_TRIP_TYPE = "select trip type";
        static String SELECT_ORIGIN = "select origin";
        static String SELECT_DESTINATION = "select destination";
        static String SELECT_PASSENGER = "select Passenger";
        static String SELECT_CLASS = "select flight class";
        static String CLICK_SEARCH = "click search flight";
        static String VIEW_SEARCH = "view search result flight";
        static String CLICK_SEARCH_PRODUCT = "product click";
        static String CLICK_SEARCH_PRODUCT_NOT_FOUND = "product not found";
        static String CLICK_SEARCH_DETAIL = "click see the details";
        static String PRODUCT_DETAIL_IMPRESSION = "product detail impression";
        static String CLICK_PRICE_TAB = "click price tab";
        static String CLICK_FACILITIES_TAB = "click facilities tab";
        static String CLICK_DETAIL_TAB = "click flights detail tab";
        static String ADD_TO_CART = "add to cart";
        static String BOOKING_DETAIL = "click detail";
        static String BOOKING_NEXT = "click next on customer page";
        static String REVIEW_NEXT = "click next on summary page";
        static String VOUCHER = "click gunakan voucher code";
        static String VOUCHER_SUCCESS = "voucher success";
        static String VOUCHER_ERROR = "voucher error";
        static String PURCHASE_ATTEMPT = "purchase attempt";
        static String ADD_INSURANCE = "add insurance";
        static String REMOVE_INSURANCE = "remove insurance";
        static String MORE_INSURANCE_INFO = "click more insurance information";
        static String MORE_INSURANCE = "see another insurance benefit";
        static String DIGITAL_FLIGHT = "digital - flight";
        static String VIEW_ORDER_DETAIL = "view order detail";
        static String CLICK_SEND_ETICKET = "click send eticket";
        static String CLICK_DOWNLOAD_ETICKET = "click download eticket";
        static String CLICK_WEB_CHECKIN = "click web checkin";
        static String CLICK_CANCEL_TICKET = "click cancel ticket";
        static String CLICK_CHECKIN_DEPARTURE = "click checkin on depart route";
        static String CLICK_CHECKIN_RETURN = "click checkin on return route";
        static String CLICK_NEXT_CANCELLATION_PASSENGER = "click next on cancellation passenger";
        static String CLICK_NEXT_CANCELLATION_REASON = "click next on cancellation reason";
        static String CLICK_SUBMIT_CANCELLATION = "click submit cancellation";
    }

    private static class Action {
        static String PROMOTION_VIEW = "banner impression";
        static String PROMOTION_CLICK = "click banner";
        static String PRODUCT_CLICK_SEARCH_LIST = "product click v2";
        static String PRODUCT_CLICK_SEARCH_DETAIL = "click pilih on flight detail";
        static String PRODUCT_VIEW_ACTION = "product impressions";
        static String PRODUCT_VIEW_ACTION_V2 = "product impressions v2";
        static String WIDGET_CLICK_FILTER = "click widget filter";
        static String CLICK_CHANGE_SEARCH = "click change search";
        static String CLICK_DEPARTURE_PROMOTION_CHIPS = "click on departure promo chip";
        static String CLICK_RETURN_PROMOTION_CHIPS = "click on return promo chip";
        static String VIDEO_BANNER_VIEW = "view travel video";
        static String VIDEO_BANNER_CLICK = "click travel video";
    }

    private static class Label {
        static String NONE = "none";
        static String FAILED_PURCHASE = "FAILED";
        static String SUCCESS_PURCHASE = "SUCCESS";
        static String CANCEL_PURCHASE = "CANCEL";
        static String PRICE_PREFIX = " - ";
        static String ADULT = "adult";
        static String CHILD = "child";
        static String INFANT = "baby";
        static String REVIEW_NEXT = "on order details page";
        static String REFUNDABLE = "- refundable";
        static String NOT_REFUNDABLE = "- not refundable";
        static String PARTIALLY_REFUNDABLE = "- partially refundable";
        static String FLIGHT = "Flight";
        static String FLIGHT_SMALL = "flight";
        static String PRODUCT_VIEW = "flight - %s-%s";
        static String WIDGET_FLIGHT_FILTER = "flight - %s";
        static String FLIGHT_CHANGE_SEARCH = "flight - change search";
        static String FLIGHT_TRAVEL_VIDEO_BANNER = "travel video";
        static String SLASH_FLIGHT = "/flight";
    }

    private static class EnhanceEccomerce {
        static String NAME = "name";
        static String ID = "id";
        static String PRICE = "price";
        static String BRAND = "brand";
        static String CATEGORY = "category";
        static String VARIANT = "variant";
        static String QUANTITY = "quantity";
        static String COUPON = "coupon";

        static String AFFILIATION = "affiliation";
        static String REVENUE = "revenue";
        static String TAX = "tax";
        static String SHIPING = "shipping";

        static String DIMENSION66 = "dimension66";
        static String DIMENSION67 = "dimension67";
        static String DIMENSION68 = "dimension68";
        static String DIMENSION69 = "dimension69";
        static String DIMENSION70 = "dimension70";
        static String DIMENSION71 = "dimension71";
        static String DIMENSION72 = "dimension72";
        static String DIMENSION73 = "dimension73";
        static String DIMENSION74 = "dimension74";
        static String DIMENSION75 = "dimension75";
        static String DIMENSION76 = "dimension76";
        static String DIMENSION107 = "dimension107";

        static String POSITIONS = "positions";
        static String LIST = "list";
    }
}
