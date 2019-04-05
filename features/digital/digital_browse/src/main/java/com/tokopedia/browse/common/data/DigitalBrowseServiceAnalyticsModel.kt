package com.tokopedia.browse.common.data

import com.google.android.gms.tagmanager.DataLayer

/**
 * @author by furqan on 18/09/18.
 */

class DigitalBrowseServiceAnalyticsModel (
    var headerName: String = "",
    var headerPosition: Int = 0,
    var iconName: String = "",
    var iconPosition: Int = 0
) {
    val TRACKING_FIELD_ID = "id"
    val TRACKING_FIELD_NAME = "name"
    val TRACKING_FIELD_CREATIVE = "creative"
    val TRACKING_FIELD_POSITION = "position"
    val TRACKING_VALUE_NAME = "/kategori_lainnya - %s - %s"

    fun getPromoFieldObject() : Any {
        return DataLayer.mapOf(
                TRACKING_FIELD_ID, iconPosition.toString(),
                TRACKING_FIELD_NAME, String.format(TRACKING_VALUE_NAME, headerName, headerPosition),
                TRACKING_FIELD_CREATIVE, iconName,
                TRACKING_FIELD_POSITION, iconPosition.toString()
        )
    }
}
