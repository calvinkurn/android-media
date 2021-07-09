package com.tokopedia.flight.common.util

import android.os.Bundle
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.presentation.model.TravelVideoBannerModel
import com.tokopedia.flight.detail.view.model.FlightDetailModel
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightHomepageModel
import com.tokopedia.flight.promo_chips.data.model.AirlinePrice
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.getDayDiffFromToday
import com.tokopedia.utils.date.toDate
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

/**
 * @author by furqan on 11/06/2021
 */
class FlightAnalytics @Inject constructor() {

    fun eventOpenScreen(screenName: String) {
        val mapOpenScreen = mapOf(
                FlightAnalyticsKeys.EVENT_NAME to FlightAnalyticsEvents.OPEN_SCREEN_EVENT,
                FlightAnalyticsKeys.CURRENT_SITE to FlightAnalyticsDefaults.FLIGHT_CURRENT_SITE,
                FlightAnalyticsKeys.BUSSINESS_UNIT to FlightAnalyticsDefaults.FLIGHT_BU,
                FlightAnalyticsKeys.CATEGORY to FlightAnalyticsDefaults.FLIGHT_SMALL
        )
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, mapOpenScreen)
    }

    fun eventClickTransactions(screenName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        FlightAnalyticsEvents.GENERIC_EVENT,
                        FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        FlightAnalyticsCategory.CLICK_TRANSACTIONS,
                        screenName
                )
        )
    }

    fun eventPromotionClick(position: Int,
                            banner: TravelCollectiveBannerModel.Banner,
                            screenName: String,
                            userId: String) {
        val promo = Bundle().apply {
            putString(FlightAnalyticsKeys.ID, banner.id)
            putString(FlightAnalyticsKeys.NAME, "${banner.attribute.promoCode} - slider banner")
            putString(FlightAnalyticsKeys.CREATIVE_SLOT, position.toString())
            putString(FlightAnalyticsKeys.CREATIVE_NAME, banner.attribute.description.toLowerCase(Locale.getDefault()))
            putString(FlightAnalyticsKeys.CREATIVE_URL, banner.attribute.appUrl)
        }
        val mapBundle = constructFlightParams(
                eventName = FlightAnalyticsEvents.PROMO_CLICK_EVENT,
                eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                eventAction = FlightAnalyticsAction.PROMOTION_CLICK,
                eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - $position - ${banner.attribute.promoCode}",
                screenName = screenName,
                userId = userId,
                eCommerce = Bundle().apply {
                    putParcelableArrayList(FlightAnalyticsKeys.PROMOTIONS, arrayListOf(promo))
                }
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(FlightAnalyticsEvents.PROMO_CLICK_EVENT, mapBundle)
    }

    fun eventTripTypeClick(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        FlightAnalyticsEvents.GENERIC_EVENT,
                        FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        FlightAnalyticsCategory.CLICK_TRIP_TYPE,
                        label
                )
        )
    }

    fun eventOriginClick(cityName: String, airportId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        FlightAnalyticsEvents.GENERIC_EVENT,
                        FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        FlightAnalyticsCategory.SELECT_ORIGIN,
                        "$cityName|$airportId"
                )
        )
    }

    fun eventDestinationClick(cityName: String, airportId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        FlightAnalyticsEvents.GENERIC_EVENT,
                        FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        FlightAnalyticsCategory.SELECT_DESTINATION,
                        "$cityName|$airportId"
                )
        )
    }

    fun eventClassClick(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        FlightAnalyticsEvents.GENERIC_EVENT,
                        FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        FlightAnalyticsCategory.SELECT_CLASS,
                        label
                )
        )
    }

    fun eventSearchClick(homepageModel: FlightHomepageModel,
                         screenName: String,
                         userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.CLICK_SEARCH_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.CLICK_SEARCH,
                        // flight - departure-arrival - oneway|roundtrip - adult-child-infant - class - departure date (optional : " - return date" if round trip)
                        eventLabel = String.format("%s - %s-%s - %s - %s-%s-%s - %s - %s%s",
                                FlightAnalyticsDefaults.FLIGHT_SMALL,
                                if (homepageModel.departureAirport != null &&
                                        homepageModel.departureAirport?.airportCode?.isEmpty() == true) {
                                    homepageModel.departureAirport?.cityCode
                                } else if (homepageModel.departureAirport != null) {
                                    homepageModel.departureAirport?.airportCode
                                } else {
                                    ""
                                },
                                if (homepageModel.arrivalAirport != null &&
                                        homepageModel.arrivalAirport?.airportCode?.isEmpty() == true) {
                                    homepageModel.arrivalAirport?.cityCode
                                } else if (homepageModel.arrivalAirport != null) {
                                    homepageModel.arrivalAirport?.airportCode
                                } else {
                                    ""
                                },
                                if (homepageModel.isOneWay) "oneway" else "roundtrip",
                                homepageModel.flightPassengerViewModel?.adult ?: 1,
                                homepageModel.flightPassengerViewModel?.children ?: 0,
                                homepageModel.flightPassengerViewModel?.infant ?: 0,
                                homepageModel.flightClass?.title
                                        ?: FlightAnalyticsDefaults.CLASS_EKONOMI,
                                DateUtil.formatDate(DateUtil.YYYY_MM_DD,
                                        DateUtil.YYYYMMDD,
                                        homepageModel.departureDate ?: ""),
                                if (homepageModel.isOneWay) ""
                                else String.format(" - %s", DateUtil.formatDate(
                                        DateUtil.YYYY_MM_DD,
                                        DateUtil.YYYYMMDD,
                                        homepageModel.returnDate ?: ""))
                        ),
                        screenName = screenName,
                        userId = userId
                )
        )
    }

    fun eventQuickFilterClick(filterName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.FLIGHT_CLICK_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.WIDGET_CLICK_FILTER,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - $filterName",
                        screenName = FlightAnalyticsScreenName.SEARCH,
                        userId = userId
                )
        )
    }

    fun eventChangeSearchClick(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.FLIGHT_CLICK_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.CLICK_CHANGE_SEARCH,
                        eventLabel = FlightAnalyticsLabel.FLIGHT_CHANGE_SEARCH,
                        screenName = FlightAnalyticsScreenName.SEARCH,
                        userId = userId
                )
        )
    }

    fun eventSearchView(searchPassModel: FlightSearchPassDataModel,
                        searchFound: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.VIEW_SEARCH_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.VIEW_SEARCH,
                        eventLabel = String.format("%s - %s-%s - %s - %s-%s-%s - %s - %s%s",
                                FlightAnalyticsDefaults.FLIGHT_SMALL,
                                if (searchPassModel.departureAirport.airportCode.isEmpty()) {
                                    searchPassModel.departureAirport.cityCode
                                } else {
                                    searchPassModel.departureAirport.airportCode
                                },
                                if (searchPassModel.arrivalAirport.airportCode.isEmpty()) {
                                    searchPassModel.arrivalAirport.cityCode
                                } else {
                                    searchPassModel.arrivalAirport.airportCode
                                },
                                if (searchPassModel.isOneWay) "oneway" else "roundtrip",
                                searchPassModel.flightPassengerModel.adult,
                                searchPassModel.flightPassengerModel.children,
                                searchPassModel.flightPassengerModel.infant,
                                searchPassModel.flightClass.title,
                                DateUtil.formatDate(DateUtil.YYYY_MM_DD,
                                        DateUtil.YYYYMMDD, searchPassModel.departureDate),
                                if (searchPassModel.isOneWay) "" else String.format(" - %s",
                                        DateUtil.formatDate(DateUtil.YYYY_MM_DD,
                                                DateUtil.YYYYMMDD, searchPassModel.returnDate))
                        ),
                        customMap = hashMapOf(
                                FlightAnalyticsKeys.FROM to if (searchPassModel.departureAirport.airportCode.isEmpty()) {
                                    searchPassModel.departureAirport.cityCode
                                } else {
                                    searchPassModel.departureAirport.airportCode
                                },
                                FlightAnalyticsKeys.DESTINATION to if (searchPassModel.arrivalAirport.airportCode.isEmpty()) {
                                    searchPassModel.arrivalAirport.cityCode
                                } else {
                                    searchPassModel.arrivalAirport.airportCode
                                },
                                FlightAnalyticsKeys.DEPARTURE_DATE to DateUtil.formatDate(
                                        DateUtil.YYYY_MM_DD,
                                        DateUtil.YYYYMMDD,
                                        searchPassModel.departureDate),
                                FlightAnalyticsKeys.DEPARTURE_DATE_FORMATTED to searchPassModel.departureDate,
                                FlightAnalyticsKeys.RETURN_DATE to if (searchPassModel.isOneWay) ""
                                else DateUtil.formatDate(
                                        DateUtil.YYYY_MM_DD,
                                        DateUtil.YYYYMMDD,
                                        searchPassModel.returnDate),
                                FlightAnalyticsKeys.RETURN_DATE_FORMATTED to if (searchPassModel.isOneWay) ""
                                else searchPassModel.returnDate,
                                FlightAnalyticsKeys.RETURN_TICKET to if (searchPassModel.isOneWay) "false" else "true",
                                FlightAnalyticsKeys.PASSENGER to "${searchPassModel.flightPassengerModel.adult}${searchPassModel.flightPassengerModel.children}${searchPassModel.flightPassengerModel.infant}",
                                FlightAnalyticsKeys.TRAVEL_WITH_KIDS to if (searchPassModel.flightPassengerModel.children > 0 || searchPassModel.flightPassengerModel.infant > 0) "true" else "false",
                                FlightAnalyticsKeys.CLASS to searchPassModel.flightClass.title,
                                FlightAnalyticsKeys.SEARCH_FOUND to if (searchFound) "true" else "false"
                        ).also {
                            if (searchPassModel.linkUrl.contains("tokopedia://pesawat") ||
                                    searchPassModel.linkUrl.contains("tokopedia-android-internal://pesawat")) {
                                it[FlightAnalyticsKeys.DEEPLINK_URL] = searchPassModel.linkUrl
                                it[FlightAnalyticsKeys.URL] = ""
                            } else {
                                it[FlightAnalyticsKeys.DEEPLINK_URL] = ""
                                it[FlightAnalyticsKeys.URL] = searchPassModel.linkUrl
                            }
                        },
                        screenName = null,
                        userId = null
                )
        )
    }

    fun eventSearchProductClickFromDetail(searchPassData: FlightSearchPassDataModel, journeyModel: FlightJourneyModel) {
        val label = transformSearchProductClickLabel(journeyModel)
        label.append(" - ${journeyModel.fare.adultNumeric}")
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        FlightAnalyticsEvents.GENERIC_EVENT,
                        FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        FlightAnalyticsAction.CLICK_SEARCH_PRODUCT,
                        label.toString()
                )
        )
        productClickEnhanceEcommmerce(
                searchPassData,
                journeyModel,
                label
        )
    }

    fun eventProductViewNotFound(searchPassData: FlightSearchPassDataModel,
                                 userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.SEARCH_RESULT_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.CLICK_SEARCH_PRODUCT_NOT_FOUND,
                        eventLabel = String.format("%s - %s-%s - %s - %s-%s-%s - %s - %s%s",
                                FlightAnalyticsDefaults.FLIGHT_SMALL,
                                if (searchPassData.departureAirport.airportCode.isEmpty()) searchPassData.departureAirport.cityCode else searchPassData.departureAirport.airportCode,
                                if (searchPassData.arrivalAirport.airportCode.isEmpty()) searchPassData.arrivalAirport.cityCode else searchPassData.arrivalAirport.airportCode,
                                if (searchPassData.isOneWay) "oneway" else "roundtrip",
                                searchPassData.flightPassengerModel.adult,
                                searchPassData.flightPassengerModel.children,
                                searchPassData.flightPassengerModel.infant,
                                searchPassData.flightClass.title,
                                DateUtil.formatDate(DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD, searchPassData.departureDate),
                                if (searchPassData.isOneWay) "" else String.format(" - %s", DateUtil.formatDate(
                                        DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD, searchPassData.returnDate))
                        ),
                        screenName = FlightAnalyticsScreenName.SEARCH,
                        userId = userId
                )
        )
    }

    fun eventProductViewV2EnhanceEcommerce(searchPassData: FlightSearchPassDataModel,
                                           journeyModelList: List<FlightJourneyModel>,
                                           screenName: String,
                                           userId: String) {

        val products = arrayListOf<Bundle>()
        for ((index, item) in journeyModelList.withIndex()) {
            products.add(transformSearchProductViewV2(searchPassData, item, index + 1))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                FlightAnalyticsEvents.PRODUCT_VIEW_EVENT,
                constructFlightParams(
                        eventName = FlightAnalyticsEvents.PRODUCT_VIEW_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.PRODUCT_VIEW_ACTION_V2,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - " +
                                "${if (searchPassData.departureAirport.airportCode.isEmpty()) searchPassData.departureAirport.cityCode else searchPassData.departureAirport.airportCode}-" +
                                (if (searchPassData.arrivalAirport.airportCode.isEmpty()) searchPassData.arrivalAirport.cityCode else searchPassData.arrivalAirport.airportCode),
                        screenName = screenName,
                        userId = userId,
                        eCommerce = Bundle().apply {
                            putParcelableArrayList(FlightAnalyticsKeys.ITEMS, products)
                            putString(FlightAnalyticsKeys.LIST, "/flight")
                        }
                )
        )
    }

    fun eventSearchProductClickV2FromList(searchPassData: FlightSearchPassDataModel,
                                          journeyModel: FlightJourneyModel?,
                                          screenName: String,
                                          userId: String) {
        journeyModel?.let {
            val product = transformSearchProductClickV2(searchPassData, journeyModel, 0)

            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    FlightAnalyticsEvents.PRODUCT_CLICK_EVENT,
                    constructFlightParams(
                            eventName = FlightAnalyticsEvents.PRODUCT_CLICK_EVENT,
                            eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                            eventAction = FlightAnalyticsAction.PRODUCT_CLICK_SEARCH_LIST,
                            eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - ${journeyModel.departureAirport}-${journeyModel.arrivalAirport}",
                            screenName = screenName,
                            userId = userId,
                            eCommerce = Bundle().apply {
                                putParcelableArrayList(FlightAnalyticsKeys.ITEMS, arrayListOf(product))
                                putString(FlightAnalyticsKeys.LIST, "/flight")
                            }
                    )
            )
        }
    }

    fun eventSearchProductClickV2FromList(searchPassData: FlightSearchPassDataModel,
                                          journeyModel: FlightJourneyModel?,
                                          adapterPosition: Int,
                                          screenName: String,
                                          userId: String) {
        journeyModel?.let {
            val product = transformSearchProductClickV2(searchPassData, journeyModel, adapterPosition)

            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    FlightAnalyticsEvents.PRODUCT_CLICK_EVENT,
                    constructFlightParams(
                            eventName = FlightAnalyticsEvents.PRODUCT_CLICK_EVENT,
                            eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                            eventAction = FlightAnalyticsAction.PRODUCT_CLICK_SEARCH_LIST,
                            eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - ${journeyModel.departureAirport}-${journeyModel.arrivalAirport}",
                            screenName = screenName,
                            userId = userId,
                            eCommerce = Bundle().apply {
                                putParcelableArrayList(FlightAnalyticsKeys.ITEMS, arrayListOf(product))
                                putString(FlightAnalyticsKeys.LIST, "/flight")
                            }
                    )
            )
        }
    }

    fun eventSearchDetailClick(journeyModel: FlightJourneyModel, adapterPosition: Int) {
        val label = transformSearchDetailLabel(journeyModel, adapterPosition)
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        FlightAnalyticsEvents.GENERIC_EVENT,
                        FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        FlightAnalyticsAction.CLICK_SEARCH_DETAIL,
                        label.toString()
                )
        )
    }

    fun eventDetailPriceTabClick(detailModel: FlightDetailModel) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        FlightAnalyticsEvents.GENERIC_EVENT,
                        FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        FlightAnalyticsAction.CLICK_PRICE_TAB,
                        transformEventDetailLabel(detailModel)
                )
        )
    }

    fun eventDetailFacilitiesTabClick(detailModel: FlightDetailModel) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        FlightAnalyticsEvents.GENERIC_EVENT,
                        FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        FlightAnalyticsAction.CLICK_FACILITIES_TAB,
                        transformEventDetailLabel(detailModel)
                )
        )
    }

    fun eventDetailTabClick(detailModel: FlightDetailModel) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        FlightAnalyticsEvents.GENERIC_EVENT,
                        FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        FlightAnalyticsAction.CLICK_DETAIL_TAB,
                        transformEventDetailLabel(detailModel)
                )
        )
    }

    fun eventCheckoutClick(departureTrip: FlightDetailModel?,
                           returnTrip: FlightDetailModel?,
                           searchPassData: FlightSearchPassDataModel,
                           comboKey: String,
                           userId: String) {
        val products = arrayListOf<Bundle>()

        departureTrip?.let { departure ->
            products.addAll(constructEnhanceEcommerceProduct(
                    departure,
                    comboKey,
                    searchPassData.flightClass.title,
                    returnTrip == null
            ))
            returnTrip?.let {
                products.addAll(constructEnhanceEcommerceProduct(
                        it,
                        comboKey,
                        searchPassData.flightClass.title,
                        false
                ))
            }
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                FlightAnalyticsEvents.CHECKOUT_EVENT,
                constructFlightParams(
                        eventName = FlightAnalyticsEvents.CHECKOUT_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.BOOKING_NEXT,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - ${departureTrip?.departureAirport ?: ""}-${departureTrip?.arrivalAirport ?: ""}",
                        screenName = FlightAnalyticsScreenName.BOOKING,
                        userId = userId,
                        eCommerce = Bundle().apply {
                            putParcelableArrayList(FlightAnalyticsKeys.ITEMS, products)
                            putString(FlightAnalyticsKeys.CHECKOUT_STEP, "1")
                            putString(FlightAnalyticsKeys.CHECKOUT_OPTION, FlightAnalyticsAction.BOOKING_NEXT)
                        }
                )
        )
    }

    fun eventPassengerClick(adult: Int, children: Int, infant: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        FlightAnalyticsEvents.GENERIC_EVENT,
                        FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        FlightAnalyticsAction.SELECT_PASSENGER,
                        "$adult ${FlightAnalyticsLabel.ADULT} - $children ${FlightAnalyticsLabel.CHILD} - $infant ${FlightAnalyticsLabel.INFANT}"
                )
        )
    }

    fun eventAddToCart(flightClass: FlightClassModel,
                       departureModel: FlightDetailModel?,
                       returnModel: FlightDetailModel?,
                       comboKey: String,
                       userId: String) {
        val products = arrayListOf<Bundle>()

        departureModel?.let { departure ->
            products.addAll(constructEnhanceEcommerceProduct(
                    departure,
                    comboKey,
                    flightClass.title,
                    returnModel == null))
            returnModel?.let {
                products.addAll(constructEnhanceEcommerceProduct(
                        it,
                        comboKey,
                        flightClass.title,
                        false
                ))
            }
        }

        try {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    FlightAnalyticsEvents.ATC_EVENT,
                    constructFlightParams(
                            eventName = FlightAnalyticsEvents.ATC_EVENT,
                            eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                            eventAction = FlightAnalyticsAction.ADD_TO_CART,
                            eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - ${departureModel?.departureAirport ?: ""}-${departureModel?.arrivalAirport ?: ""}",
                            screenName = FlightAnalyticsScreenName.BOOKING,
                            userId = userId,
                            eCommerce = Bundle().apply {
                                putParcelableArrayList(FlightAnalyticsKeys.ITEMS, products)
                            }
                    )
            )
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun eventPromoImpression(
            position: Int,
            banner: TravelCollectiveBannerModel.Banner,
            screenName: String,
            userId: String) {

        val promo = Bundle().apply {
            putString(FlightAnalyticsKeys.ID, banner.id)
            putString(FlightAnalyticsKeys.NAME, "${banner.attribute.promoCode} - slider banner")
            putString(FlightAnalyticsKeys.CREATIVE_NAME, banner.attribute.description.toLowerCase(Locale.getDefault()))
            putString(FlightAnalyticsKeys.CREATIVE_URL, banner.attribute.appUrl)
            putString(FlightAnalyticsKeys.CREATIVE_SLOT, (position + 1).toString())
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                FlightAnalyticsEvents.PROMO_VIEW_EVENT,
                constructFlightParams(
                        eventName = FlightAnalyticsEvents.PROMO_VIEW_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.PROMOTION_VIEW,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - ${position + 1} - ${banner.attribute.promoCode}",
                        screenName = screenName,
                        userId = userId,
                        eCommerce = Bundle().apply {
                            putParcelableArrayList(FlightAnalyticsKeys.PROMOTIONS, arrayListOf(promo))
                        }
                )
        )
    }

    fun eventProductDetailImpression(journeyModel: FlightJourneyModel,
                                     adapterPosition: Int) {
        val label = transformSearchDetailLabel(journeyModel, adapterPosition)
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        FlightAnalyticsEvents.GENERIC_EVENT,
                        FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        FlightAnalyticsAction.PRODUCT_DETAIL_IMPRESSION,
                        label.toString()
                )
        )
    }

    fun eventBranchCheckoutFlight(
            productName: String,
            journeyId: String,
            invoiceId: String,
            paymentId: String,
            userId: String,
            totalPrice: String) {
        LinkerManager.getInstance().sendEvent(
                LinkerUtils.createGenericRequest(
                        LinkerConstants.EVENT_PURCHASE_FLIGHT,
                        createLinkerData(
                                productName,
                                journeyId,
                                invoiceId,
                                paymentId,
                                userId,
                                totalPrice
                        )
                )
        )
    }

    fun openOrderDetail(eventLabel: String,
                        userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.SEARCH_RESULT_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.VIEW_ORDER_DETAIL,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - $eventLabel",
                        screenName = FlightAnalyticsScreenName.ORDER_DETAIL,
                        userId = userId
                )
        )
    }

    fun eventSendETicketOrderDetail(eventLabel: String,
                                    userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.FLIGHT_CLICK_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.CLICK_SEND_ETICKET,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - $eventLabel",
                        screenName = FlightAnalyticsScreenName.ORDER_DETAIL,
                        userId = userId
                )
        )
    }

    fun eventDownloadETicketOrderDetail(eventLabel: String,
                                        userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.FLIGHT_CLICK_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.CLICK_DOWNLOAD_ETICKET,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - $eventLabel",
                        screenName = FlightAnalyticsScreenName.ORDER_DETAIL,
                        userId = userId
                )
        )
    }

    fun eventWebCheckInOrderDetail(eventLabel: String,
                                   userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.FLIGHT_CLICK_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.CLICK_WEB_CHECKIN,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - $eventLabel",
                        screenName = FlightAnalyticsScreenName.ORDER_DETAIL,
                        userId = userId
                )
        )
    }

    fun eventCancelTicketOrderDetail(eventLabel: String,
                                     userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.FLIGHT_CLICK_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.CLICK_CANCEL_TICKET,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - $eventLabel",
                        screenName = FlightAnalyticsScreenName.ORDER_DETAIL,
                        userId = userId
                )
        )
    }

    fun eventClickOnWebCheckIn(eventLabel: String,
                               userId: String,
                               isDeparture: Boolean) {
        val action = if (isDeparture) FlightAnalyticsAction.CLICK_CHECKIN_DEPARTURE else FlightAnalyticsAction.CLICK_CHECKIN_RETURN
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.FLIGHT_CLICK_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = action,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - $eventLabel",
                        screenName = FlightAnalyticsScreenName.WEB_CHECKIN,
                        userId = userId
                )
        )
    }

    fun eventClickNextOnCancellationPassenger(eventLabel: String,
                                              userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.FLIGHT_CLICK_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.CLICK_NEXT_CANCELLATION_PASSENGER,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - $eventLabel",
                        screenName = FlightAnalyticsScreenName.CANCELLATION_PASSENGER,
                        userId = userId
                )
        )
    }

    fun eventClickNextOnCancellationReason(eventLabel: String,
                                           userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.FLIGHT_CLICK_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.CLICK_NEXT_CANCELLATION_REASON,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - $eventLabel",
                        screenName = FlightAnalyticsScreenName.CANCELLATION_REASON,
                        userId = userId
                )
        )
    }

    fun eventClickNextOnCancellationSubmit(eventLabel: String,
                                           userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                constructFlightMap(
                        eventName = FlightAnalyticsEvents.FLIGHT_CLICK_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.CLICK_SUBMIT_CANCELLATION,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - $eventLabel",
                        screenName = FlightAnalyticsScreenName.CANCELLATION_SUMMARY,
                        userId = userId
                )
        )
    }

    fun eventFlightPromotionClick(position: Int,
                                  airlinePrice: AirlinePrice,
                                  searchPassData: FlightSearchPassDataModel,
                                  screenName: String,
                                  userId: String,
                                  isReturn: Boolean) {
        val action = if (isReturn) FlightAnalyticsAction.CLICK_RETURN_PROMOTION_CHIPS else FlightAnalyticsAction.CLICK_DEPARTURE_PROMOTION_CHIPS
        val promo = Bundle().apply {
            putString(FlightAnalyticsKeys.ID, airlinePrice.airlineID)
            putString(FlightAnalyticsKeys.NAME, airlinePrice.shortName)
            putString(FlightAnalyticsKeys.CREATIVE_SLOT, position.toString())
            putString(FlightAnalyticsKeys.CREATIVE_NAME, airlinePrice.shortName)
            putString(FlightAnalyticsKeys.CREATIVE_URL, airlinePrice.logo)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                FlightAnalyticsEvents.PROMO_CLICK_EVENT,
                constructFlightParams(
                        eventName = FlightAnalyticsEvents.PROMO_CLICK_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = action,
                        eventLabel = "${airlinePrice.airlineID} - ${searchPassData.getDepartureAirport(isReturn)} - ${searchPassData.getArrivalAirport(isReturn)} - ${airlinePrice.priceNumeric}",
                        screenName = screenName,
                        userId = userId,
                        eCommerce = Bundle().apply {
                            putParcelableArrayList(FlightAnalyticsKeys.PROMOTIONS, arrayListOf(promo))
                        }
                )
        )
    }

    fun eventVideoBannerImpression(videoBannerModel: TravelVideoBannerModel,
                                   screenName: String,
                                   userId: String) {
        val promo = Bundle().apply {
            putString(FlightAnalyticsKeys.ID, videoBannerModel.id)
            putString(FlightAnalyticsKeys.NAME, "/flight")
            putString(FlightAnalyticsKeys.CREATIVE_NAME, videoBannerModel.title)
            putString(FlightAnalyticsKeys.CREATIVE_SLOT, 0.toString())
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                FlightAnalyticsEvents.PROMO_VIEW_EVENT,
                constructFlightParams(
                        eventName = FlightAnalyticsEvents.PROMO_VIEW_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.VIDEO_BANNER_VIEW,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - ${FlightAnalyticsLabel.FLIGHT_TRAVEL_VIDEO_BANNER}",
                        screenName = screenName,
                        userId = userId,
                        eCommerce = Bundle().apply {
                            putParcelableArrayList(FlightAnalyticsKeys.PROMOTIONS, arrayListOf(promo))
                        }
                )
        )
    }

    fun eventVideoBannerClick(videoBannerModel: TravelVideoBannerModel,
                              screenName: String,
                              userId: String) {
        val promo = Bundle().apply {
            putString(FlightAnalyticsKeys.ID, videoBannerModel.id)
            putString(FlightAnalyticsKeys.NAME, "/flight")
            putString(FlightAnalyticsKeys.CREATIVE_NAME, videoBannerModel.title)
            putString(FlightAnalyticsKeys.CREATIVE_SLOT, 0.toString())
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                FlightAnalyticsEvents.PROMO_CLICK_EVENT,
                constructFlightParams(
                        eventName = FlightAnalyticsEvents.PROMO_CLICK_EVENT,
                        eventCategory = FlightAnalyticsDefaults.GENERIC_CATEGORY,
                        eventAction = FlightAnalyticsAction.VIDEO_BANNER_CLICK,
                        eventLabel = "${FlightAnalyticsDefaults.FLIGHT_SMALL} - ${FlightAnalyticsLabel.FLIGHT_TRAVEL_VIDEO_BANNER}",
                        screenName = screenName,
                        userId = userId,
                        eCommerce = Bundle().apply {
                            putParcelableArrayList(FlightAnalyticsKeys.PROMOTIONS, arrayListOf(promo))
                        }
                )
        )
    }

    private fun createLinkerData(
            productName: String,
            journeyId: String,
            invoiceId: String,
            paymentId: String,
            userId: String,
            totalPrice: String
    ): LinkerData =
            LinkerData().apply {
                productCategory = FlightAnalyticsDefaults.FLIGHT_SMALL
                this.productName = productName
                this.journeyId = journeyId
                this.userId = userId
                this.invoiceId = invoiceId
                this.paymentId = paymentId
                this.price = totalPrice
            }


    private fun constructFlightParams(eventName: String?,
                                      eventCategory: String?,
                                      eventAction: String?,
                                      eventLabel: String?,
                                      screenName: String?,
                                      userId: String?,
                                      eCommerce: Bundle? = null,
                                      sendCurrentSite: Boolean = true,
                                      sendClientId: Boolean = true,
                                      sendBusinessUnit: Boolean = true,
                                      sendCategory: Boolean = true): Bundle =
            Bundle().apply {
                eventName?.let { putString(FlightAnalyticsKeys.EVENT, it) }
                eventCategory?.let { putString(FlightAnalyticsKeys.EVENT_CATEGORY, it) }
                eventAction?.let { putString(FlightAnalyticsKeys.EVENT_ACTION, it) }
                eventLabel?.let { putString(FlightAnalyticsKeys.EVENT_LABEL, it) }
                screenName?.let { putString(FlightAnalyticsKeys.SCREEN_NAME, it) }
                userId?.let { putString(FlightAnalyticsKeys.USER_ID, it) }
                eCommerce?.let { putAll(it) }
                if (sendCurrentSite) putString(FlightAnalyticsKeys.CURRENT_SITE, FlightAnalyticsDefaults.FLIGHT_CURRENT_SITE)
                if (sendClientId) putString(FlightAnalyticsKeys.CLIENT_ID, TrackApp.getInstance().gtm.clientIDString)
                if (sendBusinessUnit) putString(FlightAnalyticsKeys.BUSSINESS_UNIT, FlightAnalyticsDefaults.FLIGHT_BU)
                if (sendCategory) putString(FlightAnalyticsKeys.CATEGORY, FlightAnalyticsDefaults.FLIGHT_SMALL)

            }

    private fun constructFlightMap(eventName: String?,
                                   eventCategory: String?,
                                   eventAction: String?,
                                   eventLabel: String?,
                                   screenName: String?,
                                   userId: String?,
                                   customMap: Map<String, Any>? = null,
                                   sendCurrentSite: Boolean = true,
                                   sendClientId: Boolean = true,
                                   sendBusinessUnit: Boolean = true,
                                   sendCategory: Boolean = true): Map<String, Any> =
            HashMap<String, Any>().apply {
                eventName?.let { put(FlightAnalyticsKeys.EVENT, it) }
                eventCategory?.let { put(FlightAnalyticsKeys.EVENT_CATEGORY, it) }
                eventAction?.let { put(FlightAnalyticsKeys.EVENT_ACTION, it) }
                eventLabel?.let { put(FlightAnalyticsKeys.EVENT_LABEL, it) }
                screenName?.let { put(FlightAnalyticsKeys.SCREEN_NAME, it) }
                userId?.let { put(FlightAnalyticsKeys.USER_ID, it) }
                if (sendCurrentSite) put(FlightAnalyticsKeys.CURRENT_SITE, FlightAnalyticsDefaults.FLIGHT_CURRENT_SITE)
                if (sendClientId) put(FlightAnalyticsKeys.CLIENT_ID, TrackApp.getInstance().gtm.clientIDString)
                if (sendBusinessUnit) put(FlightAnalyticsKeys.BUSSINESS_UNIT, FlightAnalyticsDefaults.FLIGHT_BU)
                if (sendCategory) put(FlightAnalyticsKeys.CATEGORY, FlightAnalyticsDefaults.FLIGHT_SMALL)

                customMap?.let {
                    putAll(it)
                }
            }

    private fun constructEnhanceEcommerceProduct(
            detailModel: FlightDetailModel,
            comboKey: String,
            flightClass: String,
            isOneWay: Boolean): List<Bundle> {

        val name = "${detailModel.departureAirportCity}-${detailModel.arrivalAirportCity}"

        val totalPriceAdult = detailModel.adultNumericPrice * detailModel.countAdult
        val totalPriceChild = detailModel.childNumericPrice * detailModel.countChild
        val totalPriceInfant = detailModel.infantNumericPrice * detailModel.countInfant

        val layoverDayDiff: Long = DateUtil.getDayDiff(
                DateUtil.formatDate(
                        DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                        DateUtil.YYYY_MM_DD,
                        detailModel.routeList[0].departureTimestamp
                ),
                DateUtil.formatDate(
                        DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                        DateUtil.YYYY_MM_DD,
                        detailModel.routeList[detailModel.routeList.size - 1].arrivalTimestamp
                )
        )
        val dayDiffString = if (layoverDayDiff > 0) " +$layoverDayDiff" else ""

        return arrayListOf(
                Bundle().apply {
                    putString(FlightAnalyticsKeys.NAME, name)
                    putLong(FlightAnalyticsKeys.PRICE, (totalPriceAdult + totalPriceChild + totalPriceInfant).toLong())
                    putString(FlightAnalyticsKeys.ID, if (comboKey.isNotEmpty()) comboKey else detailModel.id)
                    putString(FlightAnalyticsKeys.BRAND, detailModel.routeList[0].airlineName)
                    putString(FlightAnalyticsKeys.ITEM_CATEGORY, FlightAnalyticsDefaults.FLIGHT_FIRST_CAPITAL)
                    putInt(FlightAnalyticsKeys.QUANTITY, detailModel.countAdult + detailModel.countChild + detailModel.countInfant)
                    putString(FlightAnalyticsKeys.DIMENSION66, DateUtil.formatDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, DateUtil.YYYYMMDD, detailModel.routeList[0].departureTimestamp))
                    putString(FlightAnalyticsKeys.DIMENSION67, if (isOneWay) "oneway" else "roundtrip")
                    putString(FlightAnalyticsKeys.DIMENSION68, flightClass)
                    putString(FlightAnalyticsKeys.DIMENSION69, "")
                    putString(FlightAnalyticsKeys.DIMENSION70, if (detailModel.isRefundable == RefundableEnum.NOT_REFUNDABLE) "false" else "true")
                    putString(FlightAnalyticsKeys.DIMENSION71, if (detailModel.totalTransit > 0) "true" else "false")
                    putString(FlightAnalyticsKeys.DIMENSION72, if (detailModel.beforeTotal == "") "normal" else "strike")
                    putString(FlightAnalyticsKeys.DIMENSION73, "${detailModel.countAdult} - ${detailModel.countChild} - ${detailModel.countInfant}")
                    putString(FlightAnalyticsKeys.DIMENSION74, "${detailModel.routeList[0].airlineCode} - ${detailModel.routeList[0].flightNumber}")
                    putString(FlightAnalyticsKeys.DIMENSION75, detailModel.departureTime)
                    putString(FlightAnalyticsKeys.DIMENSION76, "${detailModel.arrivalTime}$dayDiffString")
                    putString(FlightAnalyticsKeys.VARIANT, "$totalPriceAdult - $totalPriceChild - $totalPriceInfant")
                }
        )
    }

    private fun productClickEnhanceEcommmerce(searchPassData: FlightSearchPassDataModel,
                                              journeyModel: FlightJourneyModel,
                                              label: StringBuilder) {
        val products = arrayListOf<Bundle>()
        if (searchPassData.flightPassengerModel.adult > 0) {
            products.add(
                    Bundle().apply {
                        putString(FlightAnalyticsKeys.ID, journeyModel.id)
                        putString(FlightAnalyticsKeys.NAME, "${journeyModel.departureAirportCity}-${journeyModel.arrivalAirportCity} - ${FlightAnalyticsDefaults.FLIGHT_FIRST_CAPITAL}")
                        putString(FlightAnalyticsKeys.PRICE, journeyModel.fare.adultNumeric.toString())
                        putString(FlightAnalyticsKeys.BRAND, journeyModel.routeList[0].airlineName)
                        putString(FlightAnalyticsKeys.ITEM_CATEGORY, FlightAnalyticsDefaults.FLIGHT_FIRST_CAPITAL)
                        putString(FlightAnalyticsKeys.VARIANT, "${searchPassData.flightClass.title} - Adult")
                        putString(FlightAnalyticsKeys.QUANTITY, searchPassData.flightPassengerModel.adult.toString())
                        putString("list", "/flight")
                    }
            )
        }

        if (searchPassData.flightPassengerModel.children > 0) {
            products.add(
                    Bundle().apply {
                        putString(FlightAnalyticsKeys.ID, journeyModel.id)
                        putString(FlightAnalyticsKeys.NAME, "${journeyModel.departureAirportCity}-${journeyModel.arrivalAirportCity} - ${FlightAnalyticsDefaults.FLIGHT_FIRST_CAPITAL}")
                        putString(FlightAnalyticsKeys.PRICE, journeyModel.fare.childNumeric.toString())
                        putString(FlightAnalyticsKeys.BRAND, journeyModel.routeList[0].airlineName)
                        putString(FlightAnalyticsKeys.ITEM_CATEGORY, FlightAnalyticsDefaults.FLIGHT_FIRST_CAPITAL)
                        putString(FlightAnalyticsKeys.VARIANT, "${searchPassData.flightClass.title} - Child")
                        putString(FlightAnalyticsKeys.QUANTITY, searchPassData.flightPassengerModel.children.toString())
                        putString("list", "/flight")
                    }
            )
        }

        if (searchPassData.flightPassengerModel.infant > 0) {
            products.add(
                    Bundle().apply {
                        putString(FlightAnalyticsKeys.ID, journeyModel.id)
                        putString(FlightAnalyticsKeys.NAME, "${journeyModel.departureAirportCity}-${journeyModel.arrivalAirportCity} - ${FlightAnalyticsDefaults.FLIGHT_FIRST_CAPITAL}")
                        putString(FlightAnalyticsKeys.PRICE, journeyModel.fare.infantNumeric.toString())
                        putString(FlightAnalyticsKeys.BRAND, journeyModel.routeList[0].airlineName)
                        putString(FlightAnalyticsKeys.ITEM_CATEGORY, FlightAnalyticsDefaults.FLIGHT_FIRST_CAPITAL)
                        putString(FlightAnalyticsKeys.VARIANT, "${searchPassData.flightClass.title} - Infant")
                        putString(FlightAnalyticsKeys.QUANTITY, searchPassData.flightPassengerModel.infant.toString())
                        putString("list", "/flight")
                    }
            )
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                FlightAnalyticsEvents.PRODUCT_CLICK_EVENT,
                Bundle().apply {
                    putString(FlightAnalyticsKeys.EVENT, FlightAnalyticsEvents.PRODUCT_CLICK_EVENT)
                    putString(FlightAnalyticsKeys.EVENT_CATEGORY, FlightAnalyticsDefaults.GENERIC_CATEGORY)
                    putString(FlightAnalyticsKeys.EVENT_ACTION, FlightAnalyticsAction.PRODUCT_CLICK_SEARCH_DETAIL)
                    putString(FlightAnalyticsKeys.EVENT_LABEL, label.toString())
                    putBundle(FlightAnalyticsKeys.ECOMMERCE, Bundle().apply {
                        putParcelableArrayList(FlightAnalyticsKeys.PRODUCTS, products)
                        putBundle(FlightAnalyticsKeys.ACTION_FIELD, Bundle().apply {
                            putString("list", "/flight")
                        })
                    })
                    putString(FlightAnalyticsKeys.LIST, "/flight")
                }
        )
    }

    private fun transformSearchDetailLabel(journeyModel: FlightJourneyModel, adapterPosition: Int): StringBuilder {
        val label = StringBuilder()

        val airlines = arrayListOf<String>()
        for (airline in journeyModel.airlineDataList) {
            airlines.add(airline.id)
        }
        label.append(airlines.joinToString(separator = ","))

        if (journeyModel.routeList.isNotEmpty()) {
            val timeResult = "${journeyModel.routeList[0].departureTimestamp.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z).getDayDiffFromToday()} - " +
                    "${journeyModel.routeList[journeyModel.routeList.size - 1].arrivalTimestamp.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z).getDayDiffFromToday()}"
            label.append(" - $timeResult")
        }

        label.append(" - ${transformRefundableLabel(journeyModel.isRefundable)}")
        label.append(" - $adapterPosition")
        label.append(" - ${journeyModel.fare.adultNumeric}")

        return label
    }

    private fun transformRefundableLabel(refundable: RefundableEnum): String =
            when (refundable) {
                RefundableEnum.REFUNDABLE -> {
                    FlightAnalyticsLabel.REFUNDABLE
                }
                RefundableEnum.PARTIAL_REFUNDABLE -> {
                    FlightAnalyticsLabel.PARTIALLY_REFUNDABLE
                }
                else -> {
                    FlightAnalyticsLabel.NOT_REFUNDABLE
                }
            }

    private fun transformSearchProductClickLabel(journeyModel: FlightJourneyModel): StringBuilder {
        val result = StringBuilder()

        val airlines = arrayListOf<String>()
        for (airline in journeyModel.airlineDataList) {
            airlines.add(airline.shortName.toLowerCase(Locale.getDefault()))
        }
        result.append(airlines.joinToString(separator = ","))

        if (journeyModel.routeList.isNotEmpty()) {
            val timeResult = "${journeyModel.routeList[0].departureTimestamp.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z).getDayDiffFromToday()} - " +
                    "${journeyModel.routeList[journeyModel.routeList.size - 1].arrivalTimestamp.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z).getDayDiffFromToday()}"
            result.append(" - $timeResult")
        }

        return result
    }

    private fun transformSearchProductViewV2(searchPassDataViewModel: FlightSearchPassDataModel,
                                             journeyModel: FlightJourneyModel,
                                             position: Int)
            : Bundle {
        var isRefundable = "false"
        for (route in journeyModel.routeList) {
            if (route.isRefundable) {
                isRefundable = "true"
                break
            }
        }

        val totalAdultPrice: Long
        val totalChildPrice: Long
        val totalInfantPrice: Long
        if (journeyModel.fare.adultNumericCombo > 0) {
            totalAdultPrice = (journeyModel.fare.adultNumericCombo * searchPassDataViewModel.flightPassengerModel.adult).toLong()
            totalChildPrice = (journeyModel.fare.childNumericCombo * searchPassDataViewModel.flightPassengerModel.children).toLong()
            totalInfantPrice = (journeyModel.fare.infantNumericCombo * searchPassDataViewModel.flightPassengerModel.infant).toLong()
        } else {
            totalAdultPrice = (journeyModel.fare.adultNumeric * searchPassDataViewModel.flightPassengerModel.adult).toLong()
            totalChildPrice = (journeyModel.fare.childNumeric * searchPassDataViewModel.flightPassengerModel.children).toLong()
            totalInfantPrice = (journeyModel.fare.infantNumeric * searchPassDataViewModel.flightPassengerModel.infant).toLong()
        }

        val dayArrival = if (journeyModel.addDayArrival > 0)
            "+${journeyModel.addDayArrival}"
        else ""

        return Bundle().apply {
            putString(FlightAnalyticsKeys.NAME, "${journeyModel.departureAirportCity}-${journeyModel.arrivalAirportCity}")
            putString(FlightAnalyticsKeys.ID, journeyModel.id)
            putLong(FlightAnalyticsKeys.PRICE, totalAdultPrice + totalChildPrice + totalInfantPrice)
            putString(FlightAnalyticsKeys.BRAND, journeyModel.routeList[0].airlineName)
            putString(FlightAnalyticsKeys.ITEM_CATEGORY, FlightAnalyticsDefaults.FLIGHT_FIRST_CAPITAL)
            putString(FlightAnalyticsKeys.VARIANT, "$totalAdultPrice - $totalChildPrice - $totalInfantPrice")
            putInt(FlightAnalyticsKeys.INDEX, position)
            putInt(FlightAnalyticsKeys.POSITIONS, position)
            putString(FlightAnalyticsKeys.DIMENSION66, DateUtil.formatDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, DateUtil.YYYYMMDD, journeyModel.routeList[0].departureTimestamp))
            putString(FlightAnalyticsKeys.DIMENSION67, if (searchPassDataViewModel.isOneWay) "oneway" else "roundtrip")
            putString(FlightAnalyticsKeys.DIMENSION68, searchPassDataViewModel.flightClass.title.toLowerCase(Locale.getDefault()))
            putString(FlightAnalyticsKeys.DIMENSION69, "")
            putString(FlightAnalyticsKeys.DIMENSION70, isRefundable)
            putString(FlightAnalyticsKeys.DIMENSION71, if (journeyModel.totalTransit > 0) "true" else "false")
            putString(FlightAnalyticsKeys.DIMENSION72, if (journeyModel.beforeTotal == "") "normal" else "strike")
            putString(FlightAnalyticsKeys.DIMENSION73, "${searchPassDataViewModel.flightPassengerModel.adult} - " +
                    "${searchPassDataViewModel.flightPassengerModel.children} - " +
                    "${searchPassDataViewModel.flightPassengerModel.infant}")
            putString(FlightAnalyticsKeys.DIMENSION74, "${journeyModel.routeList[0].airline} - ${journeyModel.routeList[0].flightNumber}")
            putString(FlightAnalyticsKeys.DIMENSION75, journeyModel.departureTime)
            putString(FlightAnalyticsKeys.DIMENSION76, "${journeyModel.arrivalTime}$dayArrival")
            putString(FlightAnalyticsKeys.DIMENSION107, "${journeyModel.isSeatDistancing}|${journeyModel.hasFreeRapidTest}")
            putString(FlightAnalyticsKeys.DIMENSION40, "/flight")
        }
    }

    private fun transformSearchProductClickV2(searchPassData: FlightSearchPassDataModel,
                                              journeyModel: FlightJourneyModel,
                                              position: Int): Bundle {
        var isRefundable = "false"
        for (route in journeyModel.routeList) {
            if (route.isRefundable) {
                isRefundable = "true"
                break
            }
        }

        val totalAdultPrice: Long
        val totalChildPrice: Long
        val totalInfantPrice: Long
        if (journeyModel.fare.adultNumericCombo > 0) {
            totalAdultPrice = (journeyModel.fare.adultNumericCombo * searchPassData.flightPassengerModel.adult).toLong()
            totalChildPrice = (journeyModel.fare.childNumericCombo * searchPassData.flightPassengerModel.children).toLong()
            totalInfantPrice = (journeyModel.fare.infantNumericCombo * searchPassData.flightPassengerModel.infant).toLong()
        } else {
            totalAdultPrice = (journeyModel.fare.adultNumeric * searchPassData.flightPassengerModel.adult).toLong()
            totalChildPrice = (journeyModel.fare.childNumeric * searchPassData.flightPassengerModel.children).toLong()
            totalInfantPrice = (journeyModel.fare.infantNumeric * searchPassData.flightPassengerModel.infant).toLong()
        }

        val addDayInfo = if (journeyModel.addDayArrival > 0) " +${journeyModel.addDayArrival}" else ""

        return Bundle().apply {
            putString(FlightAnalyticsKeys.NAME, "${journeyModel.departureAirportCity}-${journeyModel.arrivalAirportCity}")
            putString(FlightAnalyticsKeys.ID, journeyModel.id)
            putLong(FlightAnalyticsKeys.PRICE, totalAdultPrice + totalChildPrice + totalInfantPrice)
            putString(FlightAnalyticsKeys.BRAND, journeyModel.routeList[0].airlineName)
            putString(FlightAnalyticsKeys.ITEM_CATEGORY, FlightAnalyticsDefaults.FLIGHT_FIRST_CAPITAL)
            putString(FlightAnalyticsKeys.VARIANT, "$totalAdultPrice - $totalChildPrice - $totalInfantPrice")
            putInt(FlightAnalyticsKeys.INDEX, position)
            putInt(FlightAnalyticsKeys.POSITIONS, position)
            putString(FlightAnalyticsKeys.DIMENSION66, DateUtil.formatDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, DateUtil.YYYYMMDD, journeyModel.routeList[0].departureTimestamp))
            putString(FlightAnalyticsKeys.DIMENSION67, if (searchPassData.isOneWay) "oneway" else "roundtrip")
            putString(FlightAnalyticsKeys.DIMENSION68, searchPassData.flightClass.title.toLowerCase(Locale.getDefault()))
            putString(FlightAnalyticsKeys.DIMENSION69, "")
            putString(FlightAnalyticsKeys.DIMENSION70, isRefundable)
            putString(FlightAnalyticsKeys.DIMENSION71, if (journeyModel.totalTransit > 0) "true" else "false")
            putString(FlightAnalyticsKeys.DIMENSION72, if (journeyModel.beforeTotal == "") "normal" else "strike")
            putString(FlightAnalyticsKeys.DIMENSION73, "${searchPassData.flightPassengerModel.adult} - ${searchPassData.flightPassengerModel.children} - ${searchPassData.flightPassengerModel.infant}")
            putString(FlightAnalyticsKeys.DIMENSION74, "${journeyModel.routeList[0].airline} - ${journeyModel.routeList[0].flightNumber}")
            putString(FlightAnalyticsKeys.DIMENSION75, journeyModel.departureTime)
            putString(FlightAnalyticsKeys.DIMENSION76, "${journeyModel.arrivalTime}$addDayInfo")
            putString(FlightAnalyticsKeys.DIMENSION107, "${journeyModel.isSeatDistancing}|${journeyModel.hasFreeRapidTest}")
            putString(FlightAnalyticsKeys.DIMENSION40, "/flight")
        }
    }

    private fun transformEventDetailLabel(detailModel: FlightDetailModel): String {
        val stringBuilder = StringBuilder()

        if (detailModel.routeList.isNotEmpty()) {
            val airlines = detailModel.routeList[0].airlineName.toLowerCase(Locale.getDefault())
            stringBuilder.append(airlines)
            val timeResult = "${detailModel.routeList[0].departureTimestamp.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z).getDayDiffFromToday()} - " +
                    "${detailModel.routeList[detailModel.routeList.size - 1].arrivalTimestamp.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z).getDayDiffFromToday()}"
            stringBuilder.append(" - $timeResult")
        }
        stringBuilder.append(" - ${transformRefundableLabel(detailModel.isRefundable)}")
        stringBuilder.append(" - ${detailModel.adultNumericPrice}")

        return stringBuilder.toString()
    }

}

object FlightAnalyticsDefaults {
    const val GENERIC_CATEGORY = "digital - flight"
    const val FLIGHT_CURRENT_SITE = "tokopediadigitalflight"
    const val FLIGHT_BU = "travel & entertainment"
    const val FLIGHT_FIRST_CAPITAL = "Flight"
    const val FLIGHT_SMALL = "flight"
    const val CLASS_EKONOMI = "Ekonomi"
}

object FlightAnalyticsKeys {
    const val EVENT_CATEGORY = "eventCategory"
    const val EVENT_ACTION = "eventAction"
    const val EVENT_LABEL = "eventLabel"
    const val EVENT = "event"
    const val ECOMMERCE = "ecommerce"

    const val EVENT_NAME = "eventName"
    const val SCREEN_NAME = "screenName"
    const val CLIENT_ID = "clientId"
    const val CURRENT_SITE = "currentSite"
    const val BUSSINESS_UNIT = "businessUnit"
    const val CATEGORY = "category"
    const val USER_ID = "userId"

    const val CREATIVE_SLOT = "creative_slot"
    const val CREATIVE_NAME = "creative_name"
    const val CREATIVE_URL = "creative_url"

    const val ID = "item_id"
    const val NAME = "item_name"
    const val LIST = "item_list"
    const val PROMOTIONS = "promotions"
    const val PRICE = "price"
    const val BRAND = "item_brand"
    const val VARIANT: String = "item_variant"
    const val QUANTITY: String = "quantity"
    const val PRODUCTS = "products"
    const val ITEMS = "items"
    const val ACTION_FIELD = "actionField"
    const val INDEX = "index"
    const val POSITIONS = "positions"
    const val ITEM_CATEGORY = "item_category"

    const val DIMENSION40 = "dimension40"
    const val DIMENSION66 = "dimension66"
    const val DIMENSION67 = "dimension67"
    const val DIMENSION68 = "dimension68"
    const val DIMENSION69 = "dimension69"
    const val DIMENSION70 = "dimension70"
    const val DIMENSION71 = "dimension71"
    const val DIMENSION72 = "dimension72"
    const val DIMENSION73 = "dimension73"
    const val DIMENSION74 = "dimension74"
    const val DIMENSION75 = "dimension75"
    const val DIMENSION76 = "dimension76"
    const val DIMENSION107 = "dimension107"

    const val FROM = "from"
    const val DESTINATION = "destination"
    const val DEPARTURE_DATE = "departureDate"
    const val DEPARTURE_DATE_FORMATTED = "departureDateFormatted"
    const val RETURN_DATE = "returnDate"
    const val RETURN_DATE_FORMATTED = "returnDateFormatted"
    const val RETURN_TICKET = "returnTicket"
    const val PASSENGER = "passenger"
    const val TRAVEL_WITH_KIDS = "travelWithKids"
    const val CLASS = "class"
    const val DEEPLINK_URL = "deeplinkUrl"
    const val URL = "url"
    const val SEARCH_FOUND = "searchFound"

    const val CHECKOUT_OPTION = "checkout_option"
    const val CHECKOUT_STEP = "checkout_step"
}

object FlightAnalyticsEvents {
    const val GENERIC_EVENT = "genericFlightEvent"
    const val ATC_EVENT = "add_to_cart"
    const val CHECKOUT_EVENT = "begin_checkout"
    const val PROMO_VIEW_EVENT = "view_item"
    const val PROMO_CLICK_EVENT = "select_content"
    const val FLIGHT_CLICK_EVENT = "clickFlight"
    const val PRODUCT_CLICK_EVENT = "select_content"
    const val PRODUCT_VIEW_EVENT = "view_item_list"
    const val SEARCH_RESULT_EVENT = "viewFlightIris"
    const val VIEW_SEARCH_EVENT = "viewSearchPage"
    const val CLICK_SEARCH_EVENT = "clickSearch"
    const val OPEN_SCREEN_EVENT = "openScreen"
}

object FlightAnalyticsCategory {
    const val CLICK_TRANSACTIONS = "click transaction list"
    const val SELECT_DESTINATION = "select destination"
    const val CLICK_TRIP_TYPE = "select trip type"
    const val SELECT_ORIGIN = "select origin"
    const val SELECT_CLASS = "select flight class"
}

object FlightAnalyticsLabel {
    const val FLIGHT_CHANGE_SEARCH = "flight - change search"
    const val REFUNDABLE = "refundable"
    const val NOT_REFUNDABLE = "not refundable"
    const val PARTIALLY_REFUNDABLE = "partially refundable"
    const val ADULT = "adult"
    const val CHILD = "child"
    const val INFANT = "baby"
    const val FLIGHT_TRAVEL_VIDEO_BANNER = "travel video"
}

object FlightAnalyticsAction {
    const val PROMOTION_CLICK = "click banner"
    const val PROMOTION_VIEW = "banner impression"
    const val CLICK_SEARCH = "click search flight"
    const val WIDGET_CLICK_FILTER = "click widget filter"
    const val CLICK_CHANGE_SEARCH = "click change search"
    const val VIEW_SEARCH = "view search result flight"
    const val CLICK_SEARCH_PRODUCT = "product click"
    const val PRODUCT_CLICK_SEARCH_DETAIL = "click pilih on flight detail"
    const val CLICK_SEARCH_PRODUCT_NOT_FOUND = "product not found"
    const val PRODUCT_VIEW_ACTION_V2 = "product impressions v2"
    const val PRODUCT_CLICK_SEARCH_LIST = "product click v2"
    const val PRODUCT_DETAIL_IMPRESSION = "product detail impression"
    const val CLICK_SEARCH_DETAIL = "click see the details"
    const val CLICK_PRICE_TAB = "click price tab"
    const val CLICK_FACILITIES_TAB = "click facilities tab"
    const val CLICK_DETAIL_TAB = "click flights detail tab"
    const val BOOKING_NEXT = "click next on summary page"
    const val SELECT_PASSENGER = "select Passenger"
    const val ADD_TO_CART = "add to cart"
    const val VIEW_ORDER_DETAIL = "view order detail"
    const val CLICK_SEND_ETICKET = "click send eticket"
    const val CLICK_DOWNLOAD_ETICKET = "click download eticket"
    const val CLICK_WEB_CHECKIN = "click web checkin"
    const val CLICK_CANCEL_TICKET = "click cancel ticket"
    const val CLICK_CHECKIN_DEPARTURE = "click checkin on depart route"
    const val CLICK_CHECKIN_RETURN = "click checkin on return route"
    const val CLICK_NEXT_CANCELLATION_PASSENGER = "click next on cancellation passenger"
    const val CLICK_NEXT_CANCELLATION_REASON = "click next on cancellation reason"
    const val CLICK_SUBMIT_CANCELLATION = "click submit cancellation"
    const val CLICK_DEPARTURE_PROMOTION_CHIPS = "click on departure promo chip"
    const val CLICK_RETURN_PROMOTION_CHIPS = "click on return promo chip"
    const val VIDEO_BANNER_VIEW = "view travel video"
    const val VIDEO_BANNER_CLICK = "click travel video"
}

object FlightAnalyticsScreenName {
    const val FLIGHT_CANCELLATION_STEP_TWO = "Flight Cancellation Reason and Proof"
    const val HOMEPAGE = "/flight/homepage"
    const val SEARCH_RETURN = "Search Return"
    const val SEARCH = "/flight/search"
    const val BOOKING = "/flight/booking"
    const val ORDER_DETAIL = "/flight/orderdetail"
    const val WEB_CHECKIN = "/flight/webcheckindetail"
    const val CANCELLATION_PASSENGER = "/flight/cancellationpassenger"
    const val CANCELLATION_REASON = "/flight/cancellationreason"
    const val CANCELLATION_SUMMARY = "/flight/cancellationsummary"
}