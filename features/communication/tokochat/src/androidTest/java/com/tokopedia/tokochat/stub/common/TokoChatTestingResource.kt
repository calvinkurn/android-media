package com.tokopedia.tokochat.stub.common

import androidx.test.espresso.idling.CountingIdlingResource

object TokoChatTestingResource {
    /**
     * Dummy value
     */
    const val USER_ID_DUMMY = "835a69de-577e-4881-bf1d-4e3eed13c643"
    const val GOJEK_ORDER_ID_DUMMY = "F-68720537282"
    const val TKPD_ORDER_ID_DUMMY = "52af8a53-86cc-40b7-bb98-cc3adde8e32a"
    const val CHANNEL_ID_DUMMY = "b0c80252-c6a6-40f1-a3ce-a9894a32ac6d"

    /**
     * Idling Resource
     */
    val idlingResourceDatabaseMessage = CountingIdlingResource(
        "tokochat-database-message"
    )
    val idlingResourceDatabaseChannel = CountingIdlingResource(
        "tokochat-database-channel"
    )
    val idlingResourcePrepareDb = CountingIdlingResource(
        "tokochat-prepare-db"
    )
}
