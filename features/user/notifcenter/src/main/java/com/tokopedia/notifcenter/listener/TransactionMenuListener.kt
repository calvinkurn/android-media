package com.tokopedia.notifcenter.listener

interface TransactionMenuListener {
    fun sendTrackingData(parent: String, child: String)
}