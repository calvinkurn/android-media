package com.tokopedia.analytics.byteio

interface IAppLogActivity {
    fun getPageName(): String
}

interface IAppLogPdpActivity : IAppLogActivity {
    fun getProductTrack(): TrackStayProductDetail
    fun isExiting(): Boolean
}
