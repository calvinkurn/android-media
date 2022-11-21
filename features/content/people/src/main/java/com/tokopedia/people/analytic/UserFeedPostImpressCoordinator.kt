package com.tokopedia.people.analytic

import com.tokopedia.people.views.uimodel.content.PostUiModel
import javax.inject.Inject

class UserFeedPostImpressCoordinator @Inject constructor() {

    private val mDataImpress = mutableListOf<PostUiModel>()

    fun initiateDataImpress(
        dataImpress: PostUiModel,
        callback: (dataImpress: PostUiModel) -> Unit,
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
