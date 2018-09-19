package com.tokopedia.travelcalendar.domain;

import okhttp3.Interceptor;

public interface TravelCalendarRouter {
    Interceptor getChuckInterceptor();
}
