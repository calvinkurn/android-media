package com.tokopedia.people.analytic

import com.tokopedia.people.model.Post
import javax.inject.Inject

class UserFeedPostImpressCoordinator @Inject constructor() {

    private val mDataImpress = mutableListOf<Post>()

    fun initiateDataImpress(
        dataImpress: Post,
        callback: (dataImpress: Post) -> Unit,
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
