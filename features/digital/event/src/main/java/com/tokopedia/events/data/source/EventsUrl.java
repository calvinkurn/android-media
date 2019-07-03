package com.tokopedia.events.data.source;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by ashwanityagi on 02/11/17.
 */

public class EventsUrl {

    public static String EVENTS_DOMAIN = TokopediaUrl.Companion.getInstance().getBOOKING();

    public static final String EVENTS_LIST = "v1/api/h/event";
    public static final String EVENTS_LIST_SEARCH = "v1/api/s/event";
    public static final String EVENTS_LOCATION_LIST = "v1/api/location";
    public static final String EVENTS_LIST_BY_LOCATION = "v1/api/h/event/{location}";
    public static final String EVENTS_VERIFY = "/v1/api/expresscart/verify";
    public static final String EVENTS_CHECKOUT = "/v1/api/expresscart/checkout?is_native=true";
    public static final String EVENT_DETAIL = "v1/api/p/";
    public static final String EVENT_VALIDATE = "/v1/api/event/validate-selection";
    public static final String EVENTS_LIKES = "/v1/api/rating";
    public static final String EVENT_PRODUCT_RATING = "v1/api/rating/product/{id}";
    public static final String EVENT_INIT_COUPON = "/v1/api/expresscart/init";
    public static final String EVENT_GET_USER_LIKES = "/v1/api/rating/user";
    public static final String EVENT_SEAT_LAYOUT = "/v1/api/seat-layout/category/{category_id}/product/{product_id}/schedule/{schedule_id}/group/{group_id}/package/{package_id}";

    public static final String EVENT_SCAN_TICKET_URL="v1/api/redeem/role/validate";

    public interface AppLink {

        String Events = "events";
        String EVENTS_DETAILS = "tokopedia://events/{event}";
        String EVENTS = "tokopedia://events";
        String EVENTS_HIBURAN = "tokopedia://hiburan";
        String EXTRA_FROM_PUSH = "from_notif";
    }
}
