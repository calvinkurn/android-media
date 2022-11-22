package com.tokopedia.feedcomponent.view.viewmodel.track

/**
 * @author by yfsx on 15/03/19.
 */
class TrackingModel(
        val clickURL: String = "",
        val viewURL: String = "",
        val type: String = "",
        val source: String = "",
        val viewType: String = "",
        val recomId: Long = 0
) {
    fun copy(): TrackingModel {
        return TrackingModel(clickURL, viewURL, type, source)
    }
}
