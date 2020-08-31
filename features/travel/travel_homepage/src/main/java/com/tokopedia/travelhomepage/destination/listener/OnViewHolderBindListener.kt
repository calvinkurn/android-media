package com.tokopedia.travelhomepage.destination.listener

/**
 * @author by jessica on 2020-01-03
 */

interface OnViewHolderBindListener {
    fun onCitySummaryLoaded(imgUrls: List<String>, peekSize: Int, cityName: String)
}