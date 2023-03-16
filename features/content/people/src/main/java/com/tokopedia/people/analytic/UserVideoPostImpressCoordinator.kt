package com.tokopedia.people.analytic

import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import javax.inject.Inject

class UserVideoPostImpressCoordinator @Inject constructor() {

    private val mDataImpress = mutableListOf<PlayWidgetChannelUiModel>()

    fun initiateDataImpress(
        dataImpress: PlayWidgetChannelUiModel,
        callback: (dataImpress: PlayWidgetChannelUiModel) -> Unit,
    ) {
        val findShopRecom = mDataImpress.filter { it.channelId == dataImpress.channelId }
        if (!findShopRecom.isNullOrEmpty()) return
        callback.invoke(dataImpress)
        mDataImpress.add(dataImpress)
    }

    fun sendTracker(callback: () -> Unit) {
        callback.invoke()
        if (!mDataImpress.isNullOrEmpty()) mDataImpress.clear()
    }
}
