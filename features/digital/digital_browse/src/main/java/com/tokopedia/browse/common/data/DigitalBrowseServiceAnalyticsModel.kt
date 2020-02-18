package com.tokopedia.browse.common.data

import com.tokopedia.analytic_constant.DataLayer

/**
 * @author by furqan on 18/09/18.
 */

class DigitalBrowseServiceAnalyticsModel (
    var headerName: String = "",
    var headerPosition: Int = 0,
    var iconName: String = "",
    var iconPosition: Int = 0,
    var buIdentifier: String = ""
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
                TRACKING_FIELD_CREATIVE, buIdentifier,
                TRACKING_FIELD_POSITION, iconPosition.toString()
        )
    }
}
