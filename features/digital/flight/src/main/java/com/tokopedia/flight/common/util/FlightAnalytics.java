package com.tokopedia.flight.common.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;
import com.tokopedia.flight.search.data.api.single.response.Route;
import com.tokopedia.flight.search.presentation.model.FlightAirlineViewModel;
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel;
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
    private String PRODUCT_CLICK_EVENT = "productClick";
    private String PRODUCT_VIEW_EVENT = "productView";
    private String SEARCH_RESULT_EVENT = "searchResult";
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
    private String IS_LOGIN_STATUS = "isLoggedInStatus";

    @Inject
    public FlightAnalytics(FlightDateUtil flightDateUtil) {
        this.flightDateUtil = flightDateUtil;
    }

    public void eventOpenScreen(String screenName, boolean isLoginStatus) {
        Map<String, String> mapOpenScreen = new HashMap<>();
        mapOpenScreen.put(EVENT_NAME, OPEN_SCREEN_EVENT);
        mapOpenScreen.put(IS_LOGIN_STATUS, isLoginStatus? "true" : "false");
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

    public void eventPromotionClick(int position, BannerDetail banner) {
        List<Object> promos = new ArrayList<>();
        promos.add(DataLayer.mapOf(
                EnhanceEccomerce.ID, banner.getId(),
                EnhanceEccomerce.NAME, String.format(getDefaultLocale(), "%s - %s",
                        banner.getAttributes().getPromoCode(), "slider banner"),
                "position", String.valueOf(position),
                "creative", banner.getAttributes().getDescription().toLowerCase(),
                "creative_url", banner.getAttributes().getLinkUrl()
        ));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PROMO_CLICK_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Action.PROMOTION_CLICK,
                        EVENT_LABEL, String.format(getDefaultLocale(), "%s - %d - %s",
                                Label.FLIGHT_SMALL,
                                position,
                                banner.getAttributes().getPromoCode()
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

    public void eventSearchClick(FlightDashboardViewModel dashboardViewModel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(CLICK_SEARCH_EVENT,
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
                )));
    }

    public void eventSearchView(FlightSearchPassDataViewModel passDataViewModel, boolean searchFound) {
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
                passDataViewModel.getFlightPassengerViewModel().getAdult(),
                passDataViewModel.getFlightPassengerViewModel().getChildren(),
                passDataViewModel.getFlightPassengerViewModel().getInfant(),
                passDataViewModel.getFlightClass().getTitle(),
                FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, passDataViewModel.getDepartureDate()),
                passDataViewModel.isOneWay() ? "" : String.format(" - %s", FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, passDataViewModel.getReturnDate()))
        ));
        mapModel.put("from", (passDataViewModel.getDepartureAirport().getAirportCode() == null || passDataViewModel.getDepartureAirport().getAirportCode().isEmpty()) ?
                passDataViewModel.getDepartureAirport().getCityCode() : passDataViewModel.getDepartureAirport().getAirportCode());
        mapModel.put("destination", (passDataViewModel.getArrivalAirport().getAirportCode() == null || passDataViewModel.getArrivalAirport().getAirportCode().isEmpty()) ?
                passDataViewModel.getArrivalAirport().getCityCode() : passDataViewModel.getArrivalAirport().getAirportCode());
        mapModel.put("departureDate", FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, passDataViewModel.getDepartureDate()));
        mapModel.put("returnDateFormatted", passDataViewModel.isOneWay() ? "" : String.format(" - %s", FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, passDataViewModel.getReturnDate())));
        mapModel.put("returnTicket", passDataViewModel.isOneWay()? "false": "true");
        mapModel.put("passenger", passDataViewModel.getFlightPassengerViewModel().getAdult() + passDataViewModel.getFlightPassengerViewModel().getChildren() +
                passDataViewModel.getFlightPassengerViewModel().getInfant());
        mapModel.put("travelWithKids", passDataViewModel.getFlightPassengerViewModel().getChildren() > 0 ||
                passDataViewModel.getFlightPassengerViewModel().getInfant() > 0 ? "true" : "false");
        mapModel.put("class", passDataViewModel.getFlightClass().getTitle());

        if (passDataViewModel.getLinkUrl().contains("tokopedia://pesawat")) {
            mapModel.put("deeplinkUrl", passDataViewModel.getLinkUrl());
            mapModel.put("url", "");
        } else {
            mapModel.put("deeplinkUrl", "");
            mapModel.put("url", passDataViewModel.getLinkUrl());
        }
        mapModel.put("searchFound", searchFound);
        TrackApp.getInstance().getGTM().sendGeneralEvent(mapModel);
    }

    public void eventSearchProductClickFromDetail(FlightSearchPassDataViewModel flightSearchPassData, FlightJourneyViewModel viewModel) {
        StringBuilder result = transformSearchProductClickLabel(viewModel);
        result.append(String.format("%s%s", Label.PRICE_PREFIX, String.valueOf(viewModel.getFare().getAdultNumeric())));
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_PRODUCT,
                result.toString()
        ));
        productClickEnhanceEcommerce(Action.PRODUCT_CLICK_SEARCH_DETAIL, flightSearchPassData, viewModel, result);
    }

    private void productClickEnhanceEcommerce(String action, FlightSearchPassDataViewModel flightSearchPassData, FlightJourneyViewModel viewModel, StringBuilder result) {
        List<Object> products = new ArrayList<>();
        if (flightSearchPassData.getFlightPassengerViewModel().getAdult() > 0) {
            products.add(DataLayer.mapOf(
                    EnhanceEccomerce.ID, viewModel.getId(),
                    EnhanceEccomerce.NAME, viewModel.getDepartureAirportCity() + "-" + viewModel.getArrivalAirportCity() + " - Flight",
                    EnhanceEccomerce.PRICE, String.valueOf(viewModel.getFare().getAdultNumeric()),
                    EnhanceEccomerce.BRAND, viewModel.getRouteList().get(0).getAirlineName(),
                    EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                    EnhanceEccomerce.VARIANT, flightSearchPassData.getFlightClass().getTitle() + " - Adult",
                    EnhanceEccomerce.QUANTITY, String.valueOf(flightSearchPassData.getFlightPassengerViewModel().getAdult()),
                    "list", "/flight"
            ));
        }
        if (flightSearchPassData.getFlightPassengerViewModel().getChildren() > 0) {
            products.add(DataLayer.mapOf(
                    EnhanceEccomerce.ID, viewModel.getId(),
                    EnhanceEccomerce.NAME, viewModel.getDepartureAirportCity() + "-" + viewModel.getArrivalAirportCity() + " - Flight",
                    EnhanceEccomerce.PRICE, String.valueOf(viewModel.getFare().getChildNumeric()),
                    EnhanceEccomerce.BRAND, viewModel.getRouteList().get(0).getAirlineName(),
                    EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                    EnhanceEccomerce.VARIANT, flightSearchPassData.getFlightClass().getTitle() + " - Child",
                    EnhanceEccomerce.QUANTITY, String.valueOf(flightSearchPassData.getFlightPassengerViewModel().getChildren()),
                    "list", "/flight"
            ));
        }
        if (flightSearchPassData.getFlightPassengerViewModel().getInfant() > 0) {
            products.add(DataLayer.mapOf(
                    EnhanceEccomerce.ID, viewModel.getId(),
                    EnhanceEccomerce.NAME, viewModel.getDepartureAirportCity() + "-" + viewModel.getArrivalAirportCity() + " - Flight",
                    EnhanceEccomerce.PRICE, String.valueOf(viewModel.getFare().getInfantNumeric()),
                    EnhanceEccomerce.BRAND, viewModel.getRouteList().get(0).getAirlineName(),
                    EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                    EnhanceEccomerce.VARIANT, flightSearchPassData.getFlightClass().getTitle() + " - Infant",
                    EnhanceEccomerce.QUANTITY, String.valueOf(flightSearchPassData.getFlightPassengerViewModel().getInfant()),
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

    public void eventProductViewNotFound(FlightSearchPassDataViewModel searchPassDataViewModel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(SEARCH_RESULT_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_PRODUCT_NOT_FOUND,
                String.format("%s - %s-%s - %s - %s-%s-%s - %s - %s%s",
                        Label.FLIGHT_SMALL,
                        (searchPassDataViewModel.getDepartureAirport().getAirportCode() == null || searchPassDataViewModel.getDepartureAirport().getAirportCode().isEmpty()) ?
                                searchPassDataViewModel.getDepartureAirport().getCityCode() : searchPassDataViewModel.getDepartureAirport().getAirportCode(),
                        (searchPassDataViewModel.getArrivalAirport().getAirportCode() == null || searchPassDataViewModel.getArrivalAirport().getAirportCode().isEmpty()) ?
                                searchPassDataViewModel.getArrivalAirport().getCityCode() : searchPassDataViewModel.getArrivalAirport().getAirportCode(),
                        searchPassDataViewModel.isOneWay() ? "oneway" : "roundtrip",
                        searchPassDataViewModel.getFlightPassengerViewModel().getAdult(),
                        searchPassDataViewModel.getFlightPassengerViewModel().getChildren(),
                        searchPassDataViewModel.getFlightPassengerViewModel().getInfant(),
                        searchPassDataViewModel.getFlightClass().getTitle(),
                        FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, searchPassDataViewModel.getDepartureDate()),
                        searchPassDataViewModel.isOneWay() ? "" : String.format(" - %s", FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.YYYYMMDD, searchPassDataViewModel.getReturnDate()))
                )


        ));
    }

    public void eventProductViewEnchanceEcommerce(FlightSearchPassDataViewModel searchPassDataViewModel, List<FlightJourneyViewModel> listJourneyViewModel) {

        List<Object> products = new ArrayList<>();
        int position = 0;
        for (FlightJourneyViewModel item : listJourneyViewModel) {
            position++;
            products.add(transformSearchProductView(searchPassDataViewModel, item, position));
        }

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PRODUCT_VIEW_EVENT,
                        EVENT_CATEGORY, Category.DIGITAL_FLIGHT,
                        EVENT_ACTION, Action.PRODUCT_VIEW_ACTION,
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

    public void eventSearchProductClickFromList(FlightSearchPassDataViewModel flightSearchPassData, FlightJourneyViewModel viewModel) {
        List<Object> products = new ArrayList<>();
        products.add(transformSearchProductClick(flightSearchPassData, viewModel, 0));

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PRODUCT_CLICK_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Action.PRODUCT_CLICK_SEARCH_LIST,
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

    public void eventSearchProductClickFromList(FlightSearchPassDataViewModel flightSearchPassData, FlightJourneyViewModel viewModel, int adapterPosition) {
        List<Object> products = new ArrayList<>();
        products.add(transformSearchProductClick(flightSearchPassData, viewModel, adapterPosition));

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PRODUCT_CLICK_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Action.PRODUCT_CLICK_SEARCH_LIST,
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

    private Object transformSearchProductView(FlightSearchPassDataViewModel searchPassDataViewModel, FlightJourneyViewModel journeyViewModel, int position) {
        String isRefundable = "false";
        for (Route route : journeyViewModel.getRouteList()) {
            if (route.getRefundable()) {
                isRefundable = "true";
                break;
            }
        }

        long totalAdultPrice, totalChildPrice, totalInfantPrice;
        if (journeyViewModel.getFare().getAdultNumericCombo() > 0) {
            totalAdultPrice = journeyViewModel.getFare().getAdultNumericCombo() * searchPassDataViewModel.getFlightPassengerViewModel().getAdult();
            totalChildPrice = journeyViewModel.getFare().getChildNumericCombo() * searchPassDataViewModel.getFlightPassengerViewModel().getChildren();
            totalInfantPrice = journeyViewModel.getFare().getInfantNumericCombo() * searchPassDataViewModel.getFlightPassengerViewModel().getInfant();
        } else {
            totalAdultPrice = journeyViewModel.getFare().getAdultNumeric() * searchPassDataViewModel.getFlightPassengerViewModel().getAdult();
            totalChildPrice = journeyViewModel.getFare().getChildNumeric() * searchPassDataViewModel.getFlightPassengerViewModel().getChildren();
            totalInfantPrice = journeyViewModel.getFare().getInfantNumeric() * searchPassDataViewModel.getFlightPassengerViewModel().getInfant();
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
                EnhanceEccomerce.DIMENSION73, searchPassDataViewModel.getFlightPassengerViewModel().getAdult() + " - " +
                        searchPassDataViewModel.getFlightPassengerViewModel().getChildren() + " - " + searchPassDataViewModel.getFlightPassengerViewModel().getInfant(),
                EnhanceEccomerce.ID, journeyViewModel.getId(),
                EnhanceEccomerce.BRAND, journeyViewModel.getRouteList().get(0).getAirlineName(),
                EnhanceEccomerce.DIMENSION74, journeyViewModel.getRouteList().get(0).getAirline() + " - " + journeyViewModel.getRouteList().get(0).getFlightNumber(),
                EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                EnhanceEccomerce.DIMENSION75, journeyViewModel.getDepartureTime(),
                EnhanceEccomerce.DIMENSION76, journeyViewModel.getArrivalTime() + ((journeyViewModel.getAddDayArrival() > 0) ? String.format(" +%s", journeyViewModel.getAddDayArrival()) : ""),
                EnhanceEccomerce.POSITIONS, position,
                EnhanceEccomerce.VARIANT, totalAdultPrice + " - " + totalChildPrice + " - " + totalInfantPrice,
                EnhanceEccomerce.LIST, "/flight"
        );

        return product;
    }

    private Object transformSearchProductClick(FlightSearchPassDataViewModel searchPassDataViewModel, FlightJourneyViewModel journeyViewModel, int position) {
        String isRefundable = "false";
        for (Route route : journeyViewModel.getRouteList()) {
            if (route.getRefundable()) {
                isRefundable = "true";
                break;
            }
        }

        long totalAdultPrice, totalChildPrice, totalInfantPrice;
        if (journeyViewModel.getFare().getAdultNumericCombo() > 0) {
            totalAdultPrice = journeyViewModel.getFare().getAdultNumericCombo() * searchPassDataViewModel.getFlightPassengerViewModel().getAdult();
            totalChildPrice = journeyViewModel.getFare().getChildNumericCombo() * searchPassDataViewModel.getFlightPassengerViewModel().getChildren();
            totalInfantPrice = journeyViewModel.getFare().getInfantNumericCombo() * searchPassDataViewModel.getFlightPassengerViewModel().getInfant();
        } else {
            totalAdultPrice = journeyViewModel.getFare().getAdultNumeric() * searchPassDataViewModel.getFlightPassengerViewModel().getAdult();
            totalChildPrice = journeyViewModel.getFare().getChildNumeric() * searchPassDataViewModel.getFlightPassengerViewModel().getChildren();
            totalInfantPrice = journeyViewModel.getFare().getInfantNumeric() * searchPassDataViewModel.getFlightPassengerViewModel().getInfant();
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
                EnhanceEccomerce.DIMENSION73, searchPassDataViewModel.getFlightPassengerViewModel().getAdult() + " - " +
                        searchPassDataViewModel.getFlightPassengerViewModel().getChildren() + " - " + searchPassDataViewModel.getFlightPassengerViewModel().getInfant(),
                EnhanceEccomerce.ID, journeyViewModel.getId(),
                EnhanceEccomerce.BRAND, journeyViewModel.getRouteList().get(0).getAirlineName(),
                EnhanceEccomerce.DIMENSION74, journeyViewModel.getRouteList().get(0).getAirline() + " - " + journeyViewModel.getRouteList().get(0).getFlightNumber(),
                EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                EnhanceEccomerce.DIMENSION75, journeyViewModel.getDepartureTime(),
                EnhanceEccomerce.DIMENSION76, journeyViewModel.getArrivalTime() + ((journeyViewModel.getAddDayArrival() > 0) ? String.format(" +%s", journeyViewModel.getAddDayArrival()) : ""),
                EnhanceEccomerce.POSITIONS, position,
                EnhanceEccomerce.VARIANT, totalAdultPrice + " - " + totalChildPrice + " - " + totalInfantPrice,
                EnhanceEccomerce.LIST, "/flight"
        );

        return product;
    }

    @NonNull
    private StringBuilder transformSearchProductClickLabel(FlightJourneyViewModel viewModel) {
        StringBuilder result = new StringBuilder();
        if (viewModel != null && viewModel.getAirlineDataList() != null) {
            List<String> airlines = new ArrayList<>();
            for (FlightAirlineViewModel airlineDB : viewModel.getAirlineDataList()) {
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

    public void eventSearchDetailClick(FlightJourneyViewModel viewModel, int adapterPosition) {
        StringBuilder result = transformSearchDetailLabel(viewModel, adapterPosition);
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_DETAIL,
                result.toString()
        ));
    }

    public void eventDetailPriceTabClick(FlightDetailViewModel viewModel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_PRICE_TAB,
                transformEventDetailLabel(viewModel)
        ));
    }

    public void eventDetailFacilitiesTabClick(FlightDetailViewModel viewModel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_FACILITIES_TAB,
                transformEventDetailLabel(viewModel)
        ));
    }

    public void eventDetailTabClick(FlightDetailViewModel viewModel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_DETAIL_TAB,
                transformEventDetailLabel(viewModel)
        ));
    }

    private String transformEventDetailLabel(FlightDetailViewModel viewModel) {
        StringBuilder result = new StringBuilder();
        if (viewModel.getRouteList() != null && viewModel.getRouteList().size() > 0) {
            String airlines = viewModel.getRouteList().get(0).getAirlineName().toLowerCase();
            result.append(airlines);
            String timeResult = String.format(" - %s", String.valueOf(flightDateUtil.getDayDiff(viewModel.getRouteList().get(0).getDepartureTimestamp())));
            timeResult += String.format(" - %s ", String.valueOf(flightDateUtil.getDayDiff(viewModel.getRouteList().get(viewModel.getRouteList().size() - 1).getArrivalTimestamp())));
            result.append(timeResult);
        }
        result.append(transformRefundableLabel(viewModel.getIsRefundable()));
        result.append(String.format(" - %s", String.valueOf(viewModel.getAdultNumericPrice())));
        return result.toString();
    }

    private String transformEventDetailLabel(FlightDetailViewModel departureViewModel, FlightDetailViewModel returnViewModel) {
        StringBuilder result = new StringBuilder();
        if (departureViewModel.getRouteList() != null && departureViewModel.getRouteList().size() > 0) {
            String airlines = departureViewModel.getRouteList().get(0).getAirlineName().toLowerCase();
            if (returnViewModel.getRouteList() != null && returnViewModel.getRouteList().size() > 0) {
                airlines += ", " + returnViewModel.getRouteList().get(0).getAirlineName().toLowerCase();
            }
            result.append(airlines);

            String timeResult = String.format(" - %s, %s",
                    String.valueOf(flightDateUtil.getDayDiff(departureViewModel.getRouteList().get(0).getDepartureTimestamp())),
                    String.valueOf(flightDateUtil.getDayDiff(returnViewModel.getRouteList().get(0).getDepartureTimestamp())));
            timeResult += String.format(" - %s, %s ",
                    String.valueOf(flightDateUtil.getDayDiff(departureViewModel.getRouteList().get(departureViewModel.getRouteList().size() - 1).getArrivalTimestamp())),
                    String.valueOf(flightDateUtil.getDayDiff(returnViewModel.getRouteList().get(returnViewModel.getRouteList().size() - 1).getArrivalTimestamp())));
            result.append(timeResult);
        }
        String refundable = String.format("%s, %s", transformRefundableLabel(departureViewModel.getIsRefundable()), transformRefundableLabel(returnViewModel.getIsRefundable()));
        result.append(refundable);
        String price = String.format(" - %s, %s", String.valueOf(departureViewModel.getAdultNumericPrice()), String.valueOf(returnViewModel.getAdultNumericPrice()));
        result.append(price);
        return result.toString();
    }

    @NonNull
    private String transformAirlines(FlightDetailViewModel viewModel) {
        List<String> airlines = new ArrayList<>();
        for (FlightDetailRouteViewModel airlineDB : viewModel.getRouteList()) {
            if (!airlines.contains(airlineDB.getAirlineName())) {
                airlines.add(airlineDB.getAirlineName());
            }
        }
        return TextUtils.join(",", airlines);
    }

    @NonNull
    private StringBuilder transformSearchDetailLabel(FlightJourneyViewModel viewModel, int adapterPosition) {
        StringBuilder result = new StringBuilder();
        if (viewModel.getAirlineDataList() != null) {
            List<String> airlines = new ArrayList<>();
            for (FlightAirlineViewModel airlineDB : viewModel.getAirlineDataList()) {
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

    public void eventDetailClick(FlightDetailViewModel viewModel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.BOOKING_DETAIL,
                transformEventDetailLabel(viewModel)
        ));
    }

    public void eventBookingNextClick(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.BOOKING_NEXT,
                label
        ));
    }

    public void eventBookingNextClick(FlightBookingCartData flightBookingCartData, FlightSearchPassDataViewModel searchParam, String comboKey) {

        List<Object> products = new ArrayList<>();

        if (flightBookingCartData.getDepartureTrip() != null) {
            products.addAll(constructEnhanceEcommerceProduct(flightBookingCartData.getDepartureTrip(), comboKey,
                    searchParam.getFlightClass().getTitle(), flightBookingCartData.getReturnTrip() == null));
            if (flightBookingCartData.getReturnTrip() != null) {
                products.addAll(constructEnhanceEcommerceProduct(flightBookingCartData.getReturnTrip(), comboKey,
                        searchParam.getFlightClass().getTitle(), false));
            }
        }

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, CHECKOUT_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Category.BOOKING_NEXT,
                        EVENT_LABEL, String.format(Label.PRODUCT_VIEW,
                                flightBookingCartData.getDepartureTrip().getDepartureAirport(),
                                flightBookingCartData.getDepartureTrip().getArrivalAirport()),
                        ECOMMERCE, DataLayer.mapOf(
                                CURRENCY_CODE, DEFAULT_CURRENCY_CODE,
                                "actionField", DataLayer.mapOf("step", 1,
                                        "option", Category.BOOKING_NEXT),
                                "products", DataLayer.listOf(
                                        products.toArray(new Object[products.size()])
                                )
                        )
                )
        );
    }

    public void eventCheckoutClick(FlightDetailViewModel departureTrip, FlightDetailViewModel returnTrip, FlightSearchPassDataViewModel searchParam, String comboKey) {

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
                        EVENT_LABEL, String.format(Label.PRODUCT_VIEW,
                                departureTrip.getDepartureAirport(),
                                departureTrip.getArrivalAirport()),
                        ECOMMERCE, DataLayer.mapOf(
                                CURRENCY_CODE, DEFAULT_CURRENCY_CODE,
                                "actionField", DataLayer.mapOf("step", 1,
                                        "option", Category.BOOKING_NEXT),
                                "products", DataLayer.listOf(
                                        products.toArray(new Object[products.size()])
                                )
                        )
                )
        );
    }

    public void eventReviewNextClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.REVIEW_NEXT,
                Label.REVIEW_NEXT
        ));
    }

    public void eventReviewNextClick(FlightBookingReviewModel flightBookingReviewModel, String comboKey) {

        List<Object> products = new ArrayList<>();

        if (flightBookingReviewModel.getDetailViewModelListDeparture() != null) {
            products.addAll(constructEnhanceEcommerceProduct(flightBookingReviewModel.getDetailViewModelListDeparture(), comboKey,
                    flightBookingReviewModel.getFlightClass().getTitle(), flightBookingReviewModel.getDetailViewModelListReturn() == null));
            if (flightBookingReviewModel.getDetailViewModelListReturn() != null) {
                products.addAll(constructEnhanceEcommerceProduct(flightBookingReviewModel.getDetailViewModelListReturn(), comboKey,
                        flightBookingReviewModel.getFlightClass().getTitle(), false));
            }
        }

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, CHECKOUT_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Category.REVIEW_NEXT,
                        EVENT_LABEL, String.format(Label.PRODUCT_VIEW,
                                flightBookingReviewModel.getDetailViewModelListDeparture().getDepartureAirport(),
                                flightBookingReviewModel.getDetailViewModelListDeparture().getArrivalAirport()),
                        ECOMMERCE, DataLayer.mapOf(
                                CURRENCY_CODE, DEFAULT_CURRENCY_CODE,
                                "actionField", DataLayer.mapOf("step", 2,
                                        "option", Category.REVIEW_NEXT),
                                "products", DataLayer.listOf(
                                        products.toArray(new Object[products.size()])
                                )
                        )
                )
        );
    }

    public void eventVoucherClick(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.VOUCHER,
                label
        ));
    }

    public void eventVoucherSuccess(String label, String message) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.VOUCHER_SUCCESS,
                String.format("%s - %s", label, message)
        ));
    }

    public void eventVoucherErrors(String label, String message) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.VOUCHER_ERROR,
                String.format("%s - %s", label, message)
        ));
    }

    private void eventAddToCart(String label, FlightDetailViewModel viewModel, Object actionField, List<Object> products) {
        try {
            TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                    DataLayer.mapOf(EVENT, ATC_EVENT,
                            EVENT_CATEGORY, GENERIC_CATEGORY,
                            EVENT_ACTION, Category.ADD_TO_CART,
                            EVENT_LABEL, label,
                            ECOMMERCE, DataLayer.mapOf(
                                    "currencyCode", "IDR",
                                    "add", DataLayer.mapOf(
                                            "products", DataLayer.listOf(
                                                    products.toArray(new Object[products.size()])),
                                            "actionField", actionField
                                    )
                            )
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void eventAddToCart(FlightClassViewModel flightClass, FlightBookingCartData cartData,
                               int resultTotalPrice, FlightDetailViewModel departureViewModel, FlightDetailViewModel returnViewModel,
                               String comboKey) {

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

    private List<Object> constructEnhanceEcommerceProduct(FlightDetailViewModel flightViewModel, String comboKey, String flightClass, Boolean isOneWay) {
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
                EnhanceEccomerce.PRICE, String.valueOf(totalPriceAdult + totalPriceChild + totalPriceInfant),
                EnhanceEccomerce.ID, (comboKey != null && !comboKey.isEmpty()) ? comboKey : flightViewModel.getId(),
                EnhanceEccomerce.BRAND, flightViewModel.getRouteList().get(0).getAirlineName(),
                EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                EnhanceEccomerce.QUANTITY, String.valueOf(flightViewModel.getCountAdult() + flightViewModel.getCountChild() + flightViewModel.getCountInfant()),
                EnhanceEccomerce.DIMENSION66, FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.YYYYMMDD, flightViewModel.getRouteList().get(0).getDepartureTimestamp()),
                EnhanceEccomerce.DIMENSION67, isOneWay ? "oneway" : "roundtrip",
                EnhanceEccomerce.DIMENSION68, flightClass,
                EnhanceEccomerce.DIMENSION69, "",
                EnhanceEccomerce.DIMENSION70, (flightViewModel.getIsRefundable() == RefundableEnum.NOT_REFUNDABLE) ? "false" : "true",
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

    public void eventPromoImpression(int position, BannerDetail banner) {
        List<Object> promos = new ArrayList<>();
        promos.add(DataLayer.mapOf(
                EnhanceEccomerce.ID, banner.getId(),
                EnhanceEccomerce.NAME, String.format(getDefaultLocale(), "%s - %s",
                        banner.getAttributes().getPromoCode(), "slider banner"),
                "creative", banner.getAttributes().getDescription().toLowerCase(),
                "creative_url", banner.getAttributes().getLinkUrl(),
                "position", String.valueOf(position + 1)
        ));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, PROMO_VIEW_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Action.PROMOTION_VIEW,
                        EVENT_LABEL, String.format(getDefaultLocale(), "%s - %d - %s",
                                Label.FLIGHT_SMALL,
                                position + 1,
                                banner.getAttributes().getPromoCode()
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

    public void eventProductDetailImpression(FlightJourneyViewModel flightSearchViewModel, int adapterPosition) {
        StringBuilder result = transformSearchDetailLabel(flightSearchViewModel, adapterPosition);
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.PRODUCT_DETAIL_IMPRESSION,
                result.toString()
        ));
    }

    public void eventPurchaseAttemptSuccess() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.PURCHASE_ATTEMPT,
                Label.SUCCESS_PURCHASE
        ));
    }

    public void eventPurchaseAttemptFailed() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.PURCHASE_ATTEMPT,
                Label.FAILED_PURCHASE
        ));
    }

    public void eventPurchaseAttemptCancelled() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.PURCHASE_ATTEMPT,
                Label.CANCEL_PURCHASE
        ));
    }


    public void eventInsuranceChecked(boolean checked, FlightDetailViewModel departure, FlightDetailViewModel returntrip) {
        String eventAction = checked ? Category.ADD_INSURANCE : Category.REMOVE_INSURANCE;
        String eventLabel = "";
        if (departure != null) {
            eventLabel = transformAirlines(departure);
        }
        if (returntrip != null) {
            eventLabel += String.format(",%s", transformAirlines(returntrip));
        }
        if (departure != null) {
            eventLabel += String.format("|%s", departure.getDepartureAirport());
        }
        if (returntrip != null) {
            eventLabel += String.format("|%s", returntrip.getDepartureAirport());
        }


        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GENERIC_EVENT,
                GENERIC_CATEGORY,
                eventAction,
                eventLabel
        ));
    }

    public void eventInsuranceClickMore() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.MORE_INSURANCE_INFO,
                Label.NONE
        ));
    }

    public void eventInsuranceAnotherBenefit() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.MORE_INSURANCE,
                Label.NONE
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

    public static final class Screen {

        public static String FLIGHT_CANCELLATION_STEP_TWO = "Flight Cancellation Reason and Proof";
        public static String HOMEPAGE = "/flight/homepage";
        public static String SEARCH = "/flight/search";
        public static String SEARCH_RETURN = "Search Return";
        public static String REVIEW = "/flight/summary";
        public static String BOOKING = "/flight/booking";
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

    }

    private static class Action {
        static String PROMOTION_VIEW = "banner impression";
        static String PROMOTION_CLICK = "click banner";
        static String PRODUCT_CLICK_SEARCH_LIST = "product click";
        static String PRODUCT_CLICK_SEARCH_DETAIL = "click pilih on flight detail";
        static String PRODUCT_VIEW_ACTION = "product impressions";
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

        static String POSITIONS = "positions";
        static String LIST = "list";
    }
}
