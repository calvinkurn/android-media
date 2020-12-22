package com.tkpd.library.utils.legacy;

import com.tokopedia.track.TrackApp;

/**
 * Created by Yoris Prayogo on 22/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
public class SessionAnalytics {

    private static final String EVENT = "clickLogout";
    private static final String EVENT_CATEGORY = "force logout";
    private static final String EVENT_ACTION = "refresh token";

    public static void trackRefreshTokenSuccess(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT,
                EVENT_CATEGORY,
                EVENT_ACTION,
                "refresh token success"
        );
    }

    public static void trackRefreshTokenFailed(String errorMsg){
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT,
                EVENT_CATEGORY,
                EVENT_ACTION,
                "refresh token failed - " + errorMsg
        );
    }
}
