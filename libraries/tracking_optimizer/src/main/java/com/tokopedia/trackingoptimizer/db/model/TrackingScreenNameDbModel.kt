package com.tokopedia.trackingoptimizer.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tokopedia.trackingoptimizer.db.model.TrackingScreenNameDbModel.Companion.TRACKING_SCREEN_NAME_TABLE_NAME

/**
 * Created by hendry on 21/12/18.
 */
@Entity(tableName = TRACKING_SCREEN_NAME_TABLE_NAME)
class TrackingScreenNameDbModel {
    @PrimaryKey
    var screenName: String = ""
    var shopId: String? = null
    var shopType: String? = null
    var pageType: String? = null
    var productId: String? = null

    companion object {
        const val TRACKING_SCREEN_NAME_TABLE_NAME = "tracking_screen_name_tb"
    }
}