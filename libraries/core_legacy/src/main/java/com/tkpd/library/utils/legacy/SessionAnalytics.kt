package com.tkpd.library.utils.legacy

import com.tokopedia.track.TrackApp

/**
 * Created by Yoris Prayogo on 21/12/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SessionAnalytics {
    companion object {
        const val EVENT = "clickLogout"
        const val EVENT_CATEGORY = "force logout"
        const val EVENT_ACTION = "refresh token"

        fun trackRefreshTokenSuccess(){
            TrackApp.getInstance().getGTM().sendGeneralEvent(
                    EVENT,
                    EVENT_CATEGORY,
                    EVENT_ACTION,
                    "refresh token success"
            )
        }

        fun trackRefreshTokenFailed(errorMsg: String){
            TrackApp.getInstance().getGTM().sendGeneralEvent(
                    EVENT,
                    EVENT_CATEGORY,
                    EVENT_ACTION,
                    "refresh token failed - $errorMsg"
            )
        }
    }
}