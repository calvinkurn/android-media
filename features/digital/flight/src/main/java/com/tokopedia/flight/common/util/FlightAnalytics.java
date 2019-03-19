package com.tokopedia.flight.common.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum;
import com.tokopedia.flight.search.presentation.model.FlightAirlineViewModel;
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

import javax.inject.Inject;

/**
 * @author by alvarisi on 1/19/18.
 */

public class FlightAnalytics {
    private FlightDateUtil flightDateUtil;
    private String GENERIC_EVENT = "genericFlightEvent";
    private String ATC_EVENT = "addToCart";
    private String PROMO_CLICK_EVENT = "promoClick";
    private String PRODUCT_CLICK_EVENT = "productClick";
    private String GENERIC_CATEGORY = "digital - flight";
    private String EVENT_CATEGORY = "eventCategory";
    private String EVENT_ACTION = "eventAction";
    private String EVENT_LABEL = "eventLabel";
    private String EVENT = "event";
    private String ECOMMERCE = "ecommerce";

    @Inject
    public FlightAnalytics(FlightDateUtil flightDateUtil) {
        this.flightDateUtil = flightDateUtil;
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
                EnhanceEccomerce.NAME, "/flight",
                "position", String.valueOf(position),
                "creative", banner.getAttributes().getDescription().toLowerCase(),
                "promo_id", banner.getId(),
                "promo_code", banner.getAttributes().getPromoCode().toLowerCase()
        ));
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf(EVENT, PROMO_CLICK_EVENT,
                        EVENT_CATEGORY, GENERIC_CATEGORY,
                        EVENT_ACTION, Action.PROMOTION_CLICK,
                        EVENT_LABEL, String.format(getDefaultLocale(), "%d-%s-%s",
                                position,
                                banner.getAttributes().getPromoCode(),
                                banner.getAttributes().getLinkUrl()
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

    public void eventSearchClick(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH,
                label
        ));
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

    public void eventSearchProductClickFromList(FlightSearchPassDataViewModel flightSearchPassData, FlightJourneyViewModel viewModel) {
        StringBuilder result = transformSearchProductClickLabel(viewModel);
        result.append(String.format("%s%d", Label.PRICE_PREFIX, viewModel.getFare().getAdultNumeric()));
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_PRODUCT,
                result.toString()
        ));
        productClickEnhanceEcommerce(Action.PRODUCT_CLICK_SEARCH_LIST, flightSearchPassData, viewModel, result);
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

        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
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

    public void eventSearchProductClickFromList(FlightSearchPassDataViewModel flightSearchPassData, FlightJourneyViewModel viewModel, int adapterPosition) {
        StringBuilder result = transformSearchProductClickLabel(viewModel);
        result.append(String.format(getDefaultLocale(), " - %d", adapterPosition));
        result.append(String.format("%s%s", Label.PRICE_PREFIX, String.valueOf(viewModel.getFare().getAdultNumeric())));
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_PRODUCT,
                result.toString()
        ));
        productClickEnhanceEcommerce(Action.PRODUCT_CLICK_SEARCH_LIST, flightSearchPassData, viewModel, result);
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

    public void eventReviewNextClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.REVIEW_NEXT,
                Label.REVIEW_NEXT
        ));
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
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
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

    public void eventAddToCart(FlightClassViewModel flightClass, FlightBookingCartData cartData, int resultTotalPrice, FlightDetailViewModel departureViewModel, FlightDetailViewModel returnViewModel) {
        String coupon = "";
        Object actionField = DataLayer.mapOf(
                EnhanceEccomerce.ID, cartData.getId(),
                EnhanceEccomerce.AFFILIATION, "Online Store",
                EnhanceEccomerce.REVENUE, String.valueOf(resultTotalPrice),
                EnhanceEccomerce.TAX, "0",
                EnhanceEccomerce.SHIPING, "0",
                EnhanceEccomerce.COUPON, coupon
        );

        List<Object> products = new ArrayList<>();

        String label = "";
        if (departureViewModel != null) {
            products = constructEnhanceEcommerceProduct(departureViewModel, cartData.getId(), coupon, flightClass.getTitle());
            if (returnViewModel != null) {
                products.add(constructEnhanceEcommerceProduct(returnViewModel, cartData.getId(), coupon, flightClass.getTitle()));
                label = transformEventDetailLabel(departureViewModel, returnViewModel);
            } else {
                label = transformEventDetailLabel(departureViewModel);
            }
        }
        eventAddToCart(label, returnViewModel, actionField, products);
    }

    private List<Object> constructEnhanceEcommerceProduct(FlightDetailViewModel departureViewModel, String cartId, String coupon, String flightClass) {
        List<Object> products = new ArrayList<>();
        String name = departureViewModel.getDepartureAirportCity() + "-" + departureViewModel.getArrivalAirportCity() + " - Flights";
        products.add(DataLayer.mapOf(
                EnhanceEccomerce.NAME, name,
                EnhanceEccomerce.ID, cartId,
                EnhanceEccomerce.PRICE, String.valueOf(departureViewModel.getAdultNumericPrice()),
                EnhanceEccomerce.BRAND, departureViewModel.getRouteList().get(0).getAirlineName(),
                EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                EnhanceEccomerce.VARIANT, flightClass + " - Adult",
                EnhanceEccomerce.QUANTITY, String.valueOf(departureViewModel.getCountAdult()),
                EnhanceEccomerce.COUPON, coupon
        ));
        if (departureViewModel.getCountChild() > 0) {
            products.add(DataLayer.mapOf(
                    EnhanceEccomerce.NAME, name,
                    EnhanceEccomerce.ID, cartId,
                    EnhanceEccomerce.PRICE, String.valueOf(departureViewModel.getChildNumericPrice()),
                    EnhanceEccomerce.BRAND, departureViewModel.getRouteList().get(0).getAirlineName(),
                    EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                    EnhanceEccomerce.VARIANT, flightClass + " - Child",
                    EnhanceEccomerce.QUANTITY, String.valueOf(departureViewModel.getCountChild()),
                    EnhanceEccomerce.COUPON, coupon
            ));
        }
        if (departureViewModel.getCountInfant() > 0) {
            products.add(DataLayer.mapOf(
                    EnhanceEccomerce.NAME, name,
                    EnhanceEccomerce.ID, cartId,
                    EnhanceEccomerce.PRICE, String.valueOf(departureViewModel.getInfantNumericPrice()),
                    EnhanceEccomerce.BRAND, departureViewModel.getRouteList().get(0).getAirlineName(),
                    EnhanceEccomerce.CATEGORY, Label.FLIGHT,
                    EnhanceEccomerce.VARIANT, flightClass + " - Infant",
                    EnhanceEccomerce.QUANTITY, String.valueOf(departureViewModel.getCountInfant()),
                    EnhanceEccomerce.COUPON, coupon
            ));
        }
        return products;
    }

    public void eventPromoImpression(int position, BannerDetail bannerData) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.PROMOTION_IMPRESSION,
                String.format(getDefaultLocale(),
                        "%d - %s - %s",
                        position + 1,
                        bannerData.getAttributes().getDescription(),
                        bannerData.getAttributes().getLinkUrl()
                )
        ));
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


    public static final class Screen {

        public static String FLIGHT_CANCELLATION_STEP_TWO = "Flight Cancellation Reason and Proof";
        public static String HOMEPAGE = "Homepage";
        public static String SEARCH = "Search";
        public static String SEARCH_RETURN = "Search Return";
        public static String REVIEW = "Review";
        public static String BOOKING = "Booking";
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
        static String CLICK_SEARCH_PRODUCT = "product click";
        static String CLICK_SEARCH_DETAIL = "click see the details";
        static String PRODUCT_DETAIL_IMPRESSION = "product detail impression";
        static String CLICK_PRICE_TAB = "click price tab";
        static String CLICK_FACILITIES_TAB = "click facilities tab";
        static String CLICK_DETAIL_TAB = "click flights detail tab";
        static String ADD_TO_CART = "add to cart";
        static String BOOKING_DETAIL = "click detail";
        static String BOOKING_NEXT = "click next";
        static String REVIEW_NEXT = "click next";
        static String VOUCHER = "click gunakan voucher code";
        static String VOUCHER_SUCCESS = "voucher success";
        static String VOUCHER_ERROR = "voucher error";
        static String PURCHASE_ATTEMPT = "purchase attempt";
        static String ADD_INSURANCE = "add insurance";
        static String REMOVE_INSURANCE = "remove insurance";
        static String MORE_INSURANCE_INFO = "click more insurance information";
        static String MORE_INSURANCE = "see another insurance benefit";

    }

    private static class Action {
        static String PROMOTION_CLICK = "promotion click";
        static String PRODUCT_CLICK_SEARCH_LIST = "product click from flight list";
        static String PRODUCT_CLICK_SEARCH_DETAIL = "click pilih on flight detail";
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
    }
}
