package com.tokopedia.people.analytic

import com.tokopedia.people.model.PlayPostContentItem
import javax.inject.Inject

class UserVideoPostImpressCoordinator @Inject constructor() {

    private val mDataImpress = mutableListOf<PlayPostContentItem>()

    fun initiateDataImpress(
        dataImpress: PlayPostContentItem,
        callback: (dataImpress: PlayPostContentItem) -> Unit,
    ) {
        val findShopRecom = mDataImpress.filter { it.id == dataImpress.id }
        if (!findShopRecom.isNullOrEmpty()) return
        callback.invoke(dataImpress)
        mDataImpress.add(dataImpress)
    }

    fun sendTracker(callback: () -> Unit) {
        callback.invoke()
        if (!mDataImpress.isNullOrEmpty()) mDataImpress.clear()
    }
}
